import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import java.util.Timer;
import java.util.TimerTask;

// Klasa reprezentująca planszę gry
class Plansza extends JPanel implements MouseMotionListener
{
    ObrazekBelki b;  // Zmiana na ObrazekBelki
    Kulka a;
    SilnikKulki s;
    ArrayList<Cegielka> cegielki;
    int punkty = 0;
    boolean graRozpoczeta = false;
    boolean endGameFlag = false;

    SpecjalnyPrzedmiot specjalnyPrzedmiot, spBad;
    private Timer specjalnyPrzedmiotTimer, spBadTimer;
    int liczbaPojawien = 0;
    int liczbaPojawienBad = 0;
    byte mapId;
    int range, rand;

    private Clip backgroundMusicEasy, backgroundMusicMedium, backgroundMusicHard, startSpecialItemSound;

    Image spObrazek, spBadObrazek;
    Image tloEasy, tloMedium, tloHard;

    // Konstruktor dla obiektu planszy
    Plansza()                         
    {                                 
        super();                       
        addMouseMotionListener(this);   // Dodanie obsługi zdarzeń ruchu myszy

        // Inicjalizacja obiektu belki
        Image belkaImage = Toolkit.getDefaultToolkit().getImage("sprites/belka1.png"); // Zmień ścieżkę do obrazka belki
        b = new ObrazekBelki(100, 230, 60, 10, belkaImage);  // Utwórz obiekt ObrazekBelki              

        // Inicjalizacja obiektu kulki
        Image kulkaImage = Toolkit.getDefaultToolkit().getImage("sprites/kulkaaa.png"); // Zmień ścieżkę do obrazka kuli
        a=new Kulka(this,100,200,1,1, kulkaImage); 

        // Generowanie losowego tła w zależności od wartości rand
        int max = 3;
        int min = 1;
        int range = max - min + 1;
        rand = (int)(Math.random() * range) + min;
      
        if (rand == 1){
            tloEasy = Toolkit.getDefaultToolkit().getImage("bg/easy/tlo_easy1_gaus.jpg");
            tloMedium = Toolkit.getDefaultToolkit().getImage("bg/medium/tlo_medium_gaus.jpg");
            tloHard = Toolkit.getDefaultToolkit().getImage("bg/hard/tlo_hard_gaus.jpg");
        } else if (rand == 2){
            tloEasy = Toolkit.getDefaultToolkit().getImage("bg/easy/tlo_easy2_gaus.jpg");
            tloMedium = Toolkit.getDefaultToolkit().getImage("bg/medium/tlo_medium1_gaus.jpg");
            tloHard = Toolkit.getDefaultToolkit().getImage("bg/hard/tlo_hard1_gaus.jpg");
        } else{
            tloEasy = Toolkit.getDefaultToolkit().getImage("bg/easy/tlo_easy2_gaus.jpg");
            tloMedium = Toolkit.getDefaultToolkit().getImage("bg/medium/tlo_medium2_gaus.jpg");
            tloHard = Toolkit.getDefaultToolkit().getImage("bg/hard/tlo_hard2_gaus.jpg");
        }
      
        cegielki = new ArrayList<>();   // Inicjalizacja listy cegiełek

        // Inicjalizacja dźwięków
        try {
            startSpecialItemSound = AudioSystem.getClip();
            AudioInputStream ssis = AudioSystem.getAudioInputStream(new File("sound/appearance.wav"));
            startSpecialItemSound.open(ssis);

            backgroundMusicEasy = AudioSystem.getClip();
            AudioInputStream backgroundMusicStreamEasy = AudioSystem.getAudioInputStream(new File("sound/bg_easy.wav"));
            backgroundMusicEasy.open(backgroundMusicStreamEasy);

            backgroundMusicMedium = AudioSystem.getClip();
            AudioInputStream backgroundMusicStreamMedium = AudioSystem.getAudioInputStream(new File("sound/background_mid.wav"));
            backgroundMusicMedium.open(backgroundMusicStreamMedium);

            backgroundMusicHard = AudioSystem.getClip();
            AudioInputStream backgroundMusicStreamHard = AudioSystem.getAudioInputStream(new File("sound/background_hard.wav"));
            backgroundMusicHard.open(backgroundMusicStreamHard);

        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }  

    // Metoda generująca mapę o łatwym poziomie trudności
    void generateEasyMap() {
        int cRzad = 4;
        int cKolum = 6;
        float cSzer = 50;
        float cWys = 12;
        float odstep = 13;
        Image cegielkaImage;
        int id;
        mapId = 0;

        for (int i = 0; i < cRzad; i++) {
            for (int j = 0; j < cKolum; j++) {
                float cX = odstep + j * (cSzer + odstep);
                float cY = odstep + i * (cWys + odstep);
                cegielkaImage = Toolkit.getDefaultToolkit().getImage("sprites/cegielka1.png"); // Zmień ścieżkę do obrazka belki
                int twardosc = 1;
                int punkty = 1;
                id = 1;

                if (i == 2 && j % 2 == 0) {
                    cegielkaImage = Toolkit.getDefaultToolkit().getImage("sprites/cegielka2.png"); // Zmień ścieżkę do obrazka belki
                    twardosc = 2; // Ustaw twardość na 2
                    punkty = 5;
                    id = 2;
                }

                Cegielka c = new Cegielka(cX, cY, cSzer, cWys, cegielkaImage, twardosc, punkty, id);
                cegielki.add(c);
            }
          
        }
    }

    // Metoda generująca mapę o średnim poziomie trudności
    void generateMediumMap() {
        int cRzad = 5;
        int cKolum = 6;
        float cSzer = 50;
        float cWys = 12;
        float odstep = 12;
        Image cegielkaImage;
        int id;
        mapId = 1;
    

        for (int i = 0; i < cRzad; i++) {
            for (int j = 0; j < cKolum; j++) {
                float cX = odstep + j * (cSzer + odstep);
                float cY = odstep + i * (cWys + odstep);
                cegielkaImage = Toolkit.getDefaultToolkit().getImage("sprites/cegielka1.png"); // Zmień ścieżkę do obrazka belki
                int twardosc = 1;
                int punkty = 1;
                id = 1;

                if (i == 0 || i == 1) {
                    cegielkaImage = Toolkit.getDefaultToolkit().getImage("sprites/cegielka2.png"); // Zmień ścieżkę do obrazka belki
                    twardosc = 2; // Ustaw twardość na 2
                    punkty = 5;
                    id = 2;
                } else if (i == 2 && j % 2 == 0) {
                    cegielkaImage = Toolkit.getDefaultToolkit().getImage("sprites/cegielka3.png"); // Zmień ścieżkę do obrazka belki
                    twardosc = 3; // Ustaw twardość na 3
                    punkty = 10;
                    id = 3;
                }

                Cegielka c = new Cegielka(cX, cY, cSzer, cWys, cegielkaImage, twardosc, punkty, id);
                cegielki.add(c);
            }
        }
    }

    // Metoda generująca mapę o trudnym poziomie trudności
    void generateHardMap() {
        int cRzad = 5;
        int cKolum = 6;
        float cSzer = 50;
        float cWys = 12;
        float odstep = 12;
        Image cegielkaImage;
        int id;
        mapId = 2;

        for (int i = 0; i < cRzad; i++) {
            for (int j = 0; j < cKolum; j++) {
                float cX = odstep + j * (cSzer + odstep);
                float cY = odstep + i * (cWys + odstep);
                cegielkaImage = Toolkit.getDefaultToolkit().getImage("sprites/cegielka1.png"); // Zmień ścieżkę do obrazka belki
                int twardosc = 1;
                int punkty = 1;
                id = 1;

                if ((i == 3 && j == 1 || i == 3 && j == 3 || i == 3 && j == 5)) {
                    cegielkaImage = Toolkit.getDefaultToolkit().getImage("sprites/cegielka2.png"); // Zmień ścieżkę do obrazka belki
                    twardosc = 2; // Ustaw twardość na 2
                    punkty = 5;
                    id = 2;
                    // (i == 1 && j == 1 || i == 1 && j == 3 || i == 1 && j == 5) || i == 4 && j % 2 == 0
                } else if (i == 0 || i == 2 || i == 3 || i == 1 && j % 2 == 1) {
                    cegielkaImage = Toolkit.getDefaultToolkit().getImage("sprites/cegielka3.png"); // Zmień ścieżkę do obrazka belki
                    twardosc = 3; // Ustaw twardość na 3
                    punkty = 10;
                    id = 3;
                }

                Cegielka c = new Cegielka(cX, cY, cSzer, cWys, cegielkaImage, twardosc, punkty,id);
                cegielki.add(c);
            }
        }
    }


    // Metoda odtwarzająca dźwięk
    private void playSound(Clip clip) {
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    // Metoda generująca specjalny przedmiot
    void generateSpecialItem() {
        // Losowe ustawienie pozycji początkowej
        float x = (float) (Math.random() * (getWidth() - 20));
        float y = (float) (Math.random() * (getHeight() - 110));
        float width = 20;
        float height = 20;
        spObrazek = Toolkit.getDefaultToolkit().getImage("sprites/points1.png");
        specjalnyPrzedmiot = new SpecjalnyPrzedmiot(this, x, y, width, height, spObrazek, 0);
    }

    // Metoda generująca negatywny specjalny przedmiot
    void generateBadSpecialItem() {
        // Losowe ustawienie pozycji początkowej
        float x1 = (float) (Math.random() * (getWidth() - 20));
        float y1 = (float) (Math.random() * (getHeight() - 110));
        float width = 20;
        float height = 20;

        spBadObrazek = Toolkit.getDefaultToolkit().getImage("sprites/bad_points11.png"); // Zmień ścieżkę do obrazka belki
        spBad = new SpecjalnyPrzedmiot(this, x1, y1, width, height, spBadObrazek, 1);
    
    }  

    // Metoda rysująca specjalny przedmiot
    void paintSpecialItem(Graphics2D g2d) {
        if (specjalnyPrzedmiot != null) {
        specjalnyPrzedmiot.rysuj(g2d);
        }
    }

    // Metoda rysująca negatywny specjalny przedmiot
    void paintBadSpecialItem(Graphics2D g2d) {
        if (spBad != null) {
            spBad.rysuj(g2d);
        }
    }

    // Metoda aktualizująca przeźroczystość specjalnego przedmiotu
    void updateSpecialItemAlpha(long elapsedTime) {
        if (specjalnyPrzedmiot != null) {
            specjalnyPrzedmiot.updateAlpha(elapsedTime);
        }
    }

    // Metoda aktualizująca przeźroczystość negatywnego specjalnego przedmiotu
    void updateBadSpecialItemAlpha(long elapsedTime) {
        if (spBad != null) {
            spBad.updateBadAlpha(elapsedTime);
        }
    }

    // Metoda uruchamiająca timer dla specjalnego przedmiotu
    void startSpecjalnyPrzedmiotTimer() {
        specjalnyPrzedmiotTimer = new Timer();
        if (mapId == 2){
            specjalnyPrzedmiotTimer.schedule(new TimerTask() {
                public void run() {
                    generateSpecialItem();
                    repaint();
                    specjalnyPrzedmiotTimer.cancel(); // Zatrzymaj timer po pojawieniu się przedmiotu
                    playSound(startSpecialItemSound);
                    startSpecjalnyPrzedmiotDisappearTimer(); // Uruchom timer do znikania przedmiotu
                }
            }, (long) (Math.random() * 30000)); // Pojawi się po losowym czasie
        } else{
            specjalnyPrzedmiotTimer.schedule(new TimerTask() {
                public void run() {
                    generateSpecialItem();
                    repaint();
                    specjalnyPrzedmiotTimer.cancel(); // Zatrzymaj timer po pojawieniu się przedmiotu
                    playSound(startSpecialItemSound);
                    startSpecjalnyPrzedmiotDisappearTimer(); // Uruchom timer do znikania przedmiotu
                }
            }, (long) (Math.random() * 10000)); // Pojawi się po losowym czasie
        }
        
    }

    // Metoda uruchamiająca timer dla specjalnego przedmiotu
    void startSpecjalnyPrzedmiotDisappearTimer() {
        specjalnyPrzedmiotTimer = new Timer();
        if (mapId == 2){
            specjalnyPrzedmiotTimer.schedule(new TimerTask() {

                public void run() {
                    removeSpecialItem(specjalnyPrzedmiot);
                    liczbaPojawien++;
                    if (liczbaPojawien < 1) {
                        startSpecjalnyPrzedmiotTimer();
                    }
                }
            }, 5000); // Zniknie po 5 sekundach
        } else {
            specjalnyPrzedmiotTimer.schedule(new TimerTask() {

                public void run() {
                    removeSpecialItem(specjalnyPrzedmiot);
                    liczbaPojawien++;
                    if (liczbaPojawien < 5) {
                        startSpecjalnyPrzedmiotTimer();
                    }
                }
            }, 10000); // Zniknie po 10 sekundach
        }
        
    }

    // Metoda uruchamiająca timer dla negatywnego specjalnego przedmiotu
    void startBadSpecjalnyPrzedmiotTimer() {
        // Warunek sprawdzający, czy mapa nie jest łatwa
        if (mapId != 0){
            spBadTimer = new Timer();
            spBadTimer.schedule(new TimerTask() {

                public void run() {
                    generateBadSpecialItem();
                    repaint();
                    spBadTimer.cancel(); // Zatrzymaj timer po pojawieniu się przedmiotu
                    playSound(startSpecialItemSound);
                    startBadSpecjalnyPrzedmiotDisappearTimer(); // Uruchom timer do znikania przedmiotu
                }
            }, (long) (Math.random() * 10000)); // Pojawi się po losowym czasie
        }
    }

    // Metoda uruchamiająca timer dla negatywnego specjalnego przedmiotu
    void startBadSpecjalnyPrzedmiotDisappearTimer() {
        spBadTimer = new Timer();
        spBadTimer.schedule(new TimerTask() {

            public void run() {
                removeBadSpecialItem(spBad);
                liczbaPojawienBad++;
                if (liczbaPojawienBad < 3) {
                    startBadSpecjalnyPrzedmiotTimer();
                }
            }
        }, 10000); // Zniknie po 10 sekundach
    }

    // Metoda usuwająca specjalny przedmiot
    void removeSpecialItem(SpecjalnyPrzedmiot specjalnyPrzedmiot) {
        this.specjalnyPrzedmiot = null;
        repaint();
        if (specjalnyPrzedmiotTimer != null) {
            specjalnyPrzedmiotTimer.cancel(); // Zatrzymaj timer specjalnego przedmiotu
        }
    }

    // Metoda usuwająca negatywny specjalny przedmiot
    void removeBadSpecialItem(SpecjalnyPrzedmiot specjalnyPrzedmiot) {
        this.spBad = null;
        repaint();
        if (spBadTimer != null) {
            spBadTimer.cancel(); // Zatrzymaj timer specjalnego przedmiotu
        }
    }

    // Metoda sprawdzająca kolizję z specjalnym przedmiotem
    void checkSpecialItemCollision(Kulka kulka) {
        if (specjalnyPrzedmiot != null) {
            specjalnyPrzedmiot.kolizjaZKulka(kulka, 0);
        }
    }

    // Metoda sprawdzająca kolizję z negatywnym specjalnym przedmiotem
    void checkBadSpecialItemCollision(Kulka kulka) {
        if (spBad != null) {
            spBad.kolizjaZKulka(kulka, 1);
        }
    }

    // Metoda rysująca
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);            
        Graphics2D g2d = (Graphics2D) g;

        // Wybór tła w zależności od poziomu trudności
        if (graRozpoczeta) {
            if (mapId == 0) {
                g2d.drawImage(tloEasy, 0, 0, getWidth(), getHeight(), this);
            } else if (mapId == 1) {
                g2d.drawImage(tloMedium, 0, 0, getWidth(), getHeight(), this);
            } else if (mapId == 2) {
                g2d.drawImage(tloHard, 0, 0, getWidth(), getHeight(), this);
            }
        }
 
        // Rysowanie kulki, belki, cegiełek oraz specjalnych przedmiotów
        a.rysuj(g2d);
        b.rysuj(g2d);

        // Obsługa przypadku przegranej
        if (!endGameFlag){
            for(Cegielka c : cegielki){
                c.rysuj(g2d);
            }
        } else{
            removeSpecialItem(specjalnyPrzedmiot);
            removeBadSpecialItem(spBad);
        }
        

        paintSpecialItem(g2d);
        paintBadSpecialItem(g2d);

        // Komunikat przed rozpoczęciem gry
        if (!graRozpoczeta && !endGameFlag) {
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 15));
            g2d.drawString("Nacisnij Start, aby rozpoczac gre.", 70, 270);
        } else {
            // Wyświetlanie ilości punktów
            g2d.setColor(Color.GREEN);
            Font oldFont = g2d.getFont();
            g2d.setFont(oldFont.deriveFont(Font.BOLD, 15)); // Rozmiar i grubość
            g2d.drawString("Twoje Punkty: " + punkty, 10, 300);
        }
    }

    // Obsługa ruchu myszy
    public void mouseMoved(MouseEvent e) 
    {                                    
        if (graRozpoczeta) {
            b.setX(e.getX() - 50);
            repaint();
        }                        
    }                                    
 
    // Pusta metoda do obsługi przeciągania myszy
    public void mouseDragged(MouseEvent e) 
    {                                      
 
    }          

    // Metoda usuwająca cegielkę
    public void removeC(Cegielka c){
        cegielki.remove(c);
        repaint();
    }

    // Metoda dodająca punkty
    public void addPoints(int points) {
        punkty += points;
        repaint();
    }

    // Metoda zakończająca grę
    public void endGame() {
        graRozpoczeta = false;
        endGameFlag = true;
        s.interrupt();
        stopBackgroundMusic();
        showGameOverDialog();
    }

    // Metoda wyświetlająca okno z informacją o pregraniu gry
    private void showGameOverDialog() {
        JOptionPane.showMessageDialog(this, "Game Over! Uzyskales " + punkty + " punktow.", "Koniec gry", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    // Metoda wyświetlająca gratulacje
    public void showCongratulations() {
        stopBackgroundMusic();
        JOptionPane.showMessageDialog(this, "Gratulacje! Uzyskales " + punkty + " punktow.", "Koniec gry", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    // Metoda rozpoczynająca grę
    public void startGame() {
        if (!graRozpoczeta) {
            graRozpoczeta = true;
            s = new SilnikKulki(a);
            
            startSpecjalnyPrzedmiotTimer(); // Dodaj start timer'a specjalnego przedmiotu
            startBadSpecjalnyPrzedmiotTimer(); // Dodaj start timer'a negatywnego specjalnego przedmiotu
            repaint();
        }
    }

    // Metoda rozpoczynająca odtwarzanie muzyki w tle na łatwym poziomie trudności
    void startBackgroundMusicEasy() {
        if (backgroundMusicEasy != null && !backgroundMusicEasy.isRunning()) {
            backgroundMusicEasy.start();
        }
    }

    // Metoda rozpoczynająca odtwarzanie muzyki w tle na średnim poziomie trudności
    void startBackgroundMusicMedium() {
        if (backgroundMusicMedium != null && !backgroundMusicMedium.isRunning()) {
            backgroundMusicMedium.start();
        }
    }

    // Metoda rozpoczynająca odtwarzanie muzyki w tle na trudnym poziomie trudności
    void startBackgroundMusicHard() {
        if (backgroundMusicHard != null && !backgroundMusicHard.isRunning()) {
            backgroundMusicHard.start();
        }
    }

    // Metoda zatrzymująca odtwarzanie muzyki w tle
    private void stopBackgroundMusic() {
        if (backgroundMusicEasy != null && backgroundMusicEasy.isRunning()) {
            backgroundMusicEasy.stop();
        }
        if (backgroundMusicMedium != null && backgroundMusicMedium.isRunning()) {
            backgroundMusicMedium.stop();
        }
        if (backgroundMusicHard != null && backgroundMusicHard.isRunning()) {
            backgroundMusicHard.stop();
        }
    }

    // Metoda zatrzymująca grę, ustawiająca flagę rozpoczęcia gry na false i przerywająca wątek SilnikKulki
    public void stopGame() {
        graRozpoczeta = false;
        s.interrupt();
    }
}
