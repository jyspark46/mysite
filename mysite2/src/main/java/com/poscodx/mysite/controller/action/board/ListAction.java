package com.poscodx.mysite.controller.action.board;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poscodx.mysite.controller.ActionServlet.Action;
import com.poscodx.mysite.dao.BoardDao;
import com.poscodx.mysite.vo.BoardVo;
import com.poscodx.mysite.vo.Paging;
import com.poscodx.mysite.vo.UserVo;

public class ListAction implements Action {
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		UserVo authUser = null;
		Paging paging = new Paging();
		
		String keyword = request.getParameter("kwd");
		String page = request.getParameter("page");

		int currentPage = 0;
		int first = 0;
		int second = 0;
		int groupStartNum = 0;
		int groupLastNum = 0;
		int lastPageNum = 0;
		int pageSize = Paging.getPagecount();
		
		if(keyword == null || keyword.isEmpty()) {
			keyword = "";
		}

		if(page == null || "null".equals(page)) {
		} else {
			currentPage = Integer.parseInt(request.getParameter("page"));
		}

		paging.setGroup(currentPage);
		groupStartNum = paging.getGroupStartNum();
		groupLastNum = paging.getGroupLastNum();

		paging.setLastPageNum(keyword);
		lastPageNum = paging.getLastPageNum();

		if(currentPage != 0) {
			first = (currentPage * pageSize) - pageSize;
		} else {
			first = (currentPage * pageSize);
		}
		second = (currentPage * pageSize) + (pageSize - 1);

		List<BoardVo> list = new BoardDao().findAllSearch(keyword, first, pageSize);

		request.setAttribute("curPageNum", currentPage);
		request.setAttribute("groupStartNum", groupStartNum);
		request.setAttribute("groupLastNum", groupLastNum);
		request.setAttribute("lastPageNum", lastPageNum); // lastPageNum = 6일 때, 7, 8, 9, 10는 링크를 활성화 하지 못함
		request.setAttribute("list", list);
		request.setAttribute("kwd", keyword);
		
		request
			.getRequestDispatcher("/WEB-INF/views/board/list.jsp")
			.forward(request, response);
	}
}