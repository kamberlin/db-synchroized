package view;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import util.LogManager;
import util.Logger;
import util.CommonUtil;
import util.Constans;
import util.DBUtil;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

public class ColumnSettingPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1466832287065379835L;

	private static Logger logger = LogManager.getLogger(ColumnSettingPanel.class);
	boolean isLoad = false;
	boolean isEdit = false;
	int columnLength = 0;

	ArrayList<JComboBox<String>> srcJComboxs = null;
	ArrayList<JComboBox<String>> destJComboxs = null;

	ArrayList<JTextField> srcJTextField = null;
	ArrayList<JTextField> destJTextField = null;
	JButton edit_Btn = null;

	JPanel src_panel = null;
	JPanel dest_panel = null;
	JPanel main_panel = null;
	JScrollPane srcScrollPanel = null;
	JScrollPane destScrollPanel = null;

	GridBagLayout gbl_dest_layout = null;
	GridBagLayout gbl_src_layout = null;

	/**
	 * Create the panel.
	 */
	public ColumnSettingPanel() {

		GridBagLayout outter_layout = new GridBagLayout();
		setLayout(outter_layout);

		srcScrollPanel = new JScrollPane();
		destScrollPanel = new JScrollPane();

		main_panel = new JPanel();
		main_panel.setLayout(new FlowLayout());

		TitledBorder tb_src = new TitledBorder("來源欄位設定");
		tb_src.setTitleFont(Constans.titleFont);
		tb_src.setTitleJustification(TitledBorder.LEFT);

		src_panel = new JPanel();

		src_panel.setBorder(tb_src);
		gbl_src_layout = new GridBagLayout();
		src_panel.setLayout(gbl_src_layout);

		JLabel src_noLabel = new JLabel("項次");
		src_noLabel.setFont(Constans.titleFont);
		JLabel src_columnLabel = new JLabel("欄位名稱");
		src_columnLabel.setFont(Constans.titleFont);
		JLabel src_inputLabel = new JLabel("自訂寫入值");
		src_inputLabel.setFont(Constans.titleFont);
		src_panel.add(src_noLabel);
		src_panel.add(src_columnLabel);
		src_panel.add(src_inputLabel);
		GridBagConstraints src_const = new GridBagConstraints();
		src_const.insets = new Insets(1, 3, 1, 3);
		src_const.gridx = 0;
		src_const.gridy = 0;
		src_const.gridwidth = 1;
		src_const.weightx = 0;
		src_const.weighty = 0;
		gbl_src_layout.setConstraints(src_noLabel, src_const);
		src_const.gridx = 1;
		src_const.gridy = 0;
		src_const.gridwidth = 1;
		src_const.weightx = 0;
		src_const.weighty = 0;
		gbl_src_layout.setConstraints(src_columnLabel, src_const);
		src_const.gridx = 2;
		src_const.gridy = 0;
		src_const.gridwidth = 1;
		src_const.weightx = 0;
		src_const.weighty = 0;
		gbl_src_layout.setConstraints(src_inputLabel, src_const);

		main_panel.add(src_panel);

		TitledBorder tb_dest = new TitledBorder("目標欄位設定");
		tb_dest.setTitleFont(Constans.titleFont);
		tb_dest.setTitleJustification(TitledBorder.LEFT);
		dest_panel = new JPanel();
		dest_panel.setBorder(tb_dest);
		gbl_dest_layout = new GridBagLayout();
		dest_panel.setLayout(gbl_dest_layout);

		JLabel dest_noLabel = new JLabel("項次");
		dest_noLabel.setFont(Constans.titleFont);
		JLabel dest_columnLabel = new JLabel("欄位名稱");
		dest_columnLabel.setFont(Constans.titleFont);
		JLabel dest_inputLabel = new JLabel("自訂寫入值");
		dest_inputLabel.setFont(Constans.titleFont);

		dest_panel.add(dest_noLabel);
		dest_panel.add(dest_columnLabel);
		dest_panel.add(dest_inputLabel);
		GridBagConstraints dest_const = new GridBagConstraints();
		dest_const.gridx = 0;
		dest_const.gridy = 0;
		dest_const.gridwidth = 1;
		dest_const.weightx = 0;
		dest_const.weighty = 0;
		gbl_dest_layout.setConstraints(dest_noLabel, dest_const);
		dest_const.gridx++;
		dest_const.gridy = 0;
		dest_const.gridwidth = 1;
		dest_const.weightx = 0;
		dest_const.weighty = 0;
		gbl_dest_layout.setConstraints(dest_columnLabel, dest_const);
		dest_const.gridx++;
		dest_const.gridy = 0;
		dest_const.gridwidth = 1;
		dest_const.weightx = 0;
		dest_const.weighty = 0;
		gbl_dest_layout.setConstraints(dest_inputLabel, dest_const);

		GridBagConstraints gbc_srcScrollPanel = new GridBagConstraints();
		gbc_srcScrollPanel.insets = new Insets(0, 0, 5, 5);
		gbc_srcScrollPanel.gridx = 0;
		gbc_srcScrollPanel.gridy = 0;
		gbc_srcScrollPanel.gridwidth = 1;
		gbc_srcScrollPanel.weightx = 2;
		gbc_srcScrollPanel.weighty = 1;
		gbc_srcScrollPanel.fill = GridBagConstraints.BOTH;
		gbc_srcScrollPanel.anchor = GridBagConstraints.NORTH;

		main_panel.add(dest_panel);
		srcScrollPanel.setViewportView(main_panel);
		add(srcScrollPanel, gbc_srcScrollPanel);
		edit_Btn = new JButton("修改設定");
		edit_Btn.setPreferredSize(new Dimension(300, 100));
		edit_Btn.setFont(Constans.textFont);
		GridBagConstraints gbc_btnPanel = new GridBagConstraints();
		gbc_btnPanel.insets = new Insets(0, 0, 5, 5);
		gbc_btnPanel.gridx = 2;
		gbc_btnPanel.gridy = 0;
		gbc_btnPanel.gridwidth = 1;
		gbc_btnPanel.weightx = 0.2;
		gbc_btnPanel.weighty = 1;
		gbc_btnPanel.fill = GridBagConstraints.NONE;
		gbc_btnPanel.anchor = GridBagConstraints.NORTH;
		add(edit_Btn, gbc_btnPanel);
		edit_Btn.setEnabled(false);
		edit_Btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
	}

	public void loadAllData() {
		getSrcColumnsFromDB();
		getDestColumnsFromDB();
		setColumnLength();
		genSrcColumn();
		genDestColumn();
		disableAll();
	}

	public boolean getDestColumnsFromDB() {
		boolean result = false;
		if (DBUtil.checkAllNotEmpty(Constans.destDBInfo)) {
			DBUtil.getDestColumnsFromDB();
			if (Constans.destColumns == null || Constans.destColumns.size() == 0) {
				JOptionPane.showMessageDialog(this, "目標資料庫設定不正確，請檢查資料庫設定", "資料庫連線異常", JOptionPane.ERROR_MESSAGE);
				logger.info("目標資料庫設定不正確，請檢查資料庫設定");
			}
		} else {
			JOptionPane.showMessageDialog(this, "目標資料庫設定不正確，請檢查資料庫設定", "資料庫連線異常", JOptionPane.ERROR_MESSAGE);
			logger.info("目標資料庫設定不正確，請檢查資料庫設定");
		}
		return result;
	}
	public boolean getSrcColumnsFromDB() {
		boolean result = false;
		if (DBUtil.checkAllNotEmpty(Constans.srcDBInfo)) {
			DBUtil.getSrcColumnsFromDB();
			if (Constans.srcColumns == null || Constans.srcColumns.size() == 0) {
				JOptionPane.showMessageDialog(this, "來源資料庫設定不正確，請檢查資料庫設定", "資料庫連線異常", JOptionPane.ERROR_MESSAGE);
				logger.info("來源資料庫設定不正確，請檢查資料庫設定");
			}
		} else {
			JOptionPane.showMessageDialog(this, "來源資料庫設定不正確，請檢查資料庫設定", "資料庫連線異常", JOptionPane.ERROR_MESSAGE);
			logger.info("來源資料庫設定不正確，請檢查資料庫設定");
		}
		return result;
	}

	public void setColumnLength() {
		int columnLength = 0;
		if (Constans.srcColumns != null) {
			columnLength = Constans.srcColumns.size();
		}
		if (Constans.destColumns != null) {
			if (columnLength < Constans.destColumns.size()) {
				columnLength = Constans.destColumns.size();
			}
		}
		this.columnLength = columnLength;
	}

	public void genSrcColumn() {
		try {
			if (Constans.srcColumns != null) {
				// 加密檔案
				File column_file = new File(Constans.columnSettingPath);
				// 解密檔案
				File columnDecode = new File(column_file.getParent() + File.separator
						+ CommonUtil.getFileNameWithOutExtension(column_file) + "_decode.txt");
				if (column_file.exists()) {
					CommonUtil.decrypt(column_file.getPath(), Constans.edit_pw);
					if (columnDecode.exists()) {
						CommonUtil.readColumnFile(columnDecode);
						columnDecode.delete();
					}
				}
				src_panel.removeAll();
				for (int i = 0; i < columnLength; i++) {
					if (srcJComboxs == null) {
						srcJComboxs = new ArrayList<JComboBox<String>>();
					}
					if (srcJTextField == null) {
						srcJTextField = new ArrayList<JTextField>();
					}
					JComboBox<String> tempJComboBox = new JComboBox<String>();
					tempJComboBox.setFont(Constans.textFont);
					JTextField textField = new JTextField();
					textField.setFont(Constans.textFont);
					textField.setColumns(10);
					for (int j = 0; j < Constans.srcColumns.size(); j++) {
						if (j == 0) {
							tempJComboBox.addItem("");
						}
						tempJComboBox.addItem(Constans.srcColumns.get(j));
					}
					if (Constans.columnList != null && i < Constans.columnList.size()
							&& Constans.columnList.get(i) != null) {
						tempJComboBox.setSelectedItem(Constans.columnList.get(i).getSrcColumn());
						textField.setText(Constans.columnList.get(i).getSrcContent());
					}
					tempJComboBox.setEnabled(false);
					textField.setEditable(false);
					JLabel noLabel = new JLabel("" + (i + 1));
					noLabel.setFont(Constans.textFont);
					if (i < srcJComboxs.size() && srcJComboxs.get(i) != null) {
						srcJComboxs.set(i, tempJComboBox);
					} else {
						srcJComboxs.add(tempJComboBox);
					}
					if (i < srcJTextField.size() && srcJTextField.get(i) != null) {
						srcJTextField.set(i, textField);
					} else {
						srcJTextField.add(textField);
					}
					src_panel.add(noLabel);
					GridBagConstraints src_const = new GridBagConstraints();
					src_const.insets = new Insets(1, 3, 1, 3);
					src_const.gridx = 0;
					src_const.gridy = 1 + i;
					src_const.gridwidth = 1;
					src_const.weightx = 0.1;
					src_const.weighty = 1;
					gbl_src_layout.setConstraints(noLabel, src_const);
					src_panel.add(tempJComboBox);
					src_const.gridx = 1;
					src_const.gridy = 1 + i;
					src_const.gridwidth = 1;
					src_const.weightx = 0.5;
					src_const.weighty = 1;
					gbl_src_layout.setConstraints(tempJComboBox, src_const);
					src_panel.add(textField);
					src_const.gridx = 2;
					src_const.gridy = 1 + i;
					src_const.gridwidth = 0;
					src_const.weightx = 0.3;
					src_const.weighty = 1;
					gbl_src_layout.setConstraints(textField, src_const);
				}
			} else {
				logger.info("srcColumns is null");
			}
		} catch (Exception e) {
			logger.error("ColumnSetting genSrcColumn error", e);
		}
	}

	public void genDestColumn() {
		if (Constans.destColumns != null) {
			dest_panel.removeAll();
			for (int i = 0; i < columnLength; i++) {
				if (destJComboxs == null) {
					destJComboxs = new ArrayList<JComboBox<String>>();
				}
				if (destJTextField == null) {
					destJTextField = new ArrayList<JTextField>();
				}
				JComboBox<String> tempJComboBox = new JComboBox<String>();
				tempJComboBox.setFont(Constans.textFont);
				JTextField textField = new JTextField();
				textField.setFont(Constans.textFont);
				textField.setColumns(10);
				for (int j = 0; j < Constans.destColumns.size(); j++) {
					if (j == 0) {
						tempJComboBox.addItem("");
					}
					tempJComboBox.addItem(Constans.destColumns.get(j));
				}
				if (Constans.columnList != null && i < Constans.columnList.size()
						&& Constans.columnList.get(i) != null) {
					tempJComboBox.setSelectedItem(Constans.columnList.get(i).getDestColumn());
					textField.setText(Constans.columnList.get(i).getDestContent());
				}
				tempJComboBox.setEnabled(false);
				textField.setEditable(false);
				JLabel noLabel = new JLabel("" + (i + 1));
				noLabel.setFont(Constans.textFont);
				if (i < destJComboxs.size() && destJComboxs.get(i) != null) {
					destJComboxs.set(i, tempJComboBox);
				} else {
					destJComboxs.add(tempJComboBox);
				}
				if (i < destJTextField.size() && destJTextField.get(i) != null) {
					destJTextField.set(i, textField);
				} else {
					destJTextField.add(textField);
				}
				dest_panel.add(noLabel);
				dest_panel.add(tempJComboBox);
				dest_panel.add(textField);
				GridBagConstraints dest_const = new GridBagConstraints();
				dest_const.insets = new Insets(1, 3, 1, 3);
				dest_const.gridx = 0;
				dest_const.gridy = 1 + i;
				dest_const.gridwidth = 1;
				dest_const.weightx = 0.1;
				dest_const.weighty = 1;
				gbl_dest_layout.setConstraints(noLabel, dest_const);
				dest_const.gridx++;
				dest_const.gridy = 1 + i;
				dest_const.gridwidth = 1;
				dest_const.weightx = 0.5;
				dest_const.weighty = 1;
				gbl_dest_layout.setConstraints(tempJComboBox, dest_const);
				dest_const.gridx++;
				dest_const.gridy = 1 + i;
				dest_const.gridwidth = 1;
				dest_const.weightx = 0.3;
				dest_const.weighty = 1;
				gbl_dest_layout.setConstraints(textField, dest_const);
			}
		} else {
			logger.info("destColumns is null");
		}

	}

	public void save() {
		if (srcJComboxs != null && destJTextField != null && destJComboxs != null && destJTextField != null) {
			try {
				File column_file = new File(Constans.columnSettingPath);
				if (column_file.exists()) {
					column_file.delete();
				}
				FileWriter fw = new FileWriter(column_file);
				String sign = ",";
				logger.info("srcComboBox length=" + srcJComboxs.size());
				for (int i = 0; i < srcJComboxs.size(); i++) {
					String srcColumn = null;
					String destColumn = null;
					String srcText = null;
					String destText = null;
					JComboBox<String> src = srcJComboxs.get(i);
					JComboBox<String> dest = destJComboxs.get(i);
					JTextField srcTextField = srcJTextField.get(i);
					JTextField destTextField = destJTextField.get(i);
					if (src != null) {
						srcColumn = (String) src.getSelectedItem();
					}
					if (dest != null) {
						destColumn = (String) dest.getSelectedItem();
					}
					if (srcTextField != null) {
						srcText = srcTextField.getText();
					}
					if (destTextField != null) {
						destText = destTextField.getText();
					}
					fw.append(srcColumn + sign + srcText + sign + destColumn + sign + destText);
					fw.append("\r\n");
				}
				fw.flush();
				fw.close();
				CommonUtil.encrypt(Constans.columnSettingPath, Constans.edit_pw);
			} catch (Exception e) {
				logger.error("ColumnSetting save error", e);
			}
		}
		disableAll();
	}

	public void checkPassword() {
		if (CommonUtil.enterPassword(this)) {
			enableAll();
		}
	}

	public void enableAll() {
		if (srcJComboxs != null) {
			for (int i = 0; i < srcJComboxs.size(); i++) {
				JComboBox<String> tempJCombo = srcJComboxs.get(i);
				tempJCombo.setEnabled(true);
			}
		}
		if (destJComboxs != null) {
			for (int i = 0; i < destJComboxs.size(); i++) {
				JComboBox<String> tempJCombo = destJComboxs.get(i);
				tempJCombo.setEnabled(true);
			}
		}
		if (srcJTextField != null) {
			for (int i = 0; i < srcJTextField.size(); i++) {
				JTextField tempField = srcJTextField.get(i);
				tempField.setEditable(true);
			}
		}
		if (destJTextField != null) {
			for (int i = 0; i < destJTextField.size(); i++) {
				JTextField tempField = destJTextField.get(i);
				tempField.setEditable(true);
			}
		}
		edit_Btn.setEnabled(true);
		isEdit = true;
	}

	public void disableAll() {
		if (srcJComboxs != null) {
			for (int i = 0; i < srcJComboxs.size(); i++) {
				JComboBox<String> tempJCombo = srcJComboxs.get(i);
				tempJCombo.setEnabled(false);
			}
		}
		if (destJComboxs != null) {
			for (int i = 0; i < destJComboxs.size(); i++) {
				JComboBox<String> tempJCombo = destJComboxs.get(i);
				tempJCombo.setEnabled(false);
			}
		}
		if (srcJTextField != null) {
			for (int i = 0; i < srcJTextField.size(); i++) {
				JTextField tempField = srcJTextField.get(i);
				tempField.setEditable(false);
			}
		}
		if (destJTextField != null) {
			for (int i = 0; i < destJTextField.size(); i++) {
				JTextField tempField = destJTextField.get(i);
				tempField.setEditable(false);
			}
		}
		edit_Btn.setEnabled(false);
		isEdit = false;
	}

	public void askSave() {
		int answer = JOptionPane.showConfirmDialog(this, "尚未儲存設定，是否需要儲存", "儲存設定", JOptionPane.YES_NO_OPTION);
		if (answer == JOptionPane.YES_OPTION) {
			save();
		} else {
			isEdit = false;
			loadAllData();
		}
	}

}
