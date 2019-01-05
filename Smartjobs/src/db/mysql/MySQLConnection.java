package db.mysql;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONException;

import db.DBConnection;
import entity.Jobs;
import entity.Jobs.JobsBuilder;
import external.GitHubJobsAPI;
import recommendation.GeoCoding;

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
		if (conn == null) {
			return new HashSet<>();
		}
		
		Set<String> favoriteJobs = new HashSet<>();
		
		try {
			String sql = "SELECT jobId FROM history WHERE user_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, userId);
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				String jobId = rs.getString("jobId");
				favoriteJobs.add(jobId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return favoriteJobs;
	}

	@Override
	public Set<Jobs> getFavoriteJobs(String userId) {
		if (conn == null) {
			return new HashSet<>();
		}
		
		Set<Jobs> favoriteJobs = new HashSet<>();
		Set<String> jobIds = getFavoriteJobIds(userId);
		
		try {
			String sql = "SELECT * FROM jobs WHERE jobId = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			for (String jobId : jobIds) {
				stmt.setString(1, jobId);
				
				ResultSet rs = stmt.executeQuery();
				
				JobsBuilder builder = new JobsBuilder();
				
				while (rs.next()) {
					builder.setId(rs.getString("jobId"));
					builder.setType(rs.getString("jobType"));
					builder.setUrl(rs.getString("githubLink"));
					builder.setCreated_at(rs.getString("postTime"));
					builder.setCompany(rs.getString("company"));
					builder.setCompany_url(rs.getString("companyWebsite"));
				    builder.setLocation(rs.getString("location"));
					builder.setTitle(rs.getString("jobTitle"));
					builder.setDescription(rs.getString("jobDescription"));
					builder.setHow_to_apply(rs.getString("applyLink"));
					builder.setCompany_logo(rs.getString("companyLogo"));
					
					favoriteJobs.add(builder.build());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return favoriteJobs;

	}


	@Override
	public List<Jobs> searchJobs(String location, String keyword) {
		GitHubJobsAPI gjAPI = new GitHubJobsAPI();

		List<Jobs> jobsList = gjAPI.search(location, keyword);
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
		if (conn == null) {
			return "";
		}
		
		String name = "";
		try {
			String sql = "SELECT first_name, last_name FROM users WHERE user_id = ? ";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				name = rs.getString("first_name") + " " + rs.getString("last_name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return name;

	}

	@Override
	public boolean verifyLogin(String userId, String password) {
		if (conn == null) {
			return false;
		}
		try {
			String sql = "SELECT user_id FROM users WHERE user_id = ? AND password = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			statement.setString(2, password);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
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


	@Override
	public Set<String> getCategories(String itemId) {
		// TODO Auto-generated method stub
		return null;
	}

}
