package services;

import java.sql.Connection;
import java.util.List;

import model.Customer;

public interface JdbcService {

	/**
	 * Get the database connection.
	 * 
	 * @return a JDBC database connection.
	 */
	public Connection getConnection();

	/**
	 * Get the list of customers from the database.
	 * 
	 * @return the list of customers.
	 */
	public List<Customer> getCustomers();

	/**
	 * Add a customer to the database.
	 * 
	 * @param customer
	 *            the customer to be added to the database.
	 */
	public void addCustomer(Customer customer);

	/**
	 * Edit the customer with given id to have the attributes of the new
	 * customer.
	 * 
	 * @param id
	 *            the id of the customer to be modified.
	 * @param new_customer
	 *            the customer containing the attributes that need to be
	 *            modified. Null value for attribute means the attribute must
	 *            remain unchanged.
	 */
	public void editCustomer(Integer id, Customer newCustomer);

	/**
	 * Search for customers that, for a certain criteria, they have or contain a
	 * certain value.
	 * 
	 * @param attribute
	 *            the attribute by which the search is made.
	 * @param value
	 *            the value we are looking for (used in a "LIKE" statement).
	 * @return The list of customers that meet the criteria.
	 */
	public List<Customer> searchForCustomers(String attribute, String value);

	/**
	 * Delete the customer with the given id from the database.
	 * 
	 * @param id
	 *            the ID of the customer to be deleted.
	 */
	public void deleteCustomer(Integer id);

}
