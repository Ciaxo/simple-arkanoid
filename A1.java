import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class A1 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Tworzenie okna dialogowego z opcjami wyboru poziomu trudności
                String[] options = { "Easy", "Medium", "Hard" };
                int choice = JOptionPane.showOptionDialog(null, "Wybierz poziom gry:", "Wybor poziomu",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                // Tworzenie planszy
                Plansza p = new Plansza();

                // Tworzenie ramki i dodawanie planszy do ramki
                JFrame jf = new JFrame();
                jf.add(p);

                // Wybór mapy w zależności od wybranej opcji
                if (choice == 0){
                     p.generateEasyMap();    
                }else if (choice == 1) {
                    p.generateMediumMap();
                } else if (choice == 2){
                    p.generateHardMap();
                }
                
                // Dodawanie przycisku "Start" i obsługa zdarzenia
                JButton startButton = new JButton("Start");
                startButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Startowanie muzyki w tle w zależności od wybranego poziomu trudności
                        if (choice == 0){
                            p.startBackgroundMusicEasy();
                        }else if (choice == 1) {
                            p.startBackgroundMusicMedium();
                        } else if (choice == 2){
                            p.startBackgroundMusicHard();
                        }
                        // Rozpoczęcie gry i ukrycie przycisku "Start"
                        p.startGame();
                        startButton.setVisible(false);
                    }
                });

                // Tworzenie panelu przycisków i dodawanie przycisku "Start"
                JPanel buttonPanel = new JPanel();
                buttonPanel.add(startButton);
                jf.add(BorderLayout.SOUTH, buttonPanel);

                jf.setResizable(false); // Uniemożliwia zmianę rozmiaru okna
                jf.setTitle("A1");
                jf.setSize(400, 370);
                jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jf.setVisible(true);
            }
        });
    }
}