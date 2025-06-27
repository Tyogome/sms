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

		HttpSession session = req.getSession();
		Teacher teacher = (Teacher)session.getAttribute("user");


		String cd = "";
		String name = "";
		Subject subject = new Subject();
		SubjectDao subjectDao = new SubjectDao();


		cd = req.getParameter("subject_cd");
		name = req.getParameter("subject_name");

		System.out.println(cd);
        System.out.println(name);

        subject.setCd(cd);
        subject.setName(name);
        subject.setSchool(teacher.getSchool());
        subjectDao.delete(subject);

        req.setAttribute("subject_name", subject.getName());
        req.getRequestDispatcher("subject_delete_done.jsp").forward(req, res);
	}
}
