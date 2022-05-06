package com.ball;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ListIterator;

public class StarsFrame extends JFrame {

    private static int CANVAS_WIDTH;
    private static int CANVAS_HEIGHT;
    private static final int UPDATE_INTERVAL = 10; // milliseconds


    public static BufferedImage image;
    public static Model model;

    private DrawCanvas canvas;  // the drawing canvas (an inner class extends JPanel)

    // Constructor to setup the GUI components and event handlers
    public StarsFrame() {
        model = new Model();
        CANVAS_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
        CANVAS_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
        this.setSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
//        this.setExtendedState(this.MAXIMIZED_BOTH);

        image = new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        ImageIcon imageIcon = new ImageIcon(image);
        JLabel picLabel = new JLabel(imageIcon);
        BorderLayout borderLayout = new BorderLayout();
        this.getContentPane().setLayout(borderLayout);
        this.getContentPane().add(picLabel, BorderLayout.CENTER);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.pack();
        this.setTitle("Flying stars");

        canvas = new DrawCanvas(null);
        this.add(canvas);

        this.setVisible(true);


        // Create a new thread to run update at regular interval
        Thread updateThread = new Thread() {
            @Override
            public void run() {
                long lastTime = System.currentTimeMillis();
                while (true) {
                    long time = System.currentTimeMillis();
                    model.update((time - lastTime)/3);
                    lastTime = time;

                    repaint();

                    try {
                        // Delay and give other thread a chance to run
                        Thread.sleep(UPDATE_INTERVAL);  // milliseconds
                    } catch (InterruptedException ignore) {
                    }
                }
            }
        };

        updateThread.start(); // called back run()
    }


    // Define Inner class DrawCanvas, which is a JPanel used for custom drawing
    class DrawCanvas extends JPanel {
        int r = 0;
        int g = 0;
        int b = 0;
        int dir = 1;
        float bDelta = 0;

        public DrawCanvas(LayoutManager layout) {
            super(layout);
            setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        }

        @Override
        public void paintComponent(Graphics gr) {

//            super.paintComponent(g);  // paint parent's background
            Graphics2D graphics = image.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

//--        Формируем цвет фона в темно-синих тонах
            if (dir == 1) bDelta += 0.1f;
                else bDelta -= 0.1f;
            b = (int) bDelta;

            if (b >= 80) {
                dir = -1;
            }

            if (b <= 0) {
                dir = 1;
                b = 0;
            }

            graphics.setPaint(new Color(r, g, b));
            graphics.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

            ListIterator<Point> iterator = model.getPoints().listIterator();
            synchronized (model.getPoints()) {
                while (iterator.hasNext()) {
                    Point point = iterator.next();

                    int sx = (int) (image.getWidth() / 2f + (image.getWidth() / 2f * point.x / point.z));
                    int sy = (int) (image.getHeight() / 2f + (image.getHeight() / 2f * point.y / point.z));
                    if (sx < image.getWidth() && sx > 0 && sy < image.getHeight() && sy > 0) {
                        int alpha = 255 + (int) (point.z * (255 / Math.abs(Model.INITIAL_Z_COORD)));
                        alpha += 50;
                        if (alpha > 255) alpha = 255;

                        int radius = (int) (point.maxR - point.z * (point.maxR / Model.INITIAL_Z_COORD));
//                        image.setRGB(sx, sy, 0xff000000 | color << 16 | color << 8 | color);
                        graphics.setPaint(new Color(point.color.getRed(), point.color.getGreen(), point.color.getBlue(), alpha));
                        graphics.fillOval(sx, sy, radius, radius);
                    }
                }
            }
        }
    }
}

