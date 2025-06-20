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
import dao.SubjectDao;
import dao.TestDao;


public class TestListSubjectExecuteAction extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 入力値取得
        String entYearStr = request.getParameter("entYear");
        String classNum = request.getParameter("classNum");
        String subjectCd = request.getParameter("subjectCd");
        String studentNo = request.getParameter("studentNo");

        int entYear = 0;

        if (entYearStr != null && !entYearStr.isEmpty()) {
            entYear = Integer.parseInt(entYearStr);
        }

        // 必要なリストデータを取得
        List<Integer> entYearList = TestDao.getEntYearList();
        List<String> classNumList = TestDao.getClassNumList();
        List<Subject> subjectList = SubjectDao.findAll();

        // 科目名の取得（表示用）
        Subject selectedSubject = SubjectDao.findByCd(subjectCd);

        // 成績データ取得
        List<TestListSubject> testList = TestDao.filter(entYear, classNum, subjectCd, studentNo);

        // リクエストスコープに格納
        request.setAttribute("entYearList", entYearList);
        request.setAttribute("classNumList", classNumList);
        request.setAttribute("subjectList", subjectList);
        request.setAttribute("selectedSubject", selectedSubject);
        request.setAttribute("testList", testList);

        // 画面に転送
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/test_list.jsp");
        dispatcher.forward(request, response);
    }
}
