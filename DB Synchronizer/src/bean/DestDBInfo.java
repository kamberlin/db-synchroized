package bean;

public class DestDBInfo {
	String type;
	String ip;
	String username;
	String password;
	String dbName;
	String tableName;

	public DestDBInfo() {
		super();
	}

	public DestDBInfo(String type, String ip, String username, String password, String dbName, String tableName) {
		super();
		this.type = type;
		this.ip = ip;
		this.username = username;
		this.password = password;
		this.dbName = dbName;
		this.tableName = tableName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

}
