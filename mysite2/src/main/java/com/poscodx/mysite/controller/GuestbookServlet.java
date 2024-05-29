package com.poscodx.mysite.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poscodx.mysite.controller.ActionServlet.Action;
import com.poscodx.mysite.controller.action.guestbook.AddAction;
import com.poscodx.mysite.controller.action.guestbook.DeleteAction;
import com.poscodx.mysite.controller.action.guestbook.DeleteFormAction;
import com.poscodx.mysite.controller.action.guestbook.ListAction;
import com.poscodx.mysite.controller.action.main.MainAction;
import com.poscodx.mysite.controller.action.user.JoinAction;
import com.poscodx.mysite.controller.action.user.JoinFormAction;
import com.poscodx.mysite.controller.action.user.JoinSuccess;
import com.poscodx.mysite.controller.action.user.LoginAction;
import com.poscodx.mysite.controller.action.user.LoginFormAction;
import com.poscodx.mysite.controller.action.user.LogoutAction;
import com.poscodx.mysite.controller.action.user.UpdateAction;
import com.poscodx.mysite.controller.action.user.UpdateFormAction;
import com.poscodx.mysite.dao.GuestbookDao;
import com.poscodx.mysite.vo.GuestbookVo;

public class GuestbookServlet extends ActionServlet {
	private static final long serialVersionUID = 1L;

	private Map<String, Action> mapAction = Map.of(
		"deleteform", new DeleteFormAction(),
		"delete", new DeleteAction(),
		"add", new AddAction()
	);
	
	@Override
	protected Action getAction(String actionName) {
		// 아래에서 else 절 처리 
		return mapAction.getOrDefault(actionName, new ListAction());
	}
		
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		request.setCharacterEncoding("utf-8");
//		String action = request.getParameter("a");
//		
//		if("deleteform".equals(action)) {
//			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/guestbook/deleteform.jsp");
//			rd.forward(request, response);	
//		} else if("delete".equals(action)) {
//			String no = request.getParameter("no");
//			String password = request.getParameter("password");
//			
//			new GuestbookDao().deleteByNoAndPassword(Long.parseLong(no), password);
//			
//			response.sendRedirect(request.getContextPath() + "/guestbook");
//		} else if("add".equals(action)) {
//			String name = request.getParameter("name");
//			String password = request.getParameter("password");
//			String contents = request.getParameter("contents");
//			
//			GuestbookVo vo = new GuestbookVo();
//			vo.setName(name);
//			vo.setPassword(password);
//			vo.setContents(contents);
//			
//			new GuestbookDao().insert(vo);
//			
//			response.sendRedirect(request.getContextPath() + "/guestbook");
//		} else {
//			List<GuestbookVo> list = new GuestbookDao().findAll();
//			request.setAttribute("list", list);
//			request
//				.getRequestDispatcher("/WEB-INF/views/guestbook/list.jsp")
//				.forward(request, response);
//		}
//	}
//
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		doGet(request, response);
//	}
}