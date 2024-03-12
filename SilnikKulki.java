// Klasa reprezentująca silnik kulki
class SilnikKulki extends Thread
{
    Kulka a;

    // Konstruktor dla obiektu silnika kulki
    SilnikKulki(Kulka a) 
    {                    
        this.a=a;         
        start();    // Rozpoczęcie wątku silnika kulki
    }                    
 
    // Metoda uruchamiana podczas działania wątku silnika kulki
    public void run() {
        try {
            while (true) {
                // Pętla nieskończona, która aktualizuje położenie kulki i obsługuje kolizje
                a.nextKrok();   // Wywołanie metody odpowiedzialnej za aktualizację położenia kulki
                a.p.updateSpecialItemAlpha(15); // Przekazujemy czas dla aktualizacji przezroczystości
                a.p.updateBadSpecialItemAlpha(15); // Przekazujemy czas dla aktualizacji przezroczystości
                sleep(15);
            }
        } catch (InterruptedException e) {
        }
    }                                  
}