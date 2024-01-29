import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MatchPanel extends JPanel {

    public static final String IMG = "files/match.png";
    private static BufferedImage img;

    // ints representing position image will be drawn at
    private int x;
    private int y;

    /**
     * Displays image via BufferedImage
     * @param pX x coordinate from upper left
     * @param pY y coordinate from upper left
     */
    public MatchPanel(int pX, int pY) {
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG));
                JLabel picLabel = new JLabel(new ImageIcon(img));
                add(picLabel);
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }

        x = pX;
        y = pY;
    }

    /**
     * Draw image baesd on values given into constructor
     * @param g graphics context
     */
    public void draw(Graphics g) {
        g.drawImage(img, x, y, 32, 100, null);
    }
}
