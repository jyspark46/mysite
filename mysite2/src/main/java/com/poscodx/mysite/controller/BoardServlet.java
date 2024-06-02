package com.poscodx.mysite.controller;

import java.util.Map;

import com.poscodx.mysite.controller.action.board.DeleteAction;
import com.poscodx.mysite.controller.action.board.ListAction;
import com.poscodx.mysite.controller.action.board.ModifyAction;
import com.poscodx.mysite.controller.action.board.ModifyFormAction;
import com.poscodx.mysite.controller.action.board.PagingAction;
import com.poscodx.mysite.controller.action.board.ReplyAction;
import com.poscodx.mysite.controller.action.board.ReplyFormAction;
import com.poscodx.mysite.controller.action.board.ViewAction;
import com.poscodx.mysite.controller.action.board.WriteAction;
import com.poscodx.mysite.controller.action.board.WriteFormAction;

public class BoardServlet extends ActionServlet {
	private static final long serialVersionUID = 1L;

	private Map<String, Action> mapAction = Map.of(
		"writeform", new WriteFormAction(),
		"write", new WriteAction(),
		"delete", new DeleteAction(),
		"view", new ViewAction(),
		"modifyform", new ModifyFormAction(),
		"modify", new ModifyAction(),
		"replyform", new ReplyFormAction(),
		"reply", new ReplyAction(),
		"page", new PagingAction()
	);
	
	@Override
	protected Action getAction(String actionName) {
		// 아래에서 else 절 처리 
		return mapAction.getOrDefault(actionName, new ListAction());
	}
}