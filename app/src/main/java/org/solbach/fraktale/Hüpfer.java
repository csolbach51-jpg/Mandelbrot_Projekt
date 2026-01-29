package org.solbach.fraktale;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import javax.swing.*;

// 1. Hilfsklasse nach oben oder in eigene Datei
class ComplexNumber {
    double rea, imaginär;
}

// 2. Die Basisklasse Hüpfer
public class Hüpfer extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, WindowListener, ActionListener, Printable {
    public static Hüpfer mainframe; // Muss public UND static sein!

    public static JFrame parent = new JFrame();
    public double a = 0.97, b = 0.3456, c = 50;
    public long z = 30000;
    public int u = 0, v = 0;
    public double sourcex = 1, sourcey = 1, destx = 1, desty = 1;

    public Hüpfer() {
        super();
    }

    @Override
    protected void paintComponent(Graphics g1) {
        super.paintComponent(g1);
        double x = 0, y = 0;
        for (int i = 0; i <= z; i++) {
            g1.setColor(Color.BLACK);
            g1.drawLine((int)(x*destx/sourcex + u), (int)(y*desty/sourcey + v), 
                        (int)(x*destx/sourcex + u), (int)(y*desty/sourcey + v));
            double xx = y + Math.signum(x) * Math.abs(b * x - c);
            double yy = a - x;
            x = xx; y = yy;
        }
    }

    // Pflicht-Methoden für die Listener (auch wenn leer!)
    public void windowClosing(WindowEvent e) { System.exit(0); }
    public void windowOpened(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    public void actionPerformed(ActionEvent e) {}
    public void mouseClicked(MouseEvent e) { repaint(); }
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
    public void mouseWheelMoved(MouseWheelEvent e) { repaint(); }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) return NO_SUCH_PAGE;
        paintComponent(graphics);
        return PAGE_EXISTS;
    }
    public static void main(String[] args) {
    JFrame frame = new JFrame("Hüpfer Fraktal");
    Hüpfer h = new Hüpfer();
    
    frame.add(h);
    frame.addWindowListener(h); // Registriert das Schließen des Fensters
    frame.setSize(800, 600);
    frame.setLocationRelativeTo(null); // Zentriert das Fenster
    frame.setVisible(true);
}
}