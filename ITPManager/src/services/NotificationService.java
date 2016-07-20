package services;

import java.util.List;

import model.Customer;

public interface NotificationService {

	/**
	 * Notifies the customer about their ITP being close to its expiration date.
	 * 
	 * @param customer
	 *            the customer that needs to be notified.
	 */
	public void notifyCustomer(Customer customer);

	/**
	 * Notifies a list of customers about their ITP being close to its
	 * expiration date.
	 * 
	 * @param customers
	 *            the list of customers that needs to be notified.
	 */
	public void notifyCustomers(List<Customer> customers);

}
