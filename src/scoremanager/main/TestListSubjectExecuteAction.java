package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Subject;
import bean.Teacher;
import bean.TestListSubject;
import dao.ClassNumDao;
import dao.SubjectDao;
import dao.TestListSubjectDao;
import tool.Action;

public class TestListSubjectExecuteAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// TODO 自動生成されたメソッド・スタブ

		//ローカル変数の宣言1
		HttpSession session = req.getSession();//セッション
		Teacher teacher = (Teacher)session.getAttribute("user");
		int entYear = 0; // 入力された入学年度
		String classNum = ""; // 入力されたクラス番号
		String subject = ""; //入力された科目
		String subjectName = ""; // 科目名
		TestListSubjectDao tlsDao = new TestListSubjectDao();
		SubjectDao subjectDao = new SubjectDao();
		ClassNumDao cNumDao = new ClassNumDao(); // クラス番号Dao
		LocalDate todaysDate = LocalDate.now(); // LocalDateインスタンスを取得
		int year = todaysDate.getYear(); // 現在の年を取得
		Map<String, String> errors = new HashMap<>(); // エラーメッセージ

		// リクエストパラメーターの取得2
		entYear = Integer.parseInt(req.getParameter("f1"));
		classNum = req.getParameter("f2");
		subject = req.getParameter("f3");

		// DBからの取得3
		// ビジネスロジック4
		if (entYear == 0 || classNum.equals("0") || subject.equals("0")) { // 入学年度、クラス番号、科目のいずれかが未選択の場合
			errors.put("1", "入学年度とクラス番号と科目を選択してください");
			// リクエストにエラーメッセージをセット
			req.setAttribute("errors", errors);
		} else {
			List<TestListSubject> tlslist = tlsDao.filter(entYear, classNum, subjectDao.get(subject, teacher.getSchool()), teacher.getSchool());
			// 科目名を取得
			subjectName = subjectDao.get(subject, teacher.getSchool()).getName();
			// リクエストに科目別一覧をセット
			req.setAttribute("tlslist", tlslist);
			// リクエストに科目名をセット
			req.setAttribute("subject_name", subjectName);
		}
		//DBからデータ取得3
		List<String> cNumlist = cNumDao.filter(teacher.getSchool()); //クラス情報
		List<Subject> list = subjectDao.filter(teacher.getSchool()); //科目情報

		//ビジネスロジック4
		// リストを初期化
		List<Integer> entYearSet = new ArrayList<>();
		// 10年前から10年後まで年をリストに追加
		for (int i = year - 10; i < year + 11; i++) {
			entYearSet.add(i);
		}

		// レスポンス値をセット6
		// リクエストに入学年度をセット
		req.setAttribute("f1", entYear);
		// リクエストにクラス番号をセット
		req.setAttribute("f2", classNum);
		// リクエストに科目をセット
		req.setAttribute("f3", subject);
		//リクエストにクラス情報リストをセット
		req.setAttribute("cNumlist", cNumlist);
		//リクエストに科目情報リストをセット
		req.setAttribute("list", list);
		//リクエストに入学年度リストをセット
		req.setAttribute("entYearSet", entYearSet);

		// フォワード7
		if (errors.isEmpty()) { // エラーが出なかった場合
			req.getRequestDispatcher("test_list_subject.jsp").forward(req , res);
		}else{ //エラーが出た場合
			req.getRequestDispatcher("test_list.jsp").forward(req , res);
		}

	}

}
