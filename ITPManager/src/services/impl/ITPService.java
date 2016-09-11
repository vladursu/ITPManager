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
	private Boolean englishPack;

	public ITPService(Connection db, Boolean englishPack) {
		this.db = db;
		this.englishPack = englishPack;
	}

	public Connection getConnection() {
		return this.db;
	}

	public Customer getCustomer(Integer id) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Customer customer = new CustomerImpl();

		try {
			stmt = this.getConnection().prepareStatement("SELECT * FROM customers WHERE id = ?");
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			rs.next();

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
	public List<Customer> getCustomers() {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Customer> customers = new ArrayList<Customer>();

		try {
			stmt = this.getConnection().prepareStatement("SELECT * FROM customers ORDER BY id");
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
	public void updateNotified(Integer id, Boolean notified) {
		PreparedStatement stmt = null;
		try {
			stmt = this.getConnection().prepareStatement("UPDATE customers SET email_sent = ? WHERE id = ?");
			stmt.setBoolean(1, notified);
			stmt.setInt(2, id);

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

	@Override
	public String formattedString(List<Customer> customers) {
		String fullFormatted = "";
		// Widths for columns with their minimums
		int idLen = 2;
		int nameLen = 4;
		int carLen = 9;
		final int regIdLen = 16;
		int emailLen = 5;
		int phoneLen = 10;
		final int itpLen = 10;
		final int sentLen = 10;
		int commLen = 10;

		// Define the lengths of non-final ones
		for (Customer customer : customers) {
			if (customer.getId() >= 100) {
				idLen = 3;
				if (customer.getId() >= 1000) {
					idLen = 4;
					if (customer.getId() >= 10000) {
						idLen = 5;
					}
				}
			}
			if (customer.getName().length() > nameLen) {
				nameLen = customer.getName().length();
			}
			if (customer.getCarModel().length() > carLen) {
				carLen = customer.getCarModel().length();
			}
			if (customer.getEmail().length() > emailLen) {
				emailLen = customer.getEmail().length();
			}
			if (customer.getPhoneNr().length() > phoneLen) {
				phoneLen = customer.getPhoneNr().length();
			}
			if (customer.getOther() != null) {
				if (customer.getOther().length() > commLen) {
					commLen = customer.getOther().length();
				}
			}
		}

		// Build the full formatted string
		// Start with column names
		fullFormatted += String.format(
				"%-" + idLen + "s | %-" + nameLen + "s | %-" + carLen + "s | %-" + regIdLen + "s | %-" + emailLen
						+ "s | %-" + phoneLen + "s | %-" + itpLen + "s | %-" + sentLen + "s | %-" + commLen + "s\n",
				"ID", englishPack ? "Name" : "Nume", englishPack ? "Car model" : "Masina",
				englishPack ? "Registration ID" : "Nr inmatriculare", "Email", englishPack ? "Telephone" : "Telefon",
				englishPack ? "ITP date" : "Data ITP", englishPack ? "Notified?" : "Notificat?",
				englishPack ? "Comments" : "Comentarii");
		// Add a line under column names
		int totalLen = idLen + nameLen + carLen + regIdLen + emailLen + phoneLen + itpLen + sentLen + commLen + 3 * 8;
		String line = "=";
		for (int i = 1; i < totalLen; i++) {
			line += "=";
		}
		fullFormatted += line;
		// Add the rows of customers
		for (Customer customer : customers) {
			// Format the ITP end date
			DateTimeFormatter df = DateTimeFormat.forPattern("dd-MM-yyyy");
			String itpEndDate = df.print(customer.getITPEndDate());

			fullFormatted += String.format(
					"\n%-" + idLen + "s | %-" + nameLen + "s | %-" + carLen + "s | %-" + regIdLen + "s | %-" + emailLen
							+ "s | %-" + phoneLen + "s | %-" + itpLen + "s | %-" + sentLen + "s | %-" + commLen + "s",
					customer.getId(), customer.getName(), customer.getCarModel(), customer.getRegistId(),
					customer.getEmail(), customer.getPhoneNr() == null ? "" : customer.getPhoneNr(), itpEndDate,
					customer.getEmailSent() ? (englishPack ? "Yes" : "Da") : (englishPack ? "No" : "Nu"),
					customer.getOther() == null ? "" : customer.getOther());
		}

		return fullFormatted;
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

}
