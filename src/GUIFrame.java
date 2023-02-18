import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import camera.cameraCapture;
import create.Filemaker;
import create.IMGmake;
import state.ITFState;
import state.QRState;
import state.State;

import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.io.File;
import java.awt.*;

public class GUIFrame extends JFrame implements ActionListener , ChangeListener {
    private int defX = 300;
    private int defY = 300;
    private final String levelName[] = {"7%" , "15%", "25%", "30%"};
    private final String colorName[] = {"BLACK", "BLUE", "CYAN", "DKGRAY", "GRAY", "GREEN", "LTGRAY", "MAGENTA", "RED", "WHITE", "YELLOW"};
    private final int colorInt[] = {0xff000000, 0xff0000ff, 0xff00ffff, 0xff444444, 0xff888888, 0xff00ff00, 0xffcccccc, 0xffff00ff, 0xffff0000, 	0xffffffff, 0xffffff00};
    private Filemaker maker = new IMGmake();
    private cameraCapture cam = cameraCapture.getSingleton();
    private State state = QRState.getSingleton();

    private BufferedImage img;
    private ButtonGroup levelGroup = new ButtonGroup();
    private JRadioButton levelbtn[];
    {
        levelbtn = new JRadioButton[levelName.length];
        for (int i = 0; i < levelbtn.length; i++) {
            levelbtn[i] = new JRadioButton(levelName[i], true);
            levelbtn[i].setActionCommand(String.valueOf(i));
            levelGroup.add(levelbtn[i]);
        }
    }
    private ButtonGroup Codes = new ButtonGroup();
    private JRadioButton qr = new JRadioButton("QRcode");
    private JRadioButton itf = new JRadioButton("ITFcode");
    private JComboBox<String> topcolor = new JComboBox<>(colorName);
    private JComboBox<String> backcolor = new JComboBox<>(colorName);
    private Button Createbtn = new Button("Create");
    private Button Clearlogbtn = new Button("Clear");
    private Button Clearbtn = new Button("Clear");
    private Button Sourcebtn = new Button("Source");
    private TextField urlField = new TextField(20);
    private TextField sourceField = new TextField(20);
    private TextField titleField = new TextField(20);
    private TextField sliderField = new TextField(3);
    private JSlider slider = new JSlider(1,1000);
    private JLabel string = new JLabel();
    private JLabel qrlabel;
    
    public GUIFrame(String title) {
        super(title);
        setBackground(Color.lightGray);
        
        JTabbedPane tab = new JTabbedPane();
        JPanel createPanel = new JPanel();
        JPanel readerPanel = new JPanel();
        tab.addTab("create", createPanel);
        tab.addTab("reader", readerPanel);
        readerPanel.setLayout(new BoxLayout(readerPanel, BoxLayout.Y_AXIS));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(setRadio());
        left.add(setField());

        JPanel levelpane = new JPanel();
        levelpane.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Component"));
        levelpane.add(setComponent());
        left.add(levelpane);
        left.add(setsource());

        JPanel right = new JPanel();
        right.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Code"));
        right.add(setShow());

        JPanel rleft = new JPanel();
        Clearlogbtn.addActionListener(this);
        rleft.add(cam.getMainpanel());
        readerPanel.add(rleft);
        JPanel Logbtn = new JPanel();
        Logbtn.add(Clearlogbtn);
        readerPanel.add(Logbtn);

        createPanel.add(left);
        createPanel.add(right);
        getContentPane().add(tab);
        
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private JPanel setRadio() {
        JPanel panel = new JPanel();
        
        qr.setSelected(true);
        qr.addActionListener(this);
        itf.addActionListener(this);

        Codes.add(qr);
        Codes.add(itf);

        panel.add(qr);
        panel.add(itf);

        return panel;
    }

    private JPanel setField() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JPanel urlpane = new JPanel();
        string.setText("URL ");
        urlpane.add(string);
        urlpane.add(urlField);
        panel.add(urlpane);

        JPanel titlepane = new JPanel();
        titlepane.add(new JLabel("Title"));
        titlepane.add(titleField);
        panel.add(titlepane);

        return panel;
    }

    private JPanel setComponent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel labelpane = new JPanel();
        labelpane.add(new JLabel("Error Level"));
        panel.add(labelpane);
        JPanel levelpane = new JPanel();
        levelpane.setLayout(new GridLayout(2,2));
        for (int i = 0; i < levelbtn.length; i++) {
            levelpane.add(levelbtn[i]);
        }
        panel.add(levelpane);

        JPanel sizepane = new JPanel();
        sizepane.add(new JLabel("Size"));
        sizepane.add(sliderField);
        slider.addChangeListener(this);
        slider.setMajorTickSpacing(100);
        slider.setMinorTickSpacing(50);
        slider.setPaintTicks(true);
        sizepane.add(slider);
        panel.add(sizepane);

        JPanel colorpane = new JPanel();
        colorpane.add(new JLabel("Color: "));
        colorpane.add(new JLabel("Top"));
        colorpane.add(topcolor);
        colorpane.add(new JLabel("Back"));
        colorpane.add(backcolor);
        panel.add(colorpane);

        return panel;
    }

    private JPanel setsource() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel sourcepane = new JPanel();
        Sourcebtn.addActionListener(this);
        sourcepane.add(Sourcebtn);
        sourcepane.add(sourceField);
        panel.add(sourcepane);

        JPanel btnpane = new JPanel();
        Clearbtn.addActionListener(this);
        Createbtn.addActionListener(this);
        btnpane.add(Clearbtn);
        btnpane.add(Createbtn);
        panel.add(btnpane);

        return panel;
    }

    private JPanel setShow() {
        JPanel panel = new JPanel();
        try {
            qrlabel = new JLabel(maker.make("lib\\noqrcode.png", defX, defY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        panel.add(qrlabel);

        return panel;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        slider = (JSlider)e.getSource();
        sliderField.setText(String.valueOf(slider.getValue()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Createbtn) {
             if (urlField.getText().equals("")) {
                Message("URLを入力してください。", "Info", JOptionPane.INFORMATION_MESSAGE);
             } else if (titleField.getText().equals("")) {
                 Message("Titleを入力してください。", "Info", JOptionPane.INFORMATION_MESSAGE);
             } else if (sourceField.getText().equals("")) {
                Message("Sourceを選択、入力して下さい。", "Info", JOptionPane.INFORMATION_MESSAGE);
             } else {
                 state.setUrl(urlField.getText());
                 state.setFilename(titleField.getText());
                 state.setSource(new File(sourceField.getText()));
                 state.setLevel(Integer.parseInt(levelGroup.getSelection().getActionCommand()));
                 state.setSize(slider.getValue());
                 state.setTopcolor(colorInt[topcolor.getSelectedIndex()]);
                 state.setBackcolor(colorInt[backcolor.getSelectedIndex()]);
                 
                 img = state.make();
                 if (img == null) {
                     Message("Imgの作成に失敗しました。", "Error", JOptionPane.NO_OPTION);
                 } else {
                     repaint(maker.Imgmake( img, defX, defY));
                 }
             }

        } else if (e.getSource() == Clearbtn) {
            urlField.setText("");
            sourceField.setText("");
            titleField.setText("");
            levelbtn[0].setSelected(true);
            slider.setValue(500);
            topcolor.setSelectedIndex(0);
            backcolor.setSelectedIndex(0);
            defX = 300;
            defY = 300;
            repaint(maker.make("lib\\noqrcode.png", defX, defY));

        } else if (e.getSource() == Sourcebtn) {
            sourceField.setText(maker.Open());
        } else if (e.getSource() == Clearlogbtn) {
            cam.getLog().setText("");
        } else if (e.getSource() == qr) {
            defX = 300;
            defY = 300;
            state = QRState.getSingleton();
            string.setText("URL ");
            for (int i = 0; i < levelbtn.length; i++) {
                levelbtn[i].setEnabled(true);
            }
        } else if (e.getSource() == itf) {
            defX = 200;
            defY = 50;
            state = ITFState.getSingleton();
            string.setText("VAL ");
            for (int i = 0; i < levelbtn.length; i++) {
                levelbtn[i].setEnabled(false);
            }
        }
    }

    public void repaint(ImageIcon icon) {
        qrlabel.setIcon(icon);
    }

    private void Message(String msg, String title, int type) {
        JOptionPane.showMessageDialog(this, msg, title, type);
    }
}