/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package botpubblicita;

/**
 *
 * @author Mattia
 */
public class Advertisement extends Thread {

    @Override
    public void run() {
        while (BotPubblicita.getrunning()) {
            if (BotPubblicita.isAdSet()) {

            }
        }
    }

}
