import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection db = null;
		String cmd = null;

		try {

//			// use commands until GUI is implemented
//			if (args.length < 1) {
//				return;
//			}
//
//			cmd = args[0];

			Properties prop = new Properties();
			String driverClass = new String();
			String url = new String();
			String username = new String();
			String password = new String();

			try {

				InputStream in = new FileInputStream("lib/jdbc.properties");
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
			PreparedStatement stmt = null;
			ResultSet rs = null;
			String resultingName = new String();

			try {
				stmt = db.prepareStatement("SELECT name FROM customers WHERE id = ?");
				stmt.setInt(1, 1);
				rs = stmt.executeQuery();
				rs.next();
				resultingName = rs.getString(1);
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

			System.out.println("Test name for id = 1: " + resultingName);
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
