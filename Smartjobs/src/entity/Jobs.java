package entity;

import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

public class Jobs {
	private String id;
	private String type;
	private String url;
	private String created_at;
	private String company;
	private String company_url;
	private String location;
	private String title;
	private String description;
	private String how_to_apply;
	private String company_logo;
	
	/**
	 * This is a builder pattern in Java.
	 */
	private Jobs(JobsBuilder builder) {
		this.id = builder.id;
		this.type = builder.type;
		this.url = builder.url;
		this.created_at = builder.created_at;
		this.company = builder.company;
		this.company_url = builder.company_url;
		this.location = builder.location;
		this.title = builder.title;
		this.description = builder.description;
		this.how_to_apply = builder.how_to_apply;
		this.company_logo = builder.company_logo;
	}

	
	
	public String getId() {
		return id;
	}
	public String isType() {
		return type;
	}
	public String getUrl() {
		return url;
	}
	public String getCreated_at() {
		return created_at;
	}
	public String getCompany() {
		return company;
	}
	public String getCompany_url() {
		return company_url;
	}
	public String getLocation() {
		return location;
	}
	public String getTitle() {
		return title;
	}
	public String getDescription() {
		return description;
	}
	public String getHow_to_apply() {
		return how_to_apply;
	}
	public String getCompany_logo() {
		return company_logo;
	}
	
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("jobId", id);
			obj.put("jobType", type);
			obj.put("githubLink", url);
			obj.put("postTime", created_at);
			obj.put("company", company);
			obj.put("companyWebsite", company_url);
			obj.put("location", location);
			obj.put("jobTitle", title);
			obj.put("jobDescription", description);
			obj.put("applyLink", how_to_apply);
			obj.put("companyLogo", company_logo);
						
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}

	public static class JobsBuilder {

		private String type;
		private String url;
		private String created_at;
		private String company;
		private String company_url;
		private String location;
		private String title;
		private String description;
		private String how_to_apply;
		private String company_logo;
		private String id;
		
		public void setId(String id) {
			this.id = id;
		}
		public void setType(String type) {
			this.type = type;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public void setCreated_at(String created_at) {
			this.created_at = created_at;
		}
		public void setCompany(String company) {
			this.company = company;
		}
		public void setCompany_url(String company_url) {
			this.company_url = company_url;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public void setHow_to_apply(String how_to_apply) {
			this.how_to_apply = how_to_apply;
		}
		public void setCompany_logo(String company_logo) {
			this.company_logo = company_logo;
		}
		
		public Jobs build() {
			return new Jobs(this);
		}
		
	}

}