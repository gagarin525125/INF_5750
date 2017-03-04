package no.uio.inf5750.assignment2.dao.hibernate;

import java.util.Collection;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;
import org.apache.log4j.Logger;

import no.uio.inf5750.assignment2.dao.CourseDAO;
import no.uio.inf5750.assignment2.model.Course;
import no.uio.inf5750.assignment2.model.Student;
import no.uio.inf5750.assignment2.dao.StudentDAO;

@Transactional
public class HibernateStudentDao implements StudentDAO {

	static Logger logger = Logger.getLogger(HibernateCourseDao.class);
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Persists a student. An unique id is generated if the object is persisted
	 * for the first time, and which is both set in the given student object and
	 * returned.
	 * 
	 * @param student
	 *            the student to add for persistence.
	 * @return the id of the student.
	 */
	public int saveStudent(Student student) {
		return (Integer) sessionFactory.getCurrentSession().save(student);
	}

	/**
	 * Returns a student.
	 * 
	 * @param id
	 *            the id of the student to return.
	 * @return the student or null if it doesn't exist.
	 */
	public Student getStudent(int id) {
		return (Student) sessionFactory.getCurrentSession().get(Student.class, id);
	}

	/**
	 * Returns a student with a specific name.
	 * 
	 * @param name
	 *            the name of the student to return.
	 * @return the student or null if it doesn't exist.
	 */
	public Student getStudentByName(String name) {
		return (Student) sessionFactory.getCurrentSession().get(Student.class, name);

	}

	/**
	 * Returns all students.
	 * 
	 * @return all students.
	 */
	public Collection<Student> getAllStudents() {

		Session session = sessionFactory.getCurrentSession();

		return session.createCriteria(Student.class).list();
	}

	/**
	 * Deletes a student.
	 * 
	 * @param student
	 *            the student to delete.
	 */
	@Transactional
	public void delStudent(Student student) {

		if (student == null)
			return;

		Session session = sessionFactory.getCurrentSession();

		Student studentToDelete = session.get(Student.class, student.getId());
		session.delete(studentToDelete);
	}

}
