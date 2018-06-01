package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.CommonUtil;
import util.Constans;
import util.LogManager;
import util.Logger;

import javax.swing.JTabbedPane;
import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8720432252745326845L;
	private static Logger logger =null;
	private JPanel contentPane=null;
	private static ExecutePanel execute_panel=null;
	private int selected=0;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if(args!=null && args.length==1) {
						Constans.rootPath="C:\\Users\\k7043\\Downloads\\dbSynchronize";
					}else {
						Constans.rootPath=System.getProperty("user.dir");
					}
					CommonUtil.init();
					logger= LogManager.getLogger(MainFrame.class);
					MainFrame frame = new MainFrame();
					System.out.println("logopath="+Constans.logopath);
					ImageIcon imageIcon=new ImageIcon(Constans.logopath);
					frame.setIconImage(imageIcon.getImage());
					frame.setVisible(true);
					logger.info("db Synchronizer 啟動");
					if(execute_panel!=null) {
						execute_panel.prepareRun();
					}
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
		setBounds(0, 0, 1300, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setPreferredSize(new Dimension(1100, 600));
		tabbedPane.setFont(new Font("微軟正黑體", Font.PLAIN, 24));
		contentPane.add(tabbedPane, BorderLayout.NORTH);
		
		execute_panel = new ExecutePanel();
		tabbedPane.addTab("服務歷程", null, execute_panel, null);
		
		DataBasePanel db_panel = new DataBasePanel();
		tabbedPane.addTab("資料庫設定", null, db_panel, null);

		ConditionPanel condition_panel=new ConditionPanel();
		tabbedPane.addTab("執行條件", null, condition_panel, null);
		
		ColumnSettingPanel column_panel = new ColumnSettingPanel();
		tabbedPane.addTab("欄位設定", null, column_panel, null);

		TimeSettingPanel time_panel = new TimeSettingPanel();
		tabbedPane.addTab("時間格式轉換", null, time_panel, null);

		tabbedPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				DataBasePanel db_panel = (DataBasePanel) tabbedPane.getComponentAt(1);
				ConditionPanel condition_panel=(ConditionPanel)tabbedPane.getComponentAt(2);
				ColumnSettingPanel column_panel = (ColumnSettingPanel) tabbedPane.getComponentAt(3);
				TimeSettingPanel timeSettingPanel = (TimeSettingPanel) tabbedPane.getComponentAt(4);
				int newSelected=tabbedPane.getSelectedIndex();
				
				//判斷是否需要詢問儲存
				if (selected==1 && db_panel.isEdit) {
					db_panel.askSave();
				}
				if(selected==2 && condition_panel.isEdit) {
					condition_panel.askSave();
				}
				if (selected==3 && column_panel.isEdit) {
					column_panel.askSave();
				}
				if (selected==4 && timeSettingPanel.isEdit) {
					timeSettingPanel.askSave();
				}
				
				selected=newSelected;
				if (tabbedPane.getSelectedIndex() == 1) {
					db_panel.init();
					db_panel.setDefaultText();
					db_panel.disableAll();
					db_panel.checkPassword(db_panel);
				}
				else if(tabbedPane.getSelectedIndex()==2) {
					condition_panel.init();
					condition_panel.disableAll();
					condition_panel.checkPassword();
				}
				else if (tabbedPane.getSelectedIndex() == 3) {
					column_panel.loadAllData();
					column_panel.checkPassword();
				} else if (tabbedPane.getSelectedIndex() == 4) {
					timeSettingPanel.loadAllData();
					timeSettingPanel.checkPassword();
				}
			}
		});

		execute_panel.addContainerListener(new ContainerListener() {
			
			@Override
			public void componentRemoved(ContainerEvent e) {
				
			}
			
			@Override
			public void componentAdded(ContainerEvent e) {
				
			}
		});
	}
}
