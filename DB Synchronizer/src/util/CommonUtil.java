package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JComboBox;

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
					tempJComboBox.addItem("----請選擇----");
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
						TransferBean transfer = new TransferBean("column", columns[0], columns[1], columns[2],
								columns[3]);
						if (Constans.columnList == null) {
							Constans.columnList = new ArrayList<TransferBean>();
						}
						Constans.columnList.add(transfer);
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
			String line;
			while ((line = br.readLine()) != null) {
				if (line != null && !"".equals(line)) {
					if (line.endsWith(",")) {
						line = line + " ";
					}
					Constans.timeUpList = new ArrayList<TransferBean>();
					String[] timeColumns = line.split(",");
					TransferBean timeBean = new TransferBean(Constans.timeUp, timeColumns[0], null, timeColumns[1],
							null, timeColumns[2], timeColumns[3]);
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
			String line;
			while ((line = br.readLine()) != null) {
				if (line != null && !"".equals(line)) {
					if (line.endsWith(",")) {
						line = line + " ";
					}
					Constans.timeDownList = new ArrayList<TransferBean>();
					String[] timeColumns = line.split(",");
					TransferBean timeBeanYMD = new TransferBean(Constans.timeDown, timeColumns[0], timeColumns[1], null,
							null, timeColumns[2], null);
					Constans.timeDownList.add(timeBeanYMD);
					TransferBean timeBeanHMS = new TransferBean(Constans.timeDown, timeColumns[0], timeColumns[3], null,
							null, null, timeColumns[4]);
					Constans.timeDownList.add(timeBeanHMS);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
