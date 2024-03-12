import java.awt.*;
import java.awt.geom.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

// Klasa reprezentująca kulę w grze
class Kulka extends Ellipse2D.Float
{
    Plansza p;
    int dx,dy;              // Prędkość kulki w kierunku osi x i y
    Clip hitBelka;
    Clip hitSciana;
    Clip hitCegielka;
    Clip gOver, win;
   
    private Image image;    // Obrazek reprezentujący kulę
 
    // Konstruktor dla obiektu kuli
    Kulka(Plansza p,int x,int y,int dx,int dy, Image image) 
    {                                          
        this.x=x;                               
        this.y=y;                               
        this.width=10;                          
        this.height=10;                         
 
        this.p=p;                               
        this.dx=dx;                             
        this.dy=dy;
        this.image = image;
      
      
        try {
            // Inicjalizacja obiektów dźwiękowych i załadowanie dźwięków
            hitBelka = AudioSystem.getClip();
            hitSciana = AudioSystem.getClip();
            hitCegielka = AudioSystem.getClip();
            gOver = AudioSystem.getClip();
            win = AudioSystem.getClip();

            AudioInputStream hitPaddleStream = AudioSystem.getAudioInputStream(new File("sound/uderzenie.wav"));
            AudioInputStream hitWallStream = AudioSystem.getAudioInputStream(new File("sound/sciana.wav"));
            AudioInputStream destroyBrickStream = AudioSystem.getAudioInputStream(new File("sound/zbicie.wav"));
            AudioInputStream gameOver = AudioSystem.getAudioInputStream(new File("sound/game_over.wav"));
            AudioInputStream wygrana = AudioSystem.getAudioInputStream(new File("sound/win.wav"));

            hitBelka.open(hitPaddleStream);
            hitSciana.open(hitWallStream);
            hitCegielka.open(destroyBrickStream);
            gOver.open(gameOver);
            win.open(wygrana);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    // Metoda rysująca obiekt kuli na ekranie
    void rysuj(Graphics2D g2d) {
        g2d.drawImage(image, (int) x, (int) y, null);
    }
 
    // Metoda wywoływana w każdej klatce animacji, aktualizuje położenie kulki i obsługuje kolizje
    void nextKrok()                                        
    {                                                     
        x+=dx;                                             
        y+=dy;                                             
 
        // Obsługa odbicia od ścian planszy
        if(getMinX()<0 || getMaxX()>p.getWidth()){
            dx = -dx;
            playSound(hitSciana);
        }   
        if(getMinY()<0 || getMaxY()>p.getHeight()){
            dy = -dy;
            playSound(hitSciana);
        }
      
        // Sprawdzenie, czy kulka dotarła do dolnej krawędzi planszy
        if ((getMinY()+10) > p.getHeight()){
            playSound(gOver);
            p.endGame();
        }
       
        // Obsługa odbicia kulki od belki
        if (getBounds2D().intersects(p.b.getBounds2D())) {
            odbijOdBelki(p.b);
            playSound(hitSciana);
        } 
 
        // Obsługa kolizji z cegiełkami
        for (Cegielka c : p.cegielki) {
            if (getBounds2D().intersects(c.getBounds2D())) {
                c.zmniejszTwardosc();
                if (c.getTwardosc() <= 0) {
                    p.removeC(c);
                    playSound(hitCegielka);
                    p.addPoints(c.getPoints());
                } else {
                    playSound(hitBelka); 
                    if (c.getTwardosc() == 1 && c.getId() == 2) {
                        // Zmiana obrazka zółtej cegielki po uderzeniu
                        Image nowyObrazek = Toolkit.getDefaultToolkit().getImage("sprites/cegielka22.png"); 
                        c.setObrazek(nowyObrazek);
                    } else if (c.getTwardosc() == 2 && c.getId() == 3) {
                        // Zmiana obrazka czerwonej cegielki po uderzeniu
                        Image nowyObrazek = Toolkit.getDefaultToolkit().getImage("sprites/cegielka33.png"); 
                        c.setObrazek(nowyObrazek);
                    } else if (c.getTwardosc() == 1 && c.getId() == 3) {
                        // Zmiana obrazka czerwonej cegielki po uderzeniu
                        Image nowyObrazek = Toolkit.getDefaultToolkit().getImage("sprites/cegielka333.png"); 
                        c.setObrazek(nowyObrazek);
                    }
                }
                
                Rectangle2D intersection = getBounds2D().createIntersection(c.getBounds2D());
                if (intersection.getWidth() < intersection.getHeight()) {
                    // Odbicie od bocznych krawędzi 
                    dx = -dx;
                } else {
                    // Odbicie od górnej/dolnej krawędzi (zmiana kierunku y)
                    dy = -dy;
                }
                
                break;
            }
        }
        
        // Sprawdzenie kolizji z przedmiotami specjalnymi
        p.checkSpecialItemCollision(this);
        p.checkBadSpecialItemCollision(this);

        // Sprawdzenie warunków zwycięstwa lub porażki
        if (p.cegielki.isEmpty() && p.punkty >= 0) {
            playSound(win);
            p.showCongratulations();
        }
        else if (p.cegielki.isEmpty() && p.punkty < 0){
            playSound(gOver);
            p.endGame();
        }

        p.repaint();    // Odświeżenie widoku planszy
        Toolkit.getDefaultToolkit().sync();     // Synchronizacja z systemem operacyjnym
    }

    // Metoda obsługująca odbicie kulki od belki
    private void odbijOdBelki(ObrazekBelki belka) {
        // Obliiczamy odległość od środka belki do miejsca uderzenia kulki
        double odlegloscOdSrodka = x - (belka.getX() + belka.getWidth() / 2);

        // Obliiczamy maksymalną odległość od środka belki
        double maxOdleglosc = belka.getWidth() / 2 + width / 2;
        
        // Obliczenie normalizowanej odległości
        double nOdleglosc = odlegloscOdSrodka / maxOdleglosc;

        // Ustawienie kierunku odbicia kulki na podstawie normalizowanej odległości
        dx = (int) (nOdleglosc * 5); // Przyśpieszenie w bok
        dy = -dy; // Odbij w pionie

        // Ustawiamy nową pozycję kulki
        y = (int) (belka.getY() - height);

        // Poprawka prędkości kulki, aby uniknąć zbyt wolnego ruchu
        if (dx > 0 && dx < 2) {
            dx = 2;
        } else if (dx < 0 && dx > -2) {
            dx = -2;
        } else if (dy < 0 && dy > -3) {
            dy = -3;
        }  
    }

    // Metoda do odtwarzania dźwięku
    private void playSound(Clip clip) {
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }
}