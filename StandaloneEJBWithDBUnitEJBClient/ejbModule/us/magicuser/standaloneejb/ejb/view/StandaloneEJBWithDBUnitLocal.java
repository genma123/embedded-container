package us.magicuser.standaloneejb.ejb.view;

import us.magicuser.standaloneejb.jpa.entity.Employee;

public interface StandaloneEJBWithDBUnitLocal {
	
    public Employee addEmployee(String firstName, String lastName);
    
}
