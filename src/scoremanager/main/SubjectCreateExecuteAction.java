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

public class SubjectCreateExecuteAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// ローカル変数の指定 1
		HttpSession session = req.getSession(); // セッション
		Teacher teacher = (Teacher)session.getAttribute("user");

		String subject_cd = ""; // 入力された学生番号
		String subject_name = ""; // 入力された氏名
		Subject subject = new Subject();
		SubjectDao subjectDao = new SubjectDao();
		Map<String, String> errors = new HashMap<>(); // エラーメッセージ

		// リクエストパラメーターの取得 2

		subject_cd = req.getParameter("cd");
		subject_name = req.getParameter("name");


		// DBからデータ取得 3
		// なし

		// ビジネスロジック 4
		if (subject_cd.length() != 3) { // 入学年度が未選択だった場合
			errors.put("1", "3文字以上入力してください");
			// リクエストにエラーメッセージをセット
			req.setAttribute("errors", errors);
		} else {
			if (subjectDao.get(subject_cd) != null) { // 学生番号が重複している場合
				errors.put("2", "科目番号が重複しています");
				// リクエストにエラーメッセージをセット
				req.setAttribute("errors", errors);
			} else {
				// studentに学生情報をセット
				subject.setCd(subject_cd);
				subject.setName(subject_name);
				subject.setAttend(true);
				subject.setSchool(teacher.getSchool());
				// saveメソッドで情報を登録
				subjectDao.save(subject);
			}
		}

		// レスポンス値をセット 6
		// リクエストに学生番号をセット
		req.setAttribute("cd", subject_cd);
		// リクエストに氏名をセット
		req.setAttribute("name", subject_name);


		// JSPへフォワード 7
		if (errors.isEmpty()) { // エラーメッセージがない場合
			// 登録完了画面にフォワード
			req.getRequestDispatcher("student_create_done.jsp").forward(req, res);
		} else { // エラーメッセージがある場合
			// 登録画面にフォワード
			req.getRequestDispatcher("StudentCreate.action").forward(req, res);
		}
	}

}
