package ui;

import config.Config;
import logic.Logic;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Panel extends JPanel {
    private final Config config;
    private final Logic logic;

    private int curX = -1, curY = -1;
    private double curAngle = 0;
    private BufferedImage canvas;

    public Panel(Config config, Logic logic) {
        this.config = config;
        this.logic = logic;
        setBackground(Color.WHITE);

        Thread animator = new Thread(() -> {
            while (true) {
                renderNextSegment();
                try {
                    Thread.sleep(config.getSleepTimeMs());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                repaint();
            }
        });
        animator.setPriority(Thread.MAX_PRIORITY);
        animator.start();
    }

    private void renderNextSegment() {
        if (canvas == null) {
            canvas = new BufferedImage(config.getCanvasWidth(), config.getCanvasHeight(), BufferedImage.TYPE_INT_RGB);
            curX = canvas.getWidth() / 2;
            curY = canvas.getHeight() / 4;
        }

        int turn = logic.getNextTurn();
        int currentIndex = logic.getCurrentIndex();
        curAngle += (turn * 90);

        int sideLength = config.getSideLength();
        int nextX = curX + (int) (Math.cos(Math.toRadians(curAngle)) * sideLength);
        int nextY = curY + (int) (Math.sin(Math.toRadians(curAngle)) * sideLength);

        if (config.isDynamicFrame()) {
            int padding = 50;
            int diffRight = (nextX + padding) - canvas.getWidth();
            int diffBottom = (nextY + padding) - canvas.getHeight();
            int diffLeft = padding - nextX;
            int diffTop = padding - nextY;

            if (diffRight > 0 || diffBottom > 0 || diffLeft > 0 || diffTop > 0) {
                int addRight = Math.max(0, diffRight);
                int addBottom = Math.max(0, diffBottom);
                int addLeft = Math.max(0, diffLeft);
                int addTop = Math.max(0, diffTop);

                int newWidth = canvas.getWidth() + addLeft + addRight;
                int newHeight = canvas.getHeight() + addTop + addBottom;

                BufferedImage newCanvas = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                Graphics2D gNew = newCanvas.createGraphics();
                gNew.drawImage(canvas, addLeft, addTop, null);
                gNew.dispose();

                canvas = newCanvas;
                curX += addLeft;
                curY += addTop;
                nextX += addLeft;
                nextY += addTop;

                Window w = SwingUtilities.getWindowAncestor(this);
                if (w != null) {
                    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                    Insets insets = w.getInsets();
                    int targetW = newWidth + insets.left + insets.right;
                    int targetH = newHeight + insets.top + insets.bottom;
                    w.setSize(Math.min(targetW, screen.width - 50), Math.min(targetH, screen.height - 50));
                }
            }
        }

        Graphics2D g2d = canvas.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        String colorScheme = config.getColorScheme();
        switch (colorScheme) {
            case "Rainbow":
                g2d.setColor(Color.getHSBColor(((currentIndex - 1) % 2000) / 2000f, 0.8f, 0.9f));
                break;
            case "Black":
                g2d.setColor(Color.BLACK);
                break;
            case "White":
                g2d.setColor(Color.WHITE);
                break;
            case "Red":
                g2d.setColor(Color.RED);
                break;
            case "Green":
                g2d.setColor(Color.GREEN);
                break;
            case "Blue":
                g2d.setColor(Color.BLUE);
                break;
            case "Yellow":
                g2d.setColor(Color.YELLOW);
                break;
            case "Cyan":
                g2d.setColor(Color.CYAN);
                break;
            case "Magenta":
                g2d.setColor(Color.MAGENTA);
                break;
            default:
                break;
        }

        g2d.drawLine(curX, curY, nextX, nextY);

        curX = nextX;
        curY = nextY;
        g2d.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (canvas != null) {
            g.drawImage(canvas, 0, 0, null);
        }
    }
}
