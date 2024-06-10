package com.poscodx.mysite.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.poscodx.mysite.service.UserService;
import com.poscodx.mysite.vo.UserVo;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/join", method=RequestMethod.GET)
	public String join() {
		return "user/join";
	}
	
	@RequestMapping(value="/join", method=RequestMethod.POST)
	public String join(UserVo vo) {
		userService.join(vo);
		return "redirect:/user/joinsuccess";
	}
	
	@RequestMapping(value="/joinsuccess", method=RequestMethod.GET)
	public String joinsuccess() {
		return "user/joinsuccess";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String login() {
		return "user/login";
	}
	
	// LoginInterceptor 가 대체 !!
//	@RequestMapping(value="/login", method=RequestMethod.POST)
//	public String login(HttpSession session, UserVo vo, Model model) {
//		UserVo authUser = userService.getUser(vo.getEmail(), vo.getPassword());
//		
//		if(authUser == null) {
//			model.addAttribute("email", vo.getEmail());
//			model.addAttribute("result", "fail");
//			
//			// login 실패
//			return "user/login";
//		}
//		
//		// login 성공
//		session.setAttribute("authUser", authUser);
//		return "redirect:/";
//	}
	
	// LogoutInterceptor 가 대체 !!
//	@RequestMapping("/logout")
//	public String logout(HttpSession session) {
//		session.removeAttribute("authUser");
//		session.invalidate();
//		
//		return "redirect:/";
//	}
	
	@RequestMapping(value="/update", method=RequestMethod.GET)
	// public String update(@AuthUser UserVo vo) { --> Spring스럽게 코딩
	public String update(HttpSession session, Model model) {
		// access control start
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) {
			return "redirect:/";
		}
		// access control end
		
		UserVo vo = userService.getUser(authUser.getNo());
		model.addAttribute("userVo", vo);
		
		return "user/update";
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String update(HttpSession session, UserVo vo) {
		// access control start
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) {
			return "redirect:/";
		}
		//access control end
		
		vo.setNo(authUser.getNo());
		userService.update(vo);
		
		authUser.setName(vo.getName());
		
		return "redirect:/user/update";
	}
}