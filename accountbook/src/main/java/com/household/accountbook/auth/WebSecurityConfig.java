package com.household.accountbook.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http.formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/authenticate")
            .usernameParameter("loginId")
            .passwordParameter("password")
            .defaultSuccessUrl("/main")
            .permitAll();


        http.authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/css/**.css","/built","/js/**.js","/**.png").permitAll()
            .anyRequest().authenticated()
            .and()
            .rememberMe()
            .and()
            .logout()
            .logoutSuccessUrl("/login")
            .permitAll()
            .and()
            .csrf()
            .disable();
             
       
        
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Autowired
    void configureAuthenticationManager(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }

}
