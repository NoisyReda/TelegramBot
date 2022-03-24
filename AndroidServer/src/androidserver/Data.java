package androidserver;

/**
 *
 * @author Mattia
 */
public class Data {

    private static Data _instance;
    private boolean Online;

    Data() {
        Online = false;
    }

    public synchronized boolean isOnline() {
        return Online;
    }

    public synchronized void setOnline(boolean Online) {
        this.Online = Online;
    }

    public static Data getInstance() {
        if (_instance == null)
            synchronized (Data.class) {
            if (_instance == null) {
                _instance = new Data();
            }
        }
        return _instance;
    }

}
