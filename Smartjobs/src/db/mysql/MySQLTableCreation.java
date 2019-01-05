package db.mysql;

import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Connection;


public class MySQLTableCreation {
	// Run this as Java application to reset db schema.
	public static void main(String[] args) {
		try {
			// Step 1 Connect to MySQL.
			System.out.println("Connecting to " + MySQLDBUtil.URL);
			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
			Connection conn = DriverManager.getConnection(MySQLDBUtil.URL);
			
			if (conn == null) {
				return;
			}
			
			// Step 2 Drop tables in case they exist.	
						Statement stmt = conn.createStatement();
						String sql = "DROP TABLE IF EXISTS categories";
						stmt.executeUpdate(sql);
						
						sql = "DROP TABLE IF EXISTS history";
						stmt.executeUpdate(sql);
						
						sql = "DROP TABLE IF EXISTS users";
						stmt.executeUpdate(sql);
						
						sql = "DROP TABLE IF EXISTS jobs";
						stmt.executeUpdate(sql);
			
			// Step 3 Create new tables
						sql = "CREATE TABLE jobs ("
								+ "jobId VARCHAR(255) NOT NULL,"
								+ "jobType VARCHAR(255),"
								+ "githubLink VARCHAR(255),"
								+ "postTime VARCHAR(255),"
								+ "company VARCHAR(255),"
								+ "companyWebsite VARCHAR(255),"
								+ "location VARCHAR(255),"
								+ "jobTitle VARCHAR(255),"
								+ "jobDescription VARCHAR(255),"
								+ "applyLink VARCHAR(255),"
								+ "companyLogo VARCHAR(255),"
								+ "PRIMARY KEY (jobId)"
								+ ")";
						stmt.executeUpdate(sql);
						
						sql = "CREATE TABLE users ("
								+ "user_id VARCHAR(255) NOT NULL,"
								+ "password VARCHAR(255) NOT NULL,"
								+ "first_name VARCHAR(255),"
								+ "last_name VARCHAR(255),"
								+ "PRIMARY KEY (user_id)"
								+ ")";
						stmt.executeUpdate(sql);
						
						sql = "CREATE TABLE recommendations ("
								+ "jobId VARCHAR(255) NOT NULL,"
								+ "jobTitle VARCHAR(255),"
								+ "location VARCHAR(255),"
								+ "PRIMARY KEY (jobId),"
								+ "FOREIGN KEY (jobId) REFERENCES jobs(jobId)"
								+ ")";
						stmt.executeUpdate(sql);
						
						sql = "CREATE TABLE history ("
								+ "user_id VARCHAR(255) NOT NULL,"
								+ "jobId VARCHAR(255) NOT NULL,"
								+ "last_favor_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
								+ "PRIMARY KEY (user_id, jobId),"
								+ "FOREIGN KEY (user_id) REFERENCES users(user_id),"
								+ "FOREIGN KEY (jobId) REFERENCES jobs(jobId)"
								+ ")";
						stmt.executeUpdate(sql);
						
			// Step 4: insert fake user 1111/3229c1097c00d497a0fd282d586be050
						sql = "INSERT INTO users VALUES ('1111', '3229c1097c00d497a0fd282d586be050', 'John', 'Smith')";
						stmt.executeUpdate(sql);

			
			System.out.println("Import done successfully");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
