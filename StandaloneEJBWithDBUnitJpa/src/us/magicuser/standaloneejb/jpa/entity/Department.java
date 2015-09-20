package us.magicuser.standaloneejb.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

/**
 * Entity implementation class for Entity: Department
 *
 */
@Entity
@Table(name="DEPARTMENT")
public class Department implements Serializable {
	
	private static final long serialVersionUID = -1713538372883602705L;

	@Id
	@Column(name="DEPT_ID")
	private Integer id;
	
	@Column(name="NAME")
	private String name;

	public Department() {
		super();
	}   
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}   
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
