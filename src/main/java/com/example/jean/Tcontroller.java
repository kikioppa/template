package com.example.jean;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
 
@RestController
public class Tcontroller {
 
    @RequestMapping("/test")
    public String test(){
        return "Hello World";
    }
    
    @RequestMapping(value="/index", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }
}
 

