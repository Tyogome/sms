package scoremanager.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import bean.Student;
import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectListAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		//String cd = ""; //科目番号
		//req.setAttribute("f1", cd);

		//String name = ""; //科目名
		//req.setAttribute("f2", name);


		/* セッションからユーザデータを取得 */
		HttpSession session = req.getSession();
		Teacher teacher = (Teacher)session.getAttribute("user");

		String subjectCdStr = "";
		String subjectname = "";

		int subjectcd = 0;

		subjectCdStr = req.getParameter("f1");
		subjectname = req.getParameter("f2");

		if (subjectCdStr != null) {

		}



		/* セッションのユーザデータから、ユーザが所属している学校の科目一覧データを取得 */
		SubjectDao SubjectDao = new SubjectDao();


		//成績のリストを取得
		List<Subject> subjects = SubjectDao.filter(teacher.getSchool()); // 科目リスト


		/* listのデータをjspに渡すためのコード */
		req.setAttribute("subjects", subjects);


		// JSPへフォワード
		req.getRequestDispatcher("subject_list.jsp").forward(req, res);
	}
}
