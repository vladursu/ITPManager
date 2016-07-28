package services.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

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

	@Override
	public List<Customer> getCustomers() {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Customer> customers = new ArrayList<Customer>();

		try {
			stmt = this.getConnection().prepareStatement("SELECT * FROM customers");
			rs = stmt.executeQuery();

			while (rs.next()) {
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

				customers.add(customer);

			}

			return customers;

		} catch (SQLException e) {
			// we have an issue, time to go.
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
				// we have an issue, time to go.
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
			// we have an issue, time to go.
			throw new IllegalArgumentException(e);
		} finally {
			try {
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
			} catch (SQLException e) {
				// we have an issue, time to go.
				throw new RuntimeException(e);
			}
		}

	}

	@Override
	public void editCustomer(Integer id, Customer newCustomer) {
		PreparedStatement stmt = null;
		String sql = new String("UPDATE customers SET");
		boolean[] changes = new boolean[9];

		if (newCustomer.getName() != null) {
			sql += " name = ?,";
			changes[1] = true;
		}
		if (newCustomer.getCarModel() != null) {
			sql += " car_model = ?,";
			changes[2] = true;
		}
		if (newCustomer.getRegistId() != null) {
			sql += " registration_id = ?,";
			changes[3] = true;
		}
		if (newCustomer.getEmail() != null) {
			sql += " email = ?,";
			changes[4] = true;
		}
		if (newCustomer.getPhoneNr() != null) {
			sql += " phone_number = ?,";
			changes[5] = true;
		}
		if (newCustomer.getITPEndDate() != null) {
			sql += " itp_end_date = ?,";
			changes[6] = true;
		}
		if (newCustomer.getEmailSent() != null) {
			sql += " email_sent = ?,";
			changes[7] = true;
		}
		if (newCustomer.getOther() != null) {
			sql += " other = ?,";
			changes[8] = true;
		}

		sql = sql.substring(0, sql.length() - 1) + "WHERE id = ?";

		boolean changesExist = false;

		for (boolean b : changes) {
			if (b) {
				changesExist = true;
				break;
			}
		}

		if (!changesExist) {
			throw new IllegalArgumentException("NOCHANGE");
		}

		try {
			stmt = this.getConnection().prepareStatement(sql);
			int i = 1;
			if (changes[1]) {
				stmt.setString(i++, newCustomer.getName());
			}
			if (changes[2]) {
				stmt.setString(i++, newCustomer.getCarModel());
			}
			if (changes[3]) {
				stmt.setString(i++, newCustomer.getRegistId());
			}
			if (changes[4]) {
				stmt.setString(i++, newCustomer.getEmail());
			}
			if (changes[5]) {
				stmt.setString(i++, newCustomer.getPhoneNr());
			}
			if (changes[6]) {
				stmt.setDate(i++, java.sql.Date.valueOf(newCustomer.getITPEndDate().toString()));
			}
			if (changes[7]) {
				stmt.setBoolean(i++, newCustomer.getEmailSent());
			}
			if (changes[8]) {
				stmt.setString(i++, newCustomer.getOther());
			}

			stmt.setInt(i, id);

			int updated = stmt.executeUpdate();
			if (updated == 0) {
				throw new IllegalArgumentException("INVALID");
			}
		} catch (SQLException e) {
			// we have an issue, time to go.
			throw new RuntimeException(e);
		} finally {
			try {
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
			} catch (SQLException e) {
				// we have an issue, time to go.
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
			stmt = this.getConnection().prepareStatement("SELECT * FROM customers WHERE "+attribute+" LIKE ?");
			// this is safe since attribute will be sent by a trusted party.
			stmt.setString(1, "%"+value+"%");

			rs = stmt.executeQuery();
			
			while(rs.next()) {
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

				results.add(customer);
			}
			return results;

		} catch (SQLException e) {
			// we have an issue, time to go.
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
				// we have an issue, time to go.
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
			// we have an issue, time to go.
			throw new RuntimeException(e);
		} finally {
			try {
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
			} catch (SQLException e) {
				// we have an issue, time to go.
				throw new RuntimeException(e);
			}
		}

	}

}
