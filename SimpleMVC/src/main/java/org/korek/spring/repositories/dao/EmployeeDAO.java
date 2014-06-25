package org.korek.spring.repositories.dao;

import org.korek.spring.repositories.dao.base.BaseDAO;
import org.korek.spring.repositories.dao.interfaces.IEmployeeDAO;
import org.korek.spring.repositories.entities.Employee;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeDAO extends BaseDAO<Employee> implements IEmployeeDAO
{

	public EmployeeDAO()
	{
		super(Employee.class);
	}

}
