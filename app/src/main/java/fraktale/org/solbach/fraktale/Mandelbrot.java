package fraktale.org.solbach.fraktale;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import javax.swing.*;

public class Mandelbrot extends Hüpfer implements Printable, 
    MouseWheelListener, MouseMotionListener, ComponentListener, MouseListener {

    // 1. Variablen
    public double re = -0.8;
    public double im = 0.156;
    public double amin2 = -2.0, amax2 = 1.0, bmin2 = -1.3, bmax2 = 1.3;
    public int iteration = 100;
    private Point lastMousePoint;

    public static Mandelbrot mainframe;
    public JRadioButton radiobutton = new JRadioButton("Mandelbrot/Julia");
    public static JButton druckButton = new JButton("Drucken");

    public Mandelbrot() {
        super();
        this.addMouseWheelListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addComponentListener(this);
    }

 @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g); 
    
    int w = getWidth();
    int h = getHeight();
    
    // Sicherheitscheck: Falls das Fenster noch nicht bereit ist
    if (w <= 0 || h <= 0) return;

    // 1. Das "Leinwand"-Bild erstellen
    BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

    // 2. JEDEN Pixel durchgehen und berechnen
    for (int x = 0; x < w; x++) {
        for (int y = 0; y < h; y++) {
            
            // Logik-Wahl: Mandelbrot oder Julia?
            double iterFraction = radiobutton.isSelected() ? 
                                  computeMandelbrotMenge(x, y) : 
                                  computeJuliaMenge(x, y);
            
            // 3. FARBLOGIK (Hier passiert die Magie)
            int rgbColor;
            if (iterFraction >= 1.0) {
                // Punkt ist innerhalb der Menge -> Schwarz
                rgbColor = 0x000000; 
            } else {
                // Punkt ist außerhalb -> Farbe basierend auf der Geschwindigkeit des Entkommens
                // Wir nutzen HSB: Farbton (Hue), Sättigung (Saturation), Helligkeit (Brightness)
                float hue = 0.7f + (float)iterFraction; // Ein schöner Blau-Lila Verlauf
                rgbColor = Color.HSBtoRGB(hue, 0.8f, 1.0f);
            }
            
            // Farbe in das Bild setzen
            img.setRGB(x, y, rgbColor);
        }
    
    }
    
    

    // 4. Das fertige Bild auf den Bildschirm bringen
    g.drawImage(img, 0, 0, null);
}

private double computeMandelbrotMenge(double xPixel, double yPixel) {
    // Wandelt Pixel-Position in komplexe Koordinaten um
    double cRe = amin2 + (xPixel * (amax2 - amin2) / getWidth());
    double cIm = bmin2 + (yPixel * (bmax2 - bmin2) / getHeight());
    
    double zRe = 0, zIm = 0;
    int o = 0;
    
    // Die eigentliche Mandelbrot-Formel: z = z^2 + c
    while (zRe * zRe + zIm * zIm < 4.0 && o < iteration) {
        double nextRe = zRe * zRe - zIm * zIm + cRe;
        zIm = 2.0 * zRe * zIm + cIm;
        zRe = nextRe;
        o++;
    }
    // Gibt einen Wert zwischen 0.0 und 1.0 für die Farblogik zurück
    return (double) o / iteration;
}

private double computeJuliaMenge(double xPixel, double yPixel) {
    // Bei Julia ist der Startpunkt z die Pixel-Koordinate
    double zRe = amin2 + (xPixel * (amax2 - amin2) / getWidth());
    double zIm = bmin2 + (yPixel * (bmax2 - bmin2) / getHeight());
    
    // Und c ist ein fester Parameter (deine Variablen re und im)
    double cX = this.re; 
    double cY = this.im; 
    
    int o = 0;
    while (zRe * zRe + zIm * zIm < 4.0 && o < iteration) {
        double nextRe = zRe * zRe - zIm * zIm + cX;
        zIm = 2.0 * zRe * zIm + cY;
        zRe = nextRe;
        o++;
    }
    return (double) o / iteration;
}
    private double computeMandelbrot(double xP, double yP) {
        double cRe = amin2 + (xP * (amax2 - amin2) / getWidth());
        double cIm = bmin2 + (yP * (bmax2 - bmin2) / getHeight());
        double zRe = 0, zIm = 0;
        int o = 0;
        while (zRe * zRe + zIm * zIm < 4.0 && o < iteration) {
            double nextRe = zRe * zRe - zIm * zIm + cRe;
            zIm = 2.0 * zRe * zIm + cIm;
            zRe = nextRe;
            o++;
        }
        return (double) o / iteration;
    }

    private double computeJulia(double xP, double yP) {
        double zRe = amin2 + (xP * (amax2 - amin2) / getWidth());
        double zIm = bmin2 + (yP * (bmax2 - bmin2) / getHeight());
        int o = 0;
        while (zRe * zRe + zIm * zIm < 4.0 && o < iteration) {
            double nextRe = zRe * zRe - zIm * zIm + this.re;
            zIm = 2.0 * zRe * zIm + this.im;
            zRe = nextRe;
            o++;
        }
        return (double) o / iteration;
    }

    // 3. Listener (Mouse & Component)
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double zoom = (e.getWheelRotation() < 0) ? 0.8 : 1.2;
        double mRe = amin2 + (e.getX() * (amax2 - amin2) / getWidth());
        double mIm = bmin2 + (e.getY() * (bmax2 - bmin2) / getHeight());
        amin2 = mRe + (amin2 - mRe) * zoom;
        amax2 = mRe + (amax2 - mRe) * zoom;
        bmin2 = mIm + (bmin2 - mIm) * zoom;
        bmax2 = mIm + (bmax2 - mIm) * zoom;
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (lastMousePoint != null) {
            double dx = e.getX() - lastMousePoint.x;
            double dy = e.getY() - lastMousePoint.y;
            double cW = (amax2 - amin2) / getWidth();
            double cH = (bmax2 - bmin2) / getHeight();
            amin2 -= dx * cW; amax2 -= dx * cW;
            bmin2 -= dy * cH; bmax2 -= dy * cH;
            lastMousePoint = e.getPoint();
            repaint();
        }
    }

    @Override public void mousePressed(MouseEvent e) { lastMousePoint = e.getPoint(); }
    @Override public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            double cRe = amin2 + (e.getX() * (amax2 - amin2) / getWidth());
            double cIm = bmin2 + (e.getY() * (bmax2 - bmin2) / getHeight());
            double w2 = (amax2 - amin2) / 2.0;
            double h2 = (bmax2 - bmin2) / 2.0;
            amin2 = cRe - w2; amax2 = cRe + w2;
            bmin2 = cIm - h2; bmax2 = cIm + h2;
            repaint();
        }
    }
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void componentResized(ComponentEvent e) { repaint(); }
    @Override public void componentMoved(ComponentEvent e) {}
    @Override public void componentShown(ComponentEvent e) {}
    @Override public void componentHidden(ComponentEvent e) {}

    // 4. Drucken
    public void drucken() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        if (job.printDialog()) {
            try { job.print(); } catch (PrinterException ex) { ex.printStackTrace(); }
        }
    }

    @Override
public int print(Graphics g, PageFormat pf, int pageIndex) {
    if (pageIndex > 0) return NO_SUCH_PAGE;

    Graphics2D g2d = (Graphics2D) g;
    g2d.translate(pf.getImageableX(), pf.getImageableY());

    // 1. Größe der Druckfläche bestimmen
    int printW = (int) pf.getImageableWidth();
    int printH = (int) pf.getImageableHeight();

    // 2. Ein NEUES BufferedImage nur für den Drucker erstellen
    // Tipp: Wenn du printW * 2 nimmst, wird der Druck schärfer!
    BufferedImage printImg = new BufferedImage(printW, printH, BufferedImage.TYPE_INT_RGB);

    // 3. Das Fraktal direkt in das Druck-Bild berechnen
    for (int x = 0; x < printW; x++) {
        for (int y = 0; y < printH; y++) {
            // Hier nutzen wir deine bestehenden Logik-Methoden
            double iter = radiobutton.isSelected() ? 
                          computeMandelbrotMengeForPrint(x, y, printW, printH) : 
                          computeJuliaMengeForPrint(x, y, printW, printH);
            
            int color = (iter >= 1.0) ? 0 : Color.HSBtoRGB((float) (0.7f + iter), 0.8f, 1.0f);
            printImg.setRGB(x, y, color);
        }}
    // 4. Das Bild auf das Papier zeichnen
    g2d.drawImage(printImg, 0, 0, null);

    return PAGE_EXISTS;
}
    private double computeMandelbrotMengeForPrint(double xPixel, double yPixel, int w, int h) {
    double cRe = amin2 + (xPixel * (amax2 - amin2) / w);
    double cIm = bmin2 + (yPixel * (bmax2 - bmin2) / h);
    
    double zRe = 0, zIm = 0;
    int o = 0;
    while (zRe * zRe + zIm * zIm < 4.0 && o < iteration) {
        double nextRe = zRe * zRe - zIm * zIm + cRe;
        zIm = 2.0 * zRe * zIm + cIm;
        zRe = nextRe;
        o++;
    }
    return (double) o / iteration;
}

private double computeJuliaMengeForPrint(double xPixel, double yPixel, int w, int h) {
    double zRe = amin2 + (xPixel * (amax2 - amin2) / w);
    double zIm = bmin2 + (yPixel * (bmax2 - bmin2) / h);
    
    double cX = this.re; 
    double cY = this.im; 
    
    int o = 0;
    while (zRe * zRe + zIm * zIm < 4.0 && o < iteration) {
        double nextRe = zRe * zRe - zIm * zIm + cX;
        zIm = 2.0 * zRe * zIm + cY;
        zRe = nextRe;
        o++;
    }
    return (double) o / iteration;
}

  
public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. Das Hauptobjekt erstellen
            mainframe = new Mandelbrot();
            
            // 2. Das Fenster (JFrame) bauen
            JFrame win = new JFrame("Mandelbrot & Julia Fraktal");
            win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            win.setLayout(new BorderLayout());

            // 3. Das Tool-Panel mit Radiobutton und Druck-Button
            JPanel tools = new JPanel();
            // Direkt nachdem der Radiobutton erstellt wurde:
mainframe.radiobutton.addActionListener(e -> {
    // Erzwingt, dass die paintComponent-Methode neu aufgerufen wird
    mainframe.repaint(); 
    
    // Optional: Ändert den Fenstertitel, damit du weißt, was gerade aktiv ist
    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(mainframe);
    if (topFrame != null) {
        String modus = mainframe.radiobutton.isSelected() ? "Mandelbrot" : "Julia";
        topFrame.setTitle("Fraktal-Explorer - Modus: " + modus);
    }
});
            tools.add(mainframe.radiobutton);
            
            // Die Druck-Logik an den Button binden
            druckButton.addActionListener(e -> mainframe.drucken());
            tools.add(druckButton);

            // 4. Komponenten zum Fenster hinzufügen
            win.add(mainframe, BorderLayout.CENTER);
            win.add(tools, BorderLayout.SOUTH);

            // 5. WICHTIG: Die Maus-Listener am Fenster registrieren
            // Damit das Scrollen überall funktioniert
            win.addMouseWheelListener(mainframe);
            win.addMouseMotionListener(mainframe);

            // 6. Fenstergröße setzen und anzeigen
            win.setSize(1000, 1000);
            win.setLocationRelativeTo(null); // Fenster in die Mitte des Bildschirms
            win.setVisible(true);
            
            // Einmal neu zeichnen erzwingen
            mainframe.repaint();
        });
    }
} // <--- Das ist die allerletzte Klammer der Klasse Mandelbrot}
