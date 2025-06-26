package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectDeleteExecuteAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		HttpSession session = req.getSession(); // セッション
		Teacher teacher = (Teacher)session.getAttribute("user");

        SubjectDao.subjectDao = req.getSession();
        Subject.delete;

        req.setAttribute("subject_name", subject.getName());
        req.getRequestDispatcher("subject_delete_done.jsp").forward(req, res);
	}
}
