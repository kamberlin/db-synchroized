package dao;

import java.sql.*;

import util.LogManager;
import util.Logger;

public class DAOSQL {
	private static Logger logger = LogManager.getLogger(DAOSQL.class);

	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs1 = null;

	public DAOSQL(String url, String user_id, String user_pw) throws Exception {
		try {
			//logger.info("dbConnection url=" + url + ", user=" + user_id + ", user_pw=" + user_pw + ".");
			conn = DriverManager.getConnection(url, user_id, user_pw);
			stmt = conn.createStatement();
		} catch (SQLException se) {
			if (se.getErrorCode() == 17002) {
				throw new Exception("DAOSQL.DAOSQL(): db 無法連線");
			} else if (se.getErrorCode() == 12505) {
				throw new Exception("Oracle 資料庫不存在");
			} else if (se.getErrorCode() == 1017) {
				throw new Exception("使用者名稱/使用者密碼驗證失敗");
			} else if (se.getErrorCode() == 4060) {
				throw new Exception("SQL Server 資料庫不存在");
			}
			else if (se.getErrorCode() == 18456) {
				throw new Exception("SQL Server 登入失敗");
			}
			else if(se.getErrorCode()==0) {
				throw new Exception("資料庫連線被中斷");
			}
			else {
				logger.error("se errorCode="+se.getErrorCode());
				throw new Exception("DAOSQL.DAOSQL():" + se);
			}
		} catch (Exception e) {
			throw new Exception("DAOSQL.DAOSQL():" + e);
		}
	}

	public DAOSQL(String url, String user_id, String user_pw, int timeout) throws Exception {
		try {
			//logger.info("dbConnection url=" + url + ", user=" + user_id + ", user_pw=" + user_pw + ".");
			DriverManager.setLoginTimeout(timeout);
			conn = DriverManager.getConnection(url, user_id, user_pw);
			stmt = conn.createStatement();
		} catch (SQLException se) {
			if (se.getErrorCode() == 17002) {
				throw new Exception("DAOSQL.DAOSQL(): db 無法連線");
			} else if (se.getErrorCode() == 12505) {
				throw new Exception("Oracle 資料庫不存在");
			} else if (se.getErrorCode() == 1017) {
				throw new Exception("使用者名稱/使用者密碼驗證失敗");
			} else if (se.getErrorCode() == 4060) {
				throw new Exception("SQL Server 資料庫不存在");
			}
			else if (se.getErrorCode() == 18456) {
				throw new Exception("SQL Server 登入失敗");
			}else if(se.getErrorCode()==0) {
				throw new Exception("資料庫連線被中斷");
			}
			else {
				logger.error("se errorCode="+se.getErrorCode());
				throw new Exception("DAOSQL.DAOSQL():" + se);
			}
		} catch (Exception e) {
			throw new Exception("DAOSQL.DAOSQL():" + e);
		}
	}

	public void close() throws Exception {
		try {
			if (rs1 != null) {
				rs1.close();
				rs1 = null;
			}

			if (stmt != null) {
				stmt.close();
				stmt = null;
			}

			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException sqlError) {
			logger.error("DAOSQL error", sqlError);
		}
	}

	protected void finalize() throws Throwable {
		logger.info("doing close");
		super.finalize();
		try {
			if (rs1 != null) {
				rs1.close();
				rs1 = null;
			}

			if (stmt != null) {
				stmt.close();
				stmt = null;
			}

			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException sqlError) {
			logger.error("DAOSQL error", sqlError);
		}
	}

	public ResultSet executeQuery(String sql) throws Exception {
		try {
			rs1 = stmt.executeQuery(sql);
			return rs1;
		} catch (SQLException se) {
			if (se.getErrorCode() == 17009) {
				if (conn != null && conn.isClosed())
					close();
			}
			throw new Exception("DAOSQL.executeQuery(" + sql + "):" + se);
		} catch (Exception e) {
			throw new Exception("DAOSQL.executeQuery(" + sql + "):" + e);
		}
	}

	public int executeUpdate(String sql) throws Exception {
		try {
			return stmt.executeUpdate(sql);
		} catch (SQLException se) {
			throw new Exception("DAOSQL.executeQuery(" + sql + "):" + se);
		} catch (Exception e) {
			throw new Exception("DAOSQL.executeQuery(" + sql + "):" + e);
		}
	}

	public void execute(String sql) throws Exception {
		try {
			stmt.execute(sql);
		} catch (SQLException se) {
			throw new Exception("DAOSQL.execute(" + sql + "):" + se);
		} catch (Exception e) {
			throw new Exception("DAOSQL.execute(" + sql + "):" + e);
		}
	}

	public PreparedStatement prepareStatement(String sql) throws Exception {
		try {
			return conn.prepareStatement(sql);
		} catch (SQLException se) {
			throw new Exception("DAOSQL.prepareStatement(" + sql + "):" + se);
		} catch (Exception e) {
			throw new Exception("DAOSQL.prepareStatement(" + sql + "):" + e);
		}
	}

	public void commit() throws Exception {
		try {
			if (conn != null)
				conn.commit();
		} catch (SQLException se) {
			throw new Exception("DAOSQL.commit():" + se);
		} catch (Exception e) {
			throw new Exception("DAOSQL.commit():" + e);
		}
	}

	public void rollback() throws Exception {
		try {
			conn.rollback();
			logger.info("conn.rollback()");
		} catch (SQLException se) {
			throw new Exception("DAOSQL.rollback():" + se);
		} catch (Exception e) {
			throw new Exception("DAOSQL.rollback():" + e);
		}
	}

	public Connection getConn() {
		return conn;
	}

	public boolean isClosed() {
		boolean isClosed = false;
		try {
			if (conn == null || conn.isClosed()) {
				isClosed = true;
			} else if (stmt == null) {
				isClosed = true;
			}
		} catch (SQLException e) {
			logger.error("DAOSQL error", e);
		}
		return isClosed;
	}

}
