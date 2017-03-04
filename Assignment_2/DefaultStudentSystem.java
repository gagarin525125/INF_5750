package no.uio.inf5750.assignment2.service.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import no.uio.inf5750.assignment2.dao.CourseDAO;
import no.uio.inf5750.assignment2.dao.StudentDAO;
import no.uio.inf5750.assignment2.dao.hibernate.HibernateCourseDao;
import no.uio.inf5750.assignment2.model.Course;
import no.uio.inf5750.assignment2.model.Student;
import no.uio.inf5750.assignment2.service.StudentSystem;

@Component("DefaultStudentSystem")

@Transactional
@EnableTransactionManagement
public class DefaultStudentSystem implements StudentSystem {

	@Autowired
	CourseDAO hibernateCourseDao;
	@Autowired
	StudentDAO hibernateStudentDao;

	public void setStudentLocation(int studentId, String latitude, String longitude) {

		Student s = getStudent(studentId);
		s.setLatitude(latitude);
		s.setLongitude(longitude);
		hibernateStudentDao.saveStudent(s);

	}

	public int addCourse(String courseCode, String name) {
		Course c = new Course(courseCode, name);
		return hibernateCourseDao.saveCourse(c);
	}

	public void updateCourse(int courseId, String courseCode, String name) {
		Course course = getCourse(courseId);
		if (course == null)
			return;
		course.setCourseCode(courseCode);
		course.setName(name);
		hibernateCourseDao.saveCourse(course);
	}

	public Course getCourse(int courseId) {
		return hibernateCourseDao.getCourse(courseId);
	}

	public Course getCourseByCourseCode(String courseCode) {
		return hibernateCourseDao.getCourseByCourseCode(courseCode);
	}

	public Course getCourseByName(String name) {

		return hibernateCourseDao.getCourseByName(name);
	}

	public Collection<Course> getAllCourses() {
		return hibernateCourseDao.getAllCourses();
	}

	public void delCourse(int courseId) {

		Course courseToDelete = getCourse(courseId);

		Collection<Student> students = getAllStudents();
		for (Student student : students) {

			student.getCourses().remove(courseToDelete);

		}
		Course course = getCourse(courseId);
		hibernateCourseDao.delCourse(course);
	}

	public void addAttendantToCourse(int courseId, int studentId) {
		Course course = getCourse(courseId);
		Student student = getStudent(studentId);
		Set<Student> attendants = course.getAttendants();

		attendants.add(student);
		student.getCourses().add(course);
		hibernateCourseDao.saveCourse(course);
		hibernateStudentDao.saveStudent(student);
	}

	public void removeAttendantFromCourse(int courseId, int studentId) {

		hibernateCourseDao.getCourse(courseId).getAttendants().remove(hibernateStudentDao.getStudent(studentId));
		hibernateStudentDao.getStudent(studentId).getCourses().remove(hibernateCourseDao.getCourse(courseId));

	}

	public int addStudent(String name) {
		return hibernateStudentDao.saveStudent(new Student(name));
	}

	public void updateStudent(int studentId, String name) {

		Student student = getStudent(studentId);
		student.setName(name);
		hibernateStudentDao.saveStudent(student);

	}

	public Student getStudent(int studentId) {
		return hibernateStudentDao.getStudent(studentId);
	}

	public Student getStudentByName(String name) {
		return hibernateStudentDao.getStudentByName(name);
	}

	public Collection<Student> getAllStudents() {

		return hibernateStudentDao.getAllStudents();

	}

	public void delStudent(int studentId) {

		hibernateStudentDao.delStudent(hibernateStudentDao.getStudent(studentId));
	}

}
