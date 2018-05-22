package util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bean.DestDBInfo;
import bean.SrcDBInfo;
import dao.DAOSQL;

public class DBUtil {
	private static Logger logger = LogManager.getLogger(DBUtil.class);

	public static DAOSQL getDBConnection(String db_type, String ip, String account, String password, String database)
			throws Exception {
		String url = null;
		DAOSQL daoSQL = null;
		if ("Oracle".equals(db_type)) {
			url = "jdbc:oracle:thin:@" + ip + ":1521:" + database;
			daoSQL = new DAOSQL(url, account, password);
		} else if ("SQLServer".equals(db_type)) {
			url = "jdbc:sqlserver://" + ip + ":1433;databaseName=" + database;
			daoSQL = new DAOSQL(url, account, password, 2);
		}
		return daoSQL;
	}

	public static DAOSQL getDBConnection(SrcDBInfo srcDBInfo) throws Exception {
		DAOSQL daoSQL = getDBConnection(srcDBInfo.getType(), srcDBInfo.getIp(), srcDBInfo.getUsername(),
				srcDBInfo.getPassword(), srcDBInfo.getDbName());
		return daoSQL;
	}

	public static DAOSQL getDBConnection(DestDBInfo destDBInfo) throws Exception {
		DAOSQL daoSQL = getDBConnection(destDBInfo.getType(), destDBInfo.getIp(), destDBInfo.getUsername(),
				destDBInfo.getPassword(), destDBInfo.getDbName());
		return daoSQL;
	}

	public static boolean checkConnection(SrcDBInfo srcDBInfo) {
		boolean isConnected = false;
		try {
			if (checkAllNotEmpty(srcDBInfo)) {
				DAOSQL daoSQL = getDBConnection(srcDBInfo);
				isConnected = true;
				daoSQL.close();
			} else {
				isConnected = false;
			}
		} catch (Exception e1) {
			if (!(e1.getMessage() != null && (e1.getMessage().indexOf("無法連線") > -1))) {
				logger.error("DBUtil error",e1);
			} else {
				logger.error(e1.getMessage());
			}
		}
		return isConnected;
	}

	public static boolean checkConnection(DestDBInfo destDBInfo) {
		boolean isConnected = false;
		try {
			if (checkAllNotEmpty(destDBInfo)) {
				DAOSQL daoSQL = getDBConnection(destDBInfo);
				isConnected = true;
				daoSQL.close();
			} else {
				isConnected = false;
			}
		} catch (Exception e1) {
			if (!(e1.getMessage() != null && (e1.getMessage().indexOf("無法連線") > -1))) {
				logger.error("DBUtil error",e1);
			} else {
				logger.error(e1.getMessage());
			}
		}
		return isConnected;
	}

	public static boolean checkConnection(String db_type, String ip, String username, String password, String dbName) {
		boolean isConnected = false;
		try {
			if ((db_type != null && !"".equals(db_type)) && (ip != null && !"".equals(ip))
					&& (username != null && !"".equals(username)) && (password != null && !"".equals(password))
					&& (dbName != null && !"".equals(dbName))) {
				DAOSQL daoSQL = getDBConnection(db_type, ip, username, password, dbName);
				isConnected = true;
				daoSQL.close();
			} else {
				isConnected = false;
			}
		} catch (Exception e1) {
			if (!("無法連線".indexOf(e1.getMessage()) > -1)) {
				logger.error("DBUtil error",e1);
			}
		}
		return isConnected;
	}

	public static void getDestColumnsFromDB() {
		if (Constans.destDBInfo != null) {
			try {
				DAOSQL daoSQL = getDBConnection(Constans.destDBInfo);
				PreparedStatement ps = null;
				if ("Oracle".equals(Constans.destDBInfo.getType())) {
					ps = daoSQL.prepareStatement(Constans.getOracleColumns);
					ps.setString(1, Constans.destDBInfo.getUsername());
					ps.setString(2, Constans.destDBInfo.getTableName());
				} else {
					ps = daoSQL.prepareStatement(Constans.getSQLServerColumns);
					logger.info("tableName="+Constans.destDBInfo.getTableName());
					ps.setString(1, Constans.destDBInfo.getTableName());
				}
				ResultSet rs = ps.executeQuery();
				Constans.destColumns = new ArrayList<String>();
				while (rs.next()) {
					Constans.destColumns.add(rs.getString("COLUMN_NAME"));
				}
				daoSQL.close();
			}
			catch(SQLException se) {
				logger.error("DBUtil error",se);
				logger.info("errorcode="+se.getErrorCode());
			}
			catch (Exception e) {
				logger.error("DBUtil error",e);
			}
		}
	}

	public static boolean checkAllNotEmpty(SrcDBInfo srcDBInfo) {
		boolean result = false;
		if (srcDBInfo != null) {
			if ((srcDBInfo.getType() != null && !"".equals(srcDBInfo.getType()))
					&& (srcDBInfo.getIp() != null && !"".equals(srcDBInfo.getIp()))
					&& (srcDBInfo.getUsername() != null && !"".equals(srcDBInfo.getUsername()))
					&& (srcDBInfo.getPassword() != null && !"".equals(srcDBInfo.getPassword()))
					&& (srcDBInfo.getDbName() != null && !"".equals(srcDBInfo.getDbName()))) {
				result = true;
			}
		}
		return result;
	}

	public static boolean checkAllNotEmpty(DestDBInfo destDBInfo) {
		boolean result = false;
		if (destDBInfo != null) {
			if ((destDBInfo.getType() != null && !"".equals(destDBInfo.getType()))
					&& (destDBInfo.getIp() != null && !"".equals(destDBInfo.getIp()))
					&& (destDBInfo.getUsername() != null && !"".equals(destDBInfo.getUsername()))
					&& (destDBInfo.getPassword() != null && !"".equals(destDBInfo.getPassword()))
					&& (destDBInfo.getDbName() != null && !"".equals(destDBInfo.getDbName()))) {
				result = true;
			}
		}
		return result;
	}
}
