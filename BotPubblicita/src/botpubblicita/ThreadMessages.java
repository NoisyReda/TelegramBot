/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package botpubblicita;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mattia
 */
public class ThreadMessages extends Thread {

    @Override
    public void run() {
        while (BotPubblicita.getrunning()) {
            try {                
                if (BotPubblicita.getApi().getMessage().equals("/citta")) {
                    if (BotPubblicita.getApi().SavedUsers()) {  
                        if(BotPubblicita.getApi().isAdSet())BotPubblicita.setAdSet(true);
                        BotPubblicita.getApi().sendMessage("Dove abiti?");
                        while (BotPubblicita.getApi().getMessage().equals("/citta")) {
                        }
                        BotPubblicita.getApi().saveCoordinates(BotPubblicita.getApi().getMessage());
                        int i = 0;
                        while (BotPubblicita.getApi().getLocation().equals("") && i < 10) {
                            Thread.sleep(1000);
                            i++;
                        }
                        if (i == 10) {
                            BotPubblicita.getApi().sendMessage("Tempo scaduto");
                            BotPubblicita.getApi().deleteLast();
                        } else {
                            BotPubblicita.getApi().sendMessage("Salvataggio effettuato, Area selezionata: " + BotPubblicita.getApi().getPaese());
                            BotPubblicita.getApi().deleteLast();
                            BotPubblicita.setAdSet(true);
                        }
                    } else {
                        BotPubblicita.getApi().saveUser();
                    }
                }
                Thread.sleep(1000);
            } catch (IOException ex) {
                Logger.getLogger(ThreadMessages.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadMessages.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
