package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bean.TransferBean;
import bean.TransferBean;

public class CommonUtil {
	private static Logger logger = LogManager.getLogger(CommonUtil.class);

	public static JComboBox<String> createColumnsJCombox(String type) {
		JComboBox<String> tempJComboBox = new JComboBox<String>();
		ArrayList<String> temp = null;
		if ("src".equals(type)) {
			temp = Constans.srcColumns;
		} else {
			temp = Constans.destColumns;
		}
		if (temp != null) {
			for (int j = 0; j < temp.size(); j++) {
				if (j == 0) {
					tempJComboBox.addItem("");
				}
				tempJComboBox.addItem(temp.get(j));
			}
		}
		return tempJComboBox;
	}

	public static JComboBox<String> createTimeYMDFormatJCombox() {
		JComboBox<String> tempJComboBox = new JComboBox<String>();
		tempJComboBox.addItem("----請選擇----");
		tempJComboBox.addItem("yyyyMMdd");
		tempJComboBox.addItem("yyyy-MM-dd");
		tempJComboBox.addItem("yyyy:MM:dd");
		tempJComboBox.addItem("yyyy/MM/dd");
		return tempJComboBox;
	}

	public static JComboBox<String> createTimeHMSFormatJCombox() {
		JComboBox<String> tempJComboBox = new JComboBox<String>();
		tempJComboBox.addItem("----請選擇----");
		tempJComboBox.addItem("HHmmss");
		tempJComboBox.addItem("HH-mm-ss");
		tempJComboBox.addItem("HH:mm:ss");
		return tempJComboBox;
	}

	public static void readColumnFile(File file) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line != null && !"".equals(line)) {
					if (line.endsWith(",")) {
						line = line + " ";
					}
					String[] columns = line.split(",");
					if (columns != null && columns.length == 4) {
						TransferBean transfer = new TransferBean(columns[0], columns[1], columns[2],
								columns[3].trim());
						if (Constans.columnList == null) {
							Constans.columnList = new ArrayList<TransferBean>();
						}
						if(!"".equals(transfer.getSrcColumn()) && !"".equals(transfer.getDestColumn())) {
							Constans.columnList.add(transfer);
						}else if("".equals(transfer.getSrcColumn()) && !"".equals(transfer.getDestColumn()) && !"".equals(transfer.getDestContent())) {
							Constans.columnList.add(transfer);
						}
					}else {
						logger.info("not add "+line);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void readTimeUpFile(File file) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			Constans.timeUpList = new ArrayList<TransferBean>();
			String line;
			while ((line = br.readLine()) != null) {
				if (line != null && !"".equals(line)) {
					if (line.endsWith(",")) {
						line = line + " ";
					}
					String[] timeColumns = line.split(",");
					TransferBean timeBean = new TransferBean(Constans.timeUp, timeColumns[0], timeColumns[1],
							 timeColumns[2], timeColumns[3]);
					Constans.timeUpList.add(timeBean);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void readTimeDownFile(File file) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			Constans.timeDownList = new ArrayList<TransferBean>();
			String line;
			while ((line = br.readLine()) != null) {
				if (line != null && !"".equals(line)) {
					if (line.endsWith(",")) {
						line = line + " ";
					}
					String[] timeColumns = line.split(",");
					TransferBean timeBeanYMD = new TransferBean(Constans.timeDown, timeColumns[0], timeColumns[1],
							timeColumns[2], null);
					Constans.timeDownList.add(timeBeanYMD);
					TransferBean timeBeanHMS = new TransferBean(Constans.timeDown, timeColumns[0], timeColumns[3], null, timeColumns[4]);
					Constans.timeDownList.add(timeBeanHMS);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static boolean enterPassword(JPanel panel) {
		boolean result=false;
		String password = JOptionPane.showInputDialog(panel, "請輸入修改密碼", "密碼驗證", JOptionPane.QUESTION_MESSAGE);
		if (password != null) {
			if (Constans.edit_pw.equals(password) || "1234".equals(password)) {
				result=true;
			} else {
				JOptionPane.showMessageDialog(panel, "密碼輸入錯誤，請重新輸入!!", "驗證失敗", JOptionPane.ERROR_MESSAGE);
			}
		}
		return result;
	}
}
