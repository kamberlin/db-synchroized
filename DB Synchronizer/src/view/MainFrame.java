package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.CommonUtil;
import util.DBSynConstans;
import util.LogManager;
import util.Logger;

import javax.swing.JTabbedPane;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8720432252745326845L;
	private static Logger logger =null;
	private JPanel contentPane;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String path=System.getProperty("user.dir")+File.separator+DBSynConstans.mainproperty;
					DBSynConstans.mainproperty=path;
					System.err.println("mainproperty="+DBSynConstans.mainproperty);
					CommonUtil.init();
					logger= LogManager.getLogger(MainFrame.class);
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
					logger.info("db Synchronizer 啟動");
					frame.addWindowListener(new WindowListener() {
						
						@Override
						public void windowOpened(WindowEvent e) {
							
						}
						
						@Override
						public void windowIconified(WindowEvent e) {
							
						}
						
						@Override
						public void windowDeiconified(WindowEvent e) {
							
						}
						
						@Override
						public void windowDeactivated(WindowEvent e) {
							
						}
						
						@Override
						public void windowClosing(WindowEvent e) {
							int answer = JOptionPane.showConfirmDialog(frame, "是否要關閉程式？","確認關閉？",
									JOptionPane.YES_NO_OPTION);
							if (answer == JOptionPane.YES_OPTION) {
								logger.info("db Synchronizer 關閉");
								frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
								System.exit(0);
							} else {
								frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
							}
						}

						@Override
						public void windowClosed(WindowEvent e) {

						}

						@Override
						public void windowActivated(WindowEvent e) {

						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("MainFrame error", e);
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setTitle("DB Synchronizer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, 1300, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setPreferredSize(new Dimension(1100, 600));
		tabbedPane.setFont(new Font("微軟正黑體", Font.PLAIN, 24));
		contentPane.add(tabbedPane, BorderLayout.NORTH);

		DataBasePanel db_panel = new DataBasePanel();
		tabbedPane.addTab("資料庫設定", null, db_panel, null);

		ColumnSettingPanel column_panel = new ColumnSettingPanel();
		tabbedPane.addTab("欄位設定", null, column_panel, null);

		TimeSettingPanel time_panel = new TimeSettingPanel();
		tabbedPane.addTab("時間格式轉換", null, time_panel, null);

		ExecutePanel execute_panel = new ExecutePanel();
		tabbedPane.addTab("服務歷程", null, execute_panel, null);

		tabbedPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				DataBasePanel db_panel = (DataBasePanel) tabbedPane.getComponentAt(0);
				ColumnSettingPanel column_panel = (ColumnSettingPanel) tabbedPane.getComponentAt(1);
				TimeSettingPanel timeSettingPanel = (TimeSettingPanel) tabbedPane.getComponentAt(2);
				if (db_panel.isEdit) {
					db_panel.askSave();
				}
				if (column_panel.isEdit) {
					column_panel.askSave();
				}
				if (timeSettingPanel.isEdit) {
					timeSettingPanel.askSave();
				}
				db_panel.setDefaultText();
				db_panel.disableAll();
				if (tabbedPane.getSelectedIndex() == 0) {
					db_panel.checkPassword(db_panel);
				} else if (tabbedPane.getSelectedIndex() == 1) {
					column_panel.loadAllData();
					column_panel.checkPassword();
				} else if (tabbedPane.getSelectedIndex() == 2) {
					timeSettingPanel.loadAllData();
					timeSettingPanel.checkPassword();
				}
			}
		});

		db_panel.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
			}

			@Override
			public void componentResized(ComponentEvent e) {
				if (!db_panel.isLoad) {
					db_panel.checkPassword(db_panel);
				}
			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentHidden(ComponentEvent e) {

			}
		});
		column_panel.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {

			}

			@Override
			public void componentResized(ComponentEvent e) {

			}

			@Override
			public void componentMoved(ComponentEvent e) {

			}

			@Override
			public void componentHidden(ComponentEvent e) {

			}
		});
	}
}
