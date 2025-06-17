package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Subject;
import bean.Teacher;
import dao.ClassNumDao;
import tool.Action;

public class SubjectUpdateAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		HttpSession session = req.getSession();
		Teacher teacher = (Teacher)session.getAttribute("user");
		String cd = ""; // 科目コード
		String name= ""; // 科目名
		Subject subjectDao = new Subject();
		ClassNumDao classNumDao = new ClassNumDao();

		 cd= req.getParameter("cd");

		// ビジネスロジック 4
		ent_cd = subject.getEntYear();
		name = student.getName();
		class_num = student.getClassNum();
		isAttend = student.isAttend();

		// レスポンス値をセット 6
		// リクエストに入学年度をセット
		req.setAttribute("ent_cd", ent_cd);
		// リクエストに学生番号をセット
		req.setAttribute("no", no);
		// リクエストに氏名をセット
		req.setAttribute("name", name);
		// リクエストにクラス番号をセット
		req.setAttribute("class_num", class_num);
		// リクエストにクラス番号の一覧をセット
		req.setAttribute("class_num_set", class_num_set);
		// リクエストに在学フラグをセット
		req.setAttribute("is_attend", isAttend);

		// JSPへフォワード 7
		req.getRequestDispatcher("subject_update.jsp").forward(req, res);
	}

}
