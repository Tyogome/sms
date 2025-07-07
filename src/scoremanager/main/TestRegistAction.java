package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.ClassNumDao;
import dao.SubjectDao;
import dao.TestDao;
import tool.Action;

public class TestRegistAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		HttpSession session = req.getSession(); // セッション
		Teacher teacher = (Teacher)session.getAttribute("user");

		String entYearStr = null; // 入力された入学年度
		int entYear = 0; // 入学年度
		String classNum = ""; // 入力されたクラス番号
		String subjectName = ""; //入力された科目
		String countStr = null; //入力された回数
		int count = 0; // 回数
		ClassNumDao classNumDao = new ClassNumDao(); // クラス番号Dao
		SubjectDao subjectDao = new SubjectDao(); //科目Dao
		TestDao testDao = new TestDao(); // テストDao
		School teacherSchool = teacher.getSchool(); // テスト情報
		LocalDate todaysDate = LocalDate.now(); // LocalDateインスタンスを取得
		int year = todaysDate.getYear(); // 現在の年を取得
		Map<String, String> errors = new HashMap<>();
// エラーメッセージ

		// リクエストパラメーターの取得 2
		entYearStr = req.getParameter("f1");
		classNum = req.getParameter("f2");
		subjectName = req.getParameter("f3");
		countStr = req.getParameter("f4");

		/* ユーザーデータからユーザーが所属している学校のクラスデータを取得 */
		List<String> classlist = classNumDao.filter(teacher.getSchool());


		/* ユーザーデータからユーザーが所属している学校の科目データを取得 */
		List<Subject> subjectlist = subjectDao.filter(teacher.getSchool());

		//ビジネスロジック4
		if (entYearStr != null) {
			entYear = Integer.parseInt(entYearStr);
		}
		if (countStr != null) {
			count = Integer.parseInt(countStr);
		}

		/* 入学年度リストを生成 */
		List<Integer> entYearSet = new ArrayList<>();
		// 10年前から1年後まで年をリストに追加
		for (int i = year - 10; i < year + 1; i++) {
			entYearSet.add(i);
		}

		// リストを初期化
		List<Integer> countSet = new ArrayList<>();
		// 2回まで回数をセット
		for (int i = 1; i < 3 ; i++) {
			countSet.add(i);
		}

		// 検索
		if (entYear == 0 && classNum == null && subjectName == null && count == 0) {

		} else if (entYear != 0 && !(classNum.equals("0")) && !(subjectName.equals("0")) && count != 0) {

			/* debug */
			System.out.println("debug_entYear:" + entYear);
			System.out.println("debug_classNum:" + classNum);
			Subject s = subjectDao.get(subjectName, teacherSchool);
			System.out.println("debug_subjectCd:" + s.getCd());
			System.out.println("debug_subjectName:" + s.getName());

			//テストリスト
			List<Test>testlist = testDao.filter(entYear, classNum, subjectDao.get(subjectName, teacherSchool), count, teacherSchool);
			//科目名
			String subject_name1 = subjectDao.get(subjectName, teacherSchool).getName();
			//リクエストに検索結果をセット
			req.setAttribute("testlist", testlist);
			//リクエストに科目名をセット
			req.setAttribute("subject_name", subject_name1);
		} else {
			errors.put("a", "入学年度とクラスと科目と回数を選択してください");
			req.setAttribute("errors", errors);
		}

		entYearStr = req.getParameter("f1");
		classNum = req.getParameter("f2");
		subjectName = req.getParameter("f3");
		countStr = req.getParameter("f4");


		//レスポンス値をセット6
		//リクエストに入学年度をセット
		req.setAttribute("f1", entYear);
		//リクエストにクラス番号をセット
		req.setAttribute("f2", classNum);
		//リクエストに科目をセット
		req.setAttribute("f3", subjectName);
		//リクエストに回数をセット
		req.setAttribute("f4", count);


		// リクエストにデータをセット
		req.setAttribute("ent_year_set", entYearSet);
		//リクエストにクラス情報リストをセット
		req.setAttribute("class_num_set", classlist);
		//リクエストに科目情報リストをセット
		req.setAttribute("subject_name_set", subjectlist);
		//リクエストに回数リストをセット
		req.setAttribute("countList", countSet);

		//jspへのフォワード7
		req.getRequestDispatcher("test_regist.jsp").forward(req , res);
	}

}