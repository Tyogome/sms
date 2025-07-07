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
                test.setStudent(studentDao.get(rSet.getString("STUDENT_CD")));
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
    	            + " AND ent_year = ? AND class_num = ? AND subject_cd = ? AND no = ? ORDER BY student_no";

    	        statement = connection.prepareStatement(sql);
    	        statement.setString(1, school.getCd());
    	        statement.setInt(2, entYear);
    	        statement.setString(3, classNum);
    	        statement.setString(4, subject.getCd());
    	        statement.setInt(5, num);

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
}