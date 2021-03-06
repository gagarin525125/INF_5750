package no.uio.inf5750.assignment2.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.*;
import org.springframework.transaction.annotation.Transactional;
import no.uio.inf5750.assignment2.model.*;
import no.uio.inf5750.assignment2.dao.CourseDAO;
import no.uio.inf5750.assignment2.dao.StudentDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/META-INF/assignment2/beans.xml" })
@Transactional
public class StudentSystemTest {

	@Autowired
	private CourseDAO courseDAO;
	@Autowired
	private StudentDAO studentDAO;

	@Autowired
	private StudentSystem studentSystem;

	private int catchCourseId(String str) {
		Collection<Course> collection = studentSystem.getAllCourses();
		for (Course course : collection) {
			if (course.getCourseCode().equals(str))
				return course.getId();
			if (course.getName().equals(str))
				return course.getId();
		}
		return -1;
	}

	private int catchStudent(String str) {
		Collection<Student> collection = studentSystem.getAllStudents();
		for (Student student : collection) {
			if (student.getName().equals(str))
				return student.getId();
		}
		return -1;
	}

	/**
	 * tests both addCourse and delCourse
	 */
	@Test
	public void testAddCourseDelCourse() {
		studentSystem.addCourse("INF5750", "Open Source Development");
		studentSystem.addCourse("INF5761", "Health management information systems");
		ArrayList<Integer> list = new ArrayList<>();

		for (Course course : studentSystem.getAllCourses()) {
			int j = course.getId();
			list.add(j);
		}
		for (int i = 0; i < list.size(); i++) {
			Course course = studentSystem.getCourse(list.get(i));
			studentSystem.delCourse(course.getId());

		}
		Collection<Course> collection = studentSystem.getAllCourses();
		assertEquals(0, collection.size());
	}

	public void reset() {
		ArrayList<Integer> list = new ArrayList<>();

		for (Course course : studentSystem.getAllCourses()) {
			int j = course.getId();
			list.add(j);
		}
		for (int i = 0; i < list.size(); i++) {
			Course course = studentSystem.getCourse(list.get(i));
			studentSystem.delCourse(course.getId());

		}
		Collection<Course> collection = studentSystem.getAllCourses();
		// assertEquals(0, collection.size());
	}

	@Test
	public void testAddCourse() throws Exception {
		reset();
		int a = studentSystem.addCourse("INF5750", "Open Source Development");
		int b = catchCourseId("INF5750");
		assertEquals(a, b);
		a = studentSystem.addCourse("INF5761", "Health management information systems");
		b = catchCourseId("Health management information systems");
		assertEquals(a, b);

	}

	@Test

	public void testGetCourse() throws Exception {
		reset();
		int a = studentSystem.addCourse("INF5750", "Open Source Development");
		Course course = studentSystem.getCourse(a);

		assertEquals(a, course.getId());

	}

	@Test
	public void testGetCourseByCouseCode() throws Exception {

		String courseCode = null;
		String courseName = null;
		Course course = null;
		int courseId = -1;
		int idFrom = -1;
		try {
			courseCode = "8888";
			courseName = "Spring";
			courseId = studentSystem.addCourse(courseCode, courseName);
			idFrom = catchCourseId(courseCode);
			assertEquals(courseId, idFrom);
			course = studentSystem.getCourse(idFrom);
		} catch (Exception e) {
			System.err.println(" error in getCourseByCourseCode  " + e.getMessage());
			e.printStackTrace();
		}

		assertNotNull(course);
		assertEquals(courseId, course.getId());
		assertEquals(courseCode, course.getCourseCode());

	}

	@Test
	public void testGetCourseByCourseName() throws Exception {

		String courseCode = "2222";
		String courseName = "Maven";
		studentSystem.addCourse(courseCode, courseName);
		int a = catchCourseId(courseName);
		Course course = studentSystem.getCourse(a);
		assertNotNull(course);
		assertEquals(courseName, course.getName());

	}

	@Test
	public void testGetStudentId() throws Exception {

		String studentName = "Vladimir";
		int studentId = studentSystem.addStudent(studentName);
		Student student = studentSystem.getStudent(studentId);
		assertNotNull(student);
		assertEquals(studentId, student.getId());

	}

	@Test
	public void testGetStudentByName() throws Exception {

		String studentName = "Ivan";
		int studentId = studentSystem.addStudent(studentName);
		// Student student = studentSystem.getStudentByName( studentName );
		int fromId = catchStudent(studentName);
		Student student = studentSystem.getStudent(fromId);
		assertNotNull(student);
		assertEquals(studentId, student.getId());
		assertEquals(studentName, student.getName());
	}

	@Test
	public void testAddStudent() throws Exception {

		int a = studentSystem.addStudent("Per");
		assertNotNull(a);
		if (a > 0)
			assertTrue(true);
		else
			assertTrue(false);
		int b = catchStudent("Per");
		assertEquals((studentSystem.getStudent(b)).getName(), "Per");

	}

	@Test
	public void testGetStudent() throws Exception {

		int studentId = studentSystem.addStudent("Lena");
		assertNotNull(studentId);
		assertEquals(studentId, studentSystem.getStudent(studentId).getId());

	}

	@Test
	public void testAddAttendantToCourse() throws Exception {

		int courseId = studentSystem.addCourse("333", "Foo");
		int studentId = studentSystem.addStudent("Alex");
		studentSystem.addAttendantToCourse(courseId, studentId);
		Student student = studentSystem.getStudent(studentId);
		Collection<Course> courses = student.getCourses();
		assertNotNull(courses);
		assertEquals(1, courses.size());

	}

	@Test
	public void testRemoveAttendantFromCourse() throws Exception {

		int aCourse = studentSystem.addCourse("555", "Programming");
		int bCourse = studentSystem.addCourse("777", "Sports");
		int cCourse = studentSystem.addCourse("999", "Culture");
		int aStudent = studentSystem.addStudent("Ola");
		int bStudent = studentSystem.addStudent("Bull");
		studentSystem.addAttendantToCourse(aCourse, aStudent);
		studentSystem.addAttendantToCourse(aCourse, bStudent);
		studentSystem.addAttendantToCourse(bCourse, aStudent);
		studentSystem.addAttendantToCourse(cCourse, bStudent);

		studentSystem.removeAttendantFromCourse(aCourse, bStudent);
		Course courseToCheck = studentSystem.getCourse(aCourse);
		assertEquals(1, courseToCheck.getAttendants().size());
	}

	@Test
	public void testUpdateCourse() throws Exception {
		String courseC = "1234";
		String courseN = "Ballett";
		int courseId = studentSystem.addCourse(courseC, courseN);
		Course course = studentSystem.getCourse(courseId);
		assertNotNull(course);
		assertEquals(courseC, course.getCourseCode());

		String courseUpdatedN = "Test";
		course.setCourseCode(courseUpdatedN);
		studentSystem.updateCourse(courseId, courseC, courseUpdatedN);
		Course updatedCourse = studentSystem.getCourse(courseId);
		assertNotNull(updatedCourse);
		assertEquals(courseUpdatedN, updatedCourse.getName());

	}

	@Test
	public void testUpdateStudent() throws Exception {
		String studentName = "Microsoft";
		int studentId = studentSystem.addStudent(studentName);
		Student student = studentSystem.getStudent(studentId);
		assertNotNull(student);
		assertEquals(studentName, student.getName());

		String updatedName = "Javascript";
		student.setName(updatedName);
		studentSystem.updateStudent(studentId, updatedName);
		Student updatedStudent = studentSystem.getStudent(studentId);
		assertNotNull(updatedStudent);
		assertEquals(updatedName, updatedStudent.getName());
	}

	@Test
	public void testSetStudentLocation() throws Exception {

		int notInUse = studentSystem.addStudent("Peter");
		int a = catchStudent("Peter");
		String lat = "9999";
		String lon = "8888";
		studentSystem.setStudentLocation(a, lat, lon);
		Student student = studentSystem.getStudent(a);

		assertNotNull(student);
		assertEquals(lat, student.getLatitude());
		assertEquals(lon, student.getLongitude());

	}
}