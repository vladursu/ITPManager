package model.impl;

import org.joda.time.LocalDate;

import model.Customer;

public class CustomerImpl implements Customer {

	private Integer id;
	private String name;
	private String car_model;
	private String regist_id;
	private String email;
	private String phone_nr;
	private LocalDate itp_end_date;
	private Boolean email_sent;
	private String other;

	public CustomerImpl(Integer id, String name, String car_model, String regist_id, String email, String phone_nr,
			LocalDate itp_end_date, Boolean email_sent, String other) {
		super();
		this.id = id;
		this.name = name;
		this.car_model = car_model;
		this.regist_id = regist_id;
		this.email = email;
		this.phone_nr = phone_nr;
		this.itp_end_date = itp_end_date;
		this.email_sent = email_sent;
		this.other = other;
	}

	public CustomerImpl() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCarModel() {
		return car_model;
	}

	public void setCarModel(String car_model) {
		this.car_model = car_model;
	}

	public String getRegistId() {
		return regist_id;
	}

	public void setRegistId(String regist_id) {
		this.regist_id = regist_id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNr() {
		return phone_nr;
	}

	public void setPhoneNr(String phone_nr) {
		this.phone_nr = phone_nr;
	}

	public LocalDate getITPEndDate() {
		return itp_end_date;
	}

	public void setITPEndDate(LocalDate itp_end_date) {
		this.itp_end_date = itp_end_date;
	}

	public Boolean getEmailSent() {
		return email_sent;
	}

	public void setEmailSent(Boolean email_sent) {
		this.email_sent = email_sent;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	};

}
