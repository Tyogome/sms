package scoremanager.main;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Subject;
import bean.TestListSubject;
import dao.ClassNumDao;
import dao.SubjectDao;
import dao.TestDao;

public class TestListStudentExecuteAction extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 入力値の取得
        String entYearStr = request.getParameter("entYear");
        String classNum = request.getParameter("classNum");
        String subjectCd = request.getParameter("subjectCd");
        String studentNo = request.getParameter("studentNo");

        int entYear = 0;
        if (entYearStr != null && !entYearStr.isEmpty()) {
            try {
                entYear = Integer.parseInt(entYearStr);
            } catch (NumberFormatException e) {
                entYear = 0;
            }
        }

        // 各種リストの取得
        List<Integer> entYearList = TestDao.getEntYearList();
        List<String> classNumList = new ClassNumDao().findAll();
        List<Subject> subjectList = SubjectDao.findAll();

        // 選択された科目の取得（表示用）
        Subject selectedSubject = SubjectDao.findByCd(subjectCd);

        // 成績データの取得
        List<TestListSubject> testList = TestDao.filter(entYear, classNum, subjectCd, studentNo);

        // リクエストスコープへ設定
        request.setAttribute("entYearList", entYearList);
        request.setAttribute("classNumList", classNumList);
        request.setAttribute("subjectList", subjectList);
        request.setAttribute("selectedSubject", selectedSubject);
        request.setAttribute("testList", testList);

        // JSPへフォワード
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/test_list.jsp");
        dispatcher.forward(request, response);
    }
}
