package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Test;

public class TestDao extends Dao {

	private String baseSql = "select student.no as student_no, student.name, student.ent_year, student.class_num, student.is_attend, student.school_cd, test.subject_cd, test.no as count, test.point from student left join (select * from test where subject_cd = ? and no = ?) as test on student.no = test.student_no";

	public Test get(Student student, Subject subject, School school, int no) throws Exception {

		// テストインスタンスを初期化
		Test test = new Test();
		// データベースへのコネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;

		try {
			// プリペアードステートメントにSQL文をセット
			statement = connection.prepareStatement("select * from test where student_no = ? and school_cd = ? and subject_cd = ? and no = ?");
			// プリペアードステートメントに値をバインド
			statement.setString(1, student.getNo());
			statement.setString(2, school.getCd());
			statement.setString(3, subject.getCd());
			statement.setInt(4, no);
			// プリペアードステートメントを実行
			ResultSet rSet = statement.executeQuery();

			// 学生Daoを初期化
			StudentDao studentDao = new StudentDao();
			// 科目Daoを初期化
			SubjectDao subjectDao = new SubjectDao();
			// 学校Daoを初期化
			SchoolDao schoolDao = new SchoolDao();

			if (rSet.next()) {
				// リザルトセットが存在する場合
				// テストインスタンスに検索結果をセット
				test.setStudent(studentDao.get(rSet.getString("student_no")));
				test.setSubject(subjectDao.get(rSet.getString("subject_cd"), schoolDao.get(rSet.getString("school_cd"))));
				test.setSchool(schoolDao.get(rSet.getString("school_cd")));
				test.setNo(rSet.getInt("no"));
				test.setPoint(rSet.getInt("point"));
				test.setClassNum(rSet.getString("class_num"));
			} else {
				// リザルトセットが存在しない場合
				// テストインスタンスにnullをセット
				test = null;
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

	private List<Test> postFilter(ResultSet rSet, School school) throws Exception {

		// リストを初期化
		List<Test> list = new ArrayList<>();
		StudentDao studentDao = new StudentDao();
		SubjectDao subjectDao = new SubjectDao();
		try {
			// リザルトセット全権走査
			while (rSet.next()) {
				// 得点インスタンスを初期化
				Test test = new Test();
				// 得点インスタンスに検索結果をセット
				test.setStudent(studentDao.get(rSet.getString("student_no")));
				test.setClassNum(rSet.getString("class_num"));
				test.setSubject(subjectDao.get(rSet.getString("subject_cd"), school));
				test.setSchool(school);
				test.setNo(rSet.getInt("count"));
				test.setPoint(rSet.getInt("point"));
				// リストを初期化
				list.add(test);
			}
		} catch (SQLException | NullPointerException e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<Test> filter(int entYear, String classNum, Subject subject, int num, School school) throws Exception {

		// リストを初期化
		List<Test> list = new ArrayList<>();
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;
		// リザルトセット
		ResultSet rSet = null;
		// SQL文の条件
		String condition = " where ent_year = ? and student.class_num = ? and student.school_cd = ?";
		// SQL文のソート
		String order = " order by student.no asc";

		try {
			// プリペアードステートメントにSQL文をセット
			statement = connection.prepareStatement(baseSql + condition + order);
			// プリペアードステートメントに科目番号をバインド
			statement.setString(1, subject.getCd());
			// プリペアードステートメントにテスト回数をバインド
			statement.setInt(2, num);
			// プリペアードステートメントに入学年度をバインド
			statement.setInt(3, entYear);
			// プリペアードステートメントにクラス番号をバインド
			statement.setString(4, classNum);
			// プリペアードステートメントに学校コードをバインド
			statement.setString(5, school.getCd());
			// プリペアードステートメントを実行
			rSet = statement.executeQuery();
			// リストへの格納処理を実行
			list = postFilter(rSet, school);
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

	public boolean save(List<Test> list) throws Exception {

		try {
			for (Test test : list) {
				// コネクションを確立
				Connection connection = getConnection();
				// saveメソッドで情報を保存
				save(test, connection);
			}
		} catch (Exception e) {
			throw e;
		}

		return true;
	}

	private boolean save(Test test, Connection connection) throws Exception {

		// プリペアードステートメント
		PreparedStatement statement = null;
		// 実行件数
		int count = 0;

		try {
			// データベースからテスト情報を取得
			Test old = get(test.getStudent(), test.getSubject(), test.getSchool(), test.getNo());
			if (old == null) {
				// 学生が存在しなかった場合
				// プリペアードステートメントにINSERT文をセット
				statement = connection.prepareStatement("insert into test(student_no, subject_cd, school_cd, no, point, class_num) values(?, ?, ?, ?, ?, ?)");
				// プリペアードステートメントに値をバインド
				statement.setString(1, test.getStudent().getNo());
				statement.setString(2, test.getSubject().getCd());
				statement.setString(3, test.getSchool().getCd());
				statement.setInt(4, test.getNo());
				statement.setInt(5, test.getPoint());
				statement.setString(6, test.getClassNum());
			} else {
				// テストが存在した場合
				// プリペアードステートメントにUPDATE文をセット
				statement = connection.prepareStatement("update test set point = ? where student_no = ? and subject_cd = ? and school_cd = ? and no = ?");
				// プリペアードステートメントに値をバインド
				statement.setInt(1, test.getPoint());
				statement.setString(2, test.getStudent().getNo());
				statement.setString(3, test.getSubject().getCd());
				statement.setString(4, test.getSchool().getCd());
				statement.setInt(5, test.getNo());
			}

			// プリペアードステートメントを実行
			count = statement.executeUpdate();

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

		if (count > 0) {
			// 実行件数が1件以上ある場合
			return true;
		} else {
			// 実行件数が0件の場合
			return false;
		}
	}

}



/* package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Test;

public class TestDao extends Dao {

    private String baseSql="select student.no as student_no, student.name, student.ent_year, student.class_num, student.is_attend, student.school_cd, test.subject_cd, test.no as count, test.point from student left join (select * from test where subject_cd = ? and no = ?) as test on student.no = test.student_no";


    public Test get(Student student, Subject subject, School school, int no) throws Exception {

    	Test test = new Test();
    	SchoolDao sDao = new SchoolDao();
    	StudentDao stDao = new StudentDao();

		Connection connection = getConnection();

		PreparedStatement statement = null;

		try {

			statement = connection.prepareStatement(baseSql + " and student_no = ? and subject_cd = ? and no =?");

			statement.setString(1, school.getCd());
			statement.setString(2, student.getNo());
			statement.setString(3, subject.getCd());
			statement.setInt(4, no);
	ResultSet resultSet = statement.executeQuery();



			if (resultSet.next()) {

				test.setClassNum(resultSet.getString("class_num"));
				test.setNo(resultSet.getInt("no"));
				test.setPoint(resultSet.getInt("point"));
				test.setSchool(sDao.get(resultSet.getString("school_cd")));
				test.setStudent(stDao.get(resultSet.getString("student_num")));



				student.setNo(resultSet.getString("no"));
				student.setName(resultSet.getString("name"));
				student.setEntYear(resultSet.getInt("ent_year"));
				student.setClassNum(resultSet.getString("class_num"));
				student.setAttend(resultSet.getBoolean("is_attend"));

				student.setSchool(sDao.get(resultSet.getString("school_cd")));
			} else {

				student= null;
			}
		} catch (Exception e) {
			throw e;
		} finally {

			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}

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



    public List<Test> postFilter(ResultSet rSet, School school) throws Exception{

        List<Test> ltest = new ArrayList<>();
        StudentDao studentDao = new StudentDao();
        SubjectDao subjectDao = new SubjectDao();

        try {
            while (rSet.next()) {
                Test test = new Test();

                test.setSchool(school);
                test.setStudent(studentDao.get(rSet.getString("STUDENT_NO")));
                test.setSubject(subjectDao.get(rSet.getString("SUBJECT_CD"), school));
                test.setNo(rSet.getInt("NO"));
                test.setPoint(rSet.getInt("POINT"));


                ltest.add(test);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ltest;
    }



   public List<Test> filter(int entYear, String classNum, Subject subject, int num, School school) throws Exception {
	   		List<Test> testList = new ArrayList<>();
    	    Connection connection = getConnection();
    	    PreparedStatement statement = null;
    	    ResultSet resultSet = null;

    	    try {

    	        String sql = baseSql
    	            + " where ent_year = ? AND student.class_num = ? AND subject_cd = ? AND test.no = ? ORDER BY student_no";

    	        statement = connection.prepareStatement(sql);
    	        statement.setString(1, subject.getCd());
    	        statement.setInt(2, num);

    	        statement.setInt(3, entYear);
    	        statement.setString(4, classNum);
    	        statement.setString(5, subject.getCd());

    	        statement.setInt(6, num);

    	        resultSet = statement.executeQuery();

    	        testList = this.postFilter(resultSet, school);

//    	        while (resultSet.next()) {
//    	            Test test = new Test();
//    	            test.setClassNum(resultSet.getString("class_num"));
//    	            test.setNo(resultSet.getString("no"));
//    	            test.setPoint(resultSet.getString("point"));
//    	            test.setSchool(resultSet.getString("school_cd"));
//    	            test.setStudent(resultSet.getString("student_no"));
//    	            testList.add(test);
//    	        }
    	    } catch (Exception e) {
    	        throw new RuntimeException(e);
    	    } finally {
    	        try {
    	            if (resultSet != null) resultSet.close();
    	            if (statement != null) statement.close();
    	            if (connection != null) connection.close();
    	        } catch (SQLException e) {
    	            throw new RuntimeException(e);
    	        }
    	    }

    	    return testList;
    	}



   public boolean save(List<Test> list) {
	    if (list == null || list.isEmpty()) return true;

	    Connection connection = null;
	    PreparedStatement statement = null;

	    try {
	        connection = getConnection();


	        connection.setAutoCommit(false);

	        String sql = "INSERT INTO TEST (school_cd, student_no, subject_cd, no, point, class_num) "
	                   + "VALUES (?, ?, ?, ?, ?, ?) "
	                   + "ON DUPLICATE KEY UPDATE point = VALUES(point), class_num = VALUES(class_num)";

	        statement = connection.prepareStatement(sql);

	        for (Test test : list) {
	            statement.setString(1, test.getSchool().getCd());
	            statement.setString(2, test.getStudent().getNo());
	            statement.setString(3, test.getSubject().getCd());
	            statement.setLong(4, test.getNo());
	            statement.setLong(5, test.getPoint());
	            statement.setString(6, test.getClassNum());

	            statement.addBatch();
	        }

	        statement.executeBatch();
	        connection.commit();

	    } catch (Exception e) {

	        if (connection != null) {
	            try {
	                connection.rollback();
	            } catch (SQLException se) {
	                throw new RuntimeException(se);
	            }
	        }
	        throw new RuntimeException(e);

	    } finally {

	        try {
	            if (statement != null) statement.close();
	            if (connection != null) connection.close();
	        } catch (SQLException e) {
	            throw new RuntimeException(e);
	        }
	    }

	    return true;
	}


   public boolean save(Test test, Connection connection) throws Exception {
	    PreparedStatement statement = null;

	    try {

	        String sql = "INSERT INTO TEST (school_cd, student_no, subject_cd, no, point, class_num) "
	                   + "VALUES (?, ?, ?, ?, ?, ?) "
	                   + "ON DUPLICATE KEY UPDATE point = VALUES(point), class_num = VALUES(class_num)";

	        statement = connection.prepareStatement(sql);

	        statement.setString(1, test.getSchool().getCd());
	        statement.setString(2, test.getStudent().getNo());
	        statement.setString(3, test.getSubject().getCd());
	        statement.setInt(4, test.getNo());
	        statement.setInt(5, test.getPoint());
	        statement.setString(6, test.getClassNum());

	        int result = statement.executeUpdate();

	        return result > 0;

	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    } finally {
	        try {
	            if (statement != null) statement.close();
	        } catch (SQLException e) {
	            throw new RuntimeException(e);
	        }
	    }
	}
} */