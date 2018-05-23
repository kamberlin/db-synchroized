package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import util.LogManager;
import util.Logger;

public class SystemConfigUtil {

	/** The logger. */
	private static Logger logger = LogManager.getLogger(SystemConfigUtil.class);

	/** RawData properties. */
	Properties properties = new Properties();

	public SystemConfigUtil(String propertiesName) throws Exception {
		try {
			InputStream input = null;
			input = this.getClass().getClassLoader().getResourceAsStream(propertiesName);
			if (input == null) {
				input = new FileInputStream(propertiesName);
			}
			if (input != null) {
				properties.load(input);
			}
		} catch (Exception e) {
			logger.error(propertiesName + " failure!!", e);
			throw e;
		}
	}

	public SystemConfigUtil(File propertyFile) throws Exception {
		try {
			InputStream input = new FileInputStream(propertyFile);
			if (input != null) {
				properties.load(input);
			}
			input.close();
		} catch (Exception e) {
			logger.error(propertyFile.getName() + " failure!!", e);
			throw e;
		}
	}

	/**
	 * 取得指定參數值
	 *
	 * @param String
	 *            參數名
	 * @return String 參數值
	 */
	public String get(String property_name) {
		String value = properties.getProperty(property_name);
		return value;
	}

	public void save(String key, String value) {
		FileOutputStream out;
		try {
			File dbFile = new File(DBSynConstans.dbproperty);
			File decodeDBFile = new File(dbFile.getParent() + File.separator
					+ CommonUtil.getFileNameWithOutExtension(dbFile) + "_decode.txt");
			CommonUtil.decrypt(dbFile.getPath(), DBSynConstans.edit_pw);
			out = new FileOutputStream(decodeDBFile);
			if (properties != null) {
				properties.setProperty(key, value);
				properties.store(out, null);
				out.close();
			}
			CommonUtil.encrypt(decodeDBFile.getPath(), DBSynConstans.edit_pw);
			if (dbFile.exists()) {
				dbFile.delete();
			}
			decodeDBFile.renameTo(dbFile);
			decodeDBFile.delete();
		} catch (FileNotFoundException e) {
			logger.error("SystemConfigUtil error", e);
		} catch (IOException e) {
			logger.error("SystemConfigUtil error", e);
		} catch (Exception e) {
			logger.error("SystemConfigUtil error", e);
		}
	}
}
