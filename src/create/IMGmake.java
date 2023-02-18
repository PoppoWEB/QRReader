package create;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class IMGmake implements Filemaker {
    @Override
    public ImageIcon make(String filepath, int wid, int hig) {
        ImageIcon qr = null;
        try {
            qr = new ImageIcon(ImageIO.read(new File(filepath)).getScaledInstance(wid, hig, Image.SCALE_DEFAULT));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return qr;
    }
    
    @Override
    public ImageIcon Imgmake(BufferedImage img, int wid, int hig) {
        ImageIcon qr = null;
        try {
            qr = new ImageIcon(img.getScaledInstance(wid, hig, Image.SCALE_DEFAULT));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return qr;
    }
}