package no.uio.inf5750.assignment2.dao.hibernate;

import java.util.Collection;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.mapping.List;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;
import org.apache.log4j.Logger;

import no.uio.inf5750.assignment2.dao.CourseDAO;
import no.uio.inf5750.assignment2.model.Course;
import no.uio.inf5750.assignment2.model.Student;
import no.uio.inf5750.assignment2.dao.StudentDAO;

@Transactional
public class HibernateCourseDao implements CourseDAO {

	static Logger logger = Logger.getLogger(HibernateCourseDao.class);
	public SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Persists a course. An unique id is generated if the object is persisted
	 * for the first time, and which is both set in the given course object and
	 * returned.
	 * 
	 * @param course
	 *            the course to add for persistence.
	 * @return the id of the course.
	 */

	public int saveCourse(Course course) {

		return (Integer) sessionFactory.getCurrentSession().save(course);
	}

	/**
	 * Returns a course.
	 * 
	 * @param id
	 *            the id of the course to return.
	 * @return the course or null if it doesn't exist.
	 */

	public Course getCourse(int id) {
		return (Course) sessionFactory.getCurrentSession().get(Course.class, id);
	}

	/**
	 * Returns a course with a specific course code.
	 * 
	 * @param courseCode
	 *            the course code of the course to return.
	 * @return the course code or null if it doesn't exist.
	 */

	public Course getCourseByCourseCode(String courseCode) {
		return (Course) sessionFactory.getCurrentSession().get(Course.class, courseCode);
	}

	/**
	 * Returns a course with a specific name.
	 * 
	 * @param courseCode
	 *            the course code of the course to return.
	 * @return the course code or null if it doesn't exist.
	 */
	public Course getCourseByName(String name) {

		return (Course) sessionFactory.getCurrentSession().get(Course.class, name);

	}

	/**
	 * Returns all courses.
	 * 
	 * @return all courses.
	 */
	public Collection<Course> getAllCourses() {

		return sessionFactory.getCurrentSession().createCriteria(Course.class).list();
	}

	/**
	 * Deletes a course.
	 * 
	 * @param course
	 *            the course to delete.
	 */
	public void delCourse(Course course) {
		Session session = sessionFactory.getCurrentSession();
		Course courseToDelete = session.get(Course.class, course.getId());
		session.delete(courseToDelete);

	}
}
