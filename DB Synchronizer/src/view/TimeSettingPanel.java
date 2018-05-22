package view;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bean.TransferBean;
import bean.TransferBean;
import util.CommonUtil;
import util.Constans;
import util.DBUtil;
import util.SystemConfigUtil;

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

	Font titleFont = new Font("微軟正黑體", Font.PLAIN, 24);
	Font textFont = new Font("微軟正黑體", Font.PLAIN, 18);

	/**
	 * Create the panel.
	 */
	public TimeSettingPanel() {

		GridBagLayout outter_layout = new GridBagLayout();
		// outter_layout.rowWeights = new double[] { 0.0, 1.0 };
		// outter_layout.columnWeights = new double[] { 1.0, 0.0 };
		setLayout(outter_layout);

		first_panel = new JPanel();
		TitledBorder firstTitle = new TitledBorder("時間格式轉換設定");
		firstTitle.setTitleFont(titleFont);
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
		secondTitle.setTitleFont(titleFont);
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

	public void init() {
		getDestColumns(first_panel);
		try {
			SystemConfigUtil systemConfigUtil = new SystemConfigUtil(Constans.mainproperty);
			String timeUp_folder = systemConfigUtil.get("timeUp.folder");
			String timeDown_folder = systemConfigUtil.get("timeDown.folder");
			File timeUp_file = new File(timeUp_folder);
			File timeDown_file = new File(timeDown_folder);
			if (timeUp_file.exists()) {
				CommonUtil.readTimeUpFile(timeUp_file);
			}
			if (timeDown_file.exists()) {
				CommonUtil.readTimeDownFile(timeDown_file);
			}
		} catch (Exception e) {

		}
		genSrcColumn();
		genDestColumn();
	}

	public boolean getDestColumns(JPanel jpanel) {
		boolean result = false;
		if (DBUtil.checkAllNotEmpty(Constans.destDBInfo)) {
			DBUtil.getDestColumnDetail();
			if (Constans.destColumns == null || Constans.destColumns.size() == 0) {
				JOptionPane.showMessageDialog(jpanel, "目標資料庫設定不正確，請檢查資料庫設定", "資料庫連線異常", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(jpanel, "目標資料庫設定不正確，請檢查資料庫設定", "資料庫連線異常", JOptionPane.ERROR_MESSAGE);
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
			String previosSrc="";
			for (int i = 0; i < totalColumnNum; i++) {
				GridBagConstraints gb = new GridBagConstraints();
				JLabel srcUp_Label = new JLabel("來源時間欄位:");
				srcUp_Label.setFont(titleFont);
				gb.insets = new Insets(2, 3, 2, 3);
				gb.gridx = 0;
				gb.gridy = i * 2;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 2;
				first_panel.add(srcUp_Label, gb);
				JComboBox<String> srcUpJComboBox = CommonUtil.createColumnsJCombox("src");
				srcUpJComboBox.setFont(textFont);
				srcUpJComboxs.add(srcUpJComboBox);
				gb.gridx++;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 1;
				first_panel.add(srcUpJComboBox, gb);

				JLabel srcDown_Label = new JLabel("來源時間欄位:");
				srcDown_Label.setFont(titleFont);
				gb.gridx = 0;
				gb.gridy = i * 2;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 2;
				second_panel.add(srcDown_Label, gb);
				JComboBox<String> srcDownJComboBox = CommonUtil.createColumnsJCombox("src");
				srcDownJComboBox.setFont(textFont);
				srcDownJComboxs.add(srcDownJComboBox);
				gb.gridx++;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 1;
				second_panel.add(srcDownJComboBox, gb);

				TransferBean transfeBean = null;
				logger.info("i=" + i);
				if (Constans.timeUpList != null && i < Constans.timeUpList.size()
						&& Constans.timeUpList.get(i) != null) {
					transfeBean = Constans.timeUpList.get(i);
					if (!"".equals(transfeBean.getSrcColumn())
							&& !Constans.defaultJComboBoxText.equals(transfeBean.getDestTimeYMDFormat())
							&& !Constans.defaultJComboBoxText.equals(transfeBean.getDestTimeHMSFormat())) {
						srcUpJComboBox.setSelectedItem(transfeBean.getSrcColumn());
					}
				}
				if (Constans.timeDownList != null && i < Constans.timeDownList.size()
						&& Constans.timeDownList.get(i) != null) {
					transfeBean = Constans.timeDownList.get(i);
					if ((!"".equals(transfeBean.getSrcColumn())
							&& !Constans.defaultJComboBoxText.equals(transfeBean.getDestTimeYMDFormat()))
							|| (!Constans.defaultJComboBoxText.equals(transfeBean.getSrcColumn())
									&& !Constans.defaultJComboBoxText.equals(transfeBean.getDestTimeHMSFormat()))) {
						if(!previosSrc.equals(transfeBean.getSrcColumn())) {
							srcDownJComboBox.setSelectedItem(transfeBean.getSrcColumn());
							previosSrc=transfeBean.getSrcColumn();
						}
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
				dest_Label.setFont(titleFont);
				gb.insets = new Insets(2, 3, 2, 3);
				gb.gridx = 2;
				gb.gridy = i * 2;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 1;
				first_panel.add(dest_Label, gb);

				JComboBox<String> destUpJComboBox = CommonUtil.createColumnsJCombox("dest");
				destUpJComboBox.setFont(textFont);
				destUpJComboxs.add(destUpJComboBox);
				gb.gridx++;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 1;
				first_panel.add(destUpJComboBox, gb);

				JLabel ymd_Label = new JLabel("年月日時間格式:");
				ymd_Label.setFont(titleFont);
				gb.gridx++;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 1;
				first_panel.add(ymd_Label, gb);

				JComboBox<String> destUpYMDFormatJComboBox = CommonUtil.createTimeYMDFormatJCombox();
				destUpYMDFormatJComboBox.setFont(textFont);
				gb.gridx++;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 1;
				destUpYMDFormatJComboxs.add(destUpYMDFormatJComboBox);
				first_panel.add(destUpYMDFormatJComboBox, gb);

				JLabel hms_Label = new JLabel("時分秒時間格式:");
				hms_Label.setFont(titleFont);
				gb.gridx = 4;
				gb.gridy = i * 2 + 1;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 1;
				first_panel.add(hms_Label, gb);

				JComboBox<String> destUpHMSFormatJComboBox = CommonUtil.createTimeHMSFormatJCombox();
				destUpHMSFormatJComboBox.setFont(textFont);
				gb.gridx = 5;
				gb.gridwidth = 1;
				gb.weightx = 0.1;
				gb.weighty = 1;
				destUpHMSFormatJComboxs.add(destUpHMSFormatJComboBox);
				first_panel.add(destUpHMSFormatJComboBox, gb);
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
			}
			// -------------down--------------------
			for (int j = 0; j < totalColumnNum; j++) {
				GridBagConstraints gbDown = new GridBagConstraints();
				JLabel destYMD_Label = new JLabel("目標時間欄位(年月日):");
				destYMD_Label.setFont(titleFont);
				gbDown.insets = new Insets(2, 3, 2, 3);
				gbDown.gridx = 2;
				gbDown.gridy = j * 2;
				gbDown.gridwidth = 1;
				gbDown.weightx = 0.1;
				gbDown.weighty = 0;
				second_panel.add(destYMD_Label, gbDown);

				JComboBox<String> destDownYMDJComboBox = CommonUtil.createColumnsJCombox("dest");
				destDownYMDJComboBox.setFont(textFont);
				gbDown.gridx++;
				gbDown.gridwidth = 1;
				gbDown.weightx = 0.1;
				gbDown.weighty = 1;
				destDownYMDJComboxs.add(destDownYMDJComboBox);
				second_panel.add(destDownYMDJComboBox, gbDown);

				JLabel ymdFormat_Label = new JLabel("時間格式:");
				ymdFormat_Label.setFont(titleFont);
				gbDown.gridx++;
				gbDown.gridwidth = 1;
				gbDown.weightx = 0.1;
				gbDown.weighty = 0;
				second_panel.add(ymdFormat_Label, gbDown);

				JComboBox<String> destDownYMDFormatJComboBox = CommonUtil.createTimeYMDFormatJCombox();
				destDownYMDFormatJComboBox.setFont(textFont);
				gbDown.gridx++;
				gbDown.gridwidth = 1;
				gbDown.weightx = 0.1;
				gbDown.weighty = 1;
				destDownYMDFormatJComboxs.add(destDownYMDFormatJComboBox);
				second_panel.add(destDownYMDFormatJComboBox, gbDown);

				JLabel destHms_Label = new JLabel("目標時間欄位(時分秒):");
				destHms_Label.setFont(titleFont);
				gbDown.gridx = 2;
				gbDown.gridy = j * 2 + 1;
				gbDown.gridwidth = 1;
				gbDown.weightx = 0.1;
				gbDown.weighty = 1;
				second_panel.add(destHms_Label, gbDown);

				JComboBox<String> destDownHMSJComboBox = CommonUtil.createColumnsJCombox("dest");
				destDownHMSJComboBox.setFont(textFont);
				gbDown.gridx++;
				gbDown.gridwidth = 1;
				gbDown.weightx = 0.1;
				gbDown.weighty = 1;
				destDownHMSJComboxs.add(destDownHMSJComboBox);
				second_panel.add(destDownHMSJComboBox, gbDown);

				JLabel hmsFormat_Label = new JLabel("時間格式:");
				hmsFormat_Label.setFont(titleFont);
				gbDown.gridx++;
				gbDown.gridwidth = 1;
				gbDown.weightx = 0.1;
				gbDown.weighty = 1;
				second_panel.add(hmsFormat_Label, gbDown);

				JComboBox<String> destDownHMSFormatJComboBox = CommonUtil.createTimeHMSFormatJCombox();
				destDownHMSFormatJComboBox.setFont(textFont);
				gbDown.gridx++;
				gbDown.gridwidth = 1;
				gbDown.weightx = 0.1;
				gbDown.weighty = 1;
				destDownHMSFormatJComboxs.add(destDownHMSFormatJComboBox);
				second_panel.add(destDownHMSFormatJComboBox, gbDown);
				if (Constans.timeDownList != null && j < Constans.timeDownList.size()
						&& Constans.timeDownList.get(j) != null) {
					int num=j;
					if(j==1) {
						num=j+1;
					}
					TransferBean transferOneBean = Constans.timeDownList.get(num);
					if (Constans.timeDown.equals(transferOneBean.getType()) && !"".equals(transferOneBean.getSrcColumn())
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
			SystemConfigUtil systemConfigUtil = new SystemConfigUtil(Constans.mainproperty);
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
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
