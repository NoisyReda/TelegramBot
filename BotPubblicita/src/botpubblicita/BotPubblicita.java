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
    private static UserInput ui;
    private static Advertisement ad;

    public static void main(String[] args) throws MalformedURLException, IOException, InterruptedException {
        running = true;
        a = new Api();
        ui = new UserInput();
        ThreadMessages messages = new ThreadMessages();
        ad = new Advertisement();
        messages.start();
        ad.start();
        ui.start();
        ui.join();
        messages.join();
        ad.join();
    }

    public static Advertisement getAd() {
        return ad;
    }

    public static Api getApi() {
        return a;
    }

    public static UserInput getUI() {
        return ui;
    }

    public static void setRunning() {
        running = !running;
    }

    public static boolean getrunning() {
        return running;
    }
}
