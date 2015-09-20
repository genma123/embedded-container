package us.magicuser.standaloneejb.ejb;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import us.magicuser.standaloneejb.ejb.view.StandaloneEJBWithDBUnitLocal;
import us.magicuser.standaloneejb.jpa.entity.Department;
import us.magicuser.standaloneejb.jpa.entity.Employee;

/**
 * Session Bean implementation class StandaloneEJBWithDBUnit
 */
@Stateless(name="StandaloneEJBWithDBUnit")
@Local(StandaloneEJBWithDBUnitLocal.class)
public class StandaloneEJBWithDBUnit implements StandaloneEJBWithDBUnitLocal {
	
	@PersistenceContext(unitName="StandaloneEJBWithDBUnitJpa")
	private EntityManager em;

    /**
     * Default constructor. 
     */
    public StandaloneEJBWithDBUnit() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public Employee addEmployee(String firstName, String lastName) {
    	Employee emp = em.find(Employee.class, 1);
    	System.err.println("emp:\n" + ToStringBuilder.reflectionToString(emp,ToStringStyle.MULTI_LINE_STYLE));
    	Department dept = em.find(Department.class, 2);
    	System.err.println("dept:\n" + ToStringBuilder.reflectionToString(dept,ToStringStyle.MULTI_LINE_STYLE));
    	Employee newEmp = new Employee();
    	newEmp.setFirstName(firstName);
    	newEmp.setLastName(lastName);
    	newEmp.setDepartment(dept);
    	newEmp.setId(4);
    	em.persist(newEmp);
    	return newEmp;
    }
}
