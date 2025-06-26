package com.mathmastery.handlers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {



        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String redirectUrl = "/login"; // default

        label:
        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

                System.out.println("User role: " + role); // Debugging line to check roles

            switch (role) {
                case "ROLE_ADMIN":
                    redirectUrl = "/admin/dashboard";
                    break label;
                case "ROLE_TUTOR":
                    redirectUrl = "/tutor/dashboard";
                    break label;
                case "ROLE_STUDENT":
                    redirectUrl = "/student/dashboard";
                    break label;
            }
        }

        response.sendRedirect(redirectUrl);
    }
}
