package com.poscodx.mysite.controller.action.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poscodx.mysite.controller.ActionServlet.Action;
import com.poscodx.mysite.dao.BoardDao;
import com.poscodx.mysite.vo.BoardVo;

public class ViewAction implements Action {
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long no = Long.parseLong(request.getParameter("no"));

		BoardVo boardvo = new BoardDao().findByNo(no);

		new BoardDao().updateHit(no);

		request.setAttribute("title", boardvo.getTitle()); 		// System.out.println("no:" + no);
		request.setAttribute("contents", boardvo.getContents());
		request.setAttribute("no", no);
		request.setAttribute("userNo", boardvo.getUserNo());
		
		request
			.getRequestDispatcher("/WEB-INF/views/board/view.jsp")
			.forward(request, response);
	}
}