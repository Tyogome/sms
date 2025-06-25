package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

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

		String subject_cd = ""; // 入力された学生番号
		String subject_name = ""; // 入力された氏名
		Subject subject = new Subject();
		SubjectDao subjectDao = new SubjectDao();
		Map<String, String> errors = new HashMap<>(); // エラーメッセージ

		// リクエストパラメーターの取得 2

		subject_cd = req.getParameter("subject_cd");
		subject_name = req.getParameter("subject_name");

        /*
         * セッションからSchoolBeanを取得してsubject.setSchool()に渡す*/


        req.setAttribute("subject", subject);

        req.getRequestDispatcher("subject_delete.jsp").forward(req, res);
	}
}
