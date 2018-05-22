package view;

import java.awt.Dimension;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import layout.TableLayout;
import util.CommonUtil;
import util.Constans;
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
		tb.setTitleJustification(TitledBorder.LEFT);
		upPanel.setBorder(tb);
		JLabel statusTitleLabel = new JLabel("服務狀態:");
		JLabel statusTextLabel = new JLabel("服務執行中!");
		JButton startBtn = new JButton("啟動服務");
		JButton stopBtn = new JButton("停止服務");
		upPanel.add(statusTitleLabel, "0,0");
		upPanel.add(statusTextLabel, "1,0");
		upPanel.add(startBtn, "2,0");
		upPanel.add(stopBtn, "3,0");
		JPanel downPanel = new JPanel();
		downPanel.setLayout(new GridLayout());
		JTextArea textArea = new JTextArea();
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
			SystemConfigUtil systemConfigUtil = new SystemConfigUtil(Constans.mainproperty);
			String column_folder = systemConfigUtil.get("column_setting.folder");
			String timeUp_folder = systemConfigUtil.get("timeUp.folder");
			String timeDown_folder = systemConfigUtil.get("timeDown.folder");
			File column_file = new File(column_folder);
			File timeUp_file = new File(timeUp_folder);
			File timeDown_file = new File(timeDown_folder);
			int fileCount = 0;
			if (column_file.exists()) {
				CommonUtil.readColumnFile(column_file);
				fileCount++;
			}
			if (timeUp_file.exists()) {
				CommonUtil.readTimeUpFile(timeUp_file);
				fileCount++;
			}
			if (timeDown_file.exists()) {
				CommonUtil.readTimeDownFile(timeDown_file);
				fileCount++;
			}
			if (fileCount < 2) {
				JOptionPane.showMessageDialog(this, "無法找到設定檔，請檢查是否有儲存設定", "執行異常", JOptionPane.ERROR_MESSAGE);
			} else {
				logger.info("已讀入所有欄位及時間設定");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
