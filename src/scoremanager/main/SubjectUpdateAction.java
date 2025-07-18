package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectUpdateAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		HttpSession session = req.getSession();
		Teacher teacher = (Teacher)session.getAttribute("user");


		String cd = ""; // 科目コード
		String name= ""; // 科目名
		Subject subject = new Subject();
		SubjectDao subjectDao = new SubjectDao();

		cd = req.getParameter("cd");

		/* DBからデータを取得する */
		subject = subjectDao.get(cd,teacher.getSchool());


		cd = subject.getCd();
		name = subject.getName();

		req.setAttribute("cd", cd);

		req.setAttribute("name", name);

		req.getRequestDispatcher("subject_update.jsp").forward(req, res);


	}
}
