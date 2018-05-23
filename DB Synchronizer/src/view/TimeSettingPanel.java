package view;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import bean.TransferBean;
import util.CommonUtil;
import util.DBSynConstans;
import util.DBUtil;
import util.SystemConfigUtil;
import util.LogManager;
import util.Logger;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

public class TimeSettingPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1466832287065379835L;

	private static Logger logger = LogManager.getLogger(TimeSettingPanel.class);
	boolean isLoad = false;
	boolean isEdit = false;
	ArrayList<JComboBox<String>> srcUpJComboxs = null;
	ArrayList<JComboBox<String>> srcDownJComboxs = null;

	ArrayList<JComboBox<String>> destUpJComboxs = null;

	ArrayList<JComboBox<String>> srcUpYMDFormatJComboxs = null;
	ArrayList<JComboBox<String>> srcUpHMSFormatJComboxs = null;

	ArrayList<JComboBox<String>> destDownYMDJComboxs = null;
	ArrayList<JComboBox<String>> destDownHMSJComboxs = null;

	ArrayList<JComboBox<String>> destUpYMDFormatJComboxs = null;
	ArrayList<JComboBox<String>> destUpHMSFormatJComboxs = null;

	ArrayList<JComboBox<String>> destDownYMDFormatJComboxs = null;
	ArrayList<JComboBox<String>> destDownHMSFormatJComboxs = null;

	JButton edit_Btn = null;

	JPanel first_panel = null;
	JPanel second_panel = null;
	JPanel btn_panel = null;
	JScrollPane srcScrollPanel = null;
	JScrollPane destScrollPanel = null;

	GridBagLayout gbl_first_layout = null;
	GridBagLayout gbl_second_layout = null;
	GridBagLayout gbl_btn_layout = null;

	int totalColumnNum = 2;

	/**
	 * Create the panel.
	 */
	public TimeSettingPanel() {

		GridBagLayout outter_layout = new GridBagLayout();
		setLayout(outter_layout);

		first_panel = new JPanel();
		TitledBorder firstTitle = new TitledBorder("時間格式轉換設定");
		firstTitle.setTitleFont(DBSynConstans.titleFont);
		firstTitle.setTitleJustification(TitledBorder.LEFT);
		first_panel.setBorder(firstTitle);
		gbl_first_layout = new GridBagLayout();
		first_panel.setLayout(gbl_first_layout);
		GridBagConstraints gbFirst = new GridBagConstraints();
		gbFirst.gridx = 0;
		gbFirst.gridy = 0;
		gbFirst.gridwidth = 1;
		gbFirst.gridheight = 1;
		gbFirst.weightx = 1.5;
		gbFirst.weighty = 1;
		gbFirst.fill = GridBagConstraints.BOTH;

		add(first_panel, gbFirst);

		second_panel = new JPanel();
		TitledBorder secondTitle = new TitledBorder("時間格式轉換設定(拆欄位)");
		secondTitle.setTitleFont(DBSynConstans.titleFont);
		secondTitle.setTitleJustification(TitledBorder.LEFT);
		second_panel.setBorder(secondTitle);
		gbl_second_layout = new GridBagLayout();
		second_panel.setLayout(gbl_second_layout);
		GridBagConstraints gbSecond = new GridBagConstraints();
		gbSecond.gridx = 0;
		gbSecond.gridy = 1;
		gbSecond.gridwidth = 1;
		gbSecond.gridheight = 1;
		gbSecond.weightx = 1.5;
		gbSecond.weighty = 1;
		gbSecond.fill = GridBagConstraints.BOTH;

		add(second_panel, gbSecond);

		btn_panel = new JPanel();
		gbl_btn_layout = new GridBagLayout();
		btn_panel.setLayout(gbl_btn_layout);
		edit_Btn = new JButton("修改設定");
		edit_Btn.setFont(DBSynConstans.textFont);
		btn_panel.add(edit_Btn);
		GridBagConstraints gbThird = new GridBagConstraints();
		gbThird.gridx = 0;
		gbThird.gridy = 2;
		gbThird.gridwidth = 1;
		gbThird.gridheight = 1;
		gbThird.fill = GridBagConstraints.BOTH;

		add(btn_panel, gbThird);
		edit_Btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});

	}

	public void loadData() {
		// 讀取存檔
		loadDataFromFile();
		// 產生來源下拉選單
		genSrcColumn();
		// 產生目標下拉選單
		genDestColumn();
		// 設定所有欄位為不可編輯
		disableAll();
	}

	public void loadAllData() {
		// 讀取存檔
		loadDataFromFile();
		// 產生來源下拉選單
		genSrcColumn();
		// 連線資料庫取得目標欄位
		getDestColumnsFromDB();
		// 產生目標下拉選單
		genDestColumn();
		// 設定所有欄位為不可編輯
		disableAll();
	}

	public void loadDataFromFile() {
		try {
			SystemConfigUtil systemConfigUtil = new SystemConfigUtil(DBSynConstans.mainproperty);
			String timeUp_folder = systemConfigUtil.get("timeUp.folder");
			String timeDown_folder = systemConfigUtil.get("timeDown.folder");
			// 加密檔案
			File timeUp_file = new File(timeUp_folder);
			File timeDown_file = new File(timeDown_folder);
			if (timeUp_file.exists()) {
				// 解密暫存檔
				File timeUpDecode = new File(timeUp_file.getParent() + File.separator
						+ CommonUtil.getFileNameWithOutExtension(timeUp_file) + "_decode.txt");
				CommonUtil.decrypt(timeUp_file.getPath(), DBSynConstans.edit_pw);
				if (timeUpDecode.exists()) {
					CommonUtil.readTimeUpFile(timeUpDecode);
					timeUpDecode.delete();
				}
			}
			if (timeDown_file.exists()) {
				File timeDownDecode = new File(timeDown_file.getParent() + File.separator
						+ CommonUtil.getFileNameWithOutExtension(timeDown_file) + "_decode.txt");
				CommonUtil.decrypt(timeDown_file.getPath(), DBSynConstans.edit_pw);
				if (timeDownDecode.exists()) {
					CommonUtil.readTimeDownFile(timeDownDecode);
					timeDownDecode.delete();
				}
			}
		} catch (Exception e) {
			logger.error("error", e);
		}
	}

	public boolean getDestColumnsFromDB() {
		boolean result = false;
		if (DBUtil.checkAllNotEmpty(DBSynConstans.destDBInfo)) {
			DBUtil.getDestColumnsFromDB();
			if (DBSynConstans.destColumns == null || DBSynConstans.destColumns.size() == 0) {
				JOptionPane.showMessageDialog(this, "目標資料庫設定不正確，請檢查資料庫設定", "資料庫連線異常", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, "目標資料庫設定不正確，請檢查資料庫設定", "資料庫連線異常", JOptionPane.ERROR_MESSAGE);
		}
		return result;
	}

	public void genSrcColumn() {
		if (DBSynConstans.srcColumns != null) {
			if (srcUpJComboxs == null) {
				srcUpJComboxs = new ArrayList<JComboBox<String>>();
			}
			if (srcDownJComboxs == null) {
				srcDownJComboxs = new ArrayList<JComboBox<String>>();
			}
			String previosSrc = "";
			for (int i = 0; i < totalColumnNum; i++) {
				GridBagConstraints gb = new GridBagConstraints();
				JLabel srcUp_Label = new JLabel("來源時間欄位:");
				srcUp_Label.setFont(DBSynConstans.titleFont);
				gb.insets = new Insets(2, 3, 2, 3);
				gb.gridx = 0;
				gb.gridy = i * 2;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 2;
				first_panel.add(srcUp_Label, gb);
				JComboBox<String> srcUpJComboBox = CommonUtil.createColumnsJCombox("src");
				srcUpJComboBox.setFont(DBSynConstans.textFont);
				srcUpJComboxs.add(srcUpJComboBox);
				gb.gridx++;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 1;
				first_panel.add(srcUpJComboBox, gb);

				JLabel srcDown_Label = new JLabel("來源時間欄位:");
				srcDown_Label.setFont(DBSynConstans.titleFont);
				gb.gridx = 0;
				gb.gridy = i * 2;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 2;
				second_panel.add(srcDown_Label, gb);
				JComboBox<String> srcDownJComboBox = CommonUtil.createColumnsJCombox("src");
				srcDownJComboBox.setFont(DBSynConstans.textFont);
				srcDownJComboxs.add(srcDownJComboBox);
				gb.gridx++;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 1;
				second_panel.add(srcDownJComboBox, gb);

				TransferBean transfeBean = null;
				if (DBSynConstans.timeUpList != null && i < DBSynConstans.timeUpList.size()
						&& DBSynConstans.timeUpList.get(i) != null) {
					transfeBean = DBSynConstans.timeUpList.get(i);
					if (!"".equals(transfeBean.getSrcColumn())
							&& !DBSynConstans.defaultJComboBoxText.equals(transfeBean.getDestTimeYMDFormat())
							&& !DBSynConstans.defaultJComboBoxText.equals(transfeBean.getDestTimeHMSFormat())) {
						srcUpJComboBox.setSelectedItem(transfeBean.getSrcColumn());
					}
				}
				if (DBSynConstans.timeDownList != null && i < DBSynConstans.timeDownList.size()
						&& DBSynConstans.timeDownList.get(i) != null) {
					transfeBean = DBSynConstans.timeDownList.get(i);
					if ((!"".equals(transfeBean.getSrcColumn())
							&& !DBSynConstans.defaultJComboBoxText.equals(transfeBean.getDestTimeYMDFormat()))
							|| (!DBSynConstans.defaultJComboBoxText.equals(transfeBean.getSrcColumn())
									&& !DBSynConstans.defaultJComboBoxText
											.equals(transfeBean.getDestTimeHMSFormat()))) {
						if (!previosSrc.equals(transfeBean.getSrcColumn())) {
							srcDownJComboBox.setSelectedItem(transfeBean.getSrcColumn());
							previosSrc = transfeBean.getSrcColumn();
						}
					}
				}
			}
		} else {
			logger.info("srcColumns is null");
		}
	}

	public void genDestColumn() {
		if (DBSynConstans.destColumns != null) {
			if (destUpJComboxs == null) {
				destUpJComboxs = new ArrayList<JComboBox<String>>();
			}
			if (destUpYMDFormatJComboxs == null) {
				destUpYMDFormatJComboxs = new ArrayList<JComboBox<String>>();
			}
			if (destUpHMSFormatJComboxs == null) {
				destUpHMSFormatJComboxs = new ArrayList<JComboBox<String>>();
			}
			if (destDownYMDJComboxs == null) {
				destDownYMDJComboxs = new ArrayList<JComboBox<String>>();
			}
			if (destDownHMSJComboxs == null) {
				destDownHMSJComboxs = new ArrayList<JComboBox<String>>();
			}
			if (destDownYMDFormatJComboxs == null) {
				destDownYMDFormatJComboxs = new ArrayList<JComboBox<String>>();
			}
			if (destDownHMSFormatJComboxs == null) {
				destDownHMSFormatJComboxs = new ArrayList<JComboBox<String>>();
			}
			for (int i = 0; i < totalColumnNum; i++) {
				GridBagConstraints gb = new GridBagConstraints();
				JLabel dest_Label = new JLabel("目標時間欄位:");
				dest_Label.setFont(DBSynConstans.titleFont);
				gb.insets = new Insets(2, 3, 2, 3);
				gb.gridx = 2;
				gb.gridy = i * 2;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 1;
				first_panel.add(dest_Label, gb);

				JComboBox<String> destUpJComboBox = CommonUtil.createColumnsJCombox("dest");
				destUpJComboBox.setFont(DBSynConstans.textFont);
				destUpJComboxs.add(destUpJComboBox);
				gb.gridx++;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 1;
				first_panel.add(destUpJComboBox, gb);

				JLabel ymd_Label = new JLabel("年月日時間格式:");
				ymd_Label.setFont(DBSynConstans.titleFont);
				gb.gridx++;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 1;
				first_panel.add(ymd_Label, gb);

				JComboBox<String> destUpYMDFormatJComboBox = CommonUtil.createTimeYMDFormatJCombox();
				destUpYMDFormatJComboBox.setFont(DBSynConstans.textFont);
				gb.gridx++;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 1;
				destUpYMDFormatJComboxs.add(destUpYMDFormatJComboBox);
				first_panel.add(destUpYMDFormatJComboBox, gb);

				JLabel hms_Label = new JLabel("時分秒時間格式:");
				hms_Label.setFont(DBSynConstans.titleFont);
				gb.gridx = 4;
				gb.gridy = i * 2 + 1;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 1;
				first_panel.add(hms_Label, gb);

				JComboBox<String> destUpHMSFormatJComboBox = CommonUtil.createTimeHMSFormatJCombox();
				destUpHMSFormatJComboBox.setFont(DBSynConstans.textFont);
				gb.gridx = 5;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 1;
				destUpHMSFormatJComboxs.add(destUpHMSFormatJComboBox);
				if (DBSynConstans.timeUpList != null && i < DBSynConstans.timeUpList.size()
						&& DBSynConstans.timeUpList.get(i) != null) {
					TransferBean transferBean = DBSynConstans.timeUpList.get(i);
					if (!"".equals(transferBean.getSrcColumn())
							&& !DBSynConstans.defaultJComboBoxText.equals(transferBean.getDestTimeYMDFormat())
							&& !DBSynConstans.defaultJComboBoxText.equals(transferBean.getDestTimeHMSFormat())) {
						destUpJComboBox.setSelectedItem(transferBean.getDestColumn());
						destUpYMDFormatJComboBox.setSelectedItem(transferBean.getDestTimeYMDFormat());
						destUpHMSFormatJComboBox.setSelectedItem(transferBean.getDestTimeHMSFormat());
					}
				}
				first_panel.add(destUpHMSFormatJComboBox, gb);
			}
			// -------------down--------------------
			for (int j = 0; j < totalColumnNum; j++) {
				GridBagConstraints gbDown = new GridBagConstraints();
				JLabel destYMD_Label = new JLabel("目標時間欄位(年月日):");
				destYMD_Label.setFont(DBSynConstans.titleFont);
				gbDown.insets = new Insets(2, 3, 2, 3);
				gbDown.gridx = 2;
				gbDown.gridy = j * 2;
				gbDown.gridwidth = 1;
				gbDown.weightx = 0.1;
				gbDown.weighty = 0;
				second_panel.add(destYMD_Label, gbDown);

				JComboBox<String> destDownYMDJComboBox = CommonUtil.createColumnsJCombox("dest");
				destDownYMDJComboBox.setFont(DBSynConstans.textFont);
				gbDown.gridx++;
				gbDown.gridwidth = 1;
				gbDown.weightx = 0.1;
				gbDown.weighty = 1;
				destDownYMDJComboxs.add(destDownYMDJComboBox);
				second_panel.add(destDownYMDJComboBox, gbDown);

				JLabel ymdFormat_Label = new JLabel("時間格式:");
				ymdFormat_Label.setFont(DBSynConstans.titleFont);
				gbDown.gridx++;
				gbDown.gridwidth = 1;
				gbDown.weightx = 0.1;
				gbDown.weighty = 0;
				second_panel.add(ymdFormat_Label, gbDown);

				JComboBox<String> destDownYMDFormatJComboBox = CommonUtil.createTimeYMDFormatJCombox();
				destDownYMDFormatJComboBox.setFont(DBSynConstans.textFont);
				gbDown.gridx++;
				gbDown.gridwidth = 1;
				gbDown.weightx = 0.1;
				gbDown.weighty = 1;
				destDownYMDFormatJComboxs.add(destDownYMDFormatJComboBox);
				second_panel.add(destDownYMDFormatJComboBox, gbDown);

				JLabel destHms_Label = new JLabel("目標時間欄位(時分秒):");
				destHms_Label.setFont(DBSynConstans.titleFont);
				gbDown.gridx = 2;
				gbDown.gridy = j * 2 + 1;
				gbDown.gridwidth = 1;
				gbDown.weightx = 0.1;
				gbDown.weighty = 1;
				second_panel.add(destHms_Label, gbDown);

				JComboBox<String> destDownHMSJComboBox = CommonUtil.createColumnsJCombox("dest");
				destDownHMSJComboBox.setFont(DBSynConstans.textFont);
				gbDown.gridx++;
				gbDown.gridwidth = 1;
				gbDown.weightx = 0.1;
				gbDown.weighty = 1;
				destDownHMSJComboxs.add(destDownHMSJComboBox);
				second_panel.add(destDownHMSJComboBox, gbDown);

				JLabel hmsFormat_Label = new JLabel("時間格式:");
				hmsFormat_Label.setFont(DBSynConstans.titleFont);
				gbDown.gridx++;
				gbDown.gridwidth = 1;
				gbDown.weightx = 0.1;
				gbDown.weighty = 1;
				second_panel.add(hmsFormat_Label, gbDown);

				JComboBox<String> destDownHMSFormatJComboBox = CommonUtil.createTimeHMSFormatJCombox();
				destDownHMSFormatJComboBox.setFont(DBSynConstans.textFont);
				gbDown.gridx++;
				gbDown.gridwidth = 1;
				gbDown.weightx = 0.1;
				gbDown.weighty = 1;
				destDownHMSFormatJComboxs.add(destDownHMSFormatJComboBox);
				second_panel.add(destDownHMSFormatJComboBox, gbDown);
				if (DBSynConstans.timeDownList != null && j < DBSynConstans.timeDownList.size()
						&& DBSynConstans.timeDownList.get(j) != null) {
					int num = j;
					if (j == 1) {
						num = j + 1;
					}
					TransferBean transferOneBean = DBSynConstans.timeDownList.get(num);
					if (DBSynConstans.timeDown.equals(transferOneBean.getType())
							&& !"".equals(transferOneBean.getSrcColumn())
							&& !DBSynConstans.defaultJComboBoxText.equals(transferOneBean.getDestTimeYMDFormat())) {
						destDownYMDJComboBox.setSelectedItem(transferOneBean.getDestColumn());
						destDownYMDFormatJComboBox.setSelectedItem(transferOneBean.getDestTimeYMDFormat());
					}
					TransferBean transferTwoBean = DBSynConstans.timeDownList.get(++num);
					if (DBSynConstans.timeDown.equals(transferTwoBean.getType())
							&& !"".equals(transferTwoBean.getSrcColumn())
							&& !DBSynConstans.defaultJComboBoxText.equals(transferTwoBean.getDestTimeHMSFormat())) {
						destDownHMSJComboBox.setSelectedItem(transferTwoBean.getDestColumn());
						destDownHMSFormatJComboBox.setSelectedItem(transferTwoBean.getDestTimeHMSFormat());
					}
				}

			}
		} else {
			logger.info("destColumns is null");
		}
	}

	public void save() {
		try {
			SystemConfigUtil systemConfigUtil = new SystemConfigUtil(DBSynConstans.mainproperty);
			String timeUp_folder = systemConfigUtil.get("timeUp.folder");
			File timeUp_file = new File(timeUp_folder);
			if (timeUp_file.exists()) {
				timeUp_file.delete();
			}
			String timeDown_folder = systemConfigUtil.get("timeDown.folder");
			File timeDown_file = new File(timeDown_folder);
			if (timeDown_file.exists()) {
				timeDown_file.delete();
			}
			FileWriter fwUp = new FileWriter(timeUp_file);
			FileWriter fwDown = new FileWriter(timeDown_file);
			if (srcUpJComboxs != null && srcDownJComboxs != null && destUpJComboxs != null
					&& destUpYMDFormatJComboxs != null && destUpHMSFormatJComboxs != null && destDownYMDJComboxs != null
					&& destDownYMDFormatJComboxs != null && destDownHMSJComboxs != null
					&& destDownHMSFormatJComboxs != null) {
				String sign = ",";
				// up
				for (int i = 0; i < srcUpJComboxs.size(); i++) {

					JComboBox<String> srcUp = srcUpJComboxs.get(i);
					String srcUpColumn = (String) srcUp.getSelectedItem();

					JComboBox<String> destUp = destUpJComboxs.get(i);
					String destUpColumn = (String) destUp.getSelectedItem();

					JComboBox<String> destUpYMD = destUpYMDFormatJComboxs.get(i);
					String destUpYMDFormat = (String) destUpYMD.getSelectedItem();

					JComboBox<String> destUpHMS = destUpHMSFormatJComboxs.get(i);
					String destUpHMSFormat = (String) destUpHMS.getSelectedItem();
					fwUp.append(srcUpColumn + sign + destUpColumn + sign + destUpYMDFormat + sign + destUpHMSFormat);
					fwUp.append("\r\n");
				}
				fwUp.flush();
				fwUp.close();

				// down
				for (int i = 0; i < srcDownJComboxs.size(); i++) {

					JComboBox<String> srcDown = srcDownJComboxs.get(i);
					String srcDownColumn = (String) srcDown.getSelectedItem();

					JComboBox<String> destDownYMD = destDownYMDJComboxs.get(i);
					String destDownYMDColumn = (String) destDownYMD.getSelectedItem();

					JComboBox<String> destDownYMDFormat = destDownYMDFormatJComboxs.get(i);
					String destYMDHMSFormat = (String) destDownYMDFormat.getSelectedItem();

					JComboBox<String> destDownHMS = destDownHMSJComboxs.get(i);
					String destDownHMSColumn = (String) destDownHMS.getSelectedItem();

					JComboBox<String> destDownHMSFormat = destDownHMSFormatJComboxs.get(i);
					String destDownHMSColumnFormat = (String) destDownHMSFormat.getSelectedItem();

					fwDown.append(srcDownColumn + sign + destDownYMDColumn + sign + destYMDHMSFormat + sign
							+ destDownHMSColumn + sign + destDownHMSColumnFormat);
					fwDown.append("\r\n");
				}
				fwDown.flush();
				fwDown.close();
				CommonUtil.encrypt(timeDown_folder, DBSynConstans.edit_pw);
			} else {

			}
		} catch (Exception e) {
			logger.error("TimeSetting save ", e);
		}
		disableAll();
		isEdit = false;
	}

	public void enableAll() {
		logger.info("breakpoint");
		if (srcUpJComboxs != null && srcDownJComboxs != null && destUpJComboxs != null
				&& destUpYMDFormatJComboxs != null && destUpHMSFormatJComboxs != null && destDownYMDJComboxs != null
				&& destDownYMDFormatJComboxs != null && destDownHMSJComboxs != null
				&& destDownHMSFormatJComboxs != null) {
			logger.info("breakpoint3");
			// up
			for (int i = 0; i < srcUpJComboxs.size(); i++) {

				JComboBox<String> srcUp = srcUpJComboxs.get(i);
				srcUp.setEnabled(true);

				JComboBox<String> destUp = destUpJComboxs.get(i);
				destUp.setEnabled(true);

				JComboBox<String> destUpYMD = destUpYMDFormatJComboxs.get(i);
				destUpYMD.setEnabled(true);

				JComboBox<String> destUpHMS = destUpHMSFormatJComboxs.get(i);
				destUpHMS.setEnabled(true);
			}

			// down
			for (int i = 0; i < srcDownJComboxs.size(); i++) {

				JComboBox<String> srcDown = srcDownJComboxs.get(i);
				srcDown.setEnabled(true);

				JComboBox<String> destDownYMD = destDownYMDJComboxs.get(i);
				destDownYMD.setEnabled(true);

				JComboBox<String> destDownYMDFormat = destDownYMDFormatJComboxs.get(i);
				destDownYMDFormat.setEnabled(true);

				JComboBox<String> destDownHMS = destDownHMSJComboxs.get(i);
				destDownHMS.setEnabled(true);

				JComboBox<String> destDownHMSFormat = destDownHMSFormatJComboxs.get(i);
				destDownHMSFormat.setEnabled(true);

			}
			edit_Btn.setEnabled(true);
		}
	}

	public void disableAll() {
		if (srcUpJComboxs != null && srcDownJComboxs != null && destUpJComboxs != null
				&& destUpYMDFormatJComboxs != null && destUpHMSFormatJComboxs != null && destDownYMDJComboxs != null
				&& destDownYMDFormatJComboxs != null && destDownHMSJComboxs != null
				&& destDownHMSFormatJComboxs != null) {
			// up
			for (int i = 0; i < srcUpJComboxs.size(); i++) {

				JComboBox<String> srcUp = srcUpJComboxs.get(i);
				srcUp.setEnabled(false);

				JComboBox<String> destUp = destUpJComboxs.get(i);
				destUp.setEnabled(false);

				JComboBox<String> destUpYMD = destUpYMDFormatJComboxs.get(i);
				destUpYMD.setEnabled(false);

				JComboBox<String> destUpHMS = destUpHMSFormatJComboxs.get(i);
				destUpHMS.setEnabled(false);
			}

			// down
			for (int i = 0; i < srcDownJComboxs.size(); i++) {

				JComboBox<String> srcDown = srcDownJComboxs.get(i);
				srcDown.setEnabled(false);

				JComboBox<String> destDownYMD = destDownYMDJComboxs.get(i);
				destDownYMD.setEnabled(false);

				JComboBox<String> destDownYMDFormat = destDownYMDFormatJComboxs.get(i);
				destDownYMDFormat.setEnabled(false);

				JComboBox<String> destDownHMS = destDownHMSJComboxs.get(i);
				destDownHMS.setEnabled(false);

				JComboBox<String> destDownHMSFormat = destDownHMSFormatJComboxs.get(i);
				destDownHMSFormat.setEnabled(false);

			}
			edit_Btn.setEnabled(false);
		}
	}

	public void askSave() {
		int answer = JOptionPane.showConfirmDialog(this, "尚未儲存設定，是否需要儲存", "儲存設定", JOptionPane.YES_NO_OPTION);
		if (answer == JOptionPane.YES_OPTION) {
			save();
		} else {
			isEdit = false;
			loadData();
		}
	}

	public void checkPassword() {
		if (CommonUtil.enterPassword(this)) {
			enableAll();
			isEdit = true;
		}
	}

	public int getSelectIndex(JComboBox<String> comboBox, String column) {
		int result = 0;
		for (int i = 0; i < comboBox.getItemCount(); i++) {
			String item = comboBox.getItemAt(i);
			if (column.equals(item)) {
				result = i;
			}
		}
		return result;
	}
}
