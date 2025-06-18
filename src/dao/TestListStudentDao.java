package dao;

import java.sql.ResultSet;
import java.util.List;

import bean.Student;
import bean.TestListStudent;

public class TestListStudentDao extends Dao {

	private String baseSql = "select * from student where school_cd = ?";

	private List<TestListStudent> postFilter(ResultSet rSet) throws Exception {
		return null;
	}

	public List<TestListStudent> filter(Student student) throws Exception {
		return null;
	}
}
