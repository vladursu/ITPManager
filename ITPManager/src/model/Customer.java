package model;

import org.joda.time.LocalDate;

public interface Customer {

	/**
	 * Get the customer's ID.
	 *
	 * @return the customer's ID
	 */
	public Integer getId();

	/**
	 * Set the customer's ID.
	 *
	 * @param id
	 *            the ID of the customer.
	 */
	public void setId(Integer id);

	/**
	 * Get the customer's name.
	 *
	 * @return the customer's name.
	 */
	public String getName();

	/**
	 * Set the customer's name.
	 *
	 * @param name
	 *            the name of the customer.
	 */
	public void setName(String name);

	/**
	 * Get the customer's car model.
	 *
	 * @return the customer's car model.
	 */
	public String getCarModel();

	/**
	 * Set the customer's car model.
	 *
	 * @param car_model
	 *            the car model of the customer.
	 */
	public void setCarModel(String car_model);

	/**
	 * Get the customer's registration id.
	 *
	 * @return the customer's registration id.
	 */
	public String getRegistId();

	/**
	 * Set the customer's registration id.
	 *
	 * @param regist_id
	 *            the registration id of the customer.
	 */
	public void setRegistId(String regist_id);

	/**
	 * Get the customer's email.
	 *
	 * @return the customer's email.
	 */
	public String getEmail();

	/**
	 * Set the customer's email address.
	 *
	 * @param email
	 *            the email address of the customer.
	 */
	public void setEmail(String email);

	/**
	 * Get the customer's phone number.
	 *
	 * @return the customer's phone number.
	 */
	public String getPhoneNr();

	/**
	 * Set the customer's phone number.
	 *
	 * @param phone_nr
	 *            the phone number of the customer.
	 */
	public void setPhoneNr(String phone_nr);

	/**
	 * Get the customer's ITP end date.
	 *
	 * @return the customer's ITP end date.
	 */
	public LocalDate getITPEndDate();

	/**
	 * Set the customer's ITP end date.
	 *
	 * @param itp_end_date
	 *            the ITP end date of the customer.
	 */
	public void setITPEndDate(LocalDate itp_end_date);

	/**
	 * Get the state of the customer. Either true if they have been notified via
	 * email or false otherwise.
	 *
	 * @return the status indicator.
	 */
	public Boolean getEmailSent();

	/**
	 * Set the customer's state. Either true if they have been notified via
	 * email or false otherwise.
	 *
	 * @param email_sent
	 *            the status indicator.
	 */
	public void setEmailSent(Boolean email_sent);

	/**
	 * Get other comments about this customer
	 *
	 * @return comments about customer
	 */
	public String getOther();

	/**
	 * Set the comments for this customer.
	 *
	 * @param other
	 *            the comments about the customer.
	 */
	public void setOther(String other);

}
