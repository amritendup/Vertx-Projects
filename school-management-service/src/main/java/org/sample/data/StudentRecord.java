package org.sample.data;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

@DataObject
public class StudentRecord {

	private String studentId;
	
	private String fistName;
	
	private String lastName;
	
	private String email;
	
	private String grade;

	
	public StudentRecord() {
		
	}
	
	
	public StudentRecord(String studentId, String fistName, String lastName, String email, String grade) {
		super();
		this.studentId = studentId;
		this.fistName = fistName;
		this.lastName = lastName;
		this.email = email;
		this.grade = grade;
	}
	
	public StudentRecord(JsonObject json) {
	
		this.studentId = json.getString("student_id");
		this.fistName = json.getString("fist_name");
		this.lastName = json.getString("last_name");
		this.email = json.getString("email");
		this.grade = json.getString("grade");
	}


	public String getStudentId() {
		return studentId;
	}


	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}


	public String getFistName() {
		return fistName;
	}


	public void setFistName(String fistName) {
		this.fistName = fistName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getGrade() {
		return grade;
	}


	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	public JsonObject toJson() {
		return JsonObject.mapFrom(this);
	}
	
	@Override
	public String toString() {
		return Json.encodePrettily(this);
	}
	
}
