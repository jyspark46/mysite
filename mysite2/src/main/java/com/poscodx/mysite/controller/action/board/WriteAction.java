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

public class WriteAction implements Action {
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		
		if(authUser == null) {
			response.sendRedirect(request.getContextPath() + "/board");
			return;
		}
		
		String title = request.getParameter("title");
		String contents = request.getParameter("contents");
		
		Integer maxGroupNo = new BoardDao().findMaxGroupNo();
		BoardVo vo = new BoardVo();
		vo.setTitle(title);
		vo.setContents(contents);
		vo.setHit(0);
		vo.setDepth(1);
		vo.setgNo(maxGroupNo + 1);
		vo.setoNo(1);
		vo.setUserNo(authUser.getNo());
		
		new BoardDao().insert(vo);
		
		response.sendRedirect(request.getContextPath() + "/board?a=board");
	}
}