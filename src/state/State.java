package state;

import java.io.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.image.*;

public abstract class State {
    private static final JFrame frame = new JFrame();
    protected String url;
    protected File source;
    protected String filename;
    protected int level;
    protected int size;
    protected int topcolor;
    protected int backcolor;

    public abstract BufferedImage make();

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSource(File source) {
        this.source = source;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setTopcolor(int topcolor) {
        this.topcolor = topcolor;
    }

    public void setBackcolor(int backcolor) {
        this.backcolor = backcolor;
    }

    @Override
    public String toString() {
        return "State\n" + 
               "URL   : " + url + "\n" +
               "Source: " + source.toString() + "\n" + 
               "Title : " + filename + "\n" + 
               "Level : " + level + "\n" + 
               "Size  : " + size + " x " + size + "\n" +
               "Color : " + topcolor + " and " + backcolor;
    }

    protected void Message(String msg, String title, int type) {
        JOptionPane.showMessageDialog(frame,msg, title, type);
    }
}