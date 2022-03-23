/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package botpubblicita;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mattia
 */
public class Advertisement extends Thread {

    @Override
    public void run() {
        while (BotPubblicita.getrunning()) {
            try {
                ArrayList<ArrayList> ar = BotPubblicita.getApi().checkCoordinates();
                for (int i = 0; i < ar.get(2).size(); i++) {
                    BotPubblicita.getApi().sendToUser(ar.get(2).get(i).toString(), "Pubblicita': " + ar.get(0).get(i) + "%0A" + ar.get(1).get(i));
                }
                Thread.sleep(100000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Advertisement.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Advertisement.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Advertisement.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
