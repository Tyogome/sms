package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Student;
import bean.TestListStudent;

public class TestListStudentDao extends Dao {

	private String baseSql = "select * from test where student_no = ?";

	private List<TestListStudent> postFilter(ResultSet rSet) throws Exception {
		// リストを初期化
		List<Student> list = new ArrayList<>();
		try {
			// リザルトセットを全権走査
			while (rSet.next()) {
				// 学生インスタンスを初期化
				TestListStudent testliststudent = new TestListStudent();
				// 学生インスタンスに検索結果をセット
				testliststudent.setSubjectName(rSet.getString("subjectName"));
				testliststudent.setSubjectCd(rSet.getString("subjectCd"));
				testliststudent.setNum(rSet.getInt("num"));
				testliststudent.setPoint(rSet.getInt("point"));
				// リストに追加
				list.add(testliststudent);
			}
		} catch (SQLException | NullPointerException e) {
			e.printStackTrace();
		}

		return testliststudent;
	}

	public List<TestListStudent> filter(Student student) throws Exception {

		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;


		/* 学生の成績一覧 */SQL文
		try {
			statement = connection.prepareStatement("select  student.no, student.name,   subject.cd, subject.name, test.no, point from test join student on test.student_no = student.no join subject on test.subject_cd = subject.cd");
			
		}


	}
}










