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

public class SubjectUpdateExecuteAction extends Action {

    // @SuppressWarnings("unused")
	@Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

    	/*
         * ログインしているユーザのセッションを取得する
         */

        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        Map<String, String> errors = new HashMap<>();

        String cd = req.getParameter("cd");
        String name = req.getParameter("name");
        Subject subject = new Subject();
        SubjectDao subjectDao = new SubjectDao();

        subject.setCd(cd);
        subject.setName(name);

        System.out.println(name);

        /* 科目が存在するか確認をする */
        subject = subjectDao.get(cd, teacher.getSchool());

        if (subject == null) { // 科目が存在しなっかた場合
			errors.put("1", "科目が存在しません");
			// リクエストにエラーメッセージをセット
			req.setAttribute("errors", errors);
        } else {
	        /*
	         * セッションからSchoolBeanを取得してsubject.setSchool()に渡す*/

	        subject.setSchool(teacher.getSchool());

	        subjectDao.save(subject);
        }

        if(errors.isEmpty()){
        	req.getRequestDispatcher("subject_update_done.jsp").forward(req, res);
        } else {
        	req.getRequestDispatcher("subject_update.jsp").forward(req, res);
        }
    }
}

