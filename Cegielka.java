import java.awt.*;
import java.awt.geom.*;

// Klasa reprezentująca cegielkę w grze
class Cegielka extends Rectangle2D.Float{
    int twardosc;
    private int points, id;
    private Image obrazek;

    // Konstruktor dla obiektu cegielki
    Cegielka(float x, float y, float width, float height, Image obrazek, int twardosc, int points, int id){
        super(x, y, width, height);
        this.obrazek = obrazek;
        this.twardosc = twardosc;
        this.points = points;
        this.id = id;
    }

    // Metoda rysująca obiekt cegielki na ekranie
    void rysuj(Graphics2D g2d) {
        g2d.drawImage(obrazek, (int) x, (int) y, null);
    }

    // Metoda zmieniająca obrazek cegielki
    void setObrazek(Image obrazek) {
        this.obrazek = obrazek;
    }

    // Metoda zwracająca identyfikator cegielki
    int getId(){
        return id;
    }

    // Metoda zwracająca poziom trudności cegielki
    int getTwardosc(){
        return twardosc;
    }

    // Metoda zmniejszająca poziom trudności cegielki po uderzeniu
    void zmniejszTwardosc(){
        twardosc--;
    }

    // Metoda zwracająca liczbę punktów za zniszczenie cegielki
    int getPoints() {
        return points;
    }
}