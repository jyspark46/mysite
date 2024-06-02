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

public class ReplyAction implements Action {
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser");

		int gNo = Integer.parseInt(request.getParameter("gNo"));
		int oNo = Integer.parseInt(request.getParameter("oNo"));
		int depth = Integer.parseInt(request.getParameter("depth"));
		String title = request.getParameter("title");
		String content = request.getParameter("contents");

		BoardVo vo = new BoardVo();
		int countSameDepth = BoardDao.findHasSameDepth(gNo, depth);
		int countPlusOneDepth = BoardDao.findHasPlusOneDepth(gNo, depth);
		
		if(countSameDepth != 0 || (countPlusOneDepth != 0 && countSameDepth == 0)) {
			new BoardDao().setOrder(gNo, oNo, depth);
			vo.setoNo(oNo + 1);
		} else {
			vo.setoNo(oNo + 1);
		}
		
		vo.setTitle(title);
		vo.setContents(content);
		vo.setHit(0);
		vo.setgNo(gNo);
		vo.setDepth(depth + 1);
		vo.setUserNo(authUser.getNo());
		
		new BoardDao().insert(vo);
		
		response.sendRedirect(request.getContextPath() + "/board?a=board");
	}
}