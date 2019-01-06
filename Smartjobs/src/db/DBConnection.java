package db;

import java.util.List;
import java.util.Set;

import entity.Jobs;

public interface DBConnection {
	/**
	 * Close the connection.
	 */
	public void close();

	/**
	 * Insert the favorite items for a user.
	 * 
	 * @param userId
	 * @param jobIds
	 */
	public void setFavoriteJobs(String userId, List<String> jobIds);

	/**
	 * Delete the favorite jobs for a user.
	 * 
	 * @param userId
	 * @param jobIds
	 */
	public void unsetFavoriteJobs(String userId, List<String> jobIds);

	/**
	 * Get the favorite job id for a user.
	 * 
	 * @param userId
	 * @return jobIds
	 */
	public Set<String> getFavoriteJobIds(String userId);

	/**
	 * Get the favorite jobs for a user.
	 * 
	 * @param userId
	 * @return jobs
	 */
	public Set<Jobs> getFavoriteJobs(String userId);

	/**
	 * Gets categories based on job id
	 * 
	 * @param jobId
	 * @return set of categories
	 */

	public List<Jobs> searchJobs(String location, String keyword);

	/**
	 * Save job into db.
	 * 
	 * @param job
	 */
	public void saveJob(Jobs job);

	/**
	 * Get full name of a user. (This is not needed for main course, just for demo
	 * and extension).
	 * 
	 * @param userId
	 * @return full name of the user
	 */
	public String getFullname(String userId);

	/**
	 * Return whether the credential is correct. (This is not needed for main
	 * course, just for demo and extension)
	 * 
	 * @param userId
	 * @param password
	 * @return boolean
	 */
	public boolean verifyLogin(String userId, String password);
	
	public Set<String> getJobTitle(String jobId);
	
	public Set<String> getJobLocation(String jobId);
}
