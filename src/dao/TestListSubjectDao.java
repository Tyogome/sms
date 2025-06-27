package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Subject;
import bean.TestListSubject;

public class TestListSubjectDao extends Dao {

	@SuppressWarnings("unused")

	private String baseSql = "select * from test where subject_no = ?";

	private List<TestListSubject> postFilter(ResultSet rSet) throws Exception {
		// リストを初期化
		List<TestListSubject> list = new ArrayList<>();
		try {
			// リザルトセットを全権走査
			while (rSet.next()) {
				// 学生インスタンスを初期化
				TestListSubject testlistsubject = new TestListSubject();
				// 科目インスタンスに検索結果をセット
				testlistsubject.setEntYear(rSet.getInt("entYear"));
				testlistsubject.setStudentNo(rSet.getString("studentNo"));
				testlistsubject.setStudentName(rSet.getString("studentName"));
				testlistsubject.setClassNum(rSet.getString("classNum"));
				testlistsubject.putPoint(1,rSet.getInt("no1"));
				testlistsubject.putPoint(2,rSet.getInt("no2"));
				// リストに追加
				list.add(testlistsubject);
			}
		} catch (SQLException | NullPointerException e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<TestListSubject> filter(Subject subject) throws Exception {

		// リストを初期化
		List<TestListSubject> list = new ArrayList<>();
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;
		// リザルトセット
		ResultSet rSet = null;


		/* 科目の成績一覧 */
		try {
			// プリペアードステートメントにSQL文をセット
			statement = connection.prepareStatement("select student.ent_year, student.class_num, student.no as student_no, student.name,  no1.subject_cd, no1.point as no1, no2.point as no2 from student left join(select test.student_no, test.subject_cd, test.no as no1, test.point from test where no = ?) as no1 on student.no = no1.student_no left join(select test.student_no, test.subject_cd, test.no as no2, test.point from test where no = ?)as no2 on student.no = no2.student_no and no1.subject_cd = no2.subject_cd where student.ent_year = ? and student.class_num = ? and  no1.subject_cd = ?;");
			// プリペアードステートメントに学校コードをバインド
			statement.setString(1, subject.getCd());
			// プリペアードステートメントを実行
			rSet = statement.executeQuery();
			// リストへの格納処理を実行
			list = postFilter(rSet);


		} catch (Exception e) {
			throw e;
		} finally {
			// プリペアードステートメントを閉じる
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
			// コネクションを閉じる
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
		}

		return list;
	}

}
