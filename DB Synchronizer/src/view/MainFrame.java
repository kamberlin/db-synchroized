package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.JTabbedPane;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8720432252745326845L;
	private static Logger logger = LogManager.getLogger(MainFrame.class);
	private JPanel contentPane;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
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
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, d.width-100, d.height-85);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setPreferredSize(new Dimension(d.width-100, d.height-150));
		tabbedPane.setFont(new Font("微軟正黑體", Font.PLAIN, 24));
		contentPane.add(tabbedPane, BorderLayout.NORTH);
		
		DataBasePanel db_panel=new DataBasePanel();
		tabbedPane.addTab("資料庫設定", null, db_panel, null);
		
		ColumnSettingPanel column_panel=new ColumnSettingPanel();
		tabbedPane.addTab("欄位設定", null, column_panel, null);
		
		TimeSettingPanel time_panel=new TimeSettingPanel();
		tabbedPane.addTab("時間格式轉換", null, time_panel, null);
		
		ExecutePanel execute_panel=new ExecutePanel();
		tabbedPane.addTab("服務歷程", null, execute_panel, null);
		
		tabbedPane.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				DataBasePanel db_panel=(DataBasePanel) tabbedPane.getComponentAt(0);
				ColumnSettingPanel column_panel=(ColumnSettingPanel) tabbedPane.getComponentAt(1);
				TimeSettingPanel timeSettingPanel=(TimeSettingPanel)tabbedPane.getComponentAt(2);
				if(db_panel.isEdit) {
					db_panel.askSave(db_panel);
				}
				db_panel.setDefaultText();
				db_panel.disableAll();
				if(tabbedPane.getSelectedIndex()==0) {
					db_panel.enterPassword(db_panel);
				}else if(tabbedPane.getSelectedIndex()==1) {
					column_panel.init();
				}else if(tabbedPane.getSelectedIndex()==2) {
					timeSettingPanel.init();
				}
			}
		});
		
		db_panel.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				if(!db_panel.isLoad) {
					db_panel.enterPassword(db_panel);
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
