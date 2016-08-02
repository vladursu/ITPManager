package services.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.joda.time.LocalDate;

import model.Customer;
import services.NotificationService;

public class EmailService implements NotificationService {

	private Session session = null;
	private String username = new String();
	private String password = new String();
	private String host = new String();

	public EmailService() {

		Properties setup = new Properties();

		try {
			InputStream in = new FileInputStream("lib/smtp.properties");
			setup.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		host = setup.getProperty("SMTP.host");
		username = setup.getProperty("SMTP.username");
		password = setup.getProperty("SMTP.password");

		// Get system properties
		Properties props = System.getProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", setup.getProperty("SMTP.port"));
		props.setProperty("mail.user", username);
		props.setProperty("mail.password", password);

		session = Session.getDefaultInstance(props);

	}

	@Override
	public void notifyCustomer(Customer customer) {

		final String placeholder = "_____";
		String body = "";

		// Create email body
		try {
			body = new String(Files.readAllBytes(Paths.get("lib/customer_message.html")), StandardCharsets.UTF_8);
			body = body.replaceFirst(placeholder, customer.getName());

			Integer days = customer.getITPEndDate().minusDays(new LocalDate().getDayOfMonth()).getDayOfMonth();
			body = body.replaceFirst(placeholder, days.toString());

			body = body.replaceFirst(placeholder, customer.getITPEndDate().dayOfMonth().getAsText() + "-"
					+ customer.getITPEndDate().getMonthOfYear() + "-" + customer.getITPEndDate().getYear());
			body = body.replaceFirst(placeholder, customer.getCarModel());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Create and send email
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(customer.getEmail()));
			message.setSentDate(new Date());
			message.setSubject("ITP expiration date");
			message.setContent(body, "text/html");

			message.saveChanges();

			// Step 4: Send the message by javax.mail.Transport .
			Transport tr = session.getTransport("smtp"); // Get Transport object
															// from session
			tr.connect(host, username, password); // We need to connect
			tr.sendMessage(message, message.getAllRecipients()); // Send message

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void notifyCustomers(List<Customer> customers) {
		for (Customer customer : customers) {
			this.notifyCustomer(customer);
		}

	}

}
