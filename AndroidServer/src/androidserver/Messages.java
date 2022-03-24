package androidserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mattia
 */
public class Messages extends Thread {

    @Override
    public void run() {
        try {
            ServerSocket ss;
            ss = new ServerSocket(6300);
            Socket s = ss.accept();
            while (Data.getInstance().isOnline()) {
                Utenti i = new Utenti();
                InputStreamReader isr = new InputStreamReader(s.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                String[] sv = br.readLine().split(" ");
                if (!"dellollo".equals(sv[0])) {
                    i.setName(sv[0]);
                    i.setPassword(sv[1]);
                    i.SaveUsers();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Messages.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
