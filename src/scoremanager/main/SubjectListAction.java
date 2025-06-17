package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tool.Action;

public class SubjectListAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {


		// Test

		// JSPへフォワード
		req.getRequestDispatcher("subject_list.jsp").forward(req, res);
	}
}
