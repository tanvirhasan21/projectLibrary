package com.example.basicauthentication.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)throws Exception{
        httpSecurity.csrf().disable()
                .authorizeHttpRequests((authorize)->
                            authorize.antMatchers("/users/newuser","/users/createNewUser","/projects/jasperpdf/export","/api/**","/static/**").permitAll()
                        .antMatchers("/projects/**").hasRole("ADMIN")
                        ).formLogin(
                                form->form
                                    .loginPage("/users/login")
                                    .loginProcessingUrl("/users/login")
                                    .defaultSuccessUrl("/projects/projectList")
                                    .permitAll()
                        ).logout(
                                logout->
                                        logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .permitAll()
        );
        return httpSecurity.build();
    }

    @Autowired
    public void ConfigureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.
                userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

}
