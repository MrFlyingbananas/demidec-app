package Database;

import GUI.MainMenu.MainMenu;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by mrfly on 7/21/2016.
 */
public class LoadingFrame {

    private JFrame frame;
    private JProgressBar progressBar;
    private JButton closeButton;
    public LoadingFrame(String infoText, int min, int max) {
        frame = new JFrame();
        Container content = frame.getContentPane();
        frame.setPreferredSize(new Dimension(300, 100));
        frame.setTitle("Loading");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        progressBar = new JProgressBar(min, max);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        Border border = BorderFactory.createTitledBorder(infoText);
        progressBar.setBorder(border);
        content.add(progressBar, BorderLayout.NORTH);

        closeButton = new JButton("Close");
        closeButton.setEnabled(false);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        content.add(closeButton, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public LoadingFrame(String infoText) {
        frame = new JFrame();
        Container content = frame.getContentPane();
        frame.setPreferredSize(new Dimension(300, 70));
        frame.setTitle("Loading");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        progressBar = new JProgressBar();
        Border border = BorderFactory.createTitledBorder(infoText);
        progressBar.setBorder(border);
        content.add(progressBar, BorderLayout.NORTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    public void setProgressText(String s){
        progressBar.setString(s);
    }

    public void setProgress(int progress) {
        progressBar.setValue(progress);
    }

    public void finish(){
        if(closeButton != null){
            closeButton.setEnabled(true);
        }else {
            frame.dispose();
        }
    }

    public void showLoadingText(boolean val){
        progressBar.setStringPainted(val);
    }

    public void setProgressUnknown(boolean val){
        progressBar.setIndeterminate(val);
    }
}
