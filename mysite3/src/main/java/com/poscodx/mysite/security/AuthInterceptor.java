package com.poscodx.mysite.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.poscodx.mysite.vo.UserVo;

public class AuthInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(
			HttpServletRequest request, 
			HttpServletResponse response, 
			Object handler) throws Exception {
		
		// 1. handler 종류 확인
		if(!(handler instanceof HandlerMethod)) {
			// DefaultServletHandler가 처리하는 경우(정적자원, /assets/**, mapping이 안 되어 있는 URL)
			return true;
		}

		// 2. casting - annotation 정보를 확인하는 method가 HandlerMethod 안에 있기 때문
		HandlerMethod handlerMethod = (HandlerMethod)handler;
		
		// 3. HandlerMethod의 @Auth 가져오기
		Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
		
		// 4. HandlerMethod에 @Auth가 없는 경우
		if(auth == null) {
			return true;
		}
		
		// 5. @Auth가 붙어있기 때문에 인증(Authentication) 확인
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		
		// 6. 인증이 안 되어 있는 경우
		if(authUser == null) {
			response.sendRedirect(request.getContextPath() + "/user/login");
			return false;
		}
		
		// 7. @Auth가 붙어있고 인증도 확인된 경우
		return true;
	}
}