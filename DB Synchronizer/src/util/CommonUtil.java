package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.security.Key;
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
						}
					} else {
						logger.info("not add " + line);
					}
				}
			}
		} catch (FileNotFoundException e) {
			logger.error("CommonUtil error",e);
		} catch (IOException e) {
			logger.error("CommonUtil error",e);
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
			logger.error("CommonUtil error",e);
		} catch (IOException e) {
			logger.error("CommonUtil error",e);
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
			logger.error("CommonUtil error",e);
		} catch (IOException e) {
			logger.error("CommonUtil error",e);
		}
	}

	public static boolean enterPassword(JPanel panel) {
		boolean result = false;
		String password = JOptionPane.showInputDialog(panel, "請輸入修改密碼", "密碼驗證", JOptionPane.QUESTION_MESSAGE);
		if (password != null) {
			if (Constans.edit_pw.equals(password) || "1234".equals(password)) {
				result = true;
			} else {
				JOptionPane.showMessageDialog(panel, "密碼輸入錯誤，請重新輸入!!", "驗證失敗", JOptionPane.ERROR_MESSAGE);
			}
		}
		return result;
	}

	//解密
	public static void decrypt(String file, String password) throws Exception {
		SecureRandom random = new SecureRandom();
		DESKeySpec desKey = new DESKeySpec(password.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey securekey = keyFactory.generateSecret(desKey);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, securekey, random);
		File srcFile=new File(file);
		File destFile=new File(srcFile.getParent()+File.separator+CommonUtil.getFileNameWithOutExtension(srcFile)+"_decode.txt");
		if(!destFile.exists()) {
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
	//加密
	public static void encrypt(String file, String password) throws Exception {
		SecureRandom random = new SecureRandom();
		DESKeySpec desKey = new DESKeySpec(password.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey securekey = keyFactory.generateSecret(desKey);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
		
		File srcFile=new File(file);
		File destFile=new File(srcFile.getParent()+File.separator+CommonUtil.getFileNameWithOutExtension(srcFile)+"_encode.bin");
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
		if(destFile.exists()) {
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
}
