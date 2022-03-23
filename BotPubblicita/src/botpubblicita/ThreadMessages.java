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
                String value = BotPubblicita.getApi().getMessage();
                if (value.contains("/citta") && value.replaceAll(" ", "").length() > 6) {
                    if (BotPubblicita.getApi().SavedUsers()) {
                        if (BotPubblicita.getApi().saveCoordinates(value.replace("/citta", ""))) {
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
                            }
                        } else {
                            BotPubblicita.getApi().sendMessage("Salvataggio effettuato, Area selezionata: " + BotPubblicita.getApi().getPaese());
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
