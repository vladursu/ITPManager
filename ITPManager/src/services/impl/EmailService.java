package services.impl;

import java.util.List;

import model.Customer;
import services.NotificationService;

public class EmailService implements NotificationService {

	// TODO need to read the list of staff email addresses from file and template messages for customers and staff (look into JSON).
	// TODO need parameters for those and initialisation in constructor. Also look into SMTP service.
	
	@Override
	public void notifyCustomer(Customer customer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyCustomers(List<Customer> customers) {
		// TODO Auto-generated method stub

	}
	
	private void notifyStaff(List<Customer> customers) {
		// TODO email the staff about the customer(s) that have been noticed.
	}

}
