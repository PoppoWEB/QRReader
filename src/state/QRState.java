package state;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import error.LevelIntException;

public class QRState extends State {
    private static final QRState singleton = new QRState();
    private ConcurrentHashMap<EncodeHintType, ErrorCorrectionLevel> hints = new ConcurrentHashMap<>();
    private QRCodeWriter writer = new QRCodeWriter();
    private BitMatrix matrix;
    private BufferedImage image;

    private QRState() { }

    public static QRState getSingleton() {
        return singleton;
    }

    @Override
    public BufferedImage make() {
        image = null;
        try {
            switch (level) {
                case 0:
                    hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
                    break;
                case 1:
                    hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
                    break;
                case 2:
                    hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
                    break;
                case 3:
                    hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
                    break;
                default:
                    throw new LevelIntException();
            }
            matrix = writer.encode(url, BarcodeFormat.QR_CODE, size, size);
            MatrixToImageConfig config = new MatrixToImageConfig(topcolor, backcolor);
            image = MatrixToImageWriter.toBufferedImage(matrix, config);
            ImageIO.write(image, "png",  new File(source.toString() + "/" + filename + ".png"));
            Message("Success: Successful generation of qr code.", "Info", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (WriterException e) {
            Message("WriterExceprion: writer encode Error.\n" + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (LevelIntException e) {
            Message("Level Error: Java Program Error.\n" + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            Message("Exception: Unexpected error.\n" + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return image;
    }
}