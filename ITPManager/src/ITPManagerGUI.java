
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.joda.time.IllegalFieldValueException;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import model.Customer;
import model.impl.CustomerImpl;
import services.JdbcService;
import services.NotificationService;
import services.impl.EmailService;
import services.impl.ITPService;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.awt.event.ActionEvent;
import java.awt.CardLayout;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.Color;

public class ITPManagerGUI extends JFrame {

	/* The menu bar */
	private JMenuBar menuBar;

	/* Panels to switch through */
	private JPanel loginContentPane;
	private JPanel welcomeContentPane;
	private JPanel dbViewContentPane;
	private JPanel dbSearchContentPane;
	private JPanel dbAddContentPane;
	private JPanel dbEditContentPane;
	private JPanel notifContentPane;

	/* Editable global variables */
	// Login panel
	private JTextField textFieldUsername;
	private JPasswordField pwdFieldPassword;
	// Database view panel
	private JTextArea dbViewTextArea;
	private JTextField textFieldCustomerIDView;
	private JComboBox comboBoxSort;
	private JComboBox comboBoxOrder;
	// Database search panel
	private JComboBox comboBoxSearch;
	private JTextField textFieldSearch;
	private JTextArea dbSearchTextArea;
	// Database add customer panel
	private JTextField textFieldIdAdd;
	private JTextField textFieldNameAdd;
	private JTextField textFieldCarModelAdd;
	private JTextField textFieldRegIdAdd;
	private JTextField textFieldEmailAdd;
	private JTextField textFieldPhoneAdd;
	private JTextField textFieldITPAdd;
	private JComboBox comboBoxNotifiedAdd;
	private JTextArea textAreaCommentsAdd;
	// Database edit existing customer panel
	private JTextField textFieldIdEdit;
	private JTextField textFieldNameEdit;
	private JTextField textFieldCarModelEdit;
	private JTextField textFieldRegIdEdit;
	private JTextField textFieldEmailEdit;
	private JTextField textFieldPhoneEdit;
	private JTextField textFieldItpEdit;
	private JComboBox comboBoxNotifiedEdit;
	private JTextArea textAreaCommentsEdit;
	// Notifications panel
	private JTextField textFieldCustomerIDNotif;
	private JTextArea notifTextArea;
	private JTextField textFieldDaysNotif;

	/* Back-end variables */
	private Connection db = null;
	private JdbcService itpService = null;
	private NotificationService emailService = null;
	private Boolean englishPack = true;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ITPManagerGUI frame = new ITPManagerGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ITPManagerGUI() {
		/* Get the preferred language */
		Properties prop = new Properties();
		try {
			InputStream in = new FileInputStream("lib/config.properties");
			prop.load(in);
			in.close();

			englishPack = prop.getProperty("GEN.language").equals("English");
		} catch (IOException e) {
			// IO error message will be triggered on login anyway
		}

		/* JFrame configuration */
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 1280, 600);
		setTitle("ITP Manager");
		getContentPane().setLayout(new CardLayout(0, 0));

		/*
		 * Custom made close operation in order to call cleanUp() on any
		 * possible exit
		 */
		WindowAdapter exitListener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int confirm = JOptionPane.showConfirmDialog(getContentPane(),
						englishPack ? "Close Application?" : "Inchide aplicatia?",
						englishPack ? "Exit Confirmation" : "Confirmare iesire", JOptionPane.YES_NO_OPTION);
				if (confirm == 0) {
					cleanUp();
					System.exit(0);
				}
			}
		};
		addWindowListener(exitListener);

		/* Create and add the menu bar */
		createMenuBar();
		menuBar.setVisible(false);
		setJMenuBar(menuBar);

		/* Create the login panel */
		loginContentPane = new JPanel();
		loginContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		loginContentPane.setLayout(null);
		getContentPane().add(loginContentPane);

		JLabel lblTitle = new JLabel("ITP Manager");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(10, 90, 1249, 37);
		lblTitle.setFont(new Font("Calibri", Font.BOLD, 40));
		loginContentPane.add(lblTitle);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsername.setBounds(460, 230, 100, 14);
		loginContentPane.add(lblUsername);

		JLabel lblPassword = new JLabel(englishPack ? "Password" : "Parola");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setBounds(710, 230, 100, 14);
		loginContentPane.add(lblPassword);

		textFieldUsername = new JTextField();
		textFieldUsername.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldUsername.setBounds(450, 247, 120, 20);
		loginContentPane.add(textFieldUsername);
		textFieldUsername.setColumns(10);

		pwdFieldPassword = new JPasswordField();
		pwdFieldPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
		pwdFieldPassword.setBounds(700, 247, 120, 20);
		loginContentPane.add(pwdFieldPassword);

		JButton btnLogin = new JButton(englishPack ? "Login" : "Intra");
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

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
					username = textFieldUsername.getText();
					password = new String(pwdFieldPassword.getPassword());

					// ensure the driver has been loaded.
					Class.forName(driverClass);
				} catch (ClassNotFoundException e) {
					JOptionPane.showMessageDialog(loginContentPane,
							englishPack ? "Driver not found." : "Nu s-a putut gasi driver-ul.");
				} catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(loginContentPane,
							englishPack ? "Config file not found." : "Nu s-a putut gasi fisierul config");
				} catch (IOException e) {
					JOptionPane.showMessageDialog(loginContentPane, "I/O exception occured: " + e.getMessage());
				}

				try {
					// connect to the database ...
					db = DriverManager.getConnection(url, username, password);
				} catch (SQLException e) {
					if (e.getMessage().contains("authentication")) {
						JOptionPane.showMessageDialog(loginContentPane,
								englishPack ? "Invalid username and/or password" : "Username si/sau parola incorecte");
					} else {
						JOptionPane.showMessageDialog(loginContentPane, "Invalid url (host/database)");
					}
				}

				if (db != null) {
					itpService = new ITPService(db, englishPack);
					emailService = new EmailService();

					changePanel("welcome");
					menuBar.setVisible(true);
				}
			}
		});

		btnLogin.setBounds(585, 330, 100, 40);
		loginContentPane.add(btnLogin);

		/* Create the welcome panel */
		welcomeContentPane = new JPanel();
		welcomeContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		welcomeContentPane.setLayout(null);
		getContentPane().add(welcomeContentPane);

		JLabel lblWelcome = new JLabel("ITP Manager");
		lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcome.setBounds(5, 50, 1249, 37);
		lblWelcome.setFont(new Font("Calibri", Font.BOLD, 40));
		welcomeContentPane.add(lblWelcome);

		JLabel lblQuickAccess = new JLabel(englishPack ? "Quick access:" : "Acces rapid:");
		lblQuickAccess.setHorizontalAlignment(SwingConstants.CENTER);
		lblQuickAccess.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblQuickAccess.setBounds(337, 238, 574, 20);
		welcomeContentPane.add(lblQuickAccess);

		JButton btnUpcoming = new JButton(englishPack ? "Upcoming" : "Notificari");
		btnUpcoming.setToolTipText(englishPack ? "Opens the panel with upcoming customers to be notified."
				: "Deschide fereastra cu clienti care trebuie notificati");
		btnUpcoming.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnUpcoming.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changePanel("notifUp");
			}
		});
		btnUpcoming.setBounds(287, 269, 150, 40);
		welcomeContentPane.add(btnUpcoming);

		JButton btnViewDatabase = new JButton(englishPack ? "View database" : "Baza de date");
		btnViewDatabase.setToolTipText(englishPack ? "Opens panel showing the full database and options to manage it."
				: "Deschide fereastra cu intreaga baza de date si optiuni pentru modificarea acesteia");
		btnViewDatabase.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnViewDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				changePanel("dbView");
			}
		});
		btnViewDatabase.setBounds(457, 269, 150, 40);
		welcomeContentPane.add(btnViewDatabase);

		JButton btnQuickSearch = new JButton(englishPack ? "Search" : "Cautare");
		btnQuickSearch.setToolTipText(englishPack ? "Opens the search panel used to search through customers."
				: "Deschide fereastra de cautare");
		btnQuickSearch.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnQuickSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				changePanel("dbSearch");
			}
		});
		btnQuickSearch.setBounds(627, 269, 150, 40);
		welcomeContentPane.add(btnQuickSearch);

		JButton btnAddCustomer = new JButton(englishPack ? "Add customer" : "Adauga client");
		btnAddCustomer.setToolTipText(
				englishPack ? "Opens a panel that provides the means to add new customers to the database."
						: "Deschide fereastra pentru a adauga un client nou.");
		btnAddCustomer.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnAddCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changePanel("dbAdd");
			}
		});
		btnAddCustomer.setBounds(797, 269, 150, 40);
		welcomeContentPane.add(btnAddCustomer);

		/* Create the database view panel */
		dbViewContentPane = new JPanel();
		getContentPane().add(dbViewContentPane);
		dbViewContentPane.setLayout(null);
		dbViewContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		JButton btnAddCustomerView = new JButton(englishPack ? "Add customer" : "Adauga client");
		btnAddCustomerView.setToolTipText(
				englishPack ? "Opens a panel that provides the means to add new customers to the database."
						: "Deschide fereastra pentru a adauga un client nou.");
		btnAddCustomerView.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnAddCustomerView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changePanel("dbAdd");
			}
		});
		btnAddCustomerView.setBounds(10, 439, 150, 40);
		dbViewContentPane.add(btnAddCustomerView);

		JButton btnSearchView = new JButton(englishPack ? "Search" : "Cautare");
		btnSearchView.setToolTipText(englishPack ? "Opens the search panel used to search through customers."
				: "Deschide fereastra de cautare");
		btnSearchView.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnSearchView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changePanel("dbSearch");
			}
		});
		btnSearchView.setBounds(10, 487, 150, 40);
		dbViewContentPane.add(btnSearchView);

		JButton btnDeleteView = new JButton(englishPack ? "Delete" : "Sterge");
		btnDeleteView.setToolTipText(
				englishPack ? "Deletes the customer corresponding to the customer ID above from the database."
						: "Sterge clientul care corespunde ID-ului de mai sus");
		btnDeleteView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Customer cust = checkID(textFieldCustomerIDView.getText());
				if (cust != null) {
					int confirm = JOptionPane.showConfirmDialog(getContentPane(), englishPack
							? ("Are you sure you want to delete customer " + cust.getName() + " from the database?")
							: ("Sunteti sigur ca vreti sa stergeti clientul cu numele " + cust.getName()
									+ " din baza de date?"),
							englishPack ? "Delete confirmation" : "Confirmare stergere", JOptionPane.YES_NO_OPTION);
					if (confirm == 0) {
						itpService.deleteCustomer(cust.getId());
						JOptionPane.showMessageDialog(getContentPane(),
								englishPack ? ("Customer " + cust.getName() + " has been deleted from the database.")
										: "Clientul cu numele " + cust.getName() + " a fost sters din baza de date.");
						changePanel("dbView");
					}
				}
			}
		});
		btnDeleteView.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnDeleteView.setBackground(Color.RED);
		btnDeleteView.setBounds(1134, 487, 120, 40);
		dbViewContentPane.add(btnDeleteView);

		JButton btnEditView = new JButton(englishPack ? "Edit" : "Modifica");
		btnEditView.setToolTipText(
				englishPack ? "Opens the edit panel for the customer with the ID given in the Customer ID box."
						: "Deschide fereastra de modificare a clientului cu ID-ul dat mai sus.");
		btnEditView.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnEditView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Customer cust = checkID(textFieldCustomerIDView.getText());
				if (cust != null) {
					textFieldIdEdit.setText(cust.getId().toString());
					textFieldNameEdit.setText(cust.getName());
					textFieldCarModelEdit.setText(cust.getCarModel());
					textFieldRegIdEdit.setText(cust.getRegistId());
					textFieldEmailEdit.setText(cust.getEmail());
					textFieldPhoneEdit.setText(cust.getPhoneNr());
					DateTimeFormatter df = DateTimeFormat.forPattern("dd-MM-yyyy");
					String itpEndDate = df.print(cust.getITPEndDate());
					textFieldItpEdit.setText(itpEndDate);
					comboBoxNotifiedEdit.setSelectedIndex(cust.getEmailSent() ? 0 : 1);
					textAreaCommentsEdit.setText(cust.getOther());
					changePanel("dbEdit");
				}
			}
		});
		btnEditView.setBounds(1004, 487, 120, 40);
		dbViewContentPane.add(btnEditView);

		JLabel lblCustomerIdView = new JLabel(englishPack ? "Customer ID:" : "ID client:");
		lblCustomerIdView.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCustomerIdView.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCustomerIdView.setBounds(1004, 450, 120, 20);
		dbViewContentPane.add(lblCustomerIdView);

		textFieldCustomerIDView = new JTextField();
		textFieldCustomerIDView.setToolTipText(englishPack ? "The ID of the customer you with to edit or delete."
				: "ID-ul clientului pe care vreti sa-l modificati sau stergeti.");
		textFieldCustomerIDView.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldCustomerIDView.setHorizontalAlignment(SwingConstants.LEFT);
		textFieldCustomerIDView.setBounds(1134, 450, 70, 20);
		dbViewContentPane.add(textFieldCustomerIDView);
		textFieldCustomerIDView.setColumns(4);

		dbViewTextArea = new JTextArea();
		dbViewTextArea.setEditable(false);
		dbViewTextArea.setFont(new Font("Courier New", Font.PLAIN, 13));
		dbViewTextArea.setBounds(10, 11, 564, 217);

		JScrollPane dbViewScrollPane = new JScrollPane(dbViewTextArea);
		dbViewScrollPane.setBounds(10, 11, 1244, 417);
		dbViewContentPane.add(dbViewScrollPane);

		JLabel lblSortBy = new JLabel("Sort by:");
		lblSortBy.setHorizontalAlignment(SwingConstants.LEFT);
		lblSortBy.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSortBy.setBounds(419, 450, 60, 20);
		dbViewContentPane.add(lblSortBy);

		JLabel lblOrder = new JLabel("Order:");
		lblOrder.setHorizontalAlignment(SwingConstants.LEFT);
		lblOrder.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblOrder.setBounds(603, 449, 53, 20);
		dbViewContentPane.add(lblOrder);

		comboBoxSort = new JComboBox();
		String[] sortOptions = null;
		if (englishPack) {
			sortOptions = new String[] { "ID", "name", "car model", "registration ID", "email", "phone number",
					"ITP end date", "notified?", "comments" };
		} else {
			sortOptions = new String[] { "ID", "nume", "masina", "nr inmatriculare", "email", "telefon", "expira ITP",
					"notificat?", "comentarii" };
		}
		comboBoxSort.setModel(new DefaultComboBoxModel(sortOptions));
		comboBoxSort.setSelectedIndex(0);
		comboBoxSort.setFont(new Font("Tahoma", Font.PLAIN, 15));
		comboBoxSort.setBounds(478, 450, 120, 20);
		dbViewContentPane.add(comboBoxSort);

		comboBoxOrder = new JComboBox();
		String[] orderOptions = null;
		if (englishPack) {
			orderOptions = new String[] { "Ascending", "Descending" };
		} else {
			orderOptions = new String[] { "Crescator", "Descrescator" };
		}
		comboBoxOrder.setModel(new DefaultComboBoxModel(orderOptions));
		comboBoxOrder.setSelectedIndex(0);
		comboBoxOrder.setFont(new Font("Tahoma", Font.PLAIN, 15));
		comboBoxOrder.setBounds(649, 450, 120, 20);
		dbViewContentPane.add(comboBoxOrder);

		JButton btnRefreshView = new JButton("Refresh");
		btnRefreshView.setToolTipText(
				englishPack ? "Refreshes the customers list to reflect your sorting and ordering choice."
						: "Rescrie lista de mai sus pentru a fi in conformitate cu optiunile de sortare.");
		btnRefreshView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String attribute = "id";
				String order = "asc";

				switch (comboBoxSort.getSelectedIndex()) {
				case -1:
				case 0:
					attribute = "id";
					break;
				case 1:
					attribute = "name";
					break;
				case 2:
					attribute = "car_model";
					break;
				case 3:
					attribute = "registration_id";
					break;
				case 4:
					attribute = "email";
					break;
				case 5:
					attribute = "phone_number";
					break;
				case 6:
					attribute = "ITP_end_date";
					break;
				case 7:
					attribute = "email_sent";
					break;
				case 8:
					attribute = "other";
					break;
				}
				if (comboBoxOrder.getSelectedIndex() == 1) {
					order = "desc";
				}
				dbViewTextArea.setText(itpService.formattedString(itpService.getSortedCustomers(attribute, order)));
			}
		});
		btnRefreshView.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnRefreshView.setBounds(525, 487, 150, 40);
		dbViewContentPane.add(btnRefreshView);

		/* Create the database search panel */
		dbSearchContentPane = new JPanel();
		getContentPane().add(dbSearchContentPane);
		dbSearchContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		dbSearchContentPane.setLayout(null);

		JLabel lblSearchBy = new JLabel(englishPack ? "Search by" : "Cauta dupa");
		lblSearchBy.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSearchBy.setBounds(10, 21, 80, 20);
		dbSearchContentPane.add(lblSearchBy);

		comboBoxSearch = new JComboBox();
		String[] searchOptions = null;
		if (englishPack) {
			searchOptions = new String[] { "ID", "name", "car model", "registration ID", "email", "phone number" };
		} else {
			searchOptions = new String[] { "ID", "nume", "masina", "nr inmatriculare", "email", "telefon" };
		}
		comboBoxSearch.setFont(new Font("Tahoma", Font.PLAIN, 15));
		comboBoxSearch.setModel(new DefaultComboBoxModel(searchOptions));
		comboBoxSearch.setSelectedIndex(0);
		comboBoxSearch.setBounds(85, 21, 120, 20);
		dbSearchContentPane.add(comboBoxSearch);

		JLabel lblColumn = new JLabel(":");
		lblColumn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblColumn.setBounds(209, 21, 10, 20);
		dbSearchContentPane.add(lblColumn);

		textFieldSearch = new JTextField();
		textFieldSearch.setToolTipText(
				englishPack ? "The term you want to search for." : "Termenul dupa care vreti sa cautati");
		textFieldSearch.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldSearch.setBounds(217, 21, 927, 20);
		dbSearchContentPane.add(textFieldSearch);
		textFieldSearch.setColumns(10);

		JButton btnSearch = new JButton(englishPack ? "Search" : "Cautare");
		btnSearch.setToolTipText(
				englishPack ? "Searches the database according to the criteria provided and lists the results below."
						: "Cauta in baza de date dupa criteriul oferit si afiseaza rezultatele mai jos.");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String attribute = "";
				String value = textFieldSearch.getText();
				switch (comboBoxSearch.getSelectedIndex()) {
				case -1:
				case 0:
					attribute = "id";
					break;
				case 1:
					attribute = "name";
					break;
				case 2:
					attribute = "car_model";
					break;
				case 3:
					attribute = "registration_id";
					break;
				case 4:
					attribute = "email";
					break;
				case 5:
					attribute = "phone_number";
					break;
				}
				try {
					dbSearchTextArea
							.setText(itpService.formattedString(itpService.searchForCustomers(attribute, value)));
				} catch (Exception e2) {
					dbSearchTextArea.setText("");
				}
			}
		});
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnSearch.setBounds(1154, 10, 100, 40);
		dbSearchContentPane.add(btnSearch);

		dbSearchTextArea = new JTextArea();
		dbSearchTextArea.setFont(new Font("Courier New", Font.PLAIN, 13));
		dbSearchTextArea.setEditable(false);
		dbSearchTextArea.setBounds(10, 44, 564, 276);

		JScrollPane dbSearchScrollPane = new JScrollPane(dbSearchTextArea);
		dbSearchScrollPane.setBounds(10, 60, 1244, 456);
		dbSearchContentPane.add(dbSearchScrollPane);

		/* Create the database add new customer panel */
		dbAddContentPane = new JPanel();
		getContentPane().add(dbAddContentPane);
		dbAddContentPane.setLayout(null);
		dbAddContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		JLabel lblAddNewCustomer = new JLabel(englishPack ? "Add new customer" : "Adaugare client nou");
		lblAddNewCustomer.setFont(new Font("Calibri", Font.BOLD, 30));
		lblAddNewCustomer.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddNewCustomer.setBounds(10, 11, 1244, 30);
		dbAddContentPane.add(lblAddNewCustomer);

		JLabel lblIdAdd = new JLabel("ID");
		lblIdAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblIdAdd.setBounds(80, 67, 120, 20);
		dbAddContentPane.add(lblIdAdd);

		textFieldIdAdd = new JTextField();
		textFieldIdAdd.setToolTipText(englishPack ? "The customer's ID (will be automatically created by the database)."
				: "ID-ul clientului (va fi creat automat de baza de date).");
		textFieldIdAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldIdAdd.setEditable(false);
		textFieldIdAdd.setBounds(210, 67, 250, 20);
		dbAddContentPane.add(textFieldIdAdd);
		textFieldIdAdd.setColumns(10);

		JLabel lblNameAdd = new JLabel(englishPack ? "Name*" : "Nume*");
		lblNameAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNameAdd.setBounds(80, 110, 120, 20);
		dbAddContentPane.add(lblNameAdd);

		textFieldNameAdd = new JTextField();
		textFieldNameAdd.setToolTipText(englishPack ? "The name of the new customer." : "Numele clientului");
		textFieldNameAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldNameAdd.setColumns(10);
		textFieldNameAdd.setBounds(210, 110, 250, 20);
		dbAddContentPane.add(textFieldNameAdd);

		JLabel lblCarModelAdd = new JLabel(englishPack ? "Car model*" : "Modelul masinii*");
		lblCarModelAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCarModelAdd.setBounds(80, 149, 120, 20);
		dbAddContentPane.add(lblCarModelAdd);

		textFieldCarModelAdd = new JTextField();
		textFieldCarModelAdd
				.setToolTipText(englishPack ? "The car model of the new customer." : "Modelul masinii clientului.");
		textFieldCarModelAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldCarModelAdd.setColumns(10);
		textFieldCarModelAdd.setBounds(210, 149, 250, 20);
		dbAddContentPane.add(textFieldCarModelAdd);

		JLabel lblRegistrationIdAdd = new JLabel(englishPack ? "Registration ID*" : "Nr inmatriculare*");
		lblRegistrationIdAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblRegistrationIdAdd.setBounds(80, 194, 120, 20);
		dbAddContentPane.add(lblRegistrationIdAdd);

		textFieldRegIdAdd = new JTextField();
		textFieldRegIdAdd.setToolTipText(englishPack ? "The registration ID of the customer's car."
				: "Numarul de inmatriculare al masinii clientului.");
		textFieldRegIdAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldRegIdAdd.setColumns(10);
		textFieldRegIdAdd.setBounds(210, 194, 250, 20);
		dbAddContentPane.add(textFieldRegIdAdd);

		JLabel lblEmailAdd = new JLabel("Email*");
		lblEmailAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblEmailAdd.setBounds(779, 67, 120, 20);
		dbAddContentPane.add(lblEmailAdd);

		textFieldEmailAdd = new JTextField();
		textFieldEmailAdd
				.setToolTipText(englishPack ? "The customer's email address." : "Adresa de email a clientului.");
		textFieldEmailAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldEmailAdd.setColumns(10);
		textFieldEmailAdd.setBounds(909, 70, 276, 20);
		dbAddContentPane.add(textFieldEmailAdd);

		JLabel lblPhoneNumberAdd = new JLabel(englishPack ? "Phone number" : "Numar telefon");
		lblPhoneNumberAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblPhoneNumberAdd.setBounds(779, 110, 120, 20);
		dbAddContentPane.add(lblPhoneNumberAdd);

		textFieldPhoneAdd = new JTextField();
		textFieldPhoneAdd
				.setToolTipText(englishPack ? "The customer's phone number." : "Numarul de telefon al clientului");
		textFieldPhoneAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldPhoneAdd.setColumns(10);
		textFieldPhoneAdd.setBounds(909, 113, 276, 20);
		dbAddContentPane.add(textFieldPhoneAdd);

		JLabel lblItpEndDateAdd = new JLabel(englishPack ? "ITP end date*" : "Data exp. ITP*");
		lblItpEndDateAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblItpEndDateAdd.setBounds(779, 152, 120, 20);
		dbAddContentPane.add(lblItpEndDateAdd);

		textFieldITPAdd = new JTextField();
		textFieldITPAdd.setToolTipText(
				englishPack ? "The date at which the ITP for the customer's car expires. Date format DD-MM-YYYY"
						: "Data la care expira ITP-ul pentru masina clientului. Format: ZZ-LL-AAAA, e.g. 23-04-2016");
		textFieldITPAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldITPAdd.setColumns(10);
		textFieldITPAdd.setBounds(909, 152, 276, 20);
		dbAddContentPane.add(textFieldITPAdd);

		JLabel lblNotifiedAdd = new JLabel(englishPack ? "Notified?" : "Notificat?");
		lblNotifiedAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNotifiedAdd.setBounds(779, 197, 120, 20);
		dbAddContentPane.add(lblNotifiedAdd);

		comboBoxNotifiedAdd = new JComboBox();
		comboBoxNotifiedAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		String[] addOptions = null;
		if (englishPack) {
			addOptions = new String[] { "Yes", "No" };
		} else {
			addOptions = new String[] { "Da", "Nu" };
		}
		comboBoxNotifiedAdd.setModel(new DefaultComboBoxModel(addOptions));
		comboBoxNotifiedAdd.setSelectedIndex(1);
		comboBoxNotifiedAdd.setBounds(909, 197, 75, 20);
		dbAddContentPane.add(comboBoxNotifiedAdd);

		JLabel lblCommentsAdd = new JLabel(englishPack ? "Comments:" : "Comentarii:");
		lblCommentsAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCommentsAdd.setBounds(80, 235, 120, 20);
		dbAddContentPane.add(lblCommentsAdd);

		textAreaCommentsAdd = new JTextArea();
		textAreaCommentsAdd.setToolTipText(
				englishPack ? "Any other comments regarding this customer." : "Orice alte comentarii despre client.");
		textAreaCommentsAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textAreaCommentsAdd.setBounds(80, 270, 1105, 130);
		textAreaCommentsAdd.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		dbAddContentPane.add(textAreaCommentsAdd);

		JButton btnAdd = new JButton(englishPack ? "Add" : "Adauga");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Customer newCustomer = new CustomerImpl();

				if (textFieldNameAdd.getText().equals("") || textFieldCarModelAdd.getText().equals("")
						|| textFieldRegIdAdd.getText().equals("") || textFieldEmailAdd.getText().equals("")
						|| textFieldITPAdd.getText().equals("")) {
					JOptionPane.showMessageDialog(getContentPane(),
							englishPack ? "All fields marked with * must be filled in."
									: "Toate zonele marcate cu asterisc * trebuie completate.");
				} else {
					if (textFieldNameAdd.getText().length() > 64) {
						JOptionPane.showMessageDialog(getContentPane(),
								englishPack ? "Customer name cannot be longer than 64 characters."
										: "Numele clientului nu poate fi mai lung de 64 de caractere.");
					} else if (textFieldCarModelAdd.getText().length() > 64) {
						JOptionPane.showMessageDialog(getContentPane(),
								englishPack ? "Car model cannot be longer than 64 characters."
										: "Modelul masinii nu poate fi mai lung de 64 de caractere.");
					} else if (textFieldRegIdAdd.getText().length() > 10) {
						JOptionPane.showMessageDialog(getContentPane(),
								englishPack ? "Registration ID cannot be longer than 10 characters."
										: "Numarul de inmatriculare nu poate fi mai lung de 10 caractere.");
					} else if (textFieldEmailAdd.getText().length() > 64) {
						JOptionPane.showMessageDialog(getContentPane(),
								englishPack ? "Email cannot be longer than 64 characters."
										: "Emailul nu poate fi mai lung de 64 de caractere.");
					} else if (textFieldPhoneAdd.getText().length() > 13) {
						JOptionPane.showMessageDialog(getContentPane(),
								englishPack ? "Phone number cannot be longer than 13 characters."
										: "Numarul de telefon nu poate fi mai lung de 13 caractere.");
					} else if (textAreaCommentsAdd.getText().length() > 512) {
						JOptionPane.showMessageDialog(getContentPane(),
								englishPack ? "Comments section cannot be longer than 512 characters."
										: "Comentariile nu pot fi mai lungi de 512 caractere.");
					} else {
						String inputDate = textFieldITPAdd.getText();
						String[] inputDateSegmented = inputDate.split("-");
						if (inputDateSegmented.length != 3) {
							JOptionPane.showMessageDialog(getContentPane(), englishPack
									? "ITP end date must be of format DD-MM-YYYY"
									: "Data la care expira ITP-ul trebuie sa fie in format: ZZ-LL-AAAA, e.g. 23-04-2016");
						} else {
							try {
								newCustomer.setITPEndDate(new LocalDate(Integer.parseInt(inputDateSegmented[2]),
										Integer.parseInt(inputDateSegmented[1]),
										Integer.parseInt(inputDateSegmented[0])));
							} catch (NumberFormatException e) {
								JOptionPane.showMessageDialog(getContentPane(), englishPack
										? "ITP end date must be of format DD-MM-YYYY"
										: "Data la care expira ITP-ul trebuie sa fie in format: ZZ-LL-AAAA, e.g. 23-04-2016");
							} catch (IllegalFieldValueException e) {
								JOptionPane.showMessageDialog(getContentPane(),
										englishPack ? "ITP end date must be a valid date."
												: "Data la care expira ITP-ul trebuie sa fie o data reala");
							}
							if (newCustomer.getITPEndDate() != null) {
								newCustomer.setName(textFieldNameAdd.getText());
								newCustomer.setCarModel(textFieldCarModelAdd.getText());
								newCustomer.setRegistId(textFieldRegIdAdd.getText());
								newCustomer.setEmail(textFieldEmailAdd.getText());
								newCustomer.setPhoneNr(textFieldPhoneAdd.getText());
								newCustomer.setEmailSent(comboBoxNotifiedAdd.getSelectedIndex() == 0 ? true : false);
								newCustomer.setOther(textAreaCommentsAdd.getText());

								boolean ok = true;
								try {
									itpService.addCustomer(newCustomer);
								} catch (IllegalArgumentException e) {
									JOptionPane.showMessageDialog(getContentPane(),
											englishPack ? "Failed to add new customer to the database."
													: "Nu s-a putut adauga clientul in baza de date (eroare interna).");
									ok = false;
								}
								if (ok) {
									JOptionPane.showMessageDialog(getContentPane(),
											englishPack ? "Customer added to the database."
													: "Clientul a fost adaugat in baza de date.");
									changePanel("dbView");
								}
							}
						}
					}
				}
			}
		});
		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnAdd.setBackground(Color.GREEN);
		btnAdd.setToolTipText(
				englishPack ? "Adds the new customer to the database. All fields marked with * must be filled in."
						: "Adauga noul client in baza de date. Toate zonele marcate cu asterisc * trebuie completate.");
		btnAdd.setBounds(570, 437, 120, 40);
		dbAddContentPane.add(btnAdd);

		/* Create the database edit existing customer panel */
		dbEditContentPane = new JPanel();
		dbEditContentPane.setLayout(null);
		dbEditContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(dbEditContentPane);

		JLabel lblEditExistingCustomer = new JLabel(englishPack ? "Edit existing customer" : "Modificare client");
		lblEditExistingCustomer.setHorizontalAlignment(SwingConstants.CENTER);
		lblEditExistingCustomer.setFont(new Font("Calibri", Font.BOLD, 30));
		lblEditExistingCustomer.setBounds(10, 11, 1244, 30);
		dbEditContentPane.add(lblEditExistingCustomer);

		JLabel lblIdEdit = new JLabel("ID");
		lblIdEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblIdEdit.setBounds(80, 67, 120, 20);
		dbEditContentPane.add(lblIdEdit);

		textFieldIdEdit = new JTextField();
		textFieldIdEdit.setToolTipText(
				englishPack ? "The customer's ID. Cannot be changed." : "ID-ul clientului. Nu poate fi schimbat");
		textFieldIdEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldIdEdit.setEditable(false);
		textFieldIdEdit.setColumns(10);
		textFieldIdEdit.setBounds(210, 67, 250, 20);
		dbEditContentPane.add(textFieldIdEdit);

		JLabel lblNameEdit = new JLabel(englishPack ? "Name*" : "Nume*");
		lblNameEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNameEdit.setBounds(80, 110, 120, 20);
		dbEditContentPane.add(lblNameEdit);

		textFieldNameEdit = new JTextField();
		textFieldNameEdit.setToolTipText(englishPack ? "The name of the new customer." : "Numele clientului");
		textFieldNameEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldNameEdit.setColumns(10);
		textFieldNameEdit.setBounds(210, 110, 250, 20);
		dbEditContentPane.add(textFieldNameEdit);

		JLabel lblCarModelEdit = new JLabel(englishPack ? "Car model*" : "Modelul masinii*");
		lblCarModelEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCarModelEdit.setBounds(80, 149, 120, 20);
		dbEditContentPane.add(lblCarModelEdit);

		textFieldCarModelEdit = new JTextField();
		textFieldCarModelEdit
				.setToolTipText(englishPack ? "The car model of the new customer." : "Modelul masinii clientului.");
		textFieldCarModelEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldCarModelEdit.setColumns(10);
		textFieldCarModelEdit.setBounds(210, 149, 250, 20);
		dbEditContentPane.add(textFieldCarModelEdit);

		JLabel lblRegistrationIdEdit = new JLabel(englishPack ? "Registration ID*" : "Nr inmatriculare*");
		lblRegistrationIdEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblRegistrationIdEdit.setBounds(80, 194, 120, 20);
		dbEditContentPane.add(lblRegistrationIdEdit);

		textFieldRegIdEdit = new JTextField();
		textFieldRegIdEdit.setToolTipText(englishPack ? "The registration ID of the customer's car."
				: "Numarul de inmatriculare al masinii clientului.");
		textFieldRegIdEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldRegIdEdit.setColumns(10);
		textFieldRegIdEdit.setBounds(210, 194, 250, 20);
		dbEditContentPane.add(textFieldRegIdEdit);

		JLabel lblEmailEdit = new JLabel("Email*");
		lblEmailEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblEmailEdit.setBounds(779, 67, 120, 20);
		dbEditContentPane.add(lblEmailEdit);

		textFieldEmailEdit = new JTextField();
		textFieldEmailEdit
				.setToolTipText(englishPack ? "The customer's email address." : "Adresa de email a clientului.");
		textFieldEmailEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldEmailEdit.setColumns(10);
		textFieldEmailEdit.setBounds(909, 70, 276, 20);
		dbEditContentPane.add(textFieldEmailEdit);

		JLabel lblPhoneNumberEdit = new JLabel(englishPack ? "Phone number" : "Numar telefon");
		lblPhoneNumberEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblPhoneNumberEdit.setBounds(779, 110, 120, 20);
		dbEditContentPane.add(lblPhoneNumberEdit);

		textFieldPhoneEdit = new JTextField();
		textFieldPhoneEdit
				.setToolTipText(englishPack ? "The customer's phone number." : "Numarul de telefon al clientului");
		textFieldPhoneEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldPhoneEdit.setColumns(10);
		textFieldPhoneEdit.setBounds(909, 113, 276, 20);
		dbEditContentPane.add(textFieldPhoneEdit);

		JLabel lblItpEndDateEdit = new JLabel(englishPack ? "ITP end date*" : "Data exp. ITP*");
		lblItpEndDateEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblItpEndDateEdit.setBounds(779, 152, 120, 20);
		dbEditContentPane.add(lblItpEndDateEdit);

		textFieldItpEdit = new JTextField();
		textFieldItpEdit.setToolTipText(
				englishPack ? "The date at which the ITP for the customer's car expires. Date format DD-MM-YYYY"
						: "Data la care expira ITP-ul pentru masina clientului. Format: ZZ-LL-AAAA, e.g. 23-04-2016");
		textFieldItpEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldItpEdit.setColumns(10);
		textFieldItpEdit.setBounds(909, 152, 276, 20);
		dbEditContentPane.add(textFieldItpEdit);

		JLabel lblNotifiedEdit = new JLabel(englishPack ? "Notified?" : "Notificat?");
		lblNotifiedEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNotifiedEdit.setBounds(779, 197, 120, 20);
		dbEditContentPane.add(lblNotifiedEdit);

		comboBoxNotifiedEdit = new JComboBox();
		String[] editOptions = null;
		if (englishPack) {
			editOptions = new String[] { "Yes", "No" };
		} else {
			editOptions = new String[] { "Da", "Nu" };
		}
		comboBoxNotifiedEdit.setModel(new DefaultComboBoxModel(editOptions));
		comboBoxNotifiedEdit.setSelectedIndex(1);
		comboBoxNotifiedEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		comboBoxNotifiedEdit.setBounds(909, 197, 75, 20);
		dbEditContentPane.add(comboBoxNotifiedEdit);

		JLabel lblCommentsEdit = new JLabel(englishPack ? "Comments:" : "Comentarii:");
		lblCommentsEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCommentsEdit.setBounds(80, 235, 120, 20);
		dbEditContentPane.add(lblCommentsEdit);

		textAreaCommentsEdit = new JTextArea();
		textAreaCommentsEdit.setToolTipText(
				englishPack ? "Any other comments regarding this customer." : "Orice alte comentarii despre client.");
		textAreaCommentsEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textAreaCommentsEdit.setBounds(80, 270, 1105, 130);
		textAreaCommentsEdit.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		dbEditContentPane.add(textAreaCommentsEdit);

		JButton btnSaveChanges = new JButton(englishPack ? "Save changes" : "Salveaza");
		btnSaveChanges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Customer newCustomer = new CustomerImpl();

				if (textFieldNameEdit.getText().equals("") || textFieldCarModelEdit.getText().equals("")
						|| textFieldRegIdEdit.getText().equals("") || textFieldEmailEdit.getText().equals("")
						|| textFieldItpEdit.getText().equals("")) {
					JOptionPane.showMessageDialog(getContentPane(),
							englishPack ? "All fields marked with * must be filled in."
									: "Toate zonele marcate cu asterisc * trebuie completate.");
				} else {
					if (textFieldNameEdit.getText().length() > 64) {
						JOptionPane.showMessageDialog(getContentPane(),
								englishPack ? "Customer name cannot be longer than 64 characters."
										: "Numele clientului nu poate fi mai lung de 64 de caractere.");
					} else if (textFieldCarModelEdit.getText().length() > 64) {
						JOptionPane.showMessageDialog(getContentPane(),
								englishPack ? "Car model cannot be longer than 64 characters."
										: "Modelul masinii nu poate fi mai lung de 64 de caractere.");
					} else if (textFieldRegIdEdit.getText().length() > 10) {
						JOptionPane.showMessageDialog(getContentPane(),
								englishPack ? "Registration ID cannot be longer than 10 characters."
										: "Numarul de inmatriculare nu poate fi mai lung de 10 caractere.");
					} else if (textFieldEmailEdit.getText().length() > 64) {
						JOptionPane.showMessageDialog(getContentPane(),
								englishPack ? "Email cannot be longer than 64 characters."
										: "Emailul nu poate fi mai lung de 64 de caractere.");
					} else if (textFieldPhoneEdit.getText().length() > 13) {
						JOptionPane.showMessageDialog(getContentPane(),
								englishPack ? "Phone number cannot be longer than 13 characters."
										: "Numarul de telefon nu poate fi mai lung de 13 caractere.");
					} else if (textAreaCommentsEdit.getText().length() > 512) {
						JOptionPane.showMessageDialog(getContentPane(),
								englishPack ? "Comments section cannot be longer than 512 characters."
										: "Comentariile nu pot fi mai lungi de 512 caractere.");
					} else {
						String inputDate = textFieldItpEdit.getText();
						String[] inputDateSegmented = inputDate.split("-");
						if (inputDateSegmented.length != 3) {
							JOptionPane.showMessageDialog(getContentPane(), englishPack
									? "ITP end date must be of format DD-MM-YYYY"
									: "Data la care expira ITP-ul trebuie sa fie in format: ZZ-LL-AAAA, e.g. 23-04-2016");
						} else {
							try {
								newCustomer.setITPEndDate(new LocalDate(Integer.parseInt(inputDateSegmented[2]),
										Integer.parseInt(inputDateSegmented[1]),
										Integer.parseInt(inputDateSegmented[0])));
							} catch (NumberFormatException e) {
								JOptionPane.showMessageDialog(getContentPane(), englishPack
										? "ITP end date must be of format DD-MM-YYYY"
										: "Data la care expira ITP-ul trebuie sa fie in format: ZZ-LL-AAAA, e.g. 23-04-2016");
							} catch (IllegalFieldValueException e) {
								JOptionPane.showMessageDialog(getContentPane(),
										englishPack ? "ITP end date must be a valid date."
												: "Data la care expira ITP-ul trebuie sa fie o data reala");
							}
							if (newCustomer.getITPEndDate() != null) {
								newCustomer.setId(Integer.parseInt(textFieldIdEdit.getText()));
								newCustomer.setName(textFieldNameEdit.getText());
								newCustomer.setCarModel(textFieldCarModelEdit.getText());
								newCustomer.setRegistId(textFieldRegIdEdit.getText());
								newCustomer.setEmail(textFieldEmailEdit.getText());
								newCustomer.setPhoneNr(textFieldPhoneEdit.getText());
								newCustomer.setEmailSent(comboBoxNotifiedEdit.getSelectedIndex() == 0 ? true : false);
								newCustomer.setOther(textAreaCommentsEdit.getText());

								try {
									itpService.editCustomer(newCustomer);
								} catch (IllegalArgumentException e) {
									JOptionPane.showMessageDialog(getContentPane(),
											englishPack ? "Failed to edit customer."
													: "Nu s-a putut modifica clientul (eroare interna).");
								}
								JOptionPane.showMessageDialog(getContentPane(),
										englishPack ? "Customer data successfully updated."
												: "Clientul a fost modificat cu succes.");
								changePanel("dbView");
							}
						}
					}
				}

			}
		});
		btnSaveChanges.setToolTipText(englishPack
				? "Saves any changes made to this customer. All fields marked with * must be filled in."
				: "Salveaza toate modificarile facute acestui client. Toate zonele marcate cu asterisc * trebuie completate.");
		btnSaveChanges.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnSaveChanges.setBackground(Color.GREEN);
		btnSaveChanges.setBounds(560, 437, 140, 40);
		dbEditContentPane.add(btnSaveChanges);
		notifContentPane = new JPanel();
		notifContentPane.setLayout(null);
		notifContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(notifContentPane);

		/* Create the notifications panel */
		JButton btnNotifyAll = new JButton(englishPack ? "Notify all" : "Notifica toti");
		btnNotifyAll.setToolTipText(englishPack
				? "Notifies via email all the customers on the above list (make sure to refresh the list before)."
				: "Notifica toti clientii din lista de mai sus (apasati Refresh mai intai)");
		btnNotifyAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Customer> customers;
				int days = -1;
				try {
					days = Integer.parseInt(textFieldDaysNotif.getText());
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(getContentPane(),
							englishPack ? "Invalid number of days. Must be a number." : "Numar invalid de zile.");
				}
				if (days >= 0) {
					int confirm = JOptionPane.showConfirmDialog(getContentPane(),
							englishPack ? "Are you sure you want to notify all customers on this page?"
									: "Sunteti sigur ca vreti sa notificati toti clientii din lista?",
							englishPack ? "Notify all confirmation" : "Confirmare notificare toti",
							JOptionPane.YES_NO_OPTION);
					if (confirm == 0) {
						customers = itpService.getNotifCustomers(days);
						emailService.notifyCustomers(customers);
						for (Customer customer : customers) {
							itpService.updateNotified(customer.getId(), true);
						}
						JOptionPane.showMessageDialog(getContentPane(),
								englishPack ? "All eligible customers have been notified."
										: "Toti clientii eligibili au fost notificati.");
						changePanel("notifUp");
					}
				}
			}
		});
		btnNotifyAll.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNotifyAll.setBounds(10, 487, 140, 40);
		notifContentPane.add(btnNotifyAll);

		JButton btnNotify = new JButton(englishPack ? "Notify" : "Notifica");
		btnNotify
				.setToolTipText(englishPack ? "Notify the customer corresponding to the customer ID provided via email."
						: "Notifica prin email clientul cu ID-ul corespunzator celui de mai sus.");
		btnNotify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Customer cust = checkID(textFieldCustomerIDNotif.getText());
				if (cust != null) {
					int confirm;
					if (cust.getEmailSent()) {
						confirm = JOptionPane.showConfirmDialog(getContentPane(),
								englishPack ? ("Are you sure you want to notify customer " + cust.getName() + " again?")
										: ("Sunteti sigur ca vreti sa notificati clientul " + cust.getName()
												+ " din nou?"),
								englishPack ? "Notification confirmation" : "Confirmare notificare",
								JOptionPane.YES_NO_OPTION);
					} else {
						confirm = JOptionPane.showConfirmDialog(getContentPane(),
								englishPack ? ("Are you sure you want to notify customer " + cust.getName() + "?")
										: ("Sunteti sigur ca vreti sa notificati clientul " + cust.getName() + "?"),
								englishPack ? "Notification confirmation" : "Confirmare notificare",
								JOptionPane.YES_NO_OPTION);
					}
					if (confirm == 0) {
						emailService.notifyCustomer(cust);
						itpService.updateNotified(cust.getId(), true);
						JOptionPane.showMessageDialog(getContentPane(),
								englishPack ? ("Customer " + cust.getName() + " has been notified.")
										: ("Clientul " + cust.getName() + " a fost notificat."));
						changePanel("notifUp");
					}
				}
			}
		});
		btnNotify.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNotify.setBackground(Color.GREEN);
		btnNotify.setBounds(1114, 437, 140, 40);
		notifContentPane.add(btnNotify);

		JButton btnDeleteNotif = new JButton(englishPack ? "Delete" : "Sterge");
		btnDeleteNotif.setToolTipText(
				englishPack ? "Deletes the customer corresponding to the customer ID above from the database."
						: "Sterge clientul care corespunde ID-ului de mai sus");
		btnDeleteNotif.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Customer cust = checkID(textFieldCustomerIDNotif.getText());
				if (cust != null) {
					int confirm = JOptionPane.showConfirmDialog(getContentPane(), englishPack
							? ("Are you sure you want to delete customer " + cust.getName() + " from the database?")
							: ("Sunteti sigur ca vreti sa stergeti clientul cu numele " + cust.getName()
									+ " din baza de date?"),
							englishPack ? "Delete confirmation" : "Confirmare stergere", JOptionPane.YES_NO_OPTION);
					if (confirm == 0) {
						itpService.deleteCustomer(cust.getId());
						JOptionPane.showMessageDialog(getContentPane(),
								englishPack ? ("Customer " + cust.getName() + " has been deleted from the database.")
										: ("Clientul cu numele " + cust.getName() + " a fost sters din baza de date."));
						changePanel("notifUp");
					}
				}
			}
		});
		btnDeleteNotif.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnDeleteNotif.setBackground(Color.RED);
		btnDeleteNotif.setBounds(1114, 487, 140, 40);
		notifContentPane.add(btnDeleteNotif);

		JButton btnEditNotif = new JButton(englishPack ? "Edit" : "Modifica");
		btnEditNotif.setToolTipText(
				englishPack ? "Opens the edit panel for the customer with the ID given in the Customer ID box."
						: "Deschide fereastra de modificare a clientului cu ID-ul dat mai sus.");
		btnEditNotif.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Customer cust = checkID(textFieldCustomerIDNotif.getText());
				if (cust != null) {
					textFieldIdEdit.setText(cust.getId().toString());
					textFieldNameEdit.setText(cust.getName());
					textFieldCarModelEdit.setText(cust.getCarModel());
					textFieldRegIdEdit.setText(cust.getRegistId());
					textFieldEmailEdit.setText(cust.getEmail());
					textFieldPhoneEdit.setText(cust.getPhoneNr());
					DateTimeFormatter df = DateTimeFormat.forPattern("dd-MM-yyyy");
					String itpEndDate = df.print(cust.getITPEndDate());
					textFieldItpEdit.setText(itpEndDate);
					comboBoxNotifiedEdit.setSelectedIndex(cust.getEmailSent() ? 0 : 1);
					textAreaCommentsEdit.setText(cust.getOther());
					changePanel("dbEdit");
				}
			}
		});
		btnEditNotif.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnEditNotif.setBounds(950, 487, 140, 40);
		notifContentPane.add(btnEditNotif);

		JLabel lblCustomerIDNotif = new JLabel(englishPack ? "Customer ID:" : "ID client:");
		lblCustomerIDNotif.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCustomerIDNotif.setHorizontalAlignment(SwingConstants.LEFT);
		lblCustomerIDNotif.setBounds(951, 447, 99, 20);
		notifContentPane.add(lblCustomerIDNotif);

		textFieldCustomerIDNotif = new JTextField();
		textFieldCustomerIDNotif
				.setToolTipText(englishPack ? "The ID of the customer you with to notify, edit or delete."
						: "ID-ul clientului pe care vreti sa-l notificati, modificati sau stergeti.");
		textFieldCustomerIDNotif.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldCustomerIDNotif.setHorizontalAlignment(SwingConstants.LEFT);
		textFieldCustomerIDNotif.setColumns(4);
		textFieldCustomerIDNotif.setBounds(1050, 447, 40, 20);
		notifContentPane.add(textFieldCustomerIDNotif);

		notifTextArea = new JTextArea();
		notifTextArea.setFont(new Font("Courier New", Font.PLAIN, 13));
		notifTextArea.setEditable(false);
		notifTextArea.setBounds(10, 11, 564, 217);

		JScrollPane notifScrollPane = new JScrollPane(notifTextArea);
		notifScrollPane.setBounds(10, 11, 1244, 415);
		notifContentPane.add(notifScrollPane);

		JLabel lblNoOfDays = new JLabel(englishPack ? "No. of days:" : "Numar zile:");
		lblNoOfDays.setHorizontalAlignment(SwingConstants.LEFT);
		lblNoOfDays.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNoOfDays.setBounds(549, 447, 99, 20);
		notifContentPane.add(lblNoOfDays);

		textFieldDaysNotif = new JTextField();
		textFieldDaysNotif.setToolTipText(englishPack
				? "The number of days from the current date used as a criterion to select the customers that must be notified about their ITP expiring."
				: "Numarul de zile de la data de astazi folosit ca si criteriu pentru selectarea clientilor care trebuie notificati.");
		textFieldDaysNotif.setHorizontalAlignment(SwingConstants.LEFT);
		textFieldDaysNotif.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldDaysNotif.setColumns(4);
		textFieldDaysNotif.setBounds(648, 447, 40, 20);
		notifContentPane.add(textFieldDaysNotif);

		JButton btnRefreshNotif = new JButton("Refresh");
		btnRefreshNotif
				.setToolTipText(englishPack ? "Refreshes the above list according to the number of days provided above."
						: "Rescrie lista de mai sus pentru a fi in conformitate cu numarul de zile selectat");
		btnRefreshNotif.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int days = -1;
				try {
					days = Integer.parseInt(textFieldDaysNotif.getText());
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(getContentPane(),
							englishPack ? "Invalid number of days. Must be a number." : "Numar invalid de zile.");
				}
				if (days >= 0) {
					notifTextArea.setText(itpService.formattedString(itpService.getNotifCustomers(days)));
				}
			}
		});
		btnRefreshNotif.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnRefreshNotif.setBounds(548, 487, 140, 40);
		notifContentPane.add(btnRefreshNotif);

	}

	/**
	 * Create the menu bar
	 */
	private void createMenuBar() {
		JMenu menu, dbSubmenu;
		JMenuItem homeMenuItem, dbViewMenuItem, dbSearchMenuItem, dbAddMenuItem, notifMenuItem, exitMenuItem;

		// Create the menu bar.
		menuBar = new JMenuBar();
		menuBar.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		menuBar.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));

		// Build the first menu.
		menu = new JMenu(englishPack ? "Menu" : "Meniu");
		menu.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		menuBar.add(menu);

		// Home menu item
		homeMenuItem = new JMenuItem("Start");
		homeMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		homeMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changePanel("welcome");
			}
		});
		menu.add(homeMenuItem);
		menu.addSeparator();

		// Database submenu
		dbSubmenu = new JMenu(englishPack ? "Database" : "Baza de date");
		dbSubmenu.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		menu.add(dbSubmenu);

		// Database > view
		dbViewMenuItem = new JMenuItem(englishPack ? "View" : "Deschide");
		dbViewMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		dbViewMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changePanel("dbView");
			}
		});

		dbSubmenu.add(dbViewMenuItem);

		// Database > search
		dbSearchMenuItem = new JMenuItem(englishPack ? "Search" : "Cautare");
		dbSearchMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		dbSearchMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changePanel("dbSearch");
			}
		});
		dbSubmenu.add(dbSearchMenuItem);

		// Database > add
		dbAddMenuItem = new JMenuItem(englishPack ? "Add new customer" : "Adauga client");
		dbAddMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		dbAddMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changePanel("dbAdd");
			}
		});
		dbSubmenu.add(dbAddMenuItem);

		// Notifications menu item
		menu.addSeparator();
		notifMenuItem = new JMenuItem(englishPack ? "Notifications" : "Notificari");
		notifMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		notifMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changePanel("notifUp");
			}
		});
		menu.add(notifMenuItem);

		// Exit menu item
		exitMenuItem = new JMenuItem(englishPack ? "Close" : "Iesire");
		exitMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int confirm = JOptionPane.showConfirmDialog(getContentPane(),
						englishPack ? "Close Application?" : "Inchide aplicatia?",
						englishPack ? "Exit Confirmation" : "Confirmare iesire", JOptionPane.YES_NO_OPTION);
				if (confirm == 0) {
					cleanUp();
					System.exit(0);
				}
			}
		});
		menu.add(exitMenuItem);
	}

	/**
	 * Switch between panels.
	 * 
	 * @param visiblePanel
	 *            the panel you want to make visible.
	 */
	private void changePanel(String visiblePanel) {
		// Set them all false first
		loginContentPane.setVisible(false);
		welcomeContentPane.setVisible(false);
		dbViewContentPane.setVisible(false);
		dbSearchContentPane.setVisible(false);
		dbAddContentPane.setVisible(false);
		dbEditContentPane.setVisible(false);
		notifContentPane.setVisible(false);

		// Enable the desired one
		if (visiblePanel.equals("welcome")) {
			welcomeContentPane.setVisible(true);
		} else if (visiblePanel.equals("dbView")) {
			// Populate dbViewTextArea
			dbViewTextArea.setText(itpService.formattedString(itpService.getCustomers()));
			textFieldCustomerIDView.setText("");
			dbViewContentPane.setVisible(true);
			setTitle(englishPack ? "ITP Manager - View database" : "ITP Manager - Baza de date");
		} else if (visiblePanel.equals("dbSearch")) {
			dbSearchContentPane.setVisible(true);
			setTitle(englishPack ? "ITP Manager - Search" : "ITP Manager - Cautare");
		} else if (visiblePanel.equals("dbAdd")) {
			clearAddPanel();
			dbAddContentPane.setVisible(true);
			setTitle(englishPack ? "ITP Manager - Add new customer" : "ITP Manager - Adaugare client");
		} else if (visiblePanel.equals("dbEdit")) {
			dbEditContentPane.setVisible(true);
			setTitle(englishPack ? "ITP Manager - Edit customer" : "ITP Manager - Modificare client");
		} else if (visiblePanel.equals("notifUp")) {
			// Populate notifTextArea - nr of days should be taken from user
			// input
			notifTextArea.setText(itpService.formattedString(itpService.getNotifCustomers(15)));
			textFieldCustomerIDNotif.setText("");
			textFieldDaysNotif.setText("15");
			notifContentPane.setVisible(true);
			setTitle(englishPack ? "ITP Manager - Notifications" : "ITP Manager - Notificari");
		}
	}

	/**
	 * Method called when closing the application to do any database cleanup
	 */
	private void cleanUp() {
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

	/**
	 * Check if a given ID as a string is indeed a valid ID (a number) and a
	 * customer with that ID exists in the database
	 * 
	 * @param id
	 *            The customer ID to be checked
	 * @return The Customer object with the given ID
	 */
	private Customer checkID(String id) {
		int custID = -1;
		Customer cust = null;
		try {
			custID = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(getContentPane(),
					englishPack ? "Invalid customer ID. Must be a number." : "ID client invalid.");
			return cust;
		}
		try {
			cust = itpService.getCustomer(custID);
		} catch (RuntimeException e) {
			JOptionPane.showMessageDialog(getContentPane(),
					englishPack ? ("Customer with ID " + custID + " not found in the database")
							: ("Clientul cu ID-ul " + custID + " nu exista in baza de date."));
		}
		return cust;
	}

	/**
	 * Clears all the text fields/areas in the Add new customer panel
	 */
	private void clearAddPanel() {
		textFieldNameAdd.setText("");
		textFieldCarModelAdd.setText("");
		textFieldRegIdAdd.setText("");
		textFieldEmailAdd.setText("");
		textFieldPhoneAdd.setText("");
		textFieldITPAdd.setText("");
		comboBoxNotifiedAdd.setSelectedIndex(1);
		textAreaCommentsAdd.setText("");
	}
}
