package view;

import javax.swing.JPanel;
import javax.swing.JPasswordField;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import util.LogManager;
import util.Logger;
import util.CommonUtil;
import util.Constans;
import util.DBUtil;
import util.SystemConfigUtil;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class DataBasePanel extends JPanel {
	/** The logger. */
	private static Logger logger = LogManager.getLogger(DataBasePanel.class);
	boolean isLoad = false;
	boolean isEdit = false;
	ArrayList<JTextField> allTexts;

	JComboBox<String> db_src_type = null;
	JComboBox<String> db_dest_type = null;

	JTextField dest_ip_text = null;
	JTextField dest_username_text = null;
	JPasswordField dest_pw_text = null;
	JTextField dest_dbName_text = null;
	JTextField dest_tableName_text = null;

	JTextField src_ip_text = null;
	JTextField src_username_text = null;
	JPasswordField src_pw_text = null;
	JTextField src_dbName_text = null;
	JTextField src_tableName_text = null;

	File dbFile = null;
	
	JButton edit_Btn = null;

	Dimension dimension = new Dimension(200, 30);
	/**
	 * 
	 */
	private static final long serialVersionUID = 2862094726236662026L;

	/**
	 * Create the panel.
	 */
	public DataBasePanel() {
		setLayout(new GridBagLayout());
		GridBagConstraints gbMain = new GridBagConstraints();
		GridBagConstraints gbUp = new GridBagConstraints();
		GridBagConstraints gbSrc = new GridBagConstraints();

		allTexts = new ArrayList<JTextField>();
		JPanel up_panel = new JPanel();
		up_panel.setLayout(new GridBagLayout());
		gbMain.gridx = 0;
		gbMain.gridy = 0;
		gbMain.gridwidth = 1;
		gbMain.gridheight = 1;
		gbMain.weightx = 1.5;
		gbMain.weighty = 2;
		gbMain.fill = GridBagConstraints.BOTH;
		add(up_panel, gbMain);
		TitledBorder tb_src = BorderFactory.createTitledBorder("來源欄位設定");
		tb_src.setTitleJustification(TitledBorder.LEFT);
		tb_src.setTitleFont(Constans.titleFont);
		JPanel dbinfo_src_panel = new JPanel();
		dbinfo_src_panel.setLayout(new GridBagLayout());
		gbUp.gridx = 0;
		gbUp.gridy = 0;
		gbUp.gridwidth = 1;
		gbUp.gridheight = 1;
		gbUp.weightx = 1;
		gbUp.weighty = 1;
		gbUp.anchor = GridBagConstraints.SOUTH;
		gbUp.fill = GridBagConstraints.BOTH;
		dbinfo_src_panel.setBorder(tb_src);

		up_panel.add(dbinfo_src_panel, gbUp);

		JLabel db_src_type_label = new JLabel("資料庫類型:");
		db_src_type_label.setFont(Constans.titleFont);
		gbSrc.gridx = 0;
		gbSrc.gridy = 0;
		gbSrc.gridwidth = 1;
		gbSrc.gridheight = 1;
		gbSrc.weightx = 1.5;
		gbSrc.weighty = 1;
		gbSrc.fill = GridBagConstraints.HORIZONTAL;
		dbinfo_src_panel.add(db_src_type_label, gbSrc);

		db_src_type = new JComboBox<String>();
		db_src_type.addItem("Oracle");
		db_src_type.addItem("SQLServer");
		if (Constans.srcDBInfo != null) {
			db_src_type.setSelectedItem(Constans.srcDBInfo.getType());
		}
		db_src_type.setPreferredSize(dimension);
		db_src_type.setFont(Constans.textFont);
		gbSrc.gridx = 1;
		gbSrc.gridy = 0;
		gbSrc.gridwidth = 1;
		gbSrc.gridheight = 1;
		gbSrc.weightx = 1.5;
		gbSrc.weighty = 1;
		dbinfo_src_panel.add(db_src_type, gbSrc);

		JLabel src_ip_label = new JLabel("伺服器名稱(IP):");
		src_ip_label.setFont(Constans.titleFont);
		gbSrc.gridx = 0;
		gbSrc.gridy = 1;
		gbSrc.gridwidth = 1;
		gbSrc.gridheight = 1;
		gbSrc.weightx = 1.5;
		gbSrc.weighty = 1;
		dbinfo_src_panel.add(src_ip_label, gbSrc);

		src_ip_text = new JTextField();
		src_ip_text.setFont(Constans.textFont);
		src_ip_text.setPreferredSize(dimension);
		gbSrc.gridx = 1;
		gbSrc.gridy = 1;
		gbSrc.gridwidth = 1;
		gbSrc.gridheight = 1;
		gbSrc.weightx = 1.5;
		gbSrc.weighty = 1;
		dbinfo_src_panel.add(src_ip_text, gbSrc);
		src_ip_text.setColumns(10);

		allTexts.add(src_ip_text);

		JLabel src_account_panel = new JLabel("登入帳號:");
		src_account_panel.setFont(Constans.titleFont);
		gbSrc.gridx = 0;
		gbSrc.gridy = 2;
		gbSrc.gridwidth = 1;
		gbSrc.gridheight = 1;
		gbSrc.weightx = 1.5;
		gbSrc.weighty = 1;
		dbinfo_src_panel.add(src_account_panel, gbSrc);

		src_username_text = new JTextField();
		src_username_text.setFont(Constans.textFont);
		gbSrc.gridx = 1;
		gbSrc.gridy = 2;
		gbSrc.gridwidth = 1;
		gbSrc.gridheight = 1;
		gbSrc.weightx = 1.5;
		gbSrc.weighty = 1;
		dbinfo_src_panel.add(src_username_text, gbSrc);
		src_username_text.setColumns(10);

		allTexts.add(src_username_text);

		JLabel src_pw_label = new JLabel("登入密碼:");
		src_pw_label.setFont(Constans.titleFont);
		gbSrc.gridx = 0;
		gbSrc.gridy = 3;
		gbSrc.gridwidth = 1;
		gbSrc.gridheight = 1;
		gbSrc.weightx = 1.5;
		gbSrc.weighty = 1;
		dbinfo_src_panel.add(src_pw_label, gbSrc);

		src_pw_text = new JPasswordField();
		src_pw_text.setFont(Constans.textFont);
		gbSrc.gridx = 1;
		gbSrc.gridy = 3;
		gbSrc.gridwidth = 1;
		gbSrc.gridheight = 1;
		gbSrc.weightx = 1.5;
		gbSrc.weighty = 1;
		dbinfo_src_panel.add(src_pw_text, gbSrc);
		src_pw_text.setColumns(10);

		allTexts.add(src_pw_text);

		JLabel src_database_label = new JLabel("資料庫名稱:");
		src_database_label.setFont(Constans.titleFont);
		gbSrc.gridx = 0;
		gbSrc.gridy = 4;
		gbSrc.gridwidth = 1;
		gbSrc.gridheight = 1;
		gbSrc.weightx = 1.5;
		gbSrc.weighty = 1;
		dbinfo_src_panel.add(src_database_label, gbSrc);

		src_dbName_text = new JTextField();
		src_dbName_text.setFont(Constans.textFont);
		gbSrc.gridx = 1;
		gbSrc.gridy = 4;
		gbSrc.gridwidth = 1;
		gbSrc.gridheight = 1;
		gbSrc.weightx = 1.5;
		gbSrc.weighty = 1;
		dbinfo_src_panel.add(src_dbName_text, gbSrc);
		src_dbName_text.setColumns(10);

		allTexts.add(src_dbName_text);

		JLabel src_table_label = new JLabel("資料表名稱:");
		src_table_label.setFont(Constans.titleFont);
		gbSrc.gridx = 0;
		gbSrc.gridy = 5;
		gbSrc.gridwidth = 1;
		gbSrc.gridheight = 1;
		gbSrc.weightx = 1.5;
		gbSrc.weighty = 1;
		dbinfo_src_panel.add(src_table_label, gbSrc);

		src_tableName_text = new JTextField();
		src_tableName_text.setFont(Constans.textFont);
		gbSrc.gridx = 1;
		gbSrc.gridy = 5;
		gbSrc.gridwidth = 1;
		gbSrc.gridheight = 1;
		gbSrc.weightx = 1.5;
		gbSrc.weighty = 1;
		dbinfo_src_panel.add(src_tableName_text, gbSrc);
		src_tableName_text.setColumns(10);

		allTexts.add(src_tableName_text);

		JButton src_btn = new JButton("連線測試");
		src_btn.setFont(Constans.titleFont);
		gbSrc.gridx = 1;
		gbSrc.gridy = 6;
		gbSrc.gridwidth = 1;
		gbSrc.gridheight = 1;
		gbSrc.weightx = 1.5;
		gbSrc.weighty = 1;
		gbSrc.fill = GridBagConstraints.NONE;
		src_btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String src_db_type = (String) db_src_type.getSelectedItem();
				String src_ip = src_ip_text.getText();
				String src_account = src_username_text.getText();
				String src_pw = String.valueOf(src_pw_text.getPassword());
				String src_database = src_dbName_text.getText();
				String src_table = src_tableName_text.getText();
				logger.info("來源資料庫連線測試");
				checkDBConnection("來源",up_panel,src_db_type, src_ip, src_account, src_pw, src_database, src_table);
			}
		});
		dbinfo_src_panel.add(src_btn, gbSrc);
		// ------------------------------------------
		GridBagConstraints gbDest = new GridBagConstraints();
		TitledBorder tb_dest = BorderFactory.createTitledBorder("目標欄位設定");
		tb_dest.setTitleFont(Constans.titleFont);
		tb_dest.setTitleJustification(TitledBorder.LEFT);

		JPanel dbinfo_dest_panel = new JPanel();
		dbinfo_dest_panel.setLayout(new GridBagLayout());
		dbinfo_dest_panel.setBorder(tb_dest);
		gbUp.gridx = 1;
		gbUp.gridy = 0;
		gbUp.gridwidth = 1;
		gbUp.gridheight = 1;
		gbUp.weightx = 1;
		gbUp.weighty = 1;
		gbUp.anchor = GridBagConstraints.SOUTH;
		gbUp.fill = GridBagConstraints.BOTH;
		up_panel.add(dbinfo_dest_panel, gbUp);

		JLabel destTitleLabel = new JLabel("資料庫類型");
		destTitleLabel.setFont(Constans.titleFont);
		gbDest.gridx = 0;
		gbDest.gridy = 0;
		gbDest.gridwidth = 1;
		gbDest.gridheight = 1;
		gbDest.weightx = 1.5;
		gbDest.weighty = 1;
		gbDest.fill = GridBagConstraints.HORIZONTAL;
		dbinfo_dest_panel.add(destTitleLabel, gbDest);

		db_dest_type = new JComboBox<String>();
		db_dest_type.addItem("Oracle");
		db_dest_type.addItem("SQLServer");
		if (Constans.destDBInfo != null) {
			db_dest_type.setSelectedItem(Constans.destDBInfo.getType());
		}
		db_dest_type.setPreferredSize(dimension);
		db_dest_type.setFont(Constans.textFont);
		gbDest.gridx = 1;
		gbDest.gridy = 0;
		gbDest.gridwidth = 1;
		gbDest.gridheight = 1;
		gbDest.weightx = 1.5;
		gbDest.weighty = 1;
		dbinfo_dest_panel.add(db_dest_type, gbDest);

		JLabel dest_ip_label = new JLabel("伺服器名稱(IP):");
		dest_ip_label.setFont(Constans.titleFont);
		gbDest.gridx = 0;
		gbDest.gridy = 1;
		gbDest.gridwidth = 1;
		gbDest.gridheight = 1;
		gbDest.weightx = 1.5;
		gbDest.weighty = 1;
		dbinfo_dest_panel.add(dest_ip_label, gbDest);

		dest_ip_text = new JTextField();
		dest_ip_text.setMaximumSize(dimension);
		dest_ip_text.setFont(Constans.textFont);
		gbDest.gridx = 1;
		gbDest.gridy = 1;
		gbDest.gridwidth = 1;
		gbDest.gridheight = 1;
		gbDest.weightx = 1.5;
		gbDest.weighty = 1;
		dbinfo_dest_panel.add(dest_ip_text, gbDest);
		dest_ip_text.setColumns(10);

		allTexts.add(dest_ip_text);

		JLabel dest_account_panel = new JLabel("登入帳號:");
		dest_account_panel.setFont(Constans.titleFont);
		gbDest.gridx = 0;
		gbDest.gridy = 2;
		gbDest.gridwidth = 1;
		gbDest.gridheight = 1;
		gbDest.weightx = 1.5;
		gbDest.weighty = 1;
		dbinfo_dest_panel.add(dest_account_panel, gbDest);

		dest_username_text = new JTextField();
		dest_username_text.setFont(Constans.textFont);
		gbDest.gridx = 1;
		gbDest.gridy = 2;
		gbDest.gridwidth = 1;
		gbDest.gridheight = 1;
		gbDest.weightx = 1.5;
		gbDest.weighty = 1;
		dbinfo_dest_panel.add(dest_username_text, gbDest);
		dest_username_text.setColumns(10);

		allTexts.add(dest_username_text);

		JLabel dest_pw_label = new JLabel("登入密碼:");
		dest_pw_label.setFont(Constans.titleFont);
		gbDest.gridx = 0;
		gbDest.gridy = 3;
		gbDest.gridwidth = 1;
		gbDest.gridheight = 1;
		gbDest.weightx = 1.5;
		gbDest.weighty = 1;
		dbinfo_dest_panel.add(dest_pw_label, gbDest);

		dest_pw_text = new JPasswordField();
		dest_pw_text.setFont(Constans.textFont);
		gbDest.gridx = 1;
		gbDest.gridy = 3;
		gbDest.gridwidth = 1;
		gbDest.gridheight = 1;
		gbDest.weightx = 1.5;
		gbDest.weighty = 1;
		dbinfo_dest_panel.add(dest_pw_text, gbDest);
		dest_pw_text.setColumns(10);

		allTexts.add(dest_pw_text);

		JLabel dest_database_label = new JLabel("資料庫名稱:");
		dest_database_label.setFont(Constans.titleFont);
		gbDest.gridx = 0;
		gbDest.gridy = 4;
		gbDest.gridwidth = 1;
		gbDest.gridheight = 1;
		gbDest.weightx = 1.5;
		gbDest.weighty = 1;
		dbinfo_dest_panel.add(dest_database_label, gbDest);

		dest_dbName_text = new JTextField();
		dest_dbName_text.setFont(Constans.textFont);
		gbDest.gridx = 1;
		gbDest.gridy = 4;
		gbDest.gridwidth = 1;
		gbDest.gridheight = 1;
		gbDest.weightx = 1.5;
		gbDest.weighty = 1;
		dbinfo_dest_panel.add(dest_dbName_text, gbDest);
		dest_dbName_text.setColumns(10);

		allTexts.add(dest_dbName_text);

		JLabel dest_table_label = new JLabel("資料表名稱:");
		dest_table_label.setFont(Constans.titleFont);
		gbDest.gridx = 0;
		gbDest.gridy = 5;
		gbDest.gridwidth = 1;
		gbDest.gridheight = 1;
		gbDest.weightx = 1.5;
		gbDest.weighty = 1;
		dbinfo_dest_panel.add(dest_table_label, gbDest);

		dest_tableName_text = new JTextField();
		dest_tableName_text.setFont(Constans.textFont);
		gbDest.gridx = 1;
		gbDest.gridy = 5;
		gbDest.gridwidth = 1;
		gbDest.gridheight = 1;
		gbDest.weightx = 1.5;
		gbDest.weighty = 1;
		dbinfo_dest_panel.add(dest_tableName_text, gbDest);
		dest_tableName_text.setColumns(10);

		allTexts.add(dest_tableName_text);

		JButton dest_btn = new JButton("連線測試");
		dest_btn.setFont(Constans.titleFont);
		dest_btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String dest_db_type = (String) db_dest_type.getSelectedItem();
				String dest_ip = dest_ip_text.getText();
				String dest_account = dest_username_text.getText();
				String dest_pw = String.valueOf(dest_pw_text.getPassword());
				String dest_database = dest_dbName_text.getText();
				String dest_table = dest_tableName_text.getText();
				logger.info("目標資料庫連線測試");
				checkDBConnection("目標", up_panel, dest_db_type, dest_ip, dest_account, dest_pw, dest_database,
						dest_table);
			}
		});
		gbDest.gridx = 1;
		gbDest.gridy = 6;
		gbDest.gridwidth = 1;
		gbDest.gridheight = 1;
		gbDest.weightx = 1.5;
		gbDest.weighty = 1;
		gbDest.fill = GridBagConstraints.NONE;
		dbinfo_dest_panel.add(dest_btn, gbDest);
		// -----------------------------------------------------

		JPanel btn_panel = new JPanel();
		gbMain.gridx = 0;
		gbMain.gridy = 2;
		gbMain.gridwidth = 1;
		gbMain.gridheight = 1;
		gbMain.weightx = 1;
		gbMain.weighty = 0.1;
		gbMain.fill = GridBagConstraints.VERTICAL;
		add(btn_panel, gbMain);
		btn_panel.setLayout(new GridLayout(1, 1, 0, 0));

		edit_Btn = new JButton("修改設定");
		edit_Btn.setFont(Constans.titleFont);
		btn_panel.add(edit_Btn);
		edit_Btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				save();
				if (dbFile != null) {
					CommonUtil.reloadDB();
				}
				disableAll();
			}
		});
	}

	public void init() {
		try {
			if (Constans.dbproperty != null) {
				dbFile = new File(Constans.dbproperty);
				if (isFileExist(dbFile)) {
					CommonUtil.reloadDB();
				}
			}
		} catch (Exception e) {
			logger.error("DataBasePanel init error", e);
		}
	}

	public boolean checkClassPathPropertiesExist(String propertyName) {
		boolean result = false;
		try {
			URL url = this.getClass().getClassLoader().getResource(propertyName);
			if (url != null && url.toURI() != null && url.toURI().getPath() != null) {
				String filePath = url.toURI().getPath();
				File tempFile = new File(filePath);
				result = tempFile.exists();
			}
		} catch (URISyntaxException e) {
			logger.error("DataBasePanel checkClassPathPropertiesExist error", e);
		}
		return result;
	}

	public boolean isFileExist(File tempFile) {
		boolean result = false;
		if (tempFile != null && tempFile.exists()) {
			result = true;
		}
		return result;
	}

	public void checkPassword(JPanel panel) {
		logger.info("驗證密碼中");
		if (dbFile != null) {
			CommonUtil.reloadDB();
			setDefaultText();
		}
		boolean result = CommonUtil.enterPassword(panel);
		//logger.info("result=" + result);
		if (result) {
			enableAll();
		}
		if (!isLoad) {
			isLoad = true;
		}
	}

	public void disableAll() {
		//logger.info("disableAll");
		for (int i = 0; i < allTexts.size(); i++) {
			JTextField tempText = allTexts.get(i);
			tempText.setEditable(false);
		}
		db_src_type.setEnabled(false);
		db_dest_type.setEnabled(false);
		edit_Btn.setEnabled(false);
		isEdit = false;
	}

	public void enableAll() {
		//logger.info("enableAll");
		for (int i = 0; i < allTexts.size(); i++) {
			JTextField tempText = allTexts.get(i);
			tempText.setEditable(true);
		}
		db_src_type.setEnabled(true);
		db_dest_type.setEnabled(true);
		edit_Btn.setEnabled(true);
		isEdit = true;
	}

	public void setDefaultText() {
		if (Constans.srcDBInfo != null) {
			src_ip_text.setText(Constans.srcDBInfo.getIp());
			src_username_text.setText(Constans.srcDBInfo.getUsername());
			src_pw_text.setText(Constans.srcDBInfo.getPassword());
			src_dbName_text.setText(Constans.srcDBInfo.getDbName());
			src_tableName_text.setText(Constans.srcDBInfo.getTableName());
		}
		if (Constans.destDBInfo != null) {
			dest_ip_text.setText(Constans.destDBInfo.getIp());
			dest_username_text.setText(Constans.destDBInfo.getUsername());
			dest_pw_text.setText(Constans.destDBInfo.getPassword());
			dest_dbName_text.setText(Constans.destDBInfo.getDbName());
			dest_tableName_text.setText(Constans.destDBInfo.getTableName());
		}
		if (dbFile != null && dbFile.exists()) {
			db_src_type.setSelectedItem(Constans.srcDBInfo.getType());
			db_dest_type.setSelectedItem(Constans.destDBInfo.getType());
		}
	}

	public void save() {
		SystemConfigUtil systemConfigUtil;
		try {
			if (dbFile != null && dbFile.exists()) {
				dbFile.delete();
			} else {
				dbFile = new File(Constans.dbproperty);
			}

			dbFile.getParentFile().mkdirs();
			dbFile.createNewFile();
			systemConfigUtil = new SystemConfigUtil(dbFile);
			// 儲存設定
			systemConfigUtil.save("src.type", (String) db_src_type.getSelectedItem());
			systemConfigUtil.save("src.ip", src_ip_text.getText());
			systemConfigUtil.save("src.username", src_username_text.getText());
			systemConfigUtil.save("src.password", String.valueOf(src_pw_text.getPassword()));
			systemConfigUtil.save("src.dbname", src_dbName_text.getText());
			systemConfigUtil.save("src.tablename", src_tableName_text.getText());

			systemConfigUtil.save("dest.type", (String) db_dest_type.getSelectedItem());
			systemConfigUtil.save("dest.ip", dest_ip_text.getText());
			systemConfigUtil.save("dest.username", dest_username_text.getText());
			systemConfigUtil.save("dest.password", String.valueOf(dest_pw_text.getPassword()));
			systemConfigUtil.save("dest.dbname", dest_dbName_text.getText());
			systemConfigUtil.save("dest.tablename", dest_tableName_text.getText());

			// 將檔案再加密
			CommonUtil.encrypt(dbFile.getPath(), Constans.edit_pw);
		} catch (Exception e) {
			logger.error("DataBasePanel save error ", e);
		}
	}

	public void askSave() {
		int answer = JOptionPane.showConfirmDialog(this, "尚未儲存設定，是否需要儲存", "儲存設定", JOptionPane.YES_NO_OPTION);
		if (answer == JOptionPane.YES_OPTION) {
			save();
		} else {
			isEdit = false;
			logger.info("資料庫設定 不需存檔  設定為不可編輯");
			setDefaultText();
			disableAll();
		}
	}

	public void checkDBConnection(String head, JPanel jpanel, String db_type, String db_ip, String db_account,
			String db_pw, String db_database, String db_table) {
		String msg = DBUtil.checkConnection(db_type, db_ip, db_account, db_pw, db_database, db_table);
		if ("資料庫連線成功".equals(msg)) {
			msg = head + msg;
			logger.info(msg);
			JOptionPane.showMessageDialog(jpanel, msg, "資料庫連線測試", JOptionPane.INFORMATION_MESSAGE);
		} else {
			msg = head + msg;
			logger.error(msg);
			JOptionPane.showMessageDialog(jpanel, msg, "資料庫連線測試", JOptionPane.ERROR_MESSAGE);
		}
	}

}
