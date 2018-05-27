package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import util.Logger;
import bean.DestDBInfo;
import bean.SrcDBInfo;
import bean.TransferBean;

public class CommonUtil {
	private static Logger logger = null;
	ClassLoader classLoader = getClass().getClassLoader();

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
			Constans.columnList = new ArrayList<TransferBean>();
			String line;
			while ((line = br.readLine()) != null) {
				if (line != null && !"".equals(line)) {
					if (line.endsWith(",")) {
						line = line + " ";
					}
					String[] columns = line.split(",");
					if (columns != null && columns.length == 4) {
						TransferBean transfer = new TransferBean(columns[0], columns[1], columns[2], columns[3].trim());
						if (!"".equals(transfer.getSrcColumn()) && !"".equals(transfer.getDestColumn())) {
							Constans.columnList.add(transfer);
						} else if ("".equals(transfer.getSrcColumn()) && !"".equals(transfer.getDestColumn())
								&& !"".equals(transfer.getDestContent())) {
							Constans.columnList.add(transfer);
						} else if (!"".equals(transfer.getSrcColumn()) && !"".equals(transfer.getSrcContent())
								&& "".equals(transfer.getDestColumn())) {
							Constans.columnList.add(transfer);
						} else {
							// logger.info(line);
						}
					} else {
						logger.info("not add " + line);
					}
				}
			}
		} catch (FileNotFoundException e) {
			logger.error("CommonUtil readColumnFile error", e);
		} catch (IOException e) {
			logger.error("CommonUtil readColumnFile error", e);
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
			logger.error("CommonUtil readTimeUpFile error", e);
		} catch (IOException e) {
			logger.error("CommonUtil readTimeUpFile error", e);
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
					TransferBean timeBeanHMS = new TransferBean(Constans.timeDown, timeColumns[0], timeColumns[3], null,
							timeColumns[4]);
					Constans.timeDownList.add(timeBeanHMS);
				}
			}
		} catch (FileNotFoundException e) {
			logger.error("CommonUtil readTimeDownFile error", e);
		} catch (IOException e) {
			logger.error("CommonUtil readTimeDownFile error", e);
		}
	}

	public static boolean enterPassword(JPanel panel) {
		boolean result = false;
		String password = JOptionPane.showInputDialog(panel, "請輸入修改密碼", "密碼驗證", JOptionPane.QUESTION_MESSAGE);
		if (password != null) {
			if (Constans.edit_pw.equals(password)) {
				result = true;
			} else {
				JOptionPane.showMessageDialog(panel, "密碼輸入錯誤，請重新輸入!!", "驗證失敗", JOptionPane.ERROR_MESSAGE);
			}
		}
		return result;
	}

	// 解密
	public static void decrypt(String file, String password) throws Exception {
		SecureRandom random = new SecureRandom();
		DESKeySpec desKey = new DESKeySpec(password.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey securekey = keyFactory.generateSecret(desKey);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, securekey, random);
		File srcFile = new File(file);
		File destFile = new File(
				srcFile.getParent() + File.separator + CommonUtil.getFileNameWithOutExtension(srcFile) + "_decode.txt");
		if (!destFile.exists()) {
			destFile.createNewFile();
		}
		InputStream is = new FileInputStream(srcFile);
		OutputStream out = new FileOutputStream(destFile);
		CipherOutputStream cos = new CipherOutputStream(out, cipher);
		byte[] buffer = new byte[1024];
		int r;
		while ((r = is.read(buffer)) >= 0) {
			cos.write(buffer, 0, r);
		}
		cos.close();
		out.close();
		is.close();
	}

	// 加密
	public static void encrypt(String file, String password) throws Exception {
		SecureRandom random = new SecureRandom();
		DESKeySpec desKey = new DESKeySpec(password.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey securekey = keyFactory.generateSecret(desKey);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, securekey, random);

		File srcFile = new File(file);
		File destFile = new File(
				srcFile.getParent() + File.separator + CommonUtil.getFileNameWithOutExtension(srcFile) + "_encode.bin");
		InputStream is = new FileInputStream(srcFile);
		OutputStream out = new FileOutputStream(destFile);
		CipherInputStream cis = new CipherInputStream(is, cipher);
		byte[] buffer = new byte[1024];
		int r;
		while ((r = cis.read(buffer)) > 0) {
			out.write(buffer, 0, r);
		}
		cis.close();
		is.close();
		out.close();
		srcFile.delete();
		if (destFile.exists()) {
			destFile.renameTo(srcFile);
		}
	}

	public static String getFileNameWithOutExtension(File file) {
		String name = file.getName();
		int pos = name.lastIndexOf(".");
		if (pos > 0) {
			name = name.substring(0, pos);
		}
		return name;
	}

	public static void init() {
		try {
			Constans.dbproperty = Constans.rootPath + File.separator + File.separator + "data" + File.separator
					+ "db.properties";
			Constans.logproperty = Constans.rootPath + File.separator + "log";
			Constans.columnproperty = Constans.rootPath + File.separator + File.separator + "etc" + File.separator
					+ "columns.txt";
			Constans.timeUpPath = Constans.rootPath + File.separator + "data" + File.separator + "timeUp.bin";
			Constans.timeDownPath = Constans.rootPath + File.separator + "data" + File.separator + "timeDown.bin";
			Constans.columnSettingPath = Constans.rootPath + File.separator + "data" + File.separator
					+ "column_setting.bin";
			reload(Constans.dbproperty);
			if (Constans.columnproperty != null) {
				File columnFile = new File(Constans.columnproperty);
				if (columnFile.exists()) {
					Constans.srcColumns = new ArrayList<String>();
					getColumns(columnFile);
				}
			}
			logger = LogManager.getLogger(CommonUtil.class);
		} catch (Exception e) {
			logger.error("無法正確載入參數");
			JOptionPane.showMessageDialog(null, "無法正確載入參數", "讀取參數檔異常", JOptionPane.ERROR_MESSAGE);
			logger.error("CommonUtil init ", e);
		}
	}

	public static void getColumns(File columnFile) {
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(columnFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			String columns = sb.toString();
			String[] columnsArray = columns.split(",");
			if (Constans.srcColumns != null) {
				for (int i = 0; i < columnsArray.length; i++) {
					String temp = columnsArray[i];
					Constans.srcColumns.add(temp);
				}
			}
		} catch (FileNotFoundException e) {
			logger.error("CommonUtil getColumns error", e);
		} catch (IOException e) {
			logger.error("CommonUtil getColumns error", e);
		}
	}

	public static void reload(String dbFilePath) {
		try {
			File dbFile = new File(dbFilePath);
			File decodeDBFile = new File(dbFile.getParent() + File.separator
					+ CommonUtil.getFileNameWithOutExtension(dbFile) + "_decode.txt");
			if (dbFile.exists()) {
				CommonUtil.decrypt(dbFile.getPath(), Constans.edit_pw);
				SystemConfigUtil dbConfig = new SystemConfigUtil(decodeDBFile);
				decodeDBFile.delete();
				Constans.srcDBInfo = new SrcDBInfo(dbConfig.get("src.type"), dbConfig.get("src.ip"),
						dbConfig.get("src.username"), dbConfig.get("src.password"), dbConfig.get("src.dbname"),
						dbConfig.get("src.tablename"));
				Constans.destDBInfo = new DestDBInfo(dbConfig.get("dest.type"), dbConfig.get("dest.ip"),
						dbConfig.get("dest.username"), dbConfig.get("dest.password"), dbConfig.get("dest.dbname"),
						dbConfig.get("dest.tablename"));
				Constans.sequence_s = dbConfig.get("sequence");
				Constans.condition = dbConfig.get("condition");
				Constans.condition_column = dbConfig.get("condition_column");
			}
		} catch (Exception e) {
			logger.error("CommonUtil reload error", e);
		}
	}

	public static boolean checkClassPathPropertiesExist(String propertyName) {
		boolean result = false;
		try {
			if (propertyName != null) {
				if (propertyName.startsWith("../")) {
					File testFile = new File(propertyName);
					if (testFile.exists()) {
						result = true;
					}
				} else {
					URL url = CommonUtil.class.getClassLoader().getResource(propertyName);
					if (url != null && url.toURI() != null && url.toURI().getPath() != null) {
						String filePath = url.toURI().getPath();
						File tempFile = new File(filePath);
						result = tempFile.exists();
					} else {
						File testFile = new File(propertyName);
						if (testFile.exists()) {
							result = true;
						}
					}
				}
			}
		} catch (URISyntaxException e) {
			logger.error("CommonUtil checkClassPathPropertiesExist error", e);
		}
		return result;
	}
}
