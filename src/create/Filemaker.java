package create;

import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public interface Filemaker {
    public final JPanel panel = new JPanel();

    public default String Open() {
        try {
            JFileChooser fileChooser = new JFileChooser("c:");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int selected = fileChooser.showSaveDialog(panel);
            if (selected == JFileChooser.APPROVE_OPTION) {
                return fileChooser.getSelectedFile().toString();
            }
        } catch (Exception e) {
            messagelog("フォルダの設定に失敗しました。","Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    ImageIcon make(String filepath, int wid, int hig);
    ImageIcon Imgmake(BufferedImage img, int wid, int hig);

    public default void messagelog(String msg, String title, int type) {
        JOptionPane.showMessageDialog(panel, msg, title, type);
    }
}
