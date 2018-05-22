package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import layout.TableLayout;

public class TableLayoutDemo extends JFrame {

	private JPanel contentPane;

    public static void main(String args[]) {

        JFrame frame = new JFrame("Example of TableLayout");
        frame.setSize(450, 450);

        double size[][] = {{10, 75, 75, 75, 75, 75, 10}, // Columns
            {10, 75, 75, 75, 75, 75, 10}}; // Rows

        frame.setLayout(new TableLayout(size));


        String label[] = {"(1,1)", "(1,5)", "(1,3)", "(5,3)", "(3,3)"};
        JButton button[] = new JButton[label.length];

        for (int i = 0; i < label.length; i++) {
            button[i] = new JButton(label[i]);
        }


        frame.add(button[0], "0, 0");
        frame.add(button[1], "0, 1");
        frame.add(button[2], "1, 3");
        frame.add(button[3], "5, 3");
        frame.add(button[4], "3, 3");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
