package no.uio.inf5750.assignment2.gui.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import no.uio.inf5750.assignment2.model.Student;
import no.uio.inf5750.assignment2.model.Course;
import no.uio.inf5750.assignment2.service.StudentSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Controller

public class ApiController {

	static Logger logger = Logger.getLogger(ApiController.class);

	@Autowired
	private StudentSystem studentSystem;

	@RequestMapping(value = "/api/student", method = RequestMethod.GET)
	// @JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class,property="@id")
	@ResponseBody
	public Collection<Student> getAllStudents() {
		System.err.println(" inside getAllStudents ApiController  path  /api/student");
		Collection<Student> studList = studentSystem.getAllStudents();
		for(Student s: studList) {
		System.out.print("\n\n\n\n" + s.getCourses().size());
		}
		return studList;
	}

	@RequestMapping(value = "/api/student/{student:.+}", method = RequestMethod.GET)
	@ResponseBody
	public Collection<Student> setLocation(@PathVariable("student") int student

			, @RequestParam(value = "latitude") String latitude, @RequestParam(value = "longitude") String longitude) {
		System.err.println("inside setLocation API controller + student = " + student + "  latitude = " + latitude
				+ " longitude = " + longitude);
		studentSystem.setStudentLocation(student, latitude, longitude);
		return studentSystem.getAllStudents();
	}

	@RequestMapping(value = "/api/course", method = RequestMethod.GET)
	// @JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class,property="@id")
	@ResponseBody
	public Collection<Course> getAllCourses() {
		return studentSystem.getAllCourses();
	}
}

/// location?latitude={}&longitude={longitude}
