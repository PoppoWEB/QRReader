package state;

import com.google.zxing.oned.ITFWriter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class ITFState extends State {
    private static final ITFState singleton = new ITFState();
    private ITFWriter writer = new ITFWriter();
    private BitMatrix matrix;
    private BufferedImage image;

    private ITFState() { }

    public static ITFState getSingleton() {
        return singleton;
    }

    @Override
    public BufferedImage make() {
        image = null;
        try {
            matrix = writer.encode(url, BarcodeFormat.ITF, size, size/4);
            MatrixToImageConfig config = new MatrixToImageConfig(topcolor, backcolor);
            image = MatrixToImageWriter.toBufferedImage(matrix, config);
            ImageIO.write(image, "png",  new File(source.toString() + "/" + filename + ".png"));
            Message("Success: Successful generation of qr code.", "Info", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (NumberFormatException e) {
            Message("NumberFormatException: ITFNumber encode Error.\n" + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (WriterException e) {
            Message("WriterExceprion: writer encode Error.\n" + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            Message("Exception: Unexpected error.\n" + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return image;
    }
}
