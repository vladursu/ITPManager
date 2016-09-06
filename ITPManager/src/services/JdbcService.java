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
	 * Get the customer with the given id.
	 * 
	 * @param id
	 *            the ID of the customer that needs to be fetched from the
	 *            database
	 * @return the Customer object consturcted with data from the database
	 */
	public Customer getCustomer(Integer id);

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
	 * Edit the customer fully, such that the customer with the ID given inside
	 * the customer object changes all its values in the database according to
	 * the fields in the customer object
	 * 
	 * @param customer
	 *            the customer object which holds the ID of the entry that needs
	 *            updated and all the new values
	 */
	public void editCustomer(Customer customer);

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

	/**
	 * Outputs the customers in a formatted string which makes the table easy to
	 * read
	 * 
	 * @param customers
	 *            a list of the customers that need to be printed
	 * @return the formatted string with all the customers' details from the
	 *         provided list
	 */
	public String formattedString(List<Customer> customers);

	/**
	 * Provides a list of customers that will have their ITP expire soon,
	 * ordered descending, from closest expiration date to farthest, up to
	 * 'days' days
	 * 
	 * @param days
	 *            The maximum number of days remaining from current date to
	 *            expiration date, used to select the customers with the closest
	 *            ITP end dates
	 * @return a list of customers ordered descending on ITP end date
	 */
	public List<Customer> getNotifCustomers(int days);

	/**
	 * Updates the email_sent field for the customer with the given id
	 * 
	 * @param id
	 *            the id of the customer to be modified
	 * @param notified
	 *            the new state desired for the binary field email_sent
	 */
	public void updateNotified(Integer id, Boolean notified);

	/**
	 * Returns a list of customers sorted by the given attribute and ordered by
	 * the given order.
	 * 
	 * @param attribute
	 *            the attribute to sort by.
	 * @param order
	 *            the order in which to be sorted.
	 * @return a list of customers sorted as specified by the parameters.
	 */
	public List<Customer> getSortedCustomers(String attribute, String order);
}
