package util;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import util.LogManager;
import util.Logger;
import bean.TransferBean;
import dao.DAOSQL;

public class TransferThread extends Thread {
	private static Logger logger = LogManager.getLogger(TransferThread.class);
	ArrayList<String> srcColumns = null;
	ArrayList<String> destColumns = null;
	String pk = null;
	boolean running = true;
	JLabel statusTextLabel=null;
	JTextArea textArea = null;
	SimpleDateFormat sf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
	ArrayList<TransferBean> allTransfer = new ArrayList<TransferBean>();

	public TransferThread(JTextArea textArea, JLabel statusTextLabel) {
		super();
		this.textArea = textArea;
		this.statusTextLabel=statusTextLabel;
		addMessage("啟動服務");
	}

	@Override
	public void run() {
		super.run();
		while (running) {
			try {
				if(loadColumnSetting()) {
					allTransfer = new ArrayList<TransferBean>();
					prepare();
					db();
					Thread.sleep(Integer.parseInt(Constans.sequence_s) * 1000);
				}else {
					addMessage("無法找到設定檔案");
					setRunning(false);
					if(statusTextLabel!=null) {
						statusTextLabel.setText("服務停止！");
					}
				}
			} catch (NumberFormatException e) {
				logger.error("TransferThread run error", e);
			} catch (InterruptedException e) {
				logger.error("TransferThread run error", e);
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
		if (Constans.srcColumns != null) {
			pk = Constans.srcColumns.get(0);
		}
		logger.info("pk=" + pk);

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

			srcSQL.append(" from " + Constans.srcDBInfo.getTableName() + " where " + Constans.condition_column
					+ " = ?");
			logger.info("srcSQL=" + srcSQL.toString());
			logger.info("destSQL=" + destSQL.toString());

			PreparedStatement srcPS = srcDAO.prepareStatement(srcSQL.toString());
			PreparedStatement destPS = destDAO.prepareStatement(destSQL.toString());
			srcPS.setString(1, Constans.condition);
			logger.info("condition="+Constans.condition);
			ResultSet srcRS = srcPS.executeQuery();
			String updateSrcSQL = "UPDATE " + Constans.srcDBInfo.getTableName() + " SET ";
			boolean updateSQLFinished = false;
			int srcCount = 0, destCount = 0;
			SimpleDateFormat srcSF = new SimpleDateFormat("yyyyMMddHHmmss");
			while (srcRS.next()) {
				try {
				srcCount++;
				int index = 1;
				for (int i = 0; i < allTransfer.size(); i++) {
					TransferBean transferBean = allTransfer.get(i);
					// 時間欄位
					if (Constans.timeUp.equals(transferBean.getType())) {
						if (!"".equals(transferBean.getSrcColumn())
								&& !"".equals(transferBean.getDestColumn())
								&& !Constans.defaultJComboBoxText.equals(transferBean.getDestTimeYMDFormat())
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
						if (!"".equals(transferBean.getSrcColumn())
								&& !"".equals(transferBean.getDestColumn())
								&& transferBean.getDestTimeYMDFormat()!=null && !Constans.defaultJComboBoxText.equals(transferBean.getDestTimeYMDFormat())) {
							SimpleDateFormat destSF = new SimpleDateFormat(transferBean.getDestTimeYMDFormat());
							String timeS = srcRS.getString(transferBean.getSrcColumn());
							String destTime = null;
							if (timeS != null) {
								destTime = destSF.format(srcSF.parse(timeS));
							}
							destPS.setString(index++, destTime);
						} else if (!"".equals(transferBean.getSrcColumn())
								&& !"".equals(transferBean.getDestColumn())
								&& transferBean.getDestTimeHMSFormat()!=null && !Constans.defaultJComboBoxText.equals(transferBean.getDestTimeHMSFormat())) {
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
					else if (!"".equals(transferBean.getSrcColumn())
							 && !"".equals(transferBean.getDestColumn())) {
						destPS.setString(index++, srcRS.getString(transferBean.getSrcColumn()));
					}
					// 寫固定值欄位
					else if ("".equals(transferBean.getSrcColumn()) && !"".equals(transferBean.getDestColumn())
							&& !"".equals(transferBean.getDestContent())) {
						destPS.setString(index++, transferBean.getDestContent());
					} else if(!"".equals(transferBean.getSrcColumn()) && !"".equals(transferBean.getSrcContent()) && "".equals(transferBean.getDestColumn())){
						//來源需更新欄位
					}else {
					}
					if (!updateSQLFinished) {
						if (!"".equals(transferBean.getSrcColumn())
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
				}catch(SQLException e) {
					logger.error("TransferThread Insert Error ",e);
				}catch(Exception e) {
					logger.error("TransferThread Insert Error ",e);
				}
			}
			destPS.close();
			addMessage("讀取來源資料庫 " + srcCount + " 筆，寫入目標資料庫成功 " + destCount + "，失敗 "
					+ (srcCount - destCount) + " 筆");
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
			logger.error("TransferThread db error", e);
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
		if (running == false) {
			addMessage("停止服務!");
			allTransfer.clear();
		} else {
			addMessage("啟動服務!");
		}
	}

	public void addMessage(String message) {
		if (textArea != null) {
			if (textArea.getLineCount() > 100) {
				textArea.setText("");
			}
			textArea.append(sf.format(new Date())+" "+message +"\r\n");
		}
	}
	public boolean loadColumnSetting() {
		boolean isLoad=false;
		try {
			File column_file = new File(Constans.columnSettingPath);
			File timeUp_file = new File(Constans.timeUpPath);
			File timeDown_file = new File(Constans.timeDownPath);
			//解密檔案
			if(column_file.exists()) {
				CommonUtil.decrypt(column_file.getPath(), Constans.edit_pw);
				File columnDecode = new File(column_file.getParent() + File.separator+CommonUtil.getFileNameWithOutExtension(column_file) + "_decode.txt");
				if (columnDecode.exists()) {
					CommonUtil.readColumnFile(columnDecode);
				}
			}
			if(timeUp_file.exists()) {
				CommonUtil.decrypt(timeUp_file.getPath(), Constans.edit_pw);
				File timeUpDecode = new File(timeUp_file.getParent() + File.separator+CommonUtil.getFileNameWithOutExtension(timeUp_file) + "_decode.txt");
				if (timeUpDecode.exists()) {
					CommonUtil.readTimeUpFile(timeUpDecode);
				}
			}
			if(timeDown_file.exists()) {
				CommonUtil.decrypt(timeDown_file.getPath(), Constans.edit_pw);
				File timeDownDecode = new File(timeDown_file.getParent() + File.separator+CommonUtil.getFileNameWithOutExtension(timeDown_file) + "_decode.txt");
				if (timeDownDecode.exists()) {
					CommonUtil.readTimeDownFile(timeDownDecode);
				}
			}
			if (!column_file.exists()) {
				JOptionPane.showMessageDialog(null, "無法找到設定檔，請檢查是否有儲存設定", "執行異常", JOptionPane.ERROR_MESSAGE);
			} else {
				isLoad=true;
				addMessage("已讀入所有欄位及時間設定");
				logger.info("已讀入所有欄位及時間設定");
			}

		} catch (Exception e) {
			logger.error("TransferThread loadColumnSetting error",e);
		}
		return isLoad;
	}
}
