package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Test;

public class TestDao extends Dao {

    private String baseSql="SELECT * FROM TEST WHERE SCHOOL_CD =?";


    public Test get(Student student, Subject subject, School school, int no) {

    	Test test = new Test();
    	SchoolDao sDao = new SchoolDao();
    	StudentDao stDao = new StudentDao();
		// データベースへのコネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;

		try {
			// プリペアードステートメントにSQL文をセット
			statement = connection.prepareStatement(baseSql + " and student_no = ? and subject_cd = ? and no =?");
			// プリペアードステートメントに学生番号をバインド
			statement.setString(1, school.getCd());
			statement.setString(2, student.getNo());
			statement.setString(3, subject.getCd());
			statement.setInt(4, no);

			// プリペアードステートメントを実行
			ResultSet resultSet = statement.executeQuery();



			if (resultSet.next()) {
				// リザルトセットが存在する場合
				test.setClassNum(resultSet.getString("class_num"));
				test.setNo(resultSet.getInt("no"));
				test.setPoint(resultSet.getInt("point"));
				test.setSchool(sDao.get(resultSet.getString("school_cd")));
				test.setStudent(stDao.get(resultSet.getString("student_num")));


				// 学生インスタンスに検索結果をセット
				student.setNo(resultSet.getString("no"));
				student.setName(resultSet.getString("name"));
				student.setEntYear(resultSet.getInt("ent_year"));
				student.setClassNum(resultSet.getString("class_num"));
				student.setAttend(resultSet.getBoolean("is_attend"));
				// 学生フィールドには学校コードで検索した学校インスタンスをセット
				student.setSchool(schoolDao.get(resultSet.getString("school_cd")));
			} else {
				// リザルトセットが存在しない場合
				// 学生インスタンスにnullをセット
				student= null;
			}
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

		return test;
	}



    public List<Test> postFilter(ResultSet rSet, School school) {

        return null;
    }


    public List<Test> filter(int entYear, String classNum, Subject subject, int num, School school) {

        return null;
    }


    public boolean save(List<Test> list) {

        return false;
    }

    public boolean save(Test test, Connection connection) {

        return false;
    }
}