/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.djamware.springsecuritymongo.configs;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.djamware.springsecuritymongo.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


        @Component
        public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                                                HttpServletResponse response, Authentication authentication)
                    throws IOException, ServletException {
                //set our response to OK status
                response.setStatus(HttpServletResponse.SC_OK);

                for (GrantedAuthority auth : authentication.getAuthorities()) {
                    if ("ADMIN".equals(auth.getAuthority())) {
                        response.sendRedirect("admin/dashboard");
                    }else {
                        response.sendRedirect("user/dashboard");
                    }
                }
            }

        }

