package scoremanager.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectCreateAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		HttpSession session = req.getSession(); // セッション
		Teacher teacher = (Teacher)session.getAttribute("user");

		// ローカル変数の指定 1
		SubjectDao subjectDao = new SubjectDao(); // クラス番号Daoを初期化


		// リクエストパラメーターの取得 2
		// なし

		// DBからデータ取得 3
		// ログインユーザーの学校コードをもとにクラス番号の一覧を取得
		List<Subject> list = subjectDao.filter(teacher.getSchool());



		// レスポンス値をセット 6
		// リクエストにデータをセット
		req.setAttribute("class_num_set", list);


		// JSPへフォワード 7
		req.getRequestDispatcher("student_create.jsp").forward(req, res);
	}

}
