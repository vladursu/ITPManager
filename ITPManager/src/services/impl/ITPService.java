package services.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import model.Customer;
import model.impl.CustomerImpl;
import services.JdbcService;

public class ITPService implements JdbcService {

	private Connection db;

	public ITPService(Connection db) {
		this.db = db;
	}

	public Connection getConnection() {
		return this.db;
	}

	public Customer getCustomer(Integer id) {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = this.getConnection().prepareStatement("SELECT * FROM customers WHERE id = ?");
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			rs.next();

			return customerFromRS(rs);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}

				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public List<Customer> getCustomers() {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Customer> customers = new ArrayList<Customer>();

		try {
			stmt = this.getConnection().prepareStatement("SELECT * FROM customers ORDER BY id");
			rs = stmt.executeQuery();

			while (rs.next()) {
				customers.add(customerFromRS(rs));
			}

			return customers;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}

				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}

	}

	@Override
	public List<Customer> getSortedCustomers(String attribute, String order) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Customer> customers = new ArrayList<Customer>();

		try {
			String sql = "SELECT * FROM customers ORDER BY " + attribute;
			if (order.equals("desc")) {
				sql += " DESC";
			}

			stmt = this.getConnection().prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				customers.add(customerFromRS(rs));
			}

			return customers;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}

				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}

	}

	@Override
	public void addCustomer(Customer customer) {
		PreparedStatement stmt = null;

		try {
			stmt = this.getConnection()
					.prepareStatement("INSERT INTO customers VALUES (default, ?, ?, ?, ?, ?, ?, ?, ?)");
			stmt.setString(1, customer.getName());
			stmt.setString(2, customer.getCarModel());
			stmt.setString(3, customer.getRegistId());
			stmt.setString(4, customer.getEmail());
			stmt.setString(5, customer.getPhoneNr());
			stmt.setDate(6, java.sql.Date.valueOf(customer.getITPEndDate().toString()));
			stmt.setBoolean(7, customer.getEmailSent());
			stmt.setString(8, customer.getOther());

			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new IllegalArgumentException(e);
		} finally {
			try {
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}

	}

	@Override
	public void updateNotified(Integer id, Boolean notified) {
		PreparedStatement stmt = null;
		try {
			stmt = this.getConnection().prepareStatement("UPDATE customers SET email_sent = ? WHERE id = ?");
			stmt.setBoolean(1, notified);
			stmt.setInt(2, id);

			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new IllegalArgumentException(e);
		} finally {
			try {
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void editCustomer(Customer customer) {
		PreparedStatement stmt = null;
		try {
			stmt = this.getConnection().prepareStatement(
					"UPDATE customers SET name = ?, car_model = ?, registration_id = ?, email = ?, phone_number = ?, itp_end_date = ?, email_sent = ?, other = ? WHERE id = ?");
			stmt.setString(1, customer.getName());
			stmt.setString(2, customer.getCarModel());
			stmt.setString(3, customer.getRegistId());
			stmt.setString(4, customer.getEmail());
			stmt.setString(5, customer.getPhoneNr());
			stmt.setDate(6, java.sql.Date.valueOf(customer.getITPEndDate().toString()));
			stmt.setBoolean(7, customer.getEmailSent());
			stmt.setString(8, customer.getOther());
			stmt.setInt(9, customer.getId());

			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new IllegalArgumentException(e);
		} finally {
			try {
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public List<Customer> searchForCustomers(String attribute, String value) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Customer> results = new ArrayList<Customer>();

		try {
			if (attribute.equals("id")) {
				stmt = this.getConnection().prepareStatement("SELECT * FROM customers WHERE " + attribute + " = ?");
				int val = 0;
				try {
					val = Integer.parseInt(value);
				} catch (NumberFormatException e) {
					stmt.setInt(1, val);
				}
				if (val != 0) {
					stmt.setInt(1, val);
				}
			} else {
				stmt = this.getConnection().prepareStatement("SELECT * FROM customers WHERE " + attribute + " LIKE ?");
				stmt.setString(1, "%" + value + "%");
			}

			rs = stmt.executeQuery();

			while (rs.next()) {
				results.add(customerFromRS(rs));
			}
			return results;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}

				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void deleteCustomer(Integer id) {
		PreparedStatement stmt = null;

		try {
			stmt = this.getConnection().prepareStatement("DELETE FROM customers WHERE id = ?");

			stmt.setInt(1, id);

			int deleted = stmt.executeUpdate();

			if (deleted == 0) {
				throw new IllegalArgumentException("Invalid id");
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}

	}

	public List<Customer> getNotifCustomers(int days) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Customer> customers = new ArrayList<Customer>();

		try {
			stmt = this.getConnection().prepareStatement(
					"SELECT * FROM customers WHERE (itp_end_date - current_date >= 0) AND (itp_end_date - current_date <= ?) AND email_sent = false ORDER BY itp_end_date");
			stmt.setInt(1, days);
			rs = stmt.executeQuery();

			while (rs.next()) {
				customers.add(customerFromRS(rs));
			}

			return customers;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}

				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Creates a customer from the first row of the given ResultSet
	 * 
	 * @param rs
	 *            The given ResultSet
	 * @return A customer with all the attributes set
	 * @throws SQLException
	 *             Any exception that can arise from handling the ResultSet
	 */
	private Customer customerFromRS(ResultSet rs) throws SQLException {
		Customer customer = new CustomerImpl();

		customer.setId(rs.getInt(1));
		customer.setName(rs.getString(2));
		customer.setCarModel(rs.getString(3));
		customer.setRegistId(rs.getString(4));
		customer.setEmail(rs.getString(5));
		customer.setPhoneNr(rs.getString(6));
		customer.setITPEndDate(new LocalDate(rs.getDate(7)));
		customer.setEmailSent(rs.getBoolean(8));
		customer.setOther(rs.getString(9));

		return customer;
	}

}
