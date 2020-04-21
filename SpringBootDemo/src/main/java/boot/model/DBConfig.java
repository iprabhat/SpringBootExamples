package main.java.boot.model;

public class DBConfig {
	
	private String DriverName;
	private String Url;
	private String UserName;
	private String Password;
	private String DatabaseName;
	public String getDriverName() {
		return DriverName;
	}
	public void setDriverName(String driverName) {
		DriverName = driverName;
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getDatabaseName() {
		return DatabaseName;
	}
	public void setDatabaseName(String databaseName) {
		DatabaseName = databaseName;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Driver Name: " + getDriverName() + " <br/> ");
		sb.append("URL: " + getUrl() + " <br/> ");
		sb.append("User Name: " + getUserName() + " <br/> ");
		sb.append("Password: " + getPassword() + " <br/> ");
		sb.append("Database Name: " + getDatabaseName() + " <br/> ");
		return sb.toString();
	}

}
