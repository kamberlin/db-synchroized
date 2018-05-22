package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

public class ScrollPaneDemo extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ScrollPaneDemo frame = new ScrollPaneDemo();
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
	public ScrollPaneDemo() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new GridLayout());
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(400, 300));
		contentPane.add(scrollPane, BorderLayout.CENTER);
		JPanel testPanel=new JPanel();
		testPanel.setLayout(new GridLayout());
		TitledBorder tb=new TitledBorder("測試");
		tb.setTitleJustification(TitledBorder.LEFT);
		testPanel.setBorder(tb);
		
		JLabel lblNewLabel = new JLabel("New label");
		testPanel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("New label");
		testPanel.add(lblNewLabel_1);
		testPanel.add(new JButton("測試"));
		scrollPane.setViewportView(testPanel);
		
		JLabel lblNewLabel_2 = new JLabel("New label");
		testPanel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("New label");
		testPanel.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("New label");
		testPanel.add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("New label");
		testPanel.add(lblNewLabel_5);
	}

}
