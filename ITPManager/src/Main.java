import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.joda.time.LocalDate;

import model.Customer;
import model.impl.CustomerImpl;
import services.JdbcService;
import services.NotificationService;
import services.impl.EmailService;
import services.impl.ITPService;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection db = null;
		String cmd = null;

		try {

			// use commands until GUI is implemented
			if (args.length < 1) {
				return;
			}

			cmd = args[0];

			Properties prop = new Properties();
			String driverClass = new String();
			String url = new String();
			String username = new String();
			String password = new String();

			try {

				InputStream in = new FileInputStream("lib/config.properties");
				prop.load(in);
				in.close();

				driverClass = prop.getProperty("PSQLJDBC.driver");
				url = prop.getProperty("PSQLJDBC.url");
				username = prop.getProperty("PSQLJDBC.username");
				password = prop.getProperty("PSQLJDBC.password");

				// ensure the driver has been loaded.
				Class.forName(driverClass);
			} catch (ClassNotFoundException e) {
				System.err.println("driver not found.");
				System.err.println(e.getMessage());
				return;
			} catch (FileNotFoundException e) {
				System.err.println("Properties file not found.");
				System.err.println(e.getMessage());
				return;
			} catch (IOException e) {
				System.err.println(e.getMessage());
				return;
			}

			try {
				// connect to the database ...
				db = DriverManager.getConnection(url, username, password);
			} catch (SQLException e) {
				System.err.println("couldn't connect to the database.");
				System.err.println(e.getMessage());
				return;
			}

			// test the connection
			JdbcService itpService = new ITPService(db);
			NotificationService emailService = new EmailService();

			if (cmd.equals("ShowCustomers")) {
				List<Customer> customers = itpService.getCustomers();
				System.out.println(itpService.formattedString(customers));
			} else if (cmd.equals("ShowNotifCustomers")) {
				List<Customer> customers = itpService.getNotifCustomers(15);
				System.out.println(itpService.formattedString(customers));
			} else if (cmd.equals("UpdateNotified")) {
				itpService.updateNotified(1,true);
			}else if (cmd.equals("AddCustomer")) {
				try {
					// TODO add local checks based on table constraints to
					// provide specific feedback
					String name = args[1];
					String model = args[2];
					String regId = args[3];
					String email = args[4];
					String phoneNo = args[5];
					String[] dateItems = args[6].split("-");
					LocalDate itpEndDate = new LocalDate(Integer.parseInt(dateItems[2]), Integer.parseInt(dateItems[1]),
							Integer.parseInt(dateItems[0]));
					boolean emailSent = Boolean.parseBoolean(args[7]);
					String other = args[8];

					Customer customer = new CustomerImpl(-1, name, model, regId, email, phoneNo, itpEndDate, emailSent,
							other);

					itpService.addCustomer(customer);

					System.out.println("Customer " + name + " added to the database");

				} catch (NumberFormatException e) {
					System.out.println("Invalid date. Must be of format DD-MM-YYYY. E.g. 02-11-2016");
				} catch (IllegalArgumentException e) {
					System.out.println("Illegal argumets. Some of them might be too long");
				}

			} else if (cmd.equals("DeleteCustomer")) {
				try {
					Integer id = Integer.parseInt(args[1]);
					itpService.deleteCustomer(id);
					System.out.println("Customer with id: " + id + " has been deleted!");
				} catch (NumberFormatException e) {
					System.out.println(args[1] + " is not a valid number");
				} catch (IllegalArgumentException e) {
					System.out.println("Invalid id: " + args[1]);
				}
			} else if (cmd.equals("UpdateCustomer")) {
				try {
					Integer id = Integer.parseInt(args[1]);
					// This is trickier to test as command input and ultimately
					// irrelevant when the GUI will be finished.
					// So I'll be doing hard coded tests

					Customer newCustomer = new CustomerImpl();
					newCustomer.setName("Update tester");
					newCustomer.setOther("Double update in one go!");
					itpService.editCustomer(id, newCustomer);
					System.out.println("Customer with id: " + id + " has been updated!");
				} catch (NumberFormatException e) {
					System.out.println(args[1] + " is not a valid number");
				} catch (IllegalArgumentException e) {
					System.out.println(e);
				}
			} else if(cmd.equals("Search")) {
				// these args don't have to be checked because they will be passed by a trusted party.
				List<Customer> searchResults = itpService.searchForCustomers(args[1], args[2]);
				if(searchResults.size()==0) {
					System.out.println("No results found");
				} else {
					for (Customer customer : searchResults) {
						System.out.println(customer.getId() + "\t" + customer.getName() + "\t" + customer.getCarModel()
								+ "\t" + customer.getRegistId() + "\t" + customer.getEmail() + "\t" + customer.getPhoneNr()
								+ "\t" + customer.getITPEndDate() + "\t" + customer.getEmailSent() + "\t"
								+ customer.getOther());
					}
				}
			} else if(cmd.equals("NotifyCustomer")) {
				
				Customer customer = itpService.getCustomers().get(1);
				emailService.notifyCustomer(customer);
				//TODO change the email_sent parameter to true
			} else {
				System.out.println("Invalid command/use");
			}

		} finally {
			// finish up.
			if (db != null) {
				try {
					if (!db.isClosed()) {
						db.close();
					}
				} catch (SQLException e) {
					System.err.println(e.getMessage());
				}
			}
		}
	}

}
