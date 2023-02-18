package camera;

import java.awt.image.BufferedImage;
import java.awt.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class cameraCapture implements Runnable, ThreadFactory {
    private ExecutorService executor = Executors.newFixedThreadPool(1);
	private static cameraCapture singleton = new cameraCapture();
    private Webcam webcam = null;
	private WebcamPanel panel = null;
	private JPanel mainpanel = new JPanel();
    private TextArea Log = new TextArea(20,40);

    private cameraCapture() {
		try {
			java.awt.Dimension size = WebcamResolution.QVGA.getSize();

			webcam = Webcam.getWebcams().get(0);
			webcam.setViewSize(size);
			panel = new WebcamPanel(webcam);
			panel.setPreferredSize(size);
			panel.setFPSDisplayed(true);

			JPanel logpane = new JPanel();
			logpane.add(Log);
			JPanel webpane = new JPanel();
			webpane.add(panel);

			mainpanel.add(logpane);
			mainpanel.add(webpane);
			executor.execute(this);
		} catch (IndexOutOfBoundsException e) {
			mainpanel.setLayout(new BoxLayout(mainpanel, BoxLayout.Y_AXIS));
			mainpanel.add(new JLabel("The camera does not exist."));
			mainpanel.add(new JLabel("Reboot the camera if you wish to use it."));
			mainpanel.add(new JLabel(e.toString()));
		}
    }

    @Override
    public void run() {
		do {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	
			Result result = null;  //QRcode結果格納
			BufferedImage image = null;  //イメージ格納
	
			//QRコード読取
			if (webcam.isOpen()) {
				//映像のイメージを取得
				if ((image = webcam.getImage()) == null) {
					continue;
				}
	
				//ビットマップ作成
				LuminanceSource source = new BufferedImageLuminanceSource(image);
				BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
	
				//QRコードをデコードしてデータ取得
				try {
					result = new MultiFormatReader().decode(bitmap);
				} catch (NotFoundException e) {
					// fall thru, it means there is no QR code in image
				}
			} 
	
			//データがある場合
			if (result != null) {
				Log.append(result.getText()+"\n");
				System.out.println(result.getText());
			}
		} while (true);
    }

    @Override
    public Thread newThread(Runnable r) {
		Thread t = new Thread(r, "example-runner");
		t.setDaemon(true);
		return t;
    }
	
	public static cameraCapture getSingleton() {
		return singleton;
	}

	public JPanel getMainpanel() {
		return mainpanel;
	}

	public TextArea getLog() {
		return Log;
	}
}
