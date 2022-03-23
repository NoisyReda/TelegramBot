/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package botpubblicita;

import java.util.Scanner;

/**
 *
 * @author Mattia
 */
public class UserInput extends Thread {

    private boolean isopen;

    @Override
    public void run() {
        Scanner scan = new Scanner(System.in);
        while (BotPubblicita.getrunning()) {
            while (isopen == false) {
                System.out.println("1) Inserisci pubblicita'\n2) Esci");
                switch (scan.nextInt()) {
                    case 1 -> {
                        Inserimento_Pubblicita ip = new Inserimento_Pubblicita();
                        ip.setVisible(true);
                        ip.setAlwaysOnTop(true);
                        isopen = true;
                    }
                    case 2 -> {
                        BotPubblicita.setRunning();
                        isopen = true;
                        BotPubblicita.getAd().stop();
                    }
                }
            }
        }
    }

    public void setIsopen(boolean isopen) {
        this.isopen = isopen;
    }

}
