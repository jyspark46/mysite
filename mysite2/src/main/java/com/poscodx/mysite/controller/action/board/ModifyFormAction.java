package com.poscodx.mysite.controller.action.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.poscodx.mysite.controller.ActionServlet.Action;
import com.poscodx.mysite.dao.BoardDao;
import com.poscodx.mysite.vo.BoardVo;
import com.poscodx.mysite.vo.UserVo;

public class ModifyFormAction implements Action {
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String no = request.getParameter("no");
		BoardVo vo = new BoardDao().findByNo(Long.parseLong(no));		

		HttpSession session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser"); //session에서 로그인자 정보 가져오기
		
		if(authUser == null) {
			response.sendRedirect(request.getContextPath() + "/board");
			return;
		}
		
		Long userNo = authUser.getNo();
		Long boardWriter = vo.getUserNo();

		if(userNo != boardWriter) {
			response.sendRedirect(request.getContextPath() + "/board");
			return;
		}
		
		request.setAttribute("vo", vo);
		
		request
			.getRequestDispatcher("/WEB-INF/views/board/modify.jsp")
			.forward(request, response);
	}
}