package view;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import bean.TransferBean;
import util.CommonUtil;
import util.Constans;
import util.DBUtil;
import util.LogManager;
import util.Logger;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
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
		firstTitle.setTitleFont(Constans.titleFont);
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
		secondTitle.setTitleFont(Constans.titleFont);
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
		edit_Btn.setFont(Constans.textFont);
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
		if (first_panel != null) {
			first_panel.removeAll();
		}
		if (second_panel != null) {
			second_panel.removeAll();
		}
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
		if (first_panel != null) {
			first_panel.removeAll();
		}
		if (second_panel != null) {
			second_panel.removeAll();
		}
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
			String timeUp_folder = Constans.timeUpPath;
			String timeDown_folder = Constans.timeDownPath;
			// 加密檔案
			File timeUp_file = new File(timeUp_folder);
			File timeDown_file = new File(timeDown_folder);
			if (timeUp_file.exists()) {
				// 解密暫存檔
				File timeUpDecode = new File(timeUp_file.getParent() + File.separator
						+ CommonUtil.getFileNameWithOutExtension(timeUp_file) + "_decode.txt");
				CommonUtil.decrypt(timeUp_file.getPath(), Constans.edit_pw);
				if (timeUpDecode.exists()) {
					CommonUtil.readTimeUpFile(timeUpDecode);
					timeUpDecode.delete();
				}
			}
			if (timeDown_file.exists()) {
				File timeDownDecode = new File(timeDown_file.getParent() + File.separator
						+ CommonUtil.getFileNameWithOutExtension(timeDown_file) + "_decode.txt");
				CommonUtil.decrypt(timeDown_file.getPath(), Constans.edit_pw);
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
		if (DBUtil.checkAllNotEmpty(Constans.destDBInfo)) {
			DBUtil.getDestColumnsFromDB();
			if (Constans.destColumns == null || Constans.destColumns.size() == 0) {
				JOptionPane.showMessageDialog(this, "目標資料庫設定不正確，請檢查資料庫設定", "資料庫連線異常", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, "目標資料庫設定不正確，請檢查資料庫設定", "資料庫連線異常", JOptionPane.ERROR_MESSAGE);
		}
		return result;
	}

	public void genSrcColumn() {
		if (Constans.srcColumns != null) {
			if (srcUpJComboxs == null) {
				srcUpJComboxs = new ArrayList<JComboBox<String>>();
			}
			if (srcDownJComboxs == null) {
				srcDownJComboxs = new ArrayList<JComboBox<String>>();
			}
			for (int i = 0; i < totalColumnNum; i++) {
				GridBagConstraints gb = new GridBagConstraints();
				JLabel srcUp_Label = new JLabel("來源時間欄位:");
				srcUp_Label.setFont(Constans.titleFont);
				gb.insets = new Insets(2, 3, 2, 3);
				gb.gridx = 0;
				gb.gridy = i * 2;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 2;
				first_panel.add(srcUp_Label, gb);
				JComboBox<String> srcUpJComboBox = CommonUtil.createColumnsJCombox("src");
				srcUpJComboBox.setFont(Constans.textFont);
				if (i < srcUpJComboxs.size() && srcUpJComboxs.get(i) != null) {
					srcUpJComboxs.set(i, srcUpJComboBox);
				} else {
					srcUpJComboxs.add(srcUpJComboBox);
				}
				gb.gridx++;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 1;
				first_panel.add(srcUpJComboBox, gb);

				JLabel srcDown_Label = new JLabel("來源時間欄位:");
				srcDown_Label.setFont(Constans.titleFont);
				gb.gridx = 0;
				gb.gridy = i * 2;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 2;
				second_panel.add(srcDown_Label, gb);
				JComboBox<String> srcDownJComboBox = CommonUtil.createColumnsJCombox("src");
				srcDownJComboBox.setFont(Constans.textFont);
				if (i < srcDownJComboxs.size() && srcDownJComboxs.get(i) != null) {
					srcDownJComboxs.set(i, srcDownJComboBox);
				} else {
					srcDownJComboxs.add(srcDownJComboBox);
				}
				gb.gridx++;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 1;
				second_panel.add(srcDownJComboBox, gb);

				TransferBean transfeBean = null;
				if (Constans.timeUpList != null && i < Constans.timeUpList.size()
						&& Constans.timeUpList.get(i) != null) {
					transfeBean = Constans.timeUpList.get(i);
					if (!"".equals(transfeBean.getSrcColumn())
							&& !Constans.defaultJComboBoxText.equals(transfeBean.getDestTimeYMDFormat())
							&& !Constans.defaultJComboBoxText.equals(transfeBean.getDestTimeHMSFormat())) {
						srcUpJComboBox.setSelectedItem(transfeBean.getSrcColumn());
					}
				}
				int num = i * 2;
				if (Constans.timeDownList != null && num < Constans.timeDownList.size()
						&& Constans.timeDownList.get(num) != null) {
					transfeBean = Constans.timeDownList.get(num);
					if ((!"".equals(transfeBean.getSrcColumn())
							&& !Constans.defaultJComboBoxText.equals(transfeBean.getDestTimeYMDFormat()))
							|| (!Constans.defaultJComboBoxText.equals(transfeBean.getSrcColumn())
									&& !Constans.defaultJComboBoxText.equals(transfeBean.getDestTimeHMSFormat()))) {
						srcDownJComboBox.setSelectedItem(transfeBean.getSrcColumn());
					}
				}
			}
		} else {
			logger.info("srcColumns is null");
		}
	}

	public void genDestColumn() {
		if (Constans.destColumns != null) {
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
				dest_Label.setFont(Constans.titleFont);
				gb.insets = new Insets(2, 3, 2, 3);
				gb.gridx = 2;
				gb.gridy = i * 2;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 1;
				first_panel.add(dest_Label, gb);

				JComboBox<String> destUpJComboBox = CommonUtil.createColumnsJCombox("dest");
				destUpJComboBox.setFont(Constans.textFont);
				if (i < destUpJComboxs.size() && destUpJComboxs.get(i) != null) {
					destUpJComboxs.set(i, destUpJComboBox);
				} else {
					destUpJComboxs.add(destUpJComboBox);
				}
				gb.gridx++;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 1;
				first_panel.add(destUpJComboBox, gb);

				JLabel ymd_Label = new JLabel("年月日時間格式:");
				ymd_Label.setFont(Constans.titleFont);
				gb.gridx++;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 1;
				first_panel.add(ymd_Label, gb);

				JComboBox<String> destUpYMDFormatJComboBox = CommonUtil.createTimeYMDFormatJCombox();
				destUpYMDFormatJComboBox.setFont(Constans.textFont);
				gb.gridx++;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 1;
				if (i < destUpYMDFormatJComboxs.size() && destUpYMDFormatJComboxs.get(i) != null) {
					destUpYMDFormatJComboxs.set(i, destUpYMDFormatJComboBox);
				} else {
					destUpYMDFormatJComboxs.add(destUpYMDFormatJComboBox);
				}
				first_panel.add(destUpYMDFormatJComboBox, gb);

				JLabel hms_Label = new JLabel("時分秒時間格式:");
				hms_Label.setFont(Constans.titleFont);
				gb.gridx = 4;
				gb.gridy = i * 2 + 1;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 1;
				first_panel.add(hms_Label, gb);

				JComboBox<String> destUpHMSFormatJComboBox = CommonUtil.createTimeHMSFormatJCombox();
				destUpHMSFormatJComboBox.setFont(Constans.textFont);
				gb.gridx = 5;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 1;
				if (i < destUpHMSFormatJComboxs.size() && destUpHMSFormatJComboxs.get(i) != null) {
					destUpHMSFormatJComboxs.set(i, destUpHMSFormatJComboBox);
				} else {
					destUpHMSFormatJComboxs.add(destUpHMSFormatJComboBox);
				}
				if (Constans.timeUpList != null && i < Constans.timeUpList.size()
						&& Constans.timeUpList.get(i) != null) {
					TransferBean transferBean = Constans.timeUpList.get(i);
					if (!"".equals(transferBean.getSrcColumn())
							&& !Constans.defaultJComboBoxText.equals(transferBean.getDestTimeYMDFormat())
							&& !Constans.defaultJComboBoxText.equals(transferBean.getDestTimeHMSFormat())) {
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
				destYMD_Label.setFont(Constans.titleFont);
				gbDown.insets = new Insets(2, 3, 2, 3);
				gbDown.gridx = 2;
				gbDown.gridy = j * 2;
				gbDown.gridwidth = 1;
				gbDown.weightx = 0.1;
				gbDown.weighty = 0;
				second_panel.add(destYMD_Label, gbDown);

				JComboBox<String> destDownYMDJComboBox = CommonUtil.createColumnsJCombox("dest");
				destDownYMDJComboBox.setFont(Constans.textFont);
				gbDown.gridx++;
				gbDown.gridwidth = 1;
				gbDown.weightx = 0.1;
				gbDown.weighty = 1;
				if (j < destDownYMDJComboxs.size() && destDownYMDJComboxs.get(j) != null) {
					destDownYMDJComboxs.set(j, destDownYMDJComboBox);
				} else {
					destDownYMDJComboxs.add(destDownYMDJComboBox);
				}
				second_panel.add(destDownYMDJComboBox, gbDown);

				JLabel ymdFormat_Label = new JLabel("時間格式:");
				ymdFormat_Label.setFont(Constans.titleFont);
				gbDown.gridx++;
				gbDown.gridwidth = 1;
				gbDown.weightx = 0.1;
				gbDown.weighty = 0;
				second_panel.add(ymdFormat_Label, gbDown);

				JComboBox<String> destDownYMDFormatJComboBox = CommonUtil.createTimeYMDFormatJCombox();
				destDownYMDFormatJComboBox.setFont(Constans.textFont);
				gbDown.gridx++;
				gbDown.gridwidth = 1;
				gbDown.weightx = 0.1;
				gbDown.weighty = 1;
				if (j < destDownYMDFormatJComboxs.size() && destDownYMDFormatJComboxs.get(j) != null) {
					destDownYMDFormatJComboxs.set(j, destDownYMDFormatJComboBox);
				} else {
					destDownYMDFormatJComboxs.add(destDownYMDFormatJComboBox);
				}
				second_panel.add(destDownYMDFormatJComboBox, gbDown);

				JLabel destHms_Label = new JLabel("目標時間欄位(時分秒):");
				destHms_Label.setFont(Constans.titleFont);
				gbDown.gridx = 2;
				gbDown.gridy = j * 2 + 1;
				gbDown.gridwidth = 1;
				gbDown.weightx = 0.1;
				gbDown.weighty = 1;
				second_panel.add(destHms_Label, gbDown);

				JComboBox<String> destDownHMSJComboBox = CommonUtil.createColumnsJCombox("dest");
				destDownHMSJComboBox.setFont(Constans.textFont);
				gbDown.gridx++;
				gbDown.gridwidth = 1;
				gbDown.weightx = 0.1;
				gbDown.weighty = 1;
				if (j < destDownHMSJComboxs.size() && destDownHMSJComboxs.get(j) != null) {
					destDownHMSJComboxs.set(j, destDownHMSJComboBox);
				} else {
					destDownHMSJComboxs.add(destDownHMSJComboBox);
				}
				second_panel.add(destDownHMSJComboBox, gbDown);

				JLabel hmsFormat_Label = new JLabel("時間格式:");
				hmsFormat_Label.setFont(Constans.titleFont);
				gbDown.gridx++;
				gbDown.gridwidth = 1;
				gbDown.weightx = 0.1;
				gbDown.weighty = 1;
				second_panel.add(hmsFormat_Label, gbDown);

				JComboBox<String> destDownHMSFormatJComboBox = CommonUtil.createTimeHMSFormatJCombox();
				destDownHMSFormatJComboBox.setFont(Constans.textFont);
				gbDown.gridx++;
				gbDown.gridwidth = 1;
				gbDown.weightx = 0.1;
				gbDown.weighty = 1;
				if (j < destDownHMSFormatJComboxs.size() && destDownHMSFormatJComboxs.get(j) != null) {
					destDownHMSFormatJComboxs.set(j, destDownHMSFormatJComboBox);
				} else {
					destDownHMSFormatJComboxs.add(destDownHMSFormatJComboBox);
				}
				second_panel.add(destDownHMSFormatJComboBox, gbDown);
				if (Constans.timeDownList != null && j < Constans.timeDownList.size()
						&& Constans.timeDownList.get(j) != null) {
					int num = j;
					if (j == 1) {
						num = j + 1;
					}
					TransferBean transferOneBean = Constans.timeDownList.get(num);
					if (Constans.timeDown.equals(transferOneBean.getType())
							&& !"".equals(transferOneBean.getSrcColumn())
							&& !Constans.defaultJComboBoxText.equals(transferOneBean.getDestTimeYMDFormat())) {
						destDownYMDJComboBox.setSelectedItem(transferOneBean.getDestColumn());
						destDownYMDFormatJComboBox.setSelectedItem(transferOneBean.getDestTimeYMDFormat());
					}
					TransferBean transferTwoBean = Constans.timeDownList.get(++num);
					if (Constans.timeDown.equals(transferTwoBean.getType())
							&& !"".equals(transferTwoBean.getSrcColumn())
							&& !Constans.defaultJComboBoxText.equals(transferTwoBean.getDestTimeHMSFormat())) {
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
			String timeUp_folder = Constans.timeUpPath;
			File timeUp_file = new File(timeUp_folder);
			if (timeUp_file.exists()) {
				timeUp_file.delete();
			}
			String timeDown_folder = Constans.timeDownPath;
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
				CommonUtil.encrypt(timeUp_folder, Constans.edit_pw);
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
				CommonUtil.encrypt(timeDown_folder, Constans.edit_pw);
				logger.info("時間格式轉換 存檔完畢");
			} else {

			}
		} catch (Exception e) {
			logger.error("TimeSetting save ", e);
		}
		disableAll();
		isEdit = false;
	}

	public void enableAll() {
		if (srcUpJComboxs != null && srcDownJComboxs != null && destUpJComboxs != null
				&& destUpYMDFormatJComboxs != null && destUpHMSFormatJComboxs != null && destDownYMDJComboxs != null
				&& destDownYMDFormatJComboxs != null && destDownHMSJComboxs != null
				&& destDownHMSFormatJComboxs != null) {
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
			logger.info("時間格式轉換 不需存檔  設定為不可編輯");
			loadData();
		}
	}

	public void checkPassword() {
		if (CommonUtil.enterPassword(this)) {
			enableAll();
			isEdit = true;
			logger.info("時間格式轉換 密碼驗證正確 已為可編輯");
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
