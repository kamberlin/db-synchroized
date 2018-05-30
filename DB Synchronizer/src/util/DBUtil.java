package util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import util.LogManager;
import util.Logger;
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

	public static DAOSQL getDBConnection(String db_type, String ip, String account, String password, String database,
			int timeout) throws Exception {
		String url = null;
		DAOSQL daoSQL = null;
		if ("Oracle".equals(db_type)) {
			url = "jdbc:oracle:thin:@" + ip + ":1521:" + database;
			daoSQL = new DAOSQL(url, account, password, timeout);
		} else if ("SQLServer".equals(db_type)) {
			url = "jdbc:sqlserver://" + ip + ":1433;databaseName=" + database;
			daoSQL = new DAOSQL(url, account, password, timeout);
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

	public static DAOSQL getDBConnection(SrcDBInfo srcDBInfo, int timeout) throws Exception {
		DAOSQL daoSQL = getDBConnection(srcDBInfo.getType(), srcDBInfo.getIp(), srcDBInfo.getUsername(),
				srcDBInfo.getPassword(), srcDBInfo.getDbName(), timeout);
		return daoSQL;
	}

	public static DAOSQL getDBConnection(DestDBInfo destDBInfo, int timeout) throws Exception {
		DAOSQL daoSQL = getDBConnection(destDBInfo.getType(), destDBInfo.getIp(), destDBInfo.getUsername(),
				destDBInfo.getPassword(), destDBInfo.getDbName(), timeout);
		return daoSQL;
	}

	public static boolean checkConnection(SrcDBInfo srcDBInfo, int timeout) throws Exception {
		boolean isConnected = false;
		DAOSQL daoSQL = null;
		try {
			if (checkAllNotEmpty(srcDBInfo)) {
				daoSQL = getDBConnection(srcDBInfo, timeout);
				isConnected = true;
				daoSQL.close();
			} else {
				isConnected = false;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (daoSQL != null) {
				daoSQL.close();
			}
		}
		return isConnected;
	}

	public static boolean checkConnection(DestDBInfo destDBInfo,int timeout) throws Exception {
		boolean isConnected = false;
		DAOSQL daoSQL = null;
		try {
			if (checkAllNotEmpty(destDBInfo)) {
				daoSQL = getDBConnection(destDBInfo, timeout);
				isConnected = true;
				daoSQL.close();
			} else {
				isConnected = false;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (daoSQL != null) {
				daoSQL.close();
			}
		}
		return isConnected;
	}

	public static String checkConnection(String db_type, String ip, String username, String password, String dbName,
			String tableName) {
		String msg = null;
		try {
			if ((db_type != null && !"".equals(db_type)) && (ip != null && !"".equals(ip))
					&& (username != null && !"".equals(username)) && (password != null && !"".equals(password))
					&& (dbName != null && !"".equals(dbName))) {
				DAOSQL daoSQL = getDBConnection(db_type, ip, username, password, dbName, 3);
				if (checkTableExist(daoSQL, db_type, username, tableName)) {
					msg = "資料庫連線成功";
				} else {
					msg = "資料庫連線成功，但資料表" + tableName + "不存在";
				}
				daoSQL.close();
			} else {
				msg = "資料庫設定不正確，請檢查資料庫設定";
			}
		} catch (Exception e1) {
			msg = "資料庫連線異常，請檢查錯誤log";
			if(e1 instanceof SQLException) {
				if(((SQLException) e1).getErrorCode()==12505) {
					msg="資料庫連線異常 資料庫"+dbName+" 不存在";
				}else if(((SQLException) e1).getErrorCode()==1017) {
					msg="資料庫連線異常 使用者名稱/使用者密碼 錯誤";
				}
			}
			if("資料庫不存在".equals(e1.getMessage())) {
				msg="資料庫連線異常 資料庫"+dbName+" 不存在";
			}else if("使用者名稱/使用者密碼驗證失敗".equals(e1.getMessage())) {
				msg="資料庫連線異常 使用者名稱/使用者密碼 錯誤";
			}
			if (!("無法連線".indexOf(e1.getMessage()) > -1)) {
				logger.error("DBUtil checkConnection error", e1);
			} else {
				logger.error("DBUtil checkConnection error " + e1.getMessage());
			}
		}
		return msg;
	}

	public static boolean checkTableExist(DAOSQL daoSQL, String db_type, String username, String tableName) {
		boolean isExist = false;
		try {

			PreparedStatement ps = null;
			if ("Oracle".equals(db_type)) {
				ps = daoSQL.prepareStatement(Constans.checkOracleTableExist);
				ps.setString(1, username);
				ps.setString(2, tableName);
			} else {
				ps = daoSQL.prepareStatement(Constans.checkSQLServerTableExist);
				ps.setString(1, tableName);
			}
			ResultSet rs = ps.executeQuery();
			int count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			if (count > 0) {
				isExist = true;
			}

		} catch (Exception e1) {
			if (!("無法連線".indexOf(e1.getMessage()) > -1)) {
				logger.error("DBUtil checkTableExist error", e1);
			} else {
				logger.error("DBUtil checkTableExist error " + e1.getMessage());
			}
		}
		//daoSQL 由外部呼叫部份關閉
		return isExist;
	}

	public static void getDestColumnsFromDB() {
		CommonUtil.reloadDB();
		if (Constans.destDBInfo != null) {
			try {
				Constans.destColumns = new ArrayList<String>();
				DAOSQL daoSQL = getDBConnection(Constans.destDBInfo);
				PreparedStatement ps = null;
				if ("Oracle".equals(Constans.destDBInfo.getType())) {
					ps = daoSQL.prepareStatement(Constans.getOracleColumns);
					ps.setString(1, Constans.destDBInfo.getUsername());
					ps.setString(2, Constans.destDBInfo.getTableName());
				} else {
					ps = daoSQL.prepareStatement(Constans.getSQLServerColumns);
					ps.setString(1, Constans.destDBInfo.getTableName());
				}
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					Constans.destColumns.add(rs.getString("COLUMN_NAME"));
				}
				daoSQL.close();
			} catch (SQLException se) {
				logger.error("DBUtil getDestColumnsFromDB error", se);
				logger.error("errorcode=" + se.getErrorCode());
			} catch (Exception e) {
				logger.error("DBUtil getDestColumnsFromDB error", e);
			}
		}
	}

	public static void getSrcColumnsFromDB() {
		if (Constans.srcDBInfo != null) {
			try {
				Constans.srcColumns = new ArrayList<String>();
				DAOSQL daoSQL = getDBConnection(Constans.srcDBInfo);
				PreparedStatement ps = null;
				if ("Oracle".equals(Constans.srcDBInfo.getType())) {
					ps = daoSQL.prepareStatement(Constans.getOracleColumns);
					ps.setString(1, Constans.srcDBInfo.getUsername());
					ps.setString(2, Constans.srcDBInfo.getTableName());
				} else {
					ps = daoSQL.prepareStatement(Constans.getSQLServerColumns);
					ps.setString(1, Constans.srcDBInfo.getTableName());
				}
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					Constans.srcColumns.add(rs.getString("COLUMN_NAME"));
				}
				daoSQL.close();
			} catch (SQLException se) {
				logger.error("DBUtil getSrcColumnsFromDB error", se);
				logger.error("errorcode=" + se.getErrorCode());
			} catch (Exception e) {
				logger.error("DBUtil getSrcColumnsFromDB error", e);
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
