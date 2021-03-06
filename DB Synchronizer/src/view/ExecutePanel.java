package view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import layout.TableLayout;
import util.Constans;
import util.LogManager;
import util.Logger;
import util.TransferThread;

public class ExecutePanel extends JPanel {
	TransferThread transferThread = null;
	Logger logger = LogManager.getLogger(ExecutePanel.class);
	boolean prepare = true;
	
	JButton startBtn=null;
	JButton stopBtn=null;
	JTextArea textArea = null;
	JLabel statusTextLabel = null;
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
		tb.setTitleFont(Constans.titleFont);
		tb.setTitleJustification(TitledBorder.LEFT);
		upPanel.setBorder(tb);
		JLabel statusTitleLabel = new JLabel("服務狀態:");
		statusTitleLabel.setFont(Constans.titleFont);
		statusTextLabel = new JLabel("服務執行中!");
		statusTextLabel.setFont(Constans.titleFont);
		startBtn = new JButton("啟動服務");
		startBtn.setFont(Constans.titleFont);
		stopBtn = new JButton("停止服務");
		stopBtn.setFont(Constans.titleFont);
		upPanel.add(statusTitleLabel, "0,0");
		upPanel.add(statusTextLabel, "1,0");
		upPanel.add(startBtn, "2,0");
		upPanel.add(stopBtn, "3,0");
		JPanel downPanel = new JPanel();
		downPanel.setLayout(new GridLayout());
		textArea = new JTextArea();
		textArea.setFont(Constans.textFont);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(400, 300));
		scrollPane.setViewportView(textArea);
		downPanel.add(scrollPane);
		add(upPanel, "0,0");
		add(downPanel, "0,1");

		stopBtn.setEnabled(false);
		statusTextLabel.setText("服務停止!");
		startBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				stopBtn.setEnabled(true);
				startBtn.setEnabled(false);
				statusTextLabel.setText("服務執行中!");
				if (transferThread == null) {
					transferThread = new TransferThread(textArea, statusTextLabel);
				}
				transferThread.start();
			}
		});

		stopBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				stopBtn.setEnabled(false);
				startBtn.setEnabled(true);
				statusTextLabel.setText("服務停止!");
				transferThread.setRunning(false);
				transferThread=null;
				prepare = false;
			}
		});

	}

	public void prepareRun() {
		new Thread() {
			public void run() {
				int countdown = 60;
				try {
					int count = 0;
					if(prepare) {
						statusTextLabel.setText("服務即將執行中!");
						stopBtn.setEnabled(true);
						startBtn.setEnabled(false);
					}
					while (prepare) {
						if (transferThread == null) {
							transferThread = new TransferThread(textArea, statusTextLabel);
						}
						transferThread.addMessage("倒數" + (countdown - count) + "秒後自動執行");
						count=count+5;
						Thread.sleep(5000);
						if (count >= countdown) {
							prepare = false;
							transferThread.start();
						}
					}
				} catch (Exception e) {
					logger.error("ExecutePanel prepare run ", e);
				}
			}
		}.start();
	}
}
