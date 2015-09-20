package us.magicuser.standaloneejb.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Id;

/**
 * Entity implementation class for Entity: Employee
 *
 */
@Entity
@Table(name="EMPLOYEE")
public class Employee implements Serializable {
	@Id
	@Column(name="EMP_ID")
	private Integer id;
	
	@Column(name="FNAME")
	private String firstName;
	
	@Column(name="LNAME")
	private String lastName;
	
	@ManyToOne
	@JoinColumn(name="DEPT_ID", nullable=false)
	private Department department;
	
	private static final long serialVersionUID = 1L;

	public Employee() {
		super();
	}   
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}   
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}   
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}   

}
