package com.xiongyingqi.util.file.search;

import javax.swing.*;

/**
 * Created by 瑛琪<a href="http://xiongyingqi.com">xiongyingqi.com</a> on 2014/6/5 0005.
 */
public class MainFrame {
    private JTextField textField1;
    private JTextField textField2;
    private JButton button1;
    private JButton button2;
    private JTextArea textArea1;
    private JCheckBox checkBox1;
    private JPanel mainPanel;

    public static void main(String[] args){
        MainFrame mainFrame = new MainFrame();
        JFrame frame = new JFrame();
//        frame.pack();
        frame.setSize(800, 600);
        frame.setContentPane(mainFrame.mainPanel);
        frame.setVisible(true);

    }

}
