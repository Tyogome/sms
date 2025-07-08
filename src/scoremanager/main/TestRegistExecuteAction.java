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
import dao.StudentDao;
import dao.SubjectDao;
import dao.TestDao;
import tool.Action;

public class TestRegistExecuteAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		HttpSession session = req.getSession(); // セッション
		Teacher teacher = (Teacher)session.getAttribute("user");
		Map<Integer, String> errors = new HashMap<>(); // エラーメッセージ

		String subject = ""; // 科目コード
		int count = 0; // 回数
		int point = 0; // 点数
		int c = 1; // エラーの番号
		StudentDao studentDao = new StudentDao();
		SubjectDao subjectDao = new SubjectDao();
		ClassNumDao classNumDao = new ClassNumDao();
		TestDao testDao = new TestDao();
		List<Test> list = new ArrayList<>();
		List<Test> testlist = new ArrayList<>();
		LocalDate todaysDate = LocalDate.now(); // LocalDateインスタンスを取得

		int year = todaysDate.getYear(); // 現在の年を取得
		School teacherSchool = teacher.getSchool(); // テスト情報


		subject = req.getParameter("subject");
		count = Integer.parseInt(req.getParameter("count"));
		String[] student_no = req.getParameterValues("regist");

		for (String item : student_no) {
			Test test = new Test();

			try {
				point = Integer.parseInt(req.getParameter("point_" + item));
				if (point >= 0 && point <= 100) { // 得点が0～100の場合
					// testに学生情報をセット
					test.setStudent(studentDao.get(item));
					test.setClassNum(studentDao.get(item).getClassNum());
					test.setSubject(subjectDao.get(subject, teacher.getSchool()));
					test.setSchool(teacher.getSchool());
					test.setNo(count);
					test.setPoint(point);
					// リストに格納
					list.add(test);
				} else {
					throw new Exception();
				}
			} catch (Exception e) {
				errors.put(c, "0～100の範囲で入力してください");

				if (testlist.size() == 0) {
					List<String>cNumlist = classNumDao.filter(teacherSchool); //クラス情報
					List<Subject>subjectlist = subjectDao.filter(teacherSchool); //科目情報

					//テストリスト
					testlist = testDao.filter(studentDao.get(item).getEntYear(), studentDao.get(item).getClassNum(), subjectDao.get(subject, teacherSchool), count, teacherSchool);
					//科目名
					String subject_name = subjectDao.get(subject, teacherSchool).getName();

					// リストを初期化
					List<Integer> entYearSet = new ArrayList<>();
					// 10年前から10年後まで年をリストに追加
					for (int i = year - 10; i < year + 11; i++) {
						entYearSet.add(i);
					}

					// リストを初期化
					List<Integer> countSet = new ArrayList<>();
					// 2回まで回数をセット
					for (int i = 1; i < 3 ; i++) {
						countSet.add(i);
					}

					//リクエストに検索結果をセット
					req.setAttribute("testlist", testlist);
					//リクエストに科目名をセット
					req.setAttribute("subject_name", subject_name);
					// リクエストに入学年度をセット
					req.setAttribute("f1", studentDao.get(item).getEntYear());
					// リクエストにクラス番号をセット
					req.setAttribute("f2", studentDao.get(item).getClassNum());
					// リクエストに科目をセット
					req.setAttribute("f3", subjectDao.get(subject, teacherSchool).getCd());
					// リクエストに回数をセット
					req.setAttribute("f4", count);
					//リクエストに入学年度リストをセット
					req.setAttribute("entYearList", entYearSet);
					//リクエストにクラス情報リストをセット
					req.setAttribute("cNumList", cNumlist);
					//リクエストに科目情報リストをセット
					req.setAttribute("list", subjectlist);
					//リクエストに回数リストをセット
					req.setAttribute("countList", countSet);
				}
			} finally {
				// エラーの番号
				c++;
			}
		}

		if (errors.isEmpty()) { // エラーがなかった場合
			// saveメソッドで情報を登録
			testDao.save(list);
		} else {
			// リクエストにエラーメッセージをセット
			req.setAttribute("errors", errors);
		}

		// レスポンス値をセット 6
		// なし

		// JSPへフォワード 7
		if (errors.isEmpty()) { // エラーメッセージがない場合
			// 登録完了画面にフォワード
			req.getRequestDispatcher("test_regist_done.jsp").forward(req, res);
		} else {
			// 成績一覧画面にフォワード
			req.getRequestDispatcher("test_regist.jsp").forward(req, res);
		}

	}

}
