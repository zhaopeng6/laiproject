package db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.List;
import java.util.Set;

import db.DBConnection;
import entity.Jobs;
import external.GitHubJobsAPI;

public class MySQLConnection implements DBConnection {
	
	private Connection conn;
    
    public MySQLConnection() {
   	 try {
   		 Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
   		 conn = DriverManager.getConnection(MySQLDBUtil.URL);
   		 
   	 } catch (Exception e) {
   		 e.printStackTrace();
   	 }
    }


	@Override
	public void close() {
	   	 if (conn != null) {
	   		 try {
	   			 conn.close();
	   		 } catch (Exception e) {
	   			 e.printStackTrace();
	   		 }
	   	 }

	}

	@Override
	public void setFavoriteJobs(String userId, List<String> jobIds) {
		  if (conn == null) {
		   		 System.err.println("DB connection failed");
		   		 return;
		   	       }
		   	 
		   	      try {
		   		 String sql = "INSERT IGNORE INTO history(user_id, jobId) VALUES (?, ?)";
		   		 PreparedStatement ps = conn.prepareStatement(sql);
		   		 ps.setString(1, userId);
		   		 for (String jobId : jobIds) {
		   			 ps.setString(2, jobId);
		   			 ps.execute();
		   		 }
		   	       } catch (Exception e) {
		   		 e.printStackTrace();
		   	       }
	}

	@Override
	public void unsetFavoriteJobs(String userId, List<String> jobIds) {
		 if (conn == null) {
	   		 System.err.println("DB connection failed");
	   		 return;
	   	       }
	   	 
	   	      try {
	   		 String sql = "DELETE FROM history WHERE user_id = ? AND jobId = ?";
	   		 PreparedStatement ps = conn.prepareStatement(sql);
	   		 ps.setString(1, userId);
	   		 for (String jobId : jobIds) {
	   			 ps.setString(2, jobId);
	   			 ps.execute();
	   		 }
	   		 
	   	       } catch (Exception e) {
	   		 e.printStackTrace();
	   	       }

	}

	@Override
	public Set<String> getFavoriteJobIds(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Jobs> getFavoriteJobs(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getCategories(String itemId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Jobs> searchJobs(double lat, double lon, String term) {
		GitHubJobsAPI gjAPI = new GitHubJobsAPI();
		List<Jobs> jobsList = gjAPI.search(lat, lon, term);
		
		   for(Jobs job : jobsList) {
		   		 saveJob(job);
		   	        }
		   	 
		   	        return jobsList;
	}

	@Override
	public void saveJob(Jobs job) {
		 if (conn == null) {
	   		   System.err.println("DB connection failed");
	   		   return;
	   	         }
		 // sql injection
	   	 // select * from users where username = '' AND password = '';
	   	 
	   	 // username: fakeuser ' OR 1 = 1; DROP  --
	   	 // select * from users where username = 'fakeuser ' OR 1 = 1 --' AND password = '';
		 try {
	   		 String sql = "INSERT IGNORE INTO Jobs VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	   		 PreparedStatement ps = conn.prepareStatement(sql);
	   		 ps.setString(1, job.getId());
	   		 ps.setString(2, job.getType());
	   		 ps.setString(3, job.getUrl());
	   		 ps.setString(4, job.getCreated_at());
	   		 ps.setString(5, job.getCompany());
	   		 ps.setString(6, job.getCompany_url());
	   		 ps.setString(7, job.getLocation());
	   		 ps.setString(8, job.getTitle());
	   		 ps.setString(9, job.getDescription());
	   		 ps.setString(10, job.getHow_to_apply());
	   		 ps.setString(11, job.getCompany_logo());
	   		 ps.execute();
	   		 
	   	/*	 sql = "INSERT IGNORE INTO categories VALUES(?, ?)";
	   		 ps = conn.prepareStatement(sql);
	   		 ps.setString(1, item.getItemId());
	   		 for(String category : item.getCategories()) {
	   			 ps.setString(2, category);
	   			 ps.execute();
	   		 } */
	   		 
	   	 } catch (Exception e) {
	   		 e.printStackTrace();
	   	 }
	}

	@Override
	public String getFullname(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean verifyLogin(String userId, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getJobTitle(String jobId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getJobLocation(String jobId) {
		// TODO Auto-generated method stub
		return null;
	}

}
