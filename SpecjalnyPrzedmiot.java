import java.awt.*;
import java.awt.geom.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

// Klasa reprezentująca specjalny przedmiot w grze
class SpecjalnyPrzedmiot extends Rectangle2D.Float {
    Plansza p;
    private float alpha = 1.0f;         // Początkowa wartość przezroczystości
    private int fadeOutTime = 10000;    // Czas znikania w milisekundach
    Clip takeItemSound, takeBadItemSound;
    private Image obrazek;
    int id;
    
    // Konstruktor dla obiektu specjalnego przedmiotu
    SpecjalnyPrzedmiot(Plansza p, float x, float y, float width, float height, Image obrazek, int id) {
        this.p = p;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.obrazek = obrazek;
        this.id = id;
    }

    // Metoda odtwarzająca dźwięk na podstawie danego obiektu Clip
    private void playSound(Clip clip) {
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }
    
    // Metoda obsługująca kolizję specjalnego przedmiotu z kulką
    void kolizjaZKulka(Kulka kulka, int id) {
        try {
            takeItemSound = AudioSystem.getClip();
            AudioInputStream tis = AudioSystem.getAudioInputStream(new File("sound/take_item.wav"));
            takeItemSound.open(tis);

            takeBadItemSound = AudioSystem.getClip();
            AudioInputStream tbis = AudioSystem.getAudioInputStream(new File("sound/bad_item.wav"));
            takeBadItemSound.open(tbis);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }

        // Sprawdzenie kolizji z kulką
        if (getBounds2D().intersects(kulka.getBounds2D()) && id == 0) {
            playSound(takeItemSound);
            p.removeSpecialItem(this);
            p.addPoints(100); // Dodaj punkty za zderzenie z kulką
        } else if (getBounds2D().intersects(kulka.getBounds2D()) && id == 1){
            playSound(takeBadItemSound);
            p.removeBadSpecialItem(this);
            p.addPoints(-50); // Odejmij punkty za zderzenie z kulką
        }
    }

    // Metoda rysująca obiekt specjalnego przedmiotu na ekranie
    void rysuj(Graphics2D g2d) {
        // Wartość alpha decyduje o przezroczystości obiektu
        if (alpha > 0.0f) {
            Composite originalComposite = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            // Rysuj obrazek
            g2d.drawImage(obrazek, (int) x, (int) y, null);
            g2d.setComposite(originalComposite);
        }
        
    }
    
    // Metoda aktualizująca wartość alpha w zależności od czasu dla specjalnego przedmiotu
    void updateAlpha(long elapsedTime) {
        if (fadeOutTime > 0) {
            float fadeStep = 1.0f / (float) fadeOutTime;
            alpha -= fadeStep * elapsedTime;
            alpha = Math.max(0.0f, alpha);
        }
    }

    // Metoda aktualizująca wartość alpha w zależności od czasu dla negatywnego specjalnego przedmiotu
    void updateBadAlpha(long elapsedTime) {
        if (fadeOutTime > 0) {
            float fadeStep = 1.0f / (float) fadeOutTime;
            alpha -= fadeStep * elapsedTime;
            alpha = Math.max(0.0f, alpha);
        }
    }
}