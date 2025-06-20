package scoremanager.main;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Subject;
import dao.SubjectDao;
import tool.Action;

public class SubjectUpdateExecuteAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// ローカル変数の指定 1
		String cd = "";
		String name = "";
		Subject subject = new Subject();
		SubjectDao subjectDao = new SubjectDao();
		// リクエストパラメーターの取得 2
		cd = req.getParameter("cd");
		name = req.getParameter("name");

		// DBからデータ取得 3
		// なし


		// subjectに科目情報をセット
		subject.setCd(cd);
		subject.setName(name);
		// 変更内容を保存
		subjectDao.save(subject);

		// レスポンス値をセット 6
		// なし

		// JSPへフォワード 7
		req.getRequestDispatcher("subject_update_done.jsp").forward(req, res);
	}

}
