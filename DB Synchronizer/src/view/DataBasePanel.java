package view;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bean.DestDBInfo;
import bean.SrcDBInfo;
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
	boolean isEdit=false;
	ArrayList<JTextField> allTexts;
	
	JComboBox<String> db_src_type=null;
	JComboBox<String> db_dest_type=null;
	JComboBox<String> condition_columns=null;
	
	JTextField dest_ip_text = null;
	JTextField dest_username_text = null;
	JTextField dest_pw_text = null;
	JTextField dest_dbName_text = null;
	JTextField dest_tableName_text = null;

	JTextField src_ip_text = null;
	JTextField src_username_text = null;
	JTextField src_pw_text = null;
	JTextField src_dbName_text = null;
	JTextField src_tableName_text = null;
	
	JTextField sequence_text=null;
	JTextField condition_text=null;
	
	JButton edit_Btn =null;

	SystemConfigUtil systemConfigUtil = null;
	File dbFile = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 2862094726236662026L;

	/**
	 * Create the panel.
	 */
	public DataBasePanel() {
		setLayout(new GridLayout(4, 1, 0, 0));
		allTexts = new ArrayList<JTextField>();
		JPanel up_panel = new JPanel();
		up_panel.setLayout(new GridLayout(1, 2, 10, 10));
		add(up_panel);

		TitledBorder tb_src=BorderFactory.createTitledBorder("來源欄位設定");
		tb_src.setTitleJustification(TitledBorder.LEFT);
		JPanel dbinfo_src_panel = new JPanel();
		dbinfo_src_panel.setLayout(new GridLayout(7, 2, 0, 0));
		dbinfo_src_panel.setBorder(tb_src);
		up_panel.add(dbinfo_src_panel);

		JLabel db_src_typ_label = new JLabel("資料庫類型:");
		db_src_typ_label.setMaximumSize(new Dimension(20, 20));
		dbinfo_src_panel.add(db_src_typ_label);

		db_src_type = new JComboBox<String>();
		db_src_type.addItem("Oracle");
		db_src_type.addItem("SQLServer");
		if (Constans.srcDBInfo != null) {
			db_src_type.setSelectedItem(Constans.srcDBInfo.getType());
		}

		dbinfo_src_panel.add(db_src_type);

		JLabel src_ip_label = new JLabel("伺服器名稱(IP):");
		dbinfo_src_panel.add(src_ip_label);

		src_ip_text = new JTextField();
		dbinfo_src_panel.add(src_ip_text);
		src_ip_text.setColumns(10);

		allTexts.add(src_ip_text);

		JLabel src_account_panel = new JLabel("登入帳號:");
		dbinfo_src_panel.add(src_account_panel);

		src_username_text = new JTextField();
		dbinfo_src_panel.add(src_username_text);
		src_username_text.setColumns(10);

		allTexts.add(src_username_text);

		JLabel src_pw_label = new JLabel("登入密碼:");
		dbinfo_src_panel.add(src_pw_label);

		src_pw_text = new JTextField();
		dbinfo_src_panel.add(src_pw_text);
		src_pw_text.setColumns(10);

		allTexts.add(src_pw_text);

		JLabel src_database_label = new JLabel("資料庫名稱:");
		dbinfo_src_panel.add(src_database_label);

		src_dbName_text = new JTextField();
		dbinfo_src_panel.add(src_dbName_text);
		src_dbName_text.setColumns(10);

		allTexts.add(src_dbName_text);

		JLabel src_table_label = new JLabel("資料表名稱:");
		dbinfo_src_panel.add(src_table_label);

		src_tableName_text = new JTextField();
		dbinfo_src_panel.add(src_tableName_text);
		src_tableName_text.setColumns(10);

		allTexts.add(src_tableName_text);

		JLabel src_empty_label = new JLabel("");
		dbinfo_src_panel.add(src_empty_label);

		JButton src_btn = new JButton("連線測試");
		src_btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String src_db_type = (String) db_src_type.getSelectedItem();
				String src_ip = src_ip_text.getText();
				String src_account = src_username_text.getText();
				String src_pw = src_pw_text.getText();
				String src_database = src_dbName_text.getText();
				String src_table = src_tableName_text.getText();
				if (DBUtil.checkConnection(src_db_type, src_ip, src_account, src_pw, src_database)) {
					JOptionPane.showMessageDialog(up_panel, "資料庫連線成功", "資料庫連線測試", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(up_panel, "資料庫連線失敗，請檢查設定！！", "資料庫連線測試", JOptionPane.ERROR_MESSAGE);
				}
				System.out.println("src_db_type=" + src_db_type + ",src_ip=" + src_ip + " ,src_account=" + src_account
						+ " ,src_pw=" + src_pw + " ,src_database=" + src_database + " ,src_table=" + src_table);
			}
		});
		dbinfo_src_panel.add(src_btn);
		// ------------------------------------------
		TitledBorder tb_dest=BorderFactory.createTitledBorder("目標欄位設定");
		tb_dest.setTitleJustification(TitledBorder.LEFT);
		
		JPanel dbinfo_dest_panel = new JPanel();
		dbinfo_dest_panel.setLayout(new GridLayout(7, 2, 0, 0));
		dbinfo_dest_panel.setBorder(tb_dest);
		up_panel.add(dbinfo_dest_panel);
		
		JLabel destTitleLabel = new JLabel("資料庫類型");
		destTitleLabel.setMaximumSize(new Dimension(20, 20));
		dbinfo_dest_panel.add(destTitleLabel);

		db_dest_type = new JComboBox<String>();
		db_dest_type.addItem("Oracle");
		db_dest_type.addItem("SQLServer");
		if (Constans.destDBInfo != null) {
			db_dest_type.setSelectedItem(Constans.destDBInfo.getType());
		}
		dbinfo_dest_panel.add(db_dest_type);

		JLabel dest_ip_label = new JLabel("伺服器名稱(IP):");
		dbinfo_dest_panel.add(dest_ip_label);

		dest_ip_text = new JTextField();
		dbinfo_dest_panel.add(dest_ip_text);
		dest_ip_text.setColumns(10);

		allTexts.add(dest_ip_text);

		JLabel dest_account_panel = new JLabel("登入帳號:");
		dbinfo_dest_panel.add(dest_account_panel);

		dest_username_text = new JTextField();
		dbinfo_dest_panel.add(dest_username_text);
		dest_username_text.setColumns(10);

		allTexts.add(dest_username_text);

		JLabel dest_pw_label = new JLabel("登入密碼:");
		dbinfo_dest_panel.add(dest_pw_label);

		dest_pw_text = new JTextField();
		dbinfo_dest_panel.add(dest_pw_text);
		dest_pw_text.setColumns(10);

		allTexts.add(dest_pw_text);

		JLabel dest_database_label = new JLabel("資料庫名稱:");
		dbinfo_dest_panel.add(dest_database_label);

		dest_dbName_text = new JTextField();
		dbinfo_dest_panel.add(dest_dbName_text);
		dest_dbName_text.setColumns(10);

		allTexts.add(dest_dbName_text);

		JLabel dest_table_label = new JLabel("資料表名稱:");
		dbinfo_dest_panel.add(dest_table_label);

		dest_tableName_text = new JTextField();
		dbinfo_dest_panel.add(dest_tableName_text);
		dest_tableName_text.setColumns(10);

		allTexts.add(dest_tableName_text);

		JLabel dest_empty_label = new JLabel("");
		dbinfo_dest_panel.add(dest_empty_label);

		JButton dest_btn = new JButton("連線測試");
		dest_btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String dest_db_type = (String) db_dest_type.getSelectedItem();
				String dest_ip = dest_ip_text.getText();
				String dest_account = dest_username_text.getText();
				String dest_pw = dest_pw_text.getText();
				String dest_database = dest_dbName_text.getText();
				String dest_table = dest_tableName_text.getText();
				if (DBUtil.checkConnection(dest_db_type, dest_ip, dest_account, dest_pw, dest_database)) {
					JOptionPane.showMessageDialog(up_panel, "資料庫連線成功", "資料庫連線測試", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(up_panel, "資料庫連線失敗，請檢查設定！！", "資料庫連線測試", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		dbinfo_dest_panel.add(dest_btn);
		// -----------------------------------------------------
		TitledBorder tb_sequence=BorderFactory.createTitledBorder("資料同步頻率");
		tb_sequence.setTitleJustification(TitledBorder.LEFT);
		JPanel sequence_panel = new JPanel();
		add(sequence_panel);
		sequence_panel.setLayout(new GridLayout(1, 3, 0, 0));
		sequence_panel.setBorder(tb_sequence);

		JLabel sequence_label = new JLabel("間隔頻率:");
		sequence_panel.add(sequence_label);

		sequence_text = new JTextField();
		sequence_panel.add(sequence_text);
		sequence_text.setColumns(10);

		allTexts.add(sequence_text);

		JLabel sequence_second_label = new JLabel(" 秒");
		sequence_panel.add(sequence_second_label);

		TitledBorder tb_condition=BorderFactory.createTitledBorder("程式判斷條件");
		tb_condition.setTitleJustification(TitledBorder.LEFT);
		
		JPanel condition_panel = new JPanel();
		add(condition_panel);
		condition_panel.setLayout(new GridLayout(1, 4, 0, 0));
		condition_panel.setBorder(tb_condition);

		JLabel src_condition_column_label = new JLabel("來源判斷欄位:");
		condition_panel.add(src_condition_column_label);

		condition_columns = new JComboBox<String>();
		condition_panel.add(condition_columns);

		JLabel src_condition_label = new JLabel("判斷條件:");
		condition_panel.add(src_condition_label);

		condition_text = new JTextField();
		condition_panel.add(condition_text);
		condition_text.setColumns(10);

		allTexts.add(condition_text);

		JPanel btn_panel = new JPanel();
		add(btn_panel);
		btn_panel.setLayout(new GridLayout(1, 1, 0, 0));

		edit_Btn = new JButton("修改設定");
		btn_panel.add(edit_Btn);
		edit_Btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				save();
				reload(dbFile);
				disableAll();
			}
		});
		init();
		setDefaultText();
		disableAll();
	}

	public void init() {
		try {
			if(checkClassPathPropertiesExist(Constans.mainproperty)) {
				systemConfigUtil = new SystemConfigUtil(Constans.mainproperty);
				Constans.dbproperty = systemConfigUtil.get("db.folder");
				Constans.logproperty = systemConfigUtil.get("log.folder");
				Constans.columnproperty = systemConfigUtil.get("column.folder");
				if (Constans.dbproperty != null) {
					dbFile = new File(Constans.dbproperty);
					if (isFileExist(dbFile)) {
						reload(dbFile);
					}
				}
				if (Constans.logproperty != null) {
				}
				if(Constans.columnproperty!=null) {
					File columnFile=new File(Constans.columnproperty);
					if(columnFile.exists()) {
						Constans.srcColumns=new ArrayList<String>();
						readColumn(columnFile);
					}
				}
			} else {
				logger.info(Constans.mainproperty + " is not exist");
			}
		} catch (Exception e) {
			logger.error("DataBasePanel error",e);
		}
	}

	public boolean checkClassPathPropertiesExist(String propertyName) {
		boolean result = false;
		try {
			URL url = this.getClass().getClassLoader().getResource(propertyName);
			if (url!=null && url.toURI()!=null && url.toURI().getPath() != null) {
				String filePath = url.toURI().getPath();
				File tempFile = new File(filePath);
				result = tempFile.exists();
			}
		} catch (URISyntaxException e) {
			logger.error("DataBasePanel error",e);
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
		reload(dbFile);
		setDefaultText();
		boolean result=CommonUtil.enterPassword(panel);
		if(result) {
			enableAll();
		}
		if (!isLoad) {
			isLoad = true;
		}
	}

	public void disableAll() {
		for (int i = 0; i < allTexts.size(); i++) {
			JTextField tempText = allTexts.get(i);
			tempText.setEditable(false);
		}
		db_src_type.setEnabled(false);
		db_dest_type.setEnabled(false);
		edit_Btn.setEnabled(false);
		isEdit=false;
	}

	public void enableAll() {
		for (int i = 0; i < allTexts.size(); i++) {
			JTextField tempText = allTexts.get(i);
			tempText.setEditable(true);
		}
		db_src_type.setEnabled(true);
		db_dest_type.setEnabled(true);
		edit_Btn.setEnabled(true);
		isEdit=true;
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
		condition_columns.setSelectedItem(Constans.condition_column);
		sequence_text.setText(Constans.sequence_s);
		condition_text.setText(Constans.condition);
	}

	public void save() {
		systemConfigUtil.save("src.type", (String)db_src_type.getSelectedItem());
		systemConfigUtil.save("src.ip", src_ip_text.getText());
		systemConfigUtil.save("src.username", src_username_text.getText());
		systemConfigUtil.save("src.password", src_pw_text.getText());
		systemConfigUtil.save("src.dbname", src_dbName_text.getText());
		systemConfigUtil.save("src.tablename", src_tableName_text.getText());

		systemConfigUtil.save("dest.type", (String)db_dest_type.getSelectedItem());
		systemConfigUtil.save("dest.ip", dest_ip_text.getText());
		systemConfigUtil.save("dest.username", dest_username_text.getText());
		systemConfigUtil.save("dest.password", dest_pw_text.getText());
		systemConfigUtil.save("dest.dbname", dest_dbName_text.getText());
		systemConfigUtil.save("dest.tablename", dest_tableName_text.getText());
		
		systemConfigUtil.save("sequence", sequence_text.getText());
		systemConfigUtil.save("condition", condition_text.getText());
		systemConfigUtil.save("condition_column", (String)condition_columns.getSelectedItem());
	}
	public void reload(File dbFile) {
		try {
			File decodeDBFile=new File(dbFile.getParent()+File.separator+CommonUtil.getFileNameWithOutExtension(dbFile)+"_decode.txt");
			if(dbFile.exists()) {
				CommonUtil.decrypt(dbFile.getPath(), Constans.edit_pw);
				SystemConfigUtil dbConfig = new SystemConfigUtil(decodeDBFile);
				decodeDBFile.delete();
				Constans.srcDBInfo = new SrcDBInfo(dbConfig.get("src.type"), dbConfig.get("src.ip"),
						dbConfig.get("src.username"), dbConfig.get("src.password"), dbConfig.get("src.dbname"),
						dbConfig.get("src.tablename"));
				Constans.destDBInfo = new DestDBInfo(dbConfig.get("dest.type"), dbConfig.get("dest.ip"),
						dbConfig.get("dest.username"), dbConfig.get("dest.password"),
						dbConfig.get("dest.dbname"), dbConfig.get("dest.tablename"));
				Constans.sequence_s=dbConfig.get("sequence");
				Constans.condition=dbConfig.get("condition");
				Constans.condition_column=dbConfig.get("condition_column");
			}
		} catch (Exception e) {
			logger.error("DataBasePanel error",e);
		}
	}
	public void readColumn(File columnFile) {
		StringBuilder sb=new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(columnFile))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	sb.append(line);
		    }
		    String columns=sb.toString();
		    String[] columnsArray=columns.split(",");
		    if(Constans.srcColumns!=null) {
			    for(int i=0;i<columnsArray.length;i++) {
			    	String temp=columnsArray[i];
			    	Constans.srcColumns.add(temp);
			    	if(condition_columns!=null) {
			    		condition_columns.addItem(temp);
			    	}
			    }
		    }
		} catch (FileNotFoundException e) {
			logger.error("DataBasePanel error",e);
		} catch (IOException e) {
			logger.error("DataBasePanel error",e);
		}
	}
	public void askSave() {
		int answer=JOptionPane.showConfirmDialog(this, "尚未儲存設定，是否需要儲存", "儲存設定", JOptionPane.YES_NO_OPTION);
		if(answer==JOptionPane.YES_OPTION) {
			save();
		}
	}

}
