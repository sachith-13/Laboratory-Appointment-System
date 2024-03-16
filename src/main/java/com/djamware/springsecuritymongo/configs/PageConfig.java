/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.djamware.springsecuritymongo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author didin
 */
@Configuration
public class PageConfig implements WebMvcConfigurer {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/login").setViewName("login");

        registry.addViewController("/admin/dashboard").setViewName("admin/dashboard");
        registry.addViewController("/admin/labs").setViewName("admin/labs");
        registry.addViewController("/admin/users").setViewName("admin/users");
        registry.addViewController("/admin/bookings").setViewName("admin/bookings");
        registry.addViewController("/admin/testresults").setViewName("admin/testresults");

        registry.addViewController("/user/dashboard").setViewName("user/dashboard");
        registry.addViewController("/user/bookings").setViewName("user/bookings");
        registry.addViewController("/user/testresult").setViewName("user/testresult");
    }

}
