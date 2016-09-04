
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

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

	/* Back-end variables */
	private Connection db = null;
	private JdbcService itpService = null;
	private NotificationService emailService = null;
	

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
		/* JFrame configuration */
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 1280, 600);
		setTitle("ITP Manager");
		getContentPane().setLayout(new CardLayout(0, 0));
		
		/* Custom made close operation in order to call cleanUp() on any possible exit */
		WindowAdapter exitListener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int confirm = JOptionPane.showConfirmDialog(getContentPane(), "Close Application?",
						"Exit Confirmation", JOptionPane.YES_NO_OPTION);
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

		JLabel lblPassword = new JLabel("Password");
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

		JButton btnLogin = new JButton("Login");
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
					JOptionPane.showMessageDialog(loginContentPane, "Driver not found.");
				} catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(loginContentPane, "Config file not found.");
				} catch (IOException e) {
					JOptionPane.showMessageDialog(loginContentPane, "I/O exception occured: " + e.getMessage());
				}

				try {
					// connect to the database ...
					db = DriverManager.getConnection(url, username, password);
				} catch (SQLException e) {
					if (e.getMessage().contains("authentication")) {
						JOptionPane.showMessageDialog(loginContentPane, "Invalid username and/or password");
					} else {
						JOptionPane.showMessageDialog(loginContentPane, "Invalid url (host/database)");
					}
				}

				if (db != null) {
					itpService = new ITPService(db);
					emailService = new EmailService();

					changePanel("welcome");
					menuBar.setVisible(true);
				}

				// JLabel lblDialog = new JLabel("Are you sure?");
				// int action = JOptionPane.showConfirmDialog(loginContentPane,
				// lblDialog, "Sure?",
				// JOptionPane.OK_CANCEL_OPTION);
				// if (action != 0) {
				// JOptionPane.showMessageDialog(loginContentPane, "Cancel, X or
				// escape key selected");
				// } else {
				// changePanel("welcome");
				// menuBar.setVisible(true);
				//
				// System.out.println("Username: " +
				// textFieldUsername.getText());
				// System.out.println("Password: " + (new
				// String(pwdFieldPassword.getPassword())));
				// }
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

		JLabel lblQuickAccess = new JLabel("Quick access:");
		lblQuickAccess.setHorizontalAlignment(SwingConstants.CENTER);
		lblQuickAccess.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblQuickAccess.setBounds(337, 238, 574, 20);
		welcomeContentPane.add(lblQuickAccess);

		JButton btnUpcoming = new JButton("Upcoming");
		btnUpcoming.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnUpcoming.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changePanel("notifUp");
			}
		});
		btnUpcoming.setBounds(287, 269, 150, 40);
		welcomeContentPane.add(btnUpcoming);

		JButton btnViewDatabase = new JButton("View database");
		btnViewDatabase.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnViewDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				changePanel("dbView");
			}
		});
		btnViewDatabase.setBounds(457, 269, 150, 40);
		welcomeContentPane.add(btnViewDatabase);

		JButton btnSearch = new JButton("Search");
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				changePanel("dbSearch");
			}
		});
		btnSearch.setBounds(627, 269, 150, 40);
		welcomeContentPane.add(btnSearch);

		JButton btnAddCustomer = new JButton("Add customer");
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

		JButton btnAddCustomerView = new JButton("Add customer");
		btnAddCustomerView.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnAddCustomerView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changePanel("dbAdd");
			}
		});
		btnAddCustomerView.setBounds(10, 439, 150, 40);
		dbViewContentPane.add(btnAddCustomerView);

		JButton btnSearchView = new JButton("Search");
		btnSearchView.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnSearchView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changePanel("dbSearch");
			}
		});
		btnSearchView.setBounds(10, 487, 150, 40);
		dbViewContentPane.add(btnSearchView);

		JButton btnDeleteView = new JButton("Delete");
		btnDeleteView.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnDeleteView.setBackground(Color.RED);
		btnDeleteView.setBounds(1134, 487, 120, 40);
		dbViewContentPane.add(btnDeleteView);

		JButton btnEditView = new JButton("Edit");
		btnEditView.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnEditView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				changePanel("dbEdit");
			}
		});
		btnEditView.setBounds(1004, 487, 120, 40);
		dbViewContentPane.add(btnEditView);

		JLabel lblCustomerIdView = new JLabel("Customer ID:");
		lblCustomerIdView.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCustomerIdView.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCustomerIdView.setBounds(1004, 450, 120, 20);
		dbViewContentPane.add(lblCustomerIdView);

		textFieldCustomerIDView = new JTextField();
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

		/* Create the database search panel */
		dbSearchContentPane = new JPanel();
		getContentPane().add(dbSearchContentPane);
		dbSearchContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		dbSearchContentPane.setLayout(null);

		JLabel lblSearchBy = new JLabel("Search by");
		lblSearchBy.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSearchBy.setBounds(10, 21, 80, 20);
		dbSearchContentPane.add(lblSearchBy);

		comboBoxSearch = new JComboBox();
		comboBoxSearch.setFont(new Font("Tahoma", Font.PLAIN, 15));
		comboBoxSearch.setModel(new DefaultComboBoxModel(
				new String[] { "ID", "name", "car model", "registration ID", "email", "phone number" }));
		comboBoxSearch.setSelectedIndex(0);
		comboBoxSearch.setBounds(85, 21, 120, 20);
		dbSearchContentPane.add(comboBoxSearch);

		JLabel lblColumn = new JLabel(":");
		lblColumn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblColumn.setBounds(209, 21, 10, 20);
		dbSearchContentPane.add(lblColumn);

		textFieldSearch = new JTextField();
		textFieldSearch.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldSearch.setBounds(217, 21, 927, 20);
		dbSearchContentPane.add(textFieldSearch);
		textFieldSearch.setColumns(10);

		JButton btnSearch_2 = new JButton("Search");
		btnSearch_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnSearch_2.setBounds(1154, 10, 100, 40);
		dbSearchContentPane.add(btnSearch_2);

		dbSearchTextArea = new JTextArea();
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

		JLabel lblAddNewCustomer = new JLabel("Add new customer");
		lblAddNewCustomer.setFont(new Font("Calibri", Font.BOLD, 30));
		lblAddNewCustomer.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddNewCustomer.setBounds(10, 11, 1244, 30);
		dbAddContentPane.add(lblAddNewCustomer);

		JLabel lblIdAdd = new JLabel("ID");
		lblIdAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblIdAdd.setBounds(80, 67, 120, 20);
		dbAddContentPane.add(lblIdAdd);

		textFieldIdAdd = new JTextField();
		textFieldIdAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldIdAdd.setEnabled(false);
		textFieldIdAdd.setEditable(false);
		textFieldIdAdd.setBounds(210, 67, 250, 20);
		dbAddContentPane.add(textFieldIdAdd);
		textFieldIdAdd.setColumns(10);

		JLabel lblNameAdd = new JLabel("Name*");
		lblNameAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNameAdd.setBounds(80, 110, 120, 20);
		dbAddContentPane.add(lblNameAdd);

		textFieldNameAdd = new JTextField();
		textFieldNameAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldNameAdd.setColumns(10);
		textFieldNameAdd.setBounds(210, 110, 250, 20);
		dbAddContentPane.add(textFieldNameAdd);

		JLabel lblCarModelAdd = new JLabel("Car model*");
		lblCarModelAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCarModelAdd.setBounds(80, 149, 120, 20);
		dbAddContentPane.add(lblCarModelAdd);

		textFieldCarModelAdd = new JTextField();
		textFieldCarModelAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldCarModelAdd.setColumns(10);
		textFieldCarModelAdd.setBounds(210, 149, 250, 20);
		dbAddContentPane.add(textFieldCarModelAdd);

		JLabel lblRegistrationIdAdd = new JLabel("Registration ID*");
		lblRegistrationIdAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblRegistrationIdAdd.setBounds(80, 194, 120, 20);
		dbAddContentPane.add(lblRegistrationIdAdd);

		textFieldRegIdAdd = new JTextField();
		textFieldRegIdAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldRegIdAdd.setColumns(10);
		textFieldRegIdAdd.setBounds(210, 194, 250, 20);
		dbAddContentPane.add(textFieldRegIdAdd);

		JLabel lblEmailAdd = new JLabel("Email*");
		lblEmailAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblEmailAdd.setBounds(779, 67, 120, 20);
		dbAddContentPane.add(lblEmailAdd);

		textFieldEmailAdd = new JTextField();
		textFieldEmailAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldEmailAdd.setColumns(10);
		textFieldEmailAdd.setBounds(909, 70, 276, 20);
		dbAddContentPane.add(textFieldEmailAdd);

		JLabel lblPhoneNumberAdd = new JLabel("Phone number");
		lblPhoneNumberAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblPhoneNumberAdd.setBounds(779, 110, 120, 20);
		dbAddContentPane.add(lblPhoneNumberAdd);

		textFieldPhoneAdd = new JTextField();
		textFieldPhoneAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldPhoneAdd.setColumns(10);
		textFieldPhoneAdd.setBounds(909, 113, 276, 20);
		dbAddContentPane.add(textFieldPhoneAdd);

		JLabel lblItpEndDateAdd = new JLabel("ITP end date*");
		lblItpEndDateAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblItpEndDateAdd.setBounds(779, 152, 120, 20);
		dbAddContentPane.add(lblItpEndDateAdd);

		textFieldITPAdd = new JTextField();
		textFieldITPAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldITPAdd.setColumns(10);
		textFieldITPAdd.setBounds(909, 152, 276, 20);
		dbAddContentPane.add(textFieldITPAdd);

		JLabel lblNotifiedAdd = new JLabel("Notified?");
		lblNotifiedAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNotifiedAdd.setBounds(779, 197, 120, 20);
		dbAddContentPane.add(lblNotifiedAdd);

		comboBoxNotifiedAdd = new JComboBox();
		comboBoxNotifiedAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		comboBoxNotifiedAdd.setModel(new DefaultComboBoxModel(new String[] { "Yes", "No" }));
		comboBoxNotifiedAdd.setSelectedIndex(1);
		comboBoxNotifiedAdd.setBounds(909, 197, 75, 20);
		dbAddContentPane.add(comboBoxNotifiedAdd);

		JLabel lblCommentsAdd = new JLabel("Comments:");
		lblCommentsAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCommentsAdd.setBounds(80, 235, 120, 20);
		dbAddContentPane.add(lblCommentsAdd);

		textAreaCommentsAdd = new JTextArea();
		textAreaCommentsAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textAreaCommentsAdd.setBounds(80, 270, 1105, 130);
		dbAddContentPane.add(textAreaCommentsAdd);

		JButton btnAdd = new JButton("Add");
		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnAdd.setBackground(Color.GREEN);
		btnAdd.setToolTipText("All fields marked with * must be filled in.");
		btnAdd.setBounds(570, 437, 120, 40);
		dbAddContentPane.add(btnAdd);

		/* Create the database edit existing customer panel */
		dbEditContentPane = new JPanel();
		dbEditContentPane.setLayout(null);
		dbEditContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(dbEditContentPane, "name_49701330959130");
		
		JLabel lblEditExistingCustomer = new JLabel("Edit existing customer");
		lblEditExistingCustomer.setHorizontalAlignment(SwingConstants.CENTER);
		lblEditExistingCustomer.setFont(new Font("Calibri", Font.BOLD, 30));
		lblEditExistingCustomer.setBounds(10, 11, 1244, 30);
		dbEditContentPane.add(lblEditExistingCustomer);
		
		JLabel lblIdEdit = new JLabel("ID");
		lblIdEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblIdEdit.setBounds(80, 67, 120, 20);
		dbEditContentPane.add(lblIdEdit);
		
		textFieldIdEdit = new JTextField();
		textFieldIdEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldIdEdit.setEnabled(false);
		textFieldIdEdit.setEditable(false);
		textFieldIdEdit.setColumns(10);
		textFieldIdEdit.setBounds(210, 67, 250, 20);
		dbEditContentPane.add(textFieldIdEdit);
		
		JLabel lblNameEdit = new JLabel("Name*");
		lblNameEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNameEdit.setBounds(80, 110, 120, 20);
		dbEditContentPane.add(lblNameEdit);
		
		textFieldNameEdit = new JTextField();
		textFieldNameEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldNameEdit.setColumns(10);
		textFieldNameEdit.setBounds(210, 110, 250, 20);
		dbEditContentPane.add(textFieldNameEdit);
		
		JLabel lblCarModelEdit = new JLabel("Car model*");
		lblCarModelEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCarModelEdit.setBounds(80, 149, 120, 20);
		dbEditContentPane.add(lblCarModelEdit);
		
		textFieldCarModelEdit = new JTextField();
		textFieldCarModelEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldCarModelEdit.setColumns(10);
		textFieldCarModelEdit.setBounds(210, 149, 250, 20);
		dbEditContentPane.add(textFieldCarModelEdit);
		
		JLabel lblRegistrationIdEdit = new JLabel("Registration ID*");
		lblRegistrationIdEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblRegistrationIdEdit.setBounds(80, 194, 120, 20);
		dbEditContentPane.add(lblRegistrationIdEdit);
		
		textFieldRegIdEdit = new JTextField();
		textFieldRegIdEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldRegIdEdit.setColumns(10);
		textFieldRegIdEdit.setBounds(210, 194, 250, 20);
		dbEditContentPane.add(textFieldRegIdEdit);
		
		JLabel lblEmailEdit = new JLabel("Email*");
		lblEmailEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblEmailEdit.setBounds(779, 67, 120, 20);
		dbEditContentPane.add(lblEmailEdit);
		
		textFieldEmailEdit = new JTextField();
		textFieldEmailEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldEmailEdit.setColumns(10);
		textFieldEmailEdit.setBounds(909, 70, 276, 20);
		dbEditContentPane.add(textFieldEmailEdit);
		
		JLabel lblPhoneNumberEdit = new JLabel("Phone number");
		lblPhoneNumberEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblPhoneNumberEdit.setBounds(779, 110, 120, 20);
		dbEditContentPane.add(lblPhoneNumberEdit);
		
		textFieldPhoneEdit = new JTextField();
		textFieldPhoneEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldPhoneEdit.setColumns(10);
		textFieldPhoneEdit.setBounds(909, 113, 276, 20);
		dbEditContentPane.add(textFieldPhoneEdit);
		
		JLabel lblItpEndDateEdit = new JLabel("ITP end date*");
		lblItpEndDateEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblItpEndDateEdit.setBounds(779, 152, 120, 20);
		dbEditContentPane.add(lblItpEndDateEdit);
		
		textFieldItpEdit = new JTextField();
		textFieldItpEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldItpEdit.setColumns(10);
		textFieldItpEdit.setBounds(909, 152, 276, 20);
		dbEditContentPane.add(textFieldItpEdit);
		
		JLabel lblNotifiedEdit = new JLabel("Notified?");
		lblNotifiedEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNotifiedEdit.setBounds(779, 197, 120, 20);
		dbEditContentPane.add(lblNotifiedEdit);
		
		comboBoxNotifiedEdit = new JComboBox();
		comboBoxNotifiedEdit.setModel(new DefaultComboBoxModel(new String[] {"Yes", "No"}));
		comboBoxNotifiedEdit.setSelectedIndex(1);
		comboBoxNotifiedEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		comboBoxNotifiedEdit.setBounds(909, 197, 75, 20);
		dbEditContentPane.add(comboBoxNotifiedEdit);
		
		JLabel lblCommentsEdit = new JLabel("Comments:");
		lblCommentsEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCommentsEdit.setBounds(80, 235, 120, 20);
		dbEditContentPane.add(lblCommentsEdit);
		
		textAreaCommentsEdit = new JTextArea();
		textAreaCommentsEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textAreaCommentsEdit.setBounds(80, 270, 1105, 130);
		dbEditContentPane.add(textAreaCommentsEdit);
		
		JButton btnSaveChanges = new JButton("Save changes");
		btnSaveChanges.setToolTipText("All fields marked with * must be filled in.");
		btnSaveChanges.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnSaveChanges.setBackground(Color.GREEN);
		btnSaveChanges.setBounds(560, 437, 140, 40);
		dbEditContentPane.add(btnSaveChanges);
		notifContentPane = new JPanel();
		notifContentPane.setLayout(null);
		notifContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(notifContentPane);

		/* Create the notifications panel */
		JButton btnNotifyAll = new JButton("Notify all");
		btnNotifyAll.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNotifyAll.setBounds(10, 487, 140, 40);
		notifContentPane.add(btnNotifyAll);

		JButton btnNotify = new JButton("Notify");
		btnNotify.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNotify.setBackground(Color.GREEN);
		btnNotify.setBounds(1114, 437, 140, 40);
		notifContentPane.add(btnNotify);

		JButton btnDeleteNotif = new JButton("Delete");
		btnDeleteNotif.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnDeleteNotif.setBackground(Color.RED);
		btnDeleteNotif.setBounds(1114, 487, 140, 40);
		notifContentPane.add(btnDeleteNotif);

		JButton btnEditNotif = new JButton("Edit");
		btnEditNotif.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnEditNotif.setBounds(950, 487, 140, 40);
		notifContentPane.add(btnEditNotif);

		JLabel lblCustomerIDNotif = new JLabel("Customer ID:");
		lblCustomerIDNotif.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCustomerIDNotif.setHorizontalAlignment(SwingConstants.LEFT);
		lblCustomerIDNotif.setBounds(951, 447, 99, 20);
		notifContentPane.add(lblCustomerIDNotif);

		textFieldCustomerIDNotif = new JTextField();
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
		menu = new JMenu("Menu");
		menu.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		menuBar.add(menu);

		// Home menu item
		homeMenuItem = new JMenuItem("Home");
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
		dbSubmenu = new JMenu("Database");
		dbSubmenu.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		menu.add(dbSubmenu);

		// Database > view
		dbViewMenuItem = new JMenuItem("View");
		dbViewMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		dbViewMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changePanel("dbView");
			}
		});

		dbSubmenu.add(dbViewMenuItem);

		// Database > search
		dbSearchMenuItem = new JMenuItem("Search");
		dbSearchMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		dbSearchMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changePanel("dbSearch");
			}
		});
		dbSubmenu.add(dbSearchMenuItem);

		// Database > add
		dbAddMenuItem = new JMenuItem("Add new customer");
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
		notifMenuItem = new JMenuItem("Notifications");
		notifMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		notifMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changePanel("notifUp");
			}
		});
		menu.add(notifMenuItem);

		// Exit menu item
		exitMenuItem = new JMenuItem("Close");
		exitMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int confirm = JOptionPane.showConfirmDialog(getContentPane(), "Close Application?",
						"Exit Confirmation", JOptionPane.YES_NO_OPTION);
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
			dbViewContentPane.setVisible(true);
			setTitle("ITP Manager - View database");
		} else if (visiblePanel.equals("dbSearch")) {
			dbSearchContentPane.setVisible(true);
			setTitle("ITP Manager - Search");
		} else if (visiblePanel.equals("dbAdd")) {
			dbAddContentPane.setVisible(true);
			setTitle("ITP Manager - Add new customer");
		} else if (visiblePanel.equals("dbEdit")) {
			dbEditContentPane.setVisible(true);
			setTitle("ITP Manager - Edit customer");
		} else if (visiblePanel.equals("notifUp")) {
			// Populate notifTextArea
			notifTextArea.setText(itpService.formattedString(itpService.getNotifCustomers(15))); // get the days from the config file
			notifContentPane.setVisible(true);
			setTitle("ITP Manager - Notifications");
		}
	}

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
}
