package com.example.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;


import com.example.login.model.ConfirmationToken;
import com.example.login.model.User;
import com.example.login.repository.ConfirmationTokenRepository;
import com.example.login.repository.UserRepository;
import com.example.service.EmailSenderService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

	
	@Autowired
	private UserRepository userRepository;
	
    @Autowired
    private UserService userService;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    
    
    @Autowired
    private EmailSenderService emailSenderService;
    
    @RequestMapping(value={"/login"}, method = RequestMethod.GET)
    public ModelAndView login(User user,HttpSession session,HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User writer = UserService.findByEmail(auth.getName());
        modelAndView.setViewName("login");
        String referrer = request.getHeader("Referer");
		request.getSession().setAttribute("prevPage", referrer);
        return modelAndView;
    }

    
    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }


    
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = UserService.findByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
        	
        	userService.saveUser(user);
        	
        	ConfirmationToken confirmationToken = new ConfirmationToken(user);
        	
        	confirmationTokenRepository.save(confirmationToken);
        	
            
        	 SimpleMailMessage mailMessage = new SimpleMailMessage();
             mailMessage.setTo(user.getEmail());
             mailMessage.setSubject("Complete Registration!");
             mailMessage.setFrom("rafdivision@gmail.com");
             mailMessage.setText("To confirm your account, please click here : "
             +"http://localhost:8080/confirm-account?token="+confirmationToken.getConfirmationToken());
        	
            emailSenderService.sendEmail(mailMessage);
        	
            modelAndView.addObject("successMessage", "User has been registered successfully please check your mail and verify you account");
            modelAndView.addObject("email",user.getEmail());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }
    
    
    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token")String confirmationToken)
    {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
            User user = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
            user.setEnabled(true);
            userRepository.save(user);
            modelAndView.setViewName("accountVerified");
        }
        else
        {
            modelAndView.addObject("message","The link is invalid or broken!");
            modelAndView.setViewName("error");
        }

        return modelAndView;
    }
    // getters and setters
    
    @RequestMapping(value="/admin/home", method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = UserService.findByEmail(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }



    
    public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

public ConfirmationTokenRepository getConfirmationTokenRepository() {
	return confirmationTokenRepository;
}

public void setConfirmationTokenRepository(ConfirmationTokenRepository confirmationTokenRepository) {
	this.confirmationTokenRepository = confirmationTokenRepository;
}

public EmailSenderService getEmailSenderService() {
	return emailSenderService;
}

public void setEmailSenderService(EmailSenderService emailSenderService) {
	this.emailSenderService = emailSenderService;
}

}