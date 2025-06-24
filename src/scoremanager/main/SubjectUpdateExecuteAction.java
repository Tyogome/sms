package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectUpdateExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

    	/*
         * ログインしているユーザのセッションを取得する
         */

        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        // DBからデータ取得（今回はなし）

        // subjectに科目情報をセット

        String cd = req.getParameter("cd");
        String name = req.getParameter("name");

        Subject subject = new Subject();
        SubjectDao subjectDao = new SubjectDao();

        subject.setCd(cd);
        subject.setName(name);

        System.out.println(name);

        /*
         * セッションからSchoolBeanを取得してsubject.setSchool()に渡す*/

        subject.setSchool(teacher.getSchool());

        subjectDao.save(subject);

        req.getRequestDispatcher("subject_update_done.jsp").forward(req, res);
    }
}
