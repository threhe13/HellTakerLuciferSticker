package WindowVer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class HellTaker extends JFrame implements Runnable{

    BufferedImage background;
    Image forDraw;
    Graphics Gc;

    BufferedImage[] images = new BufferedImage[12];
    BufferedImage image;

    Image iconImage;

    int x, y;
    int count;

    Thread th = new Thread(this);


    class MyMouseListener implements MouseMotionListener, MouseListener {
        int offX, offY;

        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mousePressed(MouseEvent e) {
            offX = e.getX();
            offY = e.getY();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {}

        @Override
        public void mouseDragged(MouseEvent e) {
            setLocation(e.getX() - offX + getLocation().x - 60, e.getY() - offY + getLocation().y - 80);
        }

        @Override
        public void mouseMoved(MouseEvent e) {}
    }

    public HellTaker() throws IOException {
        String path = "132965.png";
        image = ImageIO.read(getClass().getClassLoader().getResource(path));

        x = 0; y = 0;
        count = 0;

        setType(Type.UTILITY);
        setLayout(null);
        setSize(100, 120);
        setLocation(x, y);
        setAlwaysOnTop(true);
        setUndecorated(true);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);


        addMouseMotionListener(new MyMouseListener());
        addMouseListener(new MyMouseListener());

        setBackground(new Color(0,0,0,0));

        background = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Gc = background.createGraphics();

        for(int i = 0, j = 0; i < image.getWidth(); i+=100, j++){
            images[j] = image.getSubimage(i, 0, 100, 100);
        }

        iconImage = new ImageIcon(images[0]).getImage();
        setIconImage(iconImage);


        th.start();
    }

    public void paint(Graphics g){
        g.drawImage(background, x, y+20, this);
    }

    public void update(Graphics g){
        background = new BufferedImage(100, 120, BufferedImage.TYPE_INT_ARGB);
        Gc = background.createGraphics();

        forDraw = images[count];
        Gc.drawImage(forDraw, x, y, this);

        paint(g);
    }

    @Override
    public void run() {

        if(SystemTray.isSupported()){
            SystemTray Tray = SystemTray.getSystemTray();

            PopupMenu popupMenu = new PopupMenu();
            TrayIcon trayIcon = new TrayIcon(iconImage, "루시퍼쨔응", popupMenu);
            trayIcon.setImageAutoSize(true);

            trayIcon.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(!isVisible());
                }
            });

            MenuItem item = new MenuItem("So sweet!");
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(true);
                }
            });

            popupMenu.add(item);
            item = new MenuItem("Good Bye~");
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Tray.remove(trayIcon);
                    System.exit(0);
                }
            });

            popupMenu.add(item);
            try {
                Tray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }

        }
        else{
            System.err.println("System tray not supported.");
        }


        while(true){
            try{
                Thread.sleep(60);
            } catch (Exception e) {}

            count++;
            if(count == 12){
                count = 0;
                System.gc();
            }

            update(Gc);
            setBackground(new Color(0,0,0,0));
            repaint();
        }
    }

    public static void main(String[] args) throws IOException {
        new HellTaker();
    }
}

