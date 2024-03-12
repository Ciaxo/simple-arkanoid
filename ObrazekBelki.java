import java.awt.*;
import java.awt.geom.*;

// Klasa reprezentująca obiekt belki w grze
class ObrazekBelki extends Rectangle2D.Float {
    private Image image;
    int pRazy;

    // Konstruktor dla obiektu belki
    ObrazekBelki(float x, float y, float width, float height, Image image) {
        super(x, y, width, height);
        this.image = image;
        this.pRazy = pRazy;
    }

    // Metoda rysująca obiekt belki na ekranie
    void rysuj(Graphics2D g2d) {
        g2d.drawImage(image, (int) x, (int) y, null);
    }

    // Metoda ustawiająca pozycję X obiektu belki
    void setX(int x) 
    {                
        this.x=x;     
    }

    // Metoda zwracająca ilość "punktów" obiektu belki
    int getPointsM() {
        return pRazy;
    }   
}