package util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JTextArea;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bean.TransferBean;
import dao.DAOSQL;

public class TransferThread extends Thread {
	private static Logger logger = LogManager.getLogger(TransferThread.class);
	ArrayList<String> srcColumns = null;
	ArrayList<String> destColumns = null;
	String pk = null;
	boolean running = true;
	JTextArea textArea = null;
	SimpleDateFormat sf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
	ArrayList<TransferBean> allTransfer=new ArrayList<TransferBean>();
	public TransferThread(JTextArea textArea) {
		super();
		this.textArea = textArea;
	}

	@Override
	public void run() {
		super.run();
		while (running) {
			try {
				textArea.append(getTime() + " 啟動服務 \r\n");
				prepare();
				db();
				Thread.sleep(Integer.parseInt(Constans.sequence_s) * 1000);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		logger.info("thread is end");
	}

	public void prepare() {
		if (Constans.columnList != null) {
			allTransfer.addAll(Constans.columnList);
		}
		if (Constans.timeUpList != null) {
			allTransfer.addAll(Constans.timeUpList);
		}
		if (Constans.timeDownList != null) {
			allTransfer.addAll(Constans.timeDownList);
		}
		if(allTransfer!=null) {
			srcColumns = new ArrayList<String>();
			destColumns = new ArrayList<String>();
			for (int i = 0; i < allTransfer.size(); i++) {
				TransferBean transferBean = allTransfer.get(i);
				if (transferBean.getSrcColumn() != null && !"".equals(transferBean.getSrcColumn())) {
					srcColumns.add(transferBean.getSrcColumn());
				}
				if (transferBean.getDestColumn() != null && !"".equals(transferBean.getDestColumn())) {
					destColumns.add(transferBean.getDestColumn());
				}
			}
		}
		if (Constans.srcColumns != null) {
			pk = Constans.srcColumns.get(0);
		}
		logger.info("pk="+pk);

	}

	public void db() {
		try {
			DAOSQL srcDAO = null;
			DAOSQL destDAO = null;
			StringBuilder srcSQL = new StringBuilder(" SELECT ");
			StringBuilder destSQL = new StringBuilder(" INSERT INTO " + Constans.destDBInfo.getTableName() + "(");
			ArrayList<String> updateList = null;
			if (Constans.srcDBInfo != null) {
				srcDAO = DBUtil.getDBConnection(Constans.srcDBInfo);
			}
			if (Constans.destDBInfo != null) {
				destDAO = DBUtil.getDBConnection(Constans.destDBInfo);
			}
			for (int i = 0; i < srcColumns.size(); i++) {
				srcSQL.append(srcColumns.get(i));
				if (i != srcColumns.size() - 1) {
					srcSQL.append(",");
				}
			}
			for (int i = 0; i < destColumns.size(); i++) {
				destSQL.append(destColumns.get(i));
				if (i != destColumns.size() - 1) {
					destSQL.append(",");
				}
			}
			destSQL.append(")VALUES(");
			for (int i = 0; i < destColumns.size(); i++) {
				destSQL.append("?");
				if (i != destColumns.size() - 1) {
					destSQL.append(",");
				}
			}
			destSQL.append(")");

			srcSQL.append(
					" from " + Constans.srcDBInfo.getTableName() + " where " + Constans.condition_column + " = ?");
			 logger.info("srcSQL=" + srcSQL.toString());
			 logger.info("destSQL=" + destSQL.toString());

			PreparedStatement srcPS = srcDAO.prepareStatement(srcSQL.toString());
			PreparedStatement destPS = destDAO.prepareStatement(destSQL.toString());
			srcPS.setString(1, Constans.condition);
			ResultSet srcRS = srcPS.executeQuery();
			String updateSrcSQL = "UPDATE " + Constans.srcDBInfo.getTableName() + " SET ";
			boolean updateSQLFinished = false;
			int srcCount = 0, destCount = 0;
			SimpleDateFormat srcSF = new SimpleDateFormat("yyyyMMddHHmmss");
			logger.info("allTransfer size="+allTransfer.size());
			while (srcRS.next()) {
				srcCount++;
				int index = 1;
				for (int i = 0; i < allTransfer.size(); i++) {
					TransferBean transferBean = allTransfer.get(i);
					// 時間欄位
					if (Constans.timeUp.equals(transferBean.getType())) {
						if (transferBean.getSrcColumn() != null
								&& !"".equals(transferBean.getSrcColumn())
								&& transferBean.getDestColumn() != null
								&& !"".equals(transferBean.getDestColumn())
								&& transferBean.getDestTimeYMDFormat() != null
								&& !Constans.defaultJComboBoxText.equals(transferBean.getDestTimeYMDFormat())
								&& transferBean.getDestTimeHMSFormat() != null
								&& !Constans.defaultJComboBoxText.equals(transferBean.getDestTimeHMSFormat())) {
							SimpleDateFormat destSF = new SimpleDateFormat(
									transferBean.getDestTimeYMDFormat() + " " + transferBean.getDestTimeHMSFormat());
							String timeS = srcRS.getString(transferBean.getSrcColumn());
							String destTime = null;
							if (timeS != null) {
								destTime = destSF.format(srcSF.parse(timeS));
							}
							destPS.setString(index++, destTime);
						}
					}
					// 時間欄位 切欄位
					else if (Constans.timeDown.equals(transferBean.getType())) {
						if (transferBean.getSrcColumn() != null
								&& !"".equals(transferBean.getSrcColumn())
								&& transferBean.getDestColumn() != null
								&& !"".equals(transferBean.getDestColumn())
								&& transferBean.getDestTimeYMDFormat() != null
								&& !Constans.defaultJComboBoxText.equals(transferBean.getDestTimeYMDFormat())) {
							SimpleDateFormat destSF = new SimpleDateFormat(transferBean.getDestTimeYMDFormat());
							String timeS = srcRS.getString(transferBean.getSrcColumn());
							String destTime = null;
							if (timeS != null) {
								destTime = destSF.format(srcSF.parse(timeS));
							}
							destPS.setString(index++, destTime);
						} else if (transferBean.getSrcColumn() != null
								&& !Constans.defaultJComboBoxText.equals(transferBean.getSrcColumn())
								&& transferBean.getDestColumn() != null
								&& !Constans.defaultJComboBoxText.equals(transferBean.getDestColumn())
								&& transferBean.getDestTimeHMSFormat() != null
								&& !Constans.defaultJComboBoxText.equals(transferBean.getDestTimeHMSFormat())) {
							SimpleDateFormat destSF = new SimpleDateFormat(transferBean.getDestTimeHMSFormat());
							String timeS = srcRS.getString(transferBean.getSrcColumn());
							String destTime = null;
							if (timeS != null) {
								destTime = destSF.format(srcSF.parse(timeS));
							}
							destPS.setString(index++, destTime);
						}
					}
					// 一般欄位
					else if (transferBean.getSrcColumn() != null && !"".equals(transferBean.getSrcColumn())
							&& transferBean.getDestColumn() != null && !"".equals(transferBean.getDestColumn())) {
						destPS.setString(index++, srcRS.getString(transferBean.getSrcColumn()));
					}
					// 寫固定值欄位
					else if (transferBean.getSrcColumn() == null && transferBean.getDestColumn() != null
							&& transferBean.getDestContent() != null && !"".equals(transferBean.getDestContent())) {
						destPS.setString(index++, transferBean.getDestContent());
					}else {
						logger.info(transferBean.getSrcColumn());
					}
					if (!updateSQLFinished) {
						if (transferBean.getSrcColumn() != null && !"".equals(transferBean.getSrcColumn())
								&& transferBean.getSrcContent() != null && !"".equals(transferBean.getSrcContent())) {
							updateSrcSQL = updateSrcSQL + transferBean.getSrcColumn() + "='"
									+ transferBean.getSrcContent() + "',";
						}
					}
				}
				if (!updateSQLFinished) {
					updateSQLFinished = true;
				}
				int resut = destPS.executeUpdate();
				if (resut > 0) {
					destCount++;
					if (updateList == null) {
						updateList = new ArrayList<String>();
					}
					updateList.add(srcRS.getString(pk));
				}
			}
			destPS.close();
			textArea.append(getTime() + " 讀取來源資料庫 " + srcCount + " 筆，寫入目標資料庫成功 " + destCount + "，失敗 "
					+ (srcCount - destCount) + " 筆 \r\n");
			updateSrcSQL = updateSrcSQL.substring(0, updateSrcSQL.length() - 1) + " where " + pk + "=?";
			if (updateList != null) {
				logger.info("updateSrcSQL=" + updateSrcSQL);
				for (int i = 0; i < updateList.size(); i++) {
					PreparedStatement srcUpdatePS = srcDAO.prepareStatement(updateSrcSQL);
					srcUpdatePS.setString(1, updateList.get(i));
					srcUpdatePS.executeUpdate();
					srcUpdatePS.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
		if (running == false) {
			textArea.append(getTime() + " 停止服務! \r\n");
		} else {
			textArea.append(getTime() + " 啟動服務! \r\n");
		}
	}

	public String getTime() {
		return sf.format(new Date());
	}

}
