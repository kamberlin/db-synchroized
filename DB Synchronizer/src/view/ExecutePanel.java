package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import util.LogManager;
import util.Logger;
import layout.TableLayout;
import util.CommonUtil;
import util.DBSynConstans;
import util.SystemConfigUtil;
import util.TransferThread;


public class ExecutePanel extends JPanel {
	boolean running = false;
	private static Logger logger = LogManager.getLogger(ExecutePanel.class);
	TransferThread transferThread = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = -4307266712759501962L;

	/**
	 * Create the panel.
	 */
	public ExecutePanel() {
		double sizeMain[][] = { { 0.99 }, { 0.20, 0.79 } };
		double sizeUp[][] = { { 0.3, 0.3, 0.2, 0.2 }, { 0.99 } };

		setLayout(new TableLayout(sizeMain));

		JPanel upPanel = new JPanel();
		upPanel.setLayout(new TableLayout(sizeUp));
		TitledBorder tb = new TitledBorder("服務控制列");
		tb.setTitleFont(DBSynConstans.titleFont);
		tb.setTitleJustification(TitledBorder.LEFT);
		upPanel.setBorder(tb);
		JLabel statusTitleLabel = new JLabel("服務狀態:");
		statusTitleLabel.setFont(DBSynConstans.titleFont);
		JLabel statusTextLabel = new JLabel("服務執行中!");
		statusTextLabel.setFont(DBSynConstans.titleFont);
		JButton startBtn = new JButton("啟動服務");
		startBtn.setFont(DBSynConstans.titleFont);
		JButton stopBtn = new JButton("停止服務");
		stopBtn.setFont(DBSynConstans.titleFont);
		upPanel.add(statusTitleLabel, "0,0");
		upPanel.add(statusTextLabel, "1,0");
		upPanel.add(startBtn, "2,0");
		upPanel.add(stopBtn, "3,0");
		JPanel downPanel = new JPanel();
		downPanel.setLayout(new GridLayout());
		JTextArea textArea = new JTextArea();
		textArea.setFont(DBSynConstans.textFont);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(400, 300));
		scrollPane.setViewportView(textArea);
		downPanel.add(scrollPane);
		add(upPanel, "0,0");
		add(downPanel, "0,1");

		if (!running) {
			stopBtn.setEnabled(false);
			statusTextLabel.setText("服務停止!");
		}
		startBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				running = true;
				stopBtn.setEnabled(true);
				startBtn.setEnabled(false);
				statusTextLabel.setText("服務執行中!");
				loadColumnSetting();
				transferThread = new TransferThread(textArea);
				transferThread.start();
			}
		});

		stopBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				running = false;
				stopBtn.setEnabled(false);
				startBtn.setEnabled(true);
				statusTextLabel.setText("服務停止!");
				transferThread.setRunning(false);
			}
		});

	}

	public void loadColumnSetting() {
		try {
			SystemConfigUtil systemConfigUtil = new SystemConfigUtil(DBSynConstans.mainproperty);
			String column_folder = systemConfigUtil.get("column_setting.folder");
			String timeUp_folder = systemConfigUtil.get("timeUp.folder");
			String timeDown_folder = systemConfigUtil.get("timeDown.folder");
			File column_file = new File(column_folder);
			File timeUp_file = new File(timeUp_folder);
			File timeDown_file = new File(timeDown_folder);
			//解密檔案
			File columnDecode = new File(column_file.getParent() + File.separator+CommonUtil.getFileNameWithOutExtension(column_file) + "_decode.txt");
			CommonUtil.decrypt(column_file.getPath(), DBSynConstans.edit_pw);
			File timeUpDecode = new File(timeUp_file.getParent() + File.separator+CommonUtil.getFileNameWithOutExtension(timeUp_file) + "_decode.txt");
			File timeDownDecode = new File(timeDown_file.getParent() + File.separator+CommonUtil.getFileNameWithOutExtension(timeDown_file) + "_decode.txt");
			CommonUtil.decrypt(timeUp_file.getPath(), DBSynConstans.edit_pw);
			CommonUtil.decrypt(timeDown_file.getPath(), DBSynConstans.edit_pw);
			int fileCount = 0;
			if (columnDecode.exists()) {
				CommonUtil.readColumnFile(columnDecode);
				fileCount++;
				columnDecode.delete();
			}
			if (timeUpDecode.exists()) {
				CommonUtil.readTimeUpFile(timeUpDecode);
				fileCount++;
				timeUpDecode.delete();
			}
			if (timeDownDecode.exists()) {
				CommonUtil.readTimeDownFile(timeDownDecode);
				fileCount++;
				timeDownDecode.delete();
			}
			if (fileCount < 2) {
				JOptionPane.showMessageDialog(this, "無法找到設定檔，請檢查是否有儲存設定", "執行異常", JOptionPane.ERROR_MESSAGE);
			} else {
				logger.info("已讀入所有欄位及時間設定");
			}

		} catch (Exception e) {
			logger.error("ExecutePanel error",e);
		}
	}

}
