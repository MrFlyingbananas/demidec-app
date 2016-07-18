package QuestionPane;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.util.List;
/**
 * Created by 120658 on 3/15/2016.
 */
public class QuestionPane extends JPanel implements ActionListener {
    private enum QuestionResult{
        Correct,
        Incorrect
    }
    private final float V_QUESTION_PERCENT_SIZE = .25f, WINDOW_BUFFER = .06f, ANSWER_BUTTON_SPACE_TAKEN = .8f, X_OBJECT_BUFFER = .05f;
    private JFrame frame;
    private JButton[] buttons;
    private JTextPane[] choices;
    private JTextPane questionPane;
    private int width, height;
    private Font qFont, cFont;
    private List<Question> questionList;
    private Question question;
    private DBReader dbReader;
    private QuestionResultPane resultPane;
    public QuestionPane(JFrame frame, Connection con){
        dbReader = new DBReader(con);
        this.frame = frame;
        this.setLayout(null);
        width = (int)frame.getPreferredSize().getWidth();
        height = (int)frame.getPreferredSize().getHeight();
        questionList = dbReader.getQuestionListBySubject(DBReader.Subject.Econ);
        question = questionList.remove(0);
        buttons = new JButton[5];
        choices = new JTextPane[5];
        setMinimumSize(new Dimension(400, 400));
        init();
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                frame.setPreferredSize(new Dimension(frame.getSize()));
                triggerResize();
            }
        });
        resultPane = new QuestionResultPane();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
    }

    private void init(){
        int xWindowBuffer = (int)(WINDOW_BUFFER * width);
        int yWindowBuffer = (int)(WINDOW_BUFFER * height);
        questionPane = new JTextPane();
        questionPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        questionPane.setLocation(xWindowBuffer / 2, yWindowBuffer / 2);
        questionPane.setSize(width - xWindowBuffer * 2, (int)(height * V_QUESTION_PERCENT_SIZE));
        questionPane.setEditable(false);
        questionPane.setContentType("text/html");
        questionPane.setText(question.getText());
        resizeQuestion();
        buttons[0] = new JButton("A");
        buttons[1] = new JButton("B");
        buttons[2] = new JButton("C");
        buttons[3] = new JButton("D");
        buttons[4] = new JButton("E");
        int remainingSpace = height - questionPane.getY() - questionPane.getHeight() - yWindowBuffer;
        int answerBoxSize = (int)((remainingSpace * ANSWER_BUTTON_SPACE_TAKEN) / 5);
        int yObjectBuffer = (remainingSpace - (answerBoxSize * 5)) / 6;
        buttons[0].setBounds(xWindowBuffer, questionPane.getY() + questionPane.getHeight() + yObjectBuffer, answerBoxSize, answerBoxSize);
        for(int i = 1; i < buttons.length; i++){
            buttons[i].setBounds(xWindowBuffer, buttons[i - 1].getY() + buttons[i - 1].getHeight() + yObjectBuffer, answerBoxSize, answerBoxSize);
        }
        for(JButton b : buttons){
            b.addActionListener(this);
        }
        String[] qChoices = question.getChoices();
        for(int i = 0; i < choices.length; i++){
            choices[i] = new JTextPane();
            choices[i].putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
            choices[i].setContentType("text/html");
            choices[i].setBorder(javax.swing.BorderFactory.createEmptyBorder());
            choices[i].setBackground(this.getBackground());
            choices[i].setText(qChoices[i]);
        }
        resizeChoices();
        for(int i = 0; i < buttons.length; i++){
            add(buttons[i]);
            add(choices[i]);
        }
        this.add(questionPane);
    }

    private void answerQuestion(int selectedIndex) {
        if(selectedIndex == question.getCorrectAnswerIndex()){

        }else{

        }

    }

    private void nextQuestion(){
        question = questionList.remove(0);
        questionPane.setText(question.getText());
        String[] qChoices = question.getChoices();
        for(int i = 0; i < choices.length; i++){
            choices[i].setText(qChoices[i]);
        }
        resizeQuestion();
        resizeChoices();
    }

    public void triggerResize(){
        if(frame.getContentPane().equals(this)){
            width = (int)frame.getPreferredSize().getWidth();
            height = (int)frame.getPreferredSize().getHeight();
            int xWindowBuffer = (int)(WINDOW_BUFFER * width);
            int yWindowBuffer = (int)(WINDOW_BUFFER * height);
            questionPane.setLocation(xWindowBuffer / 2, yWindowBuffer / 2);
            questionPane.setSize(width - (int)(xWindowBuffer * 1.5), (int)(height * V_QUESTION_PERCENT_SIZE));
            int remainingSpace = height - questionPane.getY() - questionPane.getHeight() -yWindowBuffer;
            int answerBoxSize = (int)((remainingSpace * ANSWER_BUTTON_SPACE_TAKEN) / 5);
            int yObjectBuffer = (remainingSpace - (answerBoxSize * 5)) / 6;
            buttons[0].setBounds(xWindowBuffer, questionPane.getY() + questionPane.getHeight() + yObjectBuffer, answerBoxSize, answerBoxSize);
            for(int i = 1; i < buttons.length; i++){
                buttons[i].setBounds(xWindowBuffer, buttons[i - 1].getY() + buttons[i - 1].getHeight() + yObjectBuffer, answerBoxSize, answerBoxSize);
            }
            resizeQuestion();
            resizeChoices();
        }else{
            resultPane.triggerResize();
        }
    }
    private void resizeQuestion(){
        qFont = new Font("Times New Roman", Font.PLAIN, 80);
        questionPane.setFont(qFont);
        int start = 0;
        int end = -1;
        int lines = 0;
        try {
            do {
                start = Utilities.getRowStart(questionPane, end + 1);
                end = Utilities.getRowEnd(questionPane, start);
                lines++;
            } while (end < questionPane.getDocument().getLength());

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        int lineHeight = questionPane.getFontMetrics(questionPane.getFont()).getHeight() + questionPane.getFontMetrics(qFont).getMaxDescent();
        /*while(lines * lineHeight < questionPane.getHeight()){
            qFont = new Font(qFont.getFontName(), qFont.getStyle(), qFont.getSize() + 1);
            lineHeight = questionPane.getFontMetrics(questionPane.getFont()).getHeight() + questionPane.getFontMetrics(qFont).getMaxDescent();
            questionPane.setFont(qFont);
            start = 0;
            end = -1;
            lines = 0;
            try {
                do {
                    start = Utilities.getRowStart(questionPane, end + 1);
                    end = Utilities.getRowEnd(questionPane, start);
                    lines++;
                } while (end < questionPane.getDocument().getLength());

            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }*/
        while(lines * lineHeight > questionPane.getHeight()){
            qFont = new Font(qFont.getFontName(), qFont.getStyle(), qFont.getSize() - 1);
            lineHeight = questionPane.getFontMetrics(questionPane.getFont()).getHeight() + questionPane.getFontMetrics(qFont).getMaxDescent();
            questionPane.setFont(qFont);
            start = 0;
            end = -1;
            lines = 0;
            try {
                do {
                    start = Utilities.getRowStart(questionPane, end + 1);
                    end = Utilities.getRowEnd(questionPane, start);
                    lines++;
                } while (end < questionPane.getDocument().getLength());
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }
    private void resizeChoices(){
        String[] qChoices = question.getChoices();
        for(int i = 0; i < qChoices.length; i++){
            choices[i].setContentType("text/html");
            choices[i].setText(qChoices[i]);

        }
        int lStringWidth = 0;
        cFont = new Font("Times New Roman", Font.PLAIN, 50);
        JTextPane testPane = new JTextPane();
        testPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        testPane.setContentType("text/html");
        testPane.setFont(cFont);
        FontMetrics fm = testPane.getFontMetrics(cFont);
        for(JTextPane pane : choices){
            String text = pane.getText().replaceAll("\\<.*?>","").replaceAll("&.*?;","").trim();
            if(fm.stringWidth(text) > testPane.getWidth()){
                //TODO: SPLIT LINE IF TOO LONG FOR PANE TO MAKE IT LOOK NICE
            }
            if(fm.stringWidth(text) > lStringWidth){
                lStringWidth = fm.stringWidth(text);
                testPane.setText(pane.getText());
            }
        }
        int xWindowBuffer = (int)(WINDOW_BUFFER * width);
        int xObjectBuffer = (int)(width * X_OBJECT_BUFFER);
        testPane.setBounds(buttons[0].getX() + xObjectBuffer + buttons[0].getWidth(), 200,
                width - xWindowBuffer - buttons[0].getWidth() - xObjectBuffer * 2,
                buttons[0].getHeight());
        /*while(fm.stringWidth(testPane.getText()) < testPane.getBounds().width && fm.getHeight() < testPane.getBounds().height){
            cFont = new Font(cFont.getFontName(), cFont.getStyle(), cFont.getSize() + 1);
            testPane.setFont(cFont);
            fm = testPane.getFontMetrics(cFont);
        }*/
        //String text = testPane.getText().replaceAll("\\<.*?>","").replaceAll("&.*?;","").trim();
        /*while(fm.stringWidth(text) > testPane.getBounds().width || fm.getHeight() > testPane.getBounds().height){
            cFont = new Font(cFont.getFontName(), cFont.getStyle(), cFont.getSize() - 1);
            testPane.setFont(cFont);
            fm = testPane.getFontMetrics(cFont);
        }*/
        int start = 0;
        int end = -1;
        int lines = 0;
        try {
            do {
                start = Utilities.getRowStart(testPane, end + 1);
                end = Utilities.getRowEnd(testPane, start);
                lines++;
            } while (end < testPane.getDocument().getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        int lineHeight = testPane.getFontMetrics(testPane.getFont()).getHeight() + testPane.getFontMetrics(cFont).getMaxDescent();
        while (lines * lineHeight + testPane.getFontMetrics(cFont).getMaxDescent() > testPane.getHeight()) {
            cFont = new Font(cFont.getFontName(), cFont.getStyle(), cFont.getSize() - 1);
            lineHeight = testPane.getFontMetrics(testPane.getFont()).getHeight() + testPane.getFontMetrics(cFont).getMaxDescent();
            testPane.setFont(cFont);
            start = 0;
            end = -1;
            lines = 0;
            try {
                do {
                    start = Utilities.getRowStart(testPane, end + 1);
                    end = Utilities.getRowEnd(testPane, start);
                    lines++;
                } while (end < testPane.getDocument().getLength());

            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < choices.length; i++) {
            choices[i].setFont(cFont);
            start = 0;
            end = -1;
            lines = 0;
            try {
                do {
                    start = Utilities.getRowStart(choices[i], end + 1);
                    end = Utilities.getRowEnd(choices[i], start);
                    lines++;
                    if(end == -1)
                        break;
                } while (end < choices[i].getDocument().getLength());
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            if(lines != 1){
                if(lines * choices[i].getFontMetrics(cFont).getHeight() > choices[i].getHeight()){
                    lineHeight = choices[i].getFontMetrics(cFont).getHeight();
                    while (lines * lineHeight + choices[i].getFontMetrics(cFont).getMaxDescent() > choices[i].getHeight()) {
                        cFont = new Font(cFont.getFontName(), cFont.getStyle(), cFont.getSize() - 1);
                        lineHeight = choices[i].getFontMetrics(choices[i].getFont()).getHeight() + choices[i].getFontMetrics(cFont).getMaxDescent();
                        choices[i].setFont(cFont);
                        start = 0;
                        end = -1;
                        lines = 0;
                        try {
                            do {
                                start = Utilities.getRowStart(choices[i], end + 1);
                                end = Utilities.getRowEnd(choices[i], start);
                                lines++;
                            } while (end < choices[i].getDocument().getLength());

                        } catch (BadLocationException e) {
                            e.printStackTrace();
                        }
                    }
                    if(lines * choices[i].getFontMetrics(cFont).getHeight() > choices[i].getHeight())
                        System.err.println("DANGER DESU STILL");
                    i = -1;
                    continue;

                }
                choices[i].setBounds(buttons[i].getX() + xObjectBuffer + buttons[i].getWidth(),
                        buttons[i].getY() + (buttons[i].getHeight() - (lines * choices[i].getFontMetrics(cFont).getHeight())) / 2,
                        width - xWindowBuffer - buttons[i].getWidth() - xObjectBuffer * 2, buttons[i].getHeight());

            }else{
                choices[i].setBounds(buttons[i].getX() + xObjectBuffer + buttons[i].getWidth(),
                        buttons[i].getY() + buttons[i].getHeight() / 2 - choices[i].getFontMetrics(cFont).getHeight() / 2,
                        width - xWindowBuffer - buttons[i].getWidth() - xObjectBuffer * 2, buttons[i].getHeight());

            }
        }


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == buttons[0]){
            answerQuestion(0);
            System.out.println("test");
        }else if(e.getSource() == buttons[1]){
            answerQuestion(1);
        }else if(e.getSource() == buttons[2]){
            answerQuestion(2);
        }else if(e.getSource() == buttons[3]){
            answerQuestion(3);
        }else if(e.getSource() == buttons[4]){
            answerQuestion(4);
        }
    }

    private class QuestionResultPane extends JPanel{
        JButton nextButton;
        public QuestionResultPane() {
            width = (int)frame.getPreferredSize().getWidth();
            height = (int)frame.getPreferredSize().getHeight();
            int xWindowBuffer = (int)(WINDOW_BUFFER * width);
            int yWindowBuffer = (int)(WINDOW_BUFFER * height);
            nextButton = new JButton("Next Question");
            resizeButtons();
        }
        public void triggerResize(){
            resizeButtons();
        }
    }

    private void resizeButtons() {

    }
}
