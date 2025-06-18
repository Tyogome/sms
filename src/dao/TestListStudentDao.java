package dao;

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
				testliststudent.setNo(rSet.getString("no"));
				testliststudent.setName(rSet.getString("name"));
				testliststudent.setEntYear(rSet.getInt("ent_year"));
				testliststudent.setClassNum(rSet.getString("class_num"));
				testliststudent.setAttend(rSet.getBoolean("is_attend"));
				testliststudent.setSchool(school);
				// リストに追加
				list.add(testliststudent);
			}
		} catch (SQLException | NullPointerException e) {
			e.printStackTrace();
		}

		return testliststudent;
	}

	public List<TestListStudent> filter(Student student) throws Exception {

		/* 学生の成績一覧 */
		try {

		}


	}
}










