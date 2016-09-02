
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
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
	// Database edit customer panel
	private JTextField textFieldIdEdit;
	private JTextField textFieldNameEdit;
	private JTextField textFieldCarModelEdit;
	private JTextField textFieldRegIdEdit;
	private JTextField textFieldEmailEdit;
	private JTextField textFieldPhoneEdit;
	private JTextField textFieldITPEdit;
	private JComboBox comboBoxNotifiedEdit;
	private JTextArea textAreaCommentsEdit;
	// Notifications panel
	private JTextField textFieldCustomerIDNotif;
	private JTextArea notifTextArea;

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		setTitle("ITP Manager");
		getContentPane().setLayout(new CardLayout(0, 0));

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
		lblTitle.setBounds(5, 50, 574, 37);
		lblTitle.setFont(new Font("Calibri", Font.BOLD, 30));
		loginContentPane.add(lblTitle);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsername.setBounds(120, 150, 100, 14);
		loginContentPane.add(lblUsername);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setBounds(370, 150, 100, 14);
		loginContentPane.add(lblPassword);

		textFieldUsername = new JTextField();
		textFieldUsername.setBounds(120, 167, 100, 20);
		loginContentPane.add(textFieldUsername);
		textFieldUsername.setColumns(10);

		pwdFieldPassword = new JPasswordField();
		pwdFieldPassword.setBounds(370, 167, 100, 20);
		loginContentPane.add(pwdFieldPassword);

		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				changePanel("welcome");
				menuBar.setVisible(true);

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

		btnLogin.setBounds(245, 250, 100, 40);
		loginContentPane.add(btnLogin);

		/* Create the welcome panel */
		welcomeContentPane = new JPanel();
		welcomeContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		welcomeContentPane.setLayout(null);
		getContentPane().add(welcomeContentPane);

		JLabel lblWelcome = new JLabel("ITP Manager");
		lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcome.setBounds(5, 50, 574, 37);
		lblWelcome.setFont(new Font("Calibri", Font.BOLD, 30));
		welcomeContentPane.add(lblWelcome);

		JLabel lblQuickAccess = new JLabel("Quick access:");
		lblQuickAccess.setHorizontalAlignment(SwingConstants.CENTER);
		lblQuickAccess.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblQuickAccess.setBounds(5, 213, 574, 20);
		welcomeContentPane.add(lblQuickAccess);

		JButton btnUpcoming = new JButton("Upcoming");
		btnUpcoming.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changePanel("notifUp");
			}
		});
		btnUpcoming.setBounds(20, 244, 120, 40);
		welcomeContentPane.add(btnUpcoming);

		JButton btnViewDatabase = new JButton("View database");
		btnViewDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				changePanel("dbView");
			}
		});
		btnViewDatabase.setBounds(160, 244, 120, 40);
		welcomeContentPane.add(btnViewDatabase);

		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				changePanel("dbSearch");
			}
		});
		btnSearch.setBounds(300, 244, 120, 40);
		welcomeContentPane.add(btnSearch);

		JButton btnAddCustomer = new JButton("Add customer");
		btnAddCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changePanel("dbAdd");
			}
		});
		btnAddCustomer.setBounds(440, 244, 120, 40);
		welcomeContentPane.add(btnAddCustomer);

		/* Create the database view panel */
		dbViewContentPane = new JPanel();
		getContentPane().add(dbViewContentPane);
		dbViewContentPane.setLayout(null);
		dbViewContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		JButton btnAddCustomerView = new JButton("Add customer");
		btnAddCustomerView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changePanel("dbAdd");
			}
		});
		btnAddCustomerView.setBounds(10, 239, 120, 40);
		dbViewContentPane.add(btnAddCustomerView);

		JButton btnSearchView = new JButton("Search");
		btnSearchView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changePanel("dbSearch");
			}
		});
		btnSearchView.setBounds(10, 287, 120, 40);
		dbViewContentPane.add(btnSearchView);

		JButton btnDeleteView = new JButton("Delete");
		btnDeleteView.setBackground(Color.RED);
		btnDeleteView.setBounds(454, 287, 120, 40);
		dbViewContentPane.add(btnDeleteView);

		JButton btnEditView = new JButton("Edit");
		btnEditView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				changePanel("dbEdit");
			}
		});
		btnEditView.setBounds(324, 287, 120, 40);
		dbViewContentPane.add(btnEditView);

		JLabel lblCustomerIdView = new JLabel("Customer ID:");
		lblCustomerIdView.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCustomerIdView.setBounds(324, 250, 120, 20);
		dbViewContentPane.add(lblCustomerIdView);

		textFieldCustomerIDView = new JTextField();
		textFieldCustomerIDView.setHorizontalAlignment(SwingConstants.LEFT);
		textFieldCustomerIDView.setBounds(454, 250, 50, 20);
		dbViewContentPane.add(textFieldCustomerIDView);
		textFieldCustomerIDView.setColumns(4);

		dbViewTextArea = new JTextArea();
		dbViewTextArea.setEditable(false);
		dbViewTextArea.setBounds(10, 11, 564, 217);

		JScrollPane dbViewScrollPane = new JScrollPane(dbViewTextArea);
		dbViewScrollPane.setBounds(10, 11, 564, 217);
		dbViewContentPane.add(dbViewScrollPane);

		/* Create the database search panel */
		dbSearchContentPane = new JPanel();
		getContentPane().add(dbSearchContentPane);
		dbSearchContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		dbSearchContentPane.setLayout(null);

		JLabel lblSearchBy = new JLabel("Search by");
		lblSearchBy.setBounds(10, 11, 60, 20);
		dbSearchContentPane.add(lblSearchBy);

		comboBoxSearch = new JComboBox();
		comboBoxSearch.setModel(new DefaultComboBoxModel(
				new String[] { "ID", "name", "car model", "registration ID", "email", "phone number" }));
		comboBoxSearch.setSelectedIndex(0);
		comboBoxSearch.setBounds(75, 11, 120, 20);
		dbSearchContentPane.add(comboBoxSearch);

		JLabel lblColumn = new JLabel(":");
		lblColumn.setBounds(200, 11, 10, 20);
		dbSearchContentPane.add(lblColumn);

		textFieldSearch = new JTextField();
		textFieldSearch.setBounds(209, 11, 254, 20);
		dbSearchContentPane.add(textFieldSearch);
		textFieldSearch.setColumns(10);

		JButton btnSearch_2 = new JButton("Search");
		btnSearch_2.setBounds(473, 10, 100, 23);
		dbSearchContentPane.add(btnSearch_2);

		dbSearchTextArea = new JTextArea();
		dbSearchTextArea.setEditable(false);
		dbSearchTextArea.setBounds(10, 44, 564, 276);

		JScrollPane dbSearchScrollPane = new JScrollPane(dbSearchTextArea);
		dbSearchScrollPane.setBounds(10, 44, 564, 283);
		dbSearchContentPane.add(dbSearchScrollPane);

		/* Create the database add new customer panel */
		dbAddContentPane = new JPanel();
		getContentPane().add(dbAddContentPane);
		dbAddContentPane.setLayout(null);
		dbAddContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		JLabel lblAddNewCustomer = new JLabel("Add new customer");
		lblAddNewCustomer.setFont(new Font("Calibri", Font.BOLD, 20));
		lblAddNewCustomer.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddNewCustomer.setBounds(10, 11, 564, 20);
		dbAddContentPane.add(lblAddNewCustomer);

		JLabel lblIdAdd = new JLabel("ID");
		lblIdAdd.setBounds(10, 51, 90, 20);
		dbAddContentPane.add(lblIdAdd);

		textFieldIdAdd = new JTextField();
		textFieldIdAdd.setEnabled(false);
		textFieldIdAdd.setEditable(false);
		textFieldIdAdd.setBounds(113, 51, 150, 20);
		dbAddContentPane.add(textFieldIdAdd);
		textFieldIdAdd.setColumns(10);

		JLabel lblNameAdd = new JLabel("Name*");
		lblNameAdd.setBounds(10, 82, 90, 20);
		dbAddContentPane.add(lblNameAdd);

		textFieldNameAdd = new JTextField();
		textFieldNameAdd.setColumns(10);
		textFieldNameAdd.setBounds(113, 82, 150, 20);
		dbAddContentPane.add(textFieldNameAdd);

		JLabel lblCarModelAdd = new JLabel("Car model*");
		lblCarModelAdd.setBounds(10, 113, 90, 20);
		dbAddContentPane.add(lblCarModelAdd);

		textFieldCarModelAdd = new JTextField();
		textFieldCarModelAdd.setColumns(10);
		textFieldCarModelAdd.setBounds(113, 113, 150, 20);
		dbAddContentPane.add(textFieldCarModelAdd);

		JLabel lblRegistrationIdAdd = new JLabel("Registration ID*");
		lblRegistrationIdAdd.setBounds(10, 144, 90, 20);
		dbAddContentPane.add(lblRegistrationIdAdd);

		textFieldRegIdAdd = new JTextField();
		textFieldRegIdAdd.setColumns(10);
		textFieldRegIdAdd.setBounds(113, 144, 150, 20);
		dbAddContentPane.add(textFieldRegIdAdd);

		JLabel lblEmailAdd = new JLabel("Email*");
		lblEmailAdd.setBounds(321, 51, 90, 20);
		dbAddContentPane.add(lblEmailAdd);

		textFieldEmailAdd = new JTextField();
		textFieldEmailAdd.setColumns(10);
		textFieldEmailAdd.setBounds(424, 51, 150, 20);
		dbAddContentPane.add(textFieldEmailAdd);

		JLabel lblPhoneNumberAdd = new JLabel("Phone number");
		lblPhoneNumberAdd.setBounds(321, 82, 90, 20);
		dbAddContentPane.add(lblPhoneNumberAdd);

		textFieldPhoneAdd = new JTextField();
		textFieldPhoneAdd.setColumns(10);
		textFieldPhoneAdd.setBounds(424, 82, 150, 20);
		dbAddContentPane.add(textFieldPhoneAdd);

		JLabel lblItpEndDateAdd = new JLabel("ITP end date*");
		lblItpEndDateAdd.setBounds(321, 113, 90, 20);
		dbAddContentPane.add(lblItpEndDateAdd);

		textFieldITPAdd = new JTextField();
		textFieldITPAdd.setColumns(10);
		textFieldITPAdd.setBounds(424, 113, 150, 20);
		dbAddContentPane.add(textFieldITPAdd);

		JLabel lblNotifiedAdd = new JLabel("Notified?");
		lblNotifiedAdd.setBounds(321, 147, 90, 20);
		dbAddContentPane.add(lblNotifiedAdd);

		comboBoxNotifiedAdd = new JComboBox();
		comboBoxNotifiedAdd.setModel(new DefaultComboBoxModel(new String[] { "Yes", "No" }));
		comboBoxNotifiedAdd.setSelectedIndex(1);
		comboBoxNotifiedAdd.setBounds(424, 144, 75, 20);
		dbAddContentPane.add(comboBoxNotifiedAdd);

		JLabel lblCommentsAdd = new JLabel("Comments:");
		lblCommentsAdd.setBounds(10, 175, 90, 20);
		dbAddContentPane.add(lblCommentsAdd);

		textAreaCommentsAdd = new JTextArea();
		textAreaCommentsAdd.setBounds(10, 206, 564, 75);
		dbAddContentPane.add(textAreaCommentsAdd);

		JButton btnAdd = new JButton("Add");
		btnAdd.setBackground(Color.GREEN);
		btnAdd.setToolTipText("All fields marked with * must be filled in.");
		btnAdd.setBounds(250, 292, 90, 35);
		dbAddContentPane.add(btnAdd);

		/* Create the database edit existing customer panel */
		dbEditContentPane = new JPanel();
		dbEditContentPane.setLayout(null);
		dbEditContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(dbEditContentPane);

		JLabel lblEditExistingCustomer = new JLabel("Edit existing customer");
		lblEditExistingCustomer.setHorizontalAlignment(SwingConstants.CENTER);
		lblEditExistingCustomer.setFont(new Font("Calibri", Font.BOLD, 20));
		lblEditExistingCustomer.setBounds(10, 11, 564, 20);
		dbEditContentPane.add(lblEditExistingCustomer);

		JLabel lblIdEdit = new JLabel("ID");
		lblIdEdit.setBounds(10, 51, 90, 20);
		dbEditContentPane.add(lblIdEdit);

		textFieldIdEdit = new JTextField();
		textFieldIdEdit.setEnabled(false);
		textFieldIdEdit.setEditable(false);
		textFieldIdEdit.setColumns(10);
		textFieldIdEdit.setBounds(113, 51, 150, 20);
		dbEditContentPane.add(textFieldIdEdit);

		JLabel lblNameEdit = new JLabel("Name*");
		lblNameEdit.setBounds(10, 82, 90, 20);
		dbEditContentPane.add(lblNameEdit);

		textFieldNameEdit = new JTextField();
		textFieldNameEdit.setColumns(10);
		textFieldNameEdit.setBounds(113, 82, 150, 20);
		dbEditContentPane.add(textFieldNameEdit);

		JLabel lblCarModelEdit = new JLabel("Car model*");
		lblCarModelEdit.setBounds(10, 113, 90, 20);
		dbEditContentPane.add(lblCarModelEdit);

		textFieldCarModelEdit = new JTextField();
		textFieldCarModelEdit.setColumns(10);
		textFieldCarModelEdit.setBounds(113, 113, 150, 20);
		dbEditContentPane.add(textFieldCarModelEdit);

		JLabel lblRegistrationIdEdit = new JLabel("Registration ID*");
		lblRegistrationIdEdit.setBounds(10, 144, 90, 20);
		dbEditContentPane.add(lblRegistrationIdEdit);

		textFieldRegIdEdit = new JTextField();
		textFieldRegIdEdit.setColumns(10);
		textFieldRegIdEdit.setBounds(113, 144, 150, 20);
		dbEditContentPane.add(textFieldRegIdEdit);

		JLabel lblEmailEdit = new JLabel("Email*");
		lblEmailEdit.setBounds(321, 51, 90, 20);
		dbEditContentPane.add(lblEmailEdit);

		textFieldEmailEdit = new JTextField();
		textFieldEmailEdit.setColumns(10);
		textFieldEmailEdit.setBounds(424, 51, 150, 20);
		dbEditContentPane.add(textFieldEmailEdit);

		JLabel lblPhoneNumberEdit = new JLabel("Phone number");
		lblPhoneNumberEdit.setBounds(321, 82, 90, 20);
		dbEditContentPane.add(lblPhoneNumberEdit);

		textFieldPhoneEdit = new JTextField();
		textFieldPhoneEdit.setColumns(10);
		textFieldPhoneEdit.setBounds(424, 82, 150, 20);
		dbEditContentPane.add(textFieldPhoneEdit);

		JLabel lblItpEndDateEdit = new JLabel("ITP end date*");
		lblItpEndDateEdit.setBounds(321, 113, 90, 20);
		dbEditContentPane.add(lblItpEndDateEdit);

		textFieldITPEdit = new JTextField();
		textFieldITPEdit.setColumns(10);
		textFieldITPEdit.setBounds(424, 113, 150, 20);
		dbEditContentPane.add(textFieldITPEdit);

		JLabel lblNotifiedEdit = new JLabel("Notified?");
		lblNotifiedEdit.setBounds(321, 147, 90, 20);
		dbEditContentPane.add(lblNotifiedEdit);

		comboBoxNotifiedEdit = new JComboBox();
		comboBoxNotifiedEdit.setModel(new DefaultComboBoxModel(new String[] { "Yes", "No" }));
		comboBoxNotifiedEdit.setBounds(424, 144, 75, 20);
		dbEditContentPane.add(comboBoxNotifiedEdit);

		JLabel lblCommentsEdit = new JLabel("Comments:");
		lblCommentsEdit.setBounds(10, 175, 90, 20);
		dbEditContentPane.add(lblCommentsEdit);

		textAreaCommentsEdit = new JTextArea();
		textAreaCommentsEdit.setBounds(10, 206, 564, 75);
		dbEditContentPane.add(textAreaCommentsEdit);

		JButton btnSaveChanges = new JButton("Save changes");
		btnSaveChanges.setBackground(Color.GREEN);
		btnSaveChanges.setToolTipText("All fields marked with * must be filled in.");
		btnSaveChanges.setBounds(235, 292, 120, 35);
		dbEditContentPane.add(btnSaveChanges);

		/* Create the notifications panel */
		notifContentPane = new JPanel();
		notifContentPane.setLayout(null);
		notifContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(notifContentPane);

		JButton btnNotifyAll = new JButton("Notify all");
		btnNotifyAll.setBounds(10, 287, 120, 40);
		notifContentPane.add(btnNotifyAll);

		JButton btnNotify = new JButton("Notify");
		btnNotify.setBackground(Color.GREEN);
		btnNotify.setBounds(454, 237, 120, 40);
		notifContentPane.add(btnNotify);

		JButton btnDeleteNotif = new JButton("Delete");
		btnDeleteNotif.setBackground(Color.RED);
		btnDeleteNotif.setBounds(454, 287, 120, 40);
		notifContentPane.add(btnDeleteNotif);

		JButton btnEditNotif = new JButton("Edit");
		btnEditNotif.setBounds(324, 287, 120, 40);
		notifContentPane.add(btnEditNotif);

		JLabel lblCustomerIDNotif = new JLabel("Customer ID:");
		lblCustomerIDNotif.setHorizontalAlignment(SwingConstants.LEFT);
		lblCustomerIDNotif.setBounds(325, 247, 75, 20);
		notifContentPane.add(lblCustomerIDNotif);

		textFieldCustomerIDNotif = new JTextField();
		textFieldCustomerIDNotif.setHorizontalAlignment(SwingConstants.LEFT);
		textFieldCustomerIDNotif.setColumns(4);
		textFieldCustomerIDNotif.setBounds(404, 247, 40, 20);
		notifContentPane.add(textFieldCustomerIDNotif);

		notifTextArea = new JTextArea();
		notifTextArea.setEditable(false);
		notifTextArea.setBounds(10, 11, 564, 217);

		JScrollPane notifScrollPane = new JScrollPane(notifTextArea);
		notifScrollPane.setBounds(10, 11, 564, 217);
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
		menuBar.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));

		// Build the first menu.
		menu = new JMenu("Menu");
		menuBar.add(menu);

		// Home menu item
		homeMenuItem = new JMenuItem("Home");
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
		menu.add(dbSubmenu);

		// Database > view
		dbViewMenuItem = new JMenuItem("View");
		dbViewMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changePanel("dbView");
			}
		});

		dbSubmenu.add(dbViewMenuItem);

		// Database > search
		dbSearchMenuItem = new JMenuItem("Search");
		dbSearchMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changePanel("dbSearch");
			}
		});
		dbSubmenu.add(dbSearchMenuItem);

		// Database > add
		dbAddMenuItem = new JMenuItem("Add new customer");
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
		notifMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changePanel("notifUp");
			}
		});
		menu.add(notifMenuItem);

		// Exit menu item
		exitMenuItem = new JMenuItem("Close");
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu.add(exitMenuItem);
	}

	/**
	 * Switch between panels.
	 * @param visiblePanel the panel you want to make visible.
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
			notifContentPane.setVisible(true);
			setTitle("ITP Manager - Notifications");
		}
	}
}
