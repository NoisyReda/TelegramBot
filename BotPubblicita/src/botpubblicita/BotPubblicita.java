/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package botpubblicita;

import java.io.IOException;
import java.net.MalformedURLException;
import telegramApi.*;

/**
 *
 * @author Redaelli_Mattia03
 */
public class BotPubblicita {

    /**
     * @param args the command line arguments
     */
    private static Api a;
    private static boolean running;
    private static boolean adSet;

    public static void main(String[] args) throws MalformedURLException, IOException, InterruptedException {
        running = true;
        a = new Api();
        ThreadMessages messages = new ThreadMessages();
        messages.start();
        messages.join();
    }

    public static Api getApi() {
        return a;
    }

    public static boolean getrunning() {
        return running;
    }

    public static boolean isAdSet() {
        return adSet;
    }

    public static void setAdSet(boolean adSet) {
        BotPubblicita.adSet = adSet;
    }
        
}
