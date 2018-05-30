package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import util.CommonUtil;
import util.Constans;
import util.DBUtil;
import util.LogManager;
import util.Logger;
import util.SystemConfigUtil;

public class ConditionPanel extends JPanel {
	Logger logger = LogManager.getLogger(ConditionPanel.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -8998613237708584649L;
	JComboBox<String> condition_columns = null;
	JComboBox<String> pk_columns = null;

	JTextField sequence_text = null;
	JTextField condition_text = null;

	JButton edit_Btn = null;

	boolean isEdit = false;
	boolean isLoad=false;

	Dimension dimension = new Dimension(200, 30);

	File conditionFile = null;

	/**
	 * Create the panel.
	 */
	public ConditionPanel() {
		setLayout(new GridBagLayout());
		GridBagConstraints gbMain = new GridBagConstraints();

		TitledBorder tb_sequence = BorderFactory.createTitledBorder("資料同步頻率");
		tb_sequence.setTitleFont(Constans.titleFont);
		tb_sequence.setTitleJustification(TitledBorder.LEFT);

		JPanel sequence_panel = new JPanel();
		gbMain.gridx = 0;
		gbMain.gridy = 1;
		gbMain.gridwidth = 1;
		gbMain.gridheight = 1;
		gbMain.weightx = 1;
		gbMain.weighty = 0.2;
		gbMain.fill = GridBagConstraints.HORIZONTAL;
		add(sequence_panel, gbMain);
		sequence_panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		sequence_panel.setBorder(tb_sequence);

		JLabel sequence_label = new JLabel("間隔頻率:");
		sequence_label.setFont(Constans.titleFont);
		sequence_panel.add(sequence_label);

		sequence_text = new JTextField();
		sequence_text.setFont(Constans.textFont);
		sequence_panel.add(sequence_text);
		sequence_text.setColumns(10);

		JLabel sequence_second_label = new JLabel(" 秒");
		sequence_second_label.setFont(Constans.titleFont);
		sequence_panel.add(sequence_second_label);

		JPanel condition_panel = new JPanel();
		gbMain.gridx = 0;
		gbMain.gridy = 2;
		gbMain.gridwidth = 1;
		gbMain.gridheight = 1;
		gbMain.weightx = 1;
		gbMain.weighty = 0.2;
		gbMain.fill = GridBagConstraints.HORIZONTAL;
		add(condition_panel, gbMain);

		TitledBorder tb_condition = BorderFactory.createTitledBorder("程式判斷條件");
		tb_condition.setTitleFont(Constans.titleFont);
		tb_condition.setTitleJustification(TitledBorder.LEFT);

		condition_panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		condition_panel.setBorder(tb_condition);
		
		JLabel src_condition_column_label = new JLabel("來源判斷欄位:");
		src_condition_column_label.setFont(Constans.titleFont);
		condition_panel.add(src_condition_column_label);

		condition_columns = new JComboBox<String>();
		condition_columns.setFont(Constans.textFont);
		condition_columns.setMaximumSize(dimension);
		condition_panel.add(condition_columns);

		JLabel src_condition_label = new JLabel("判斷條件:");
		src_condition_label.setFont(Constans.titleFont);
		condition_panel.add(src_condition_label);

		condition_text = new JTextField();
		condition_text.setFont(Constans.textFont);
		condition_panel.add(condition_text);
		condition_text.setColumns(10);

		JLabel src_pk_label = new JLabel("來源主鍵:");
		src_pk_label.setFont(Constans.titleFont);
		condition_panel.add(src_pk_label);

		pk_columns = new JComboBox<String>();
		pk_columns.setFont(Constans.textFont);
		pk_columns.setMaximumSize(dimension);
		condition_panel.add(pk_columns);

		JPanel btn_panel = new JPanel();
		gbMain.gridx = 0;
		gbMain.gridy = 3;
		gbMain.gridwidth = 1;
		gbMain.gridheight = 1;
		gbMain.weightx = 1;
		gbMain.weighty = 0.1;
		gbMain.fill = GridBagConstraints.NONE;
		add(btn_panel, gbMain);
		btn_panel.setLayout(new GridLayout(1, 1, 0, 0));

		edit_Btn = new JButton("修改設定");
		edit_Btn.setFont(Constans.titleFont);
		btn_panel.add(edit_Btn);
		edit_Btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				 if(save()) {
					 disableAll();
				 }
			}
		});

	}

	public void init() {
		//重新載入db設定
		CommonUtil.reloadDB();
		
		if (Constans.conditionPath != null) {
			conditionFile = new File(Constans.conditionPath);
			//重新載入condition設定
			CommonUtil.reloadCondition();
		}
		//讀取來源資料表欄位
		DBUtil.getSrcColumnsFromDB();
		//將欄位填入下拉選單
		loadColumn();
		//設定選取選項
		setDefaultText();
		
		disableAll();
	}

	public boolean save() {
		boolean result=false;
		try {
			String check=checkColumns();
			if(!"".equals(check)) {
				CommonUtil.showMessageDialog(this, check, "欄位驗證錯誤", JOptionPane.ERROR_MESSAGE);
			}else {
				if(conditionFile!=null && conditionFile.exists()) {
					conditionFile.delete();
				}else {
					conditionFile=new File(Constans.conditionPath);
				}
				conditionFile.getParentFile().mkdirs();
				conditionFile.createNewFile();
				SystemConfigUtil systemConfigUtil = new SystemConfigUtil(conditionFile);
				
				systemConfigUtil.saveCondition("sequence", sequence_text.getText());
				systemConfigUtil.saveCondition("condition", condition_text.getText());
				systemConfigUtil.saveCondition("condition_column", (String) condition_columns.getSelectedItem());
				systemConfigUtil.saveCondition("pk_column", (String) pk_columns.getSelectedItem());
				
				//將檔案再加密
				CommonUtil.encrypt(conditionFile.getPath(), Constans.edit_pw);
				result=true;
			}
		} catch (Exception e) {
			logger.error("ConditionPanel save()", e);
		}
		return result;
	}

	public void disableAll() {
		condition_columns.setEnabled(false);
		pk_columns.setEnabled(false);
		sequence_text.setEditable(false);
		condition_text.setEditable(false);
		edit_Btn.setEnabled(false);
		isEdit=false;
	}

	public void enableAll() {
		condition_columns.setEnabled(true);
		pk_columns.setEnabled(true);
		sequence_text.setEditable(true);
		condition_text.setEditable(true);
		edit_Btn.setEnabled(true);
		isEdit=true;
	}

	public void checkPassword() {
		logger.info("驗證密碼中");
		boolean result = CommonUtil.enterPassword(this);
		if (result) {
			enableAll();
		}
		if (!isLoad) {
			isLoad = true;
		}
	}

	public void askSave() {
		int answer = CommonUtil.askSave();
		if (answer == JOptionPane.YES_OPTION) {
			if(!save()) {
			logger.info("條件判斷 存檔異常");
			}
		}else {
			logger.info("執行條件 不需存檔  設定為不可編輯");
		}
		setDefaultText();
		disableAll();
		isEdit=false;
	}

	public void loadColumn() {
		if(Constans.srcColumns!=null) {
			for (int i = 0; i < Constans.srcColumns.size(); i++) {
				String temp = Constans.srcColumns.get(i);
				if (condition_columns != null) {
					condition_columns.addItem(temp);
				}
				if (pk_columns != null) {
					pk_columns.addItem(temp);
				}
			}
		}else {
			CommonUtil.showMessageDialog(this, "來源資料庫設定不正確，請檢查資料庫設定", "資料庫連線異常", JOptionPane.ERROR_MESSAGE);

		}
	}
	
	public void setDefaultText() {
		condition_columns.setSelectedItem(Constans.condition_column);
		pk_columns.setSelectedItem(Constans.pk_column);
		sequence_text.setText(Constans.sequence_s);
		condition_text.setText(Constans.condition);
	}

	public String checkColumns() {
		String result="";
		if("".equals(sequence_text.getText())) {
			return "間隔頻率 未輸入";
		}else {
			try {
			int sequence=Integer.parseInt(sequence_text.getText());
			if(sequence<30) {
				logger.error("間隔頻率 不得小於30秒");
				return "間隔頻率 不得小於30秒";
			}
			}catch(NumberFormatException e) {
				logger.error("間隔頻率 輸入非數字 ");
				return "間隔頻率 輸入非數字";
			}
		}
		if("".equals(condition_text.getText())) {
			return "來源判斷條件未輸入";
		}
		if("".equals((String) condition_columns.getSelectedItem())) {
			return "來源判斷欄位未選擇";
		}
		if("".equals((String) pk_columns.getSelectedItem())) {
			return "主鍵欄位未選擇";
		}
		return result;
	}

}
