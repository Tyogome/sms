package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Student;
import bean.Subject;
import bean.Teacher;
import dao.ClassNumDao;
import dao.SubjectDao;
import tool.Action;

public class TestListAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// ローカル変数の指定
		String entYearStr = ""; // 入力された入学年度
		String classNum = ""; // 入力されたクラス番号
		String studentCd = "";// 入力された学生番号
		int entYear = 0; // 入学年度
		List<Student> students = null; // 学生リスト
		LocalDate todaysDate = LocalDate.now(); // LocalDateインスタンスを取得
		int year = todaysDate.getYear(); // 現在の年を取得
		ClassNumDao classNumDao = new ClassNumDao(); // クラス番号Daoを初期化
		SubjectDao subjectDao = new SubjectDao();	// 科目Daoを初期化
		Map<String, String> errors = new HashMap<>(); // エラーメッセージ

		/* セッションからユーザーデータを取得 */
		HttpSession session = req.getSession();
		Teacher teacher = (Teacher)session.getAttribute("user");

		String subjectCdStr = "";
		String subjectName = "";

		int subjectcd = 0;

		entYearStr = req.getParameter("f1");
		classNum = req.getParameter("f2");
		subjectName = req.getParameter("f3");
		studentCd = req.getParameter("f4");

		if (subjectCdStr != null) {
			subjectcd = Integer.parseInt(subjectCdStr);
		}

		/* ユーザーデータからユーザーが所属している学校のクラスデータを取得 */
		List<String> classlist = classNumDao.filter(teacher.getSchool());


		/* ユーザーデータからユーザーが所属している学校の科目データを取得 */
		List<Subject> subjectlist = subjectDao.filter(teacher.getSchool());


		/* 入学年度リストを生成 */
		List<Integer> entYearSet = new ArrayList<>();
		// 10年前から1年後まで年をリストに追加
		for (int i = year - 10; i < year + 1; i++) {
			entYearSet.add(i);
		}


		/* 収集したデータをリクエストに設定 */
		// リクエストに入学年度をセット
		req.setAttribute("f1", entYear);
		// リクエストにクラス番号をセット
		req.setAttribute("f2", classNum);
		// リクエストに科目名をセット
		req.setAttribute("f3", subjectName);
		// リクエストに学生番号をセット
		req.setAttribute("f4", studentCd);

		// リクエストに学生リストをセット
		req.setAttribute("students", students);

		// リクエストにデータをセット
		req.setAttribute("class_num_set", classlist);
		req.setAttribute("subject_set", subjectlist);

		// JSPへフォワード
		req.getRequestDispatcher("test_list.jsp").forward(req, res);

	}
}

