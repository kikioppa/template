package com.example.login.configuration;

import com.example.login.repository.UserRepository;

import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.CompositeFilter;
import com.example.login.configuration.CustomLoginSuccessHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	
	@Autowired
	UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    private DataSource dataSource;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.
                jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

    	 http.authorizeRequests()
    	 .antMatchers("/").permitAll()
        .antMatchers("/login").permitAll()
        .antMatchers("/registration").permitAll()
        .antMatchers("/confirm-account").permitAll()
        .antMatchers("/admin/**").hasAuthority("ADMIN").anyRequest()
        .authenticated().and().csrf().disable().formLogin()
        .permitAll()
        .and()
        .formLogin()
        .loginPage("/login").failureUrl("/login?error=true")
        .defaultSuccessUrl("/admin/home")
        .usernameParameter("email")
        .passwordParameter("password")
        .successHandler(successHandler())
        .permitAll()
       .and().logout()
       .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
       .logoutSuccessUrl("/").and().exceptionHandling()
       .accessDeniedPage("/access-denied");
		
    }

    
    @Bean
	public AuthenticationSuccessHandler successHandler() {
	    return new CustomLoginSuccessHandler("/");
	}
    



    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }
    
    

}