package com.wallpad.project.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import javax.servlet.http.*;
import java.io.IOException;

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
	                                     Authentication authentication) throws IOException {
	    String saveid = request.getParameter("saveid");
	    String username = authentication.getName();

	    if ("on".equals(saveid)) {
	        Cookie cookie = new Cookie("saveid", username);
	        cookie.setMaxAge(30 * 24 * 60 * 60); 
	        cookie.setPath("/");
	        cookie.setHttpOnly(true);
	        cookie.setSecure(false); 
	        response.addCookie(cookie);
	    } else {
	        Cookie cookie = new Cookie("saveid", null);
	        cookie.setMaxAge(0);
	        cookie.setPath("/");
	        response.addCookie(cookie);
	    }

	    response.sendRedirect("/dashboard");
	}


}
