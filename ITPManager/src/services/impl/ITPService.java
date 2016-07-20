package services.impl;
import java.sql.Connection;
import java.util.List;

import model.Customer;
import services.JdbcService;

public class ITPService implements JdbcService {

	private Connection db;
	
	public ITPService(Connection db) {
		this.db = db;
	}
	
	public Connection getConnection() {
		return this.db;
	}
	
	@Override
	public List<Customer> getCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addCustomer(Customer customer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void editCustomer(Integer id, Customer new_customer) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Customer> searchForCustomers(String attribute, String value) {
		// TODO Auto-generated method stub
		return null;
	}



	

}
