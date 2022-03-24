package androidserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Utenti {

    private String name;
    private String password;

    public Utenti() {
        name = "";
        password = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void SaveUsers() throws IOException {
        File file = new File("Save.txt");
        if (file.createNewFile()) {
            FileWriter write = new FileWriter(file);
            write.append(name + " " + password + "\n");
            System.out.println(name + " " + password);
            write.close();
        } else {
            FileWriter write = new FileWriter(file);
            ArrayList<Utenti> users = readUsers();
            boolean event = true;
            for (int i = 0; i < users.size(); i++) {
                if (name.equals(users.get(i).name) && password.equals(users.get(i).password)) {
                    event = false;
                }
            }
            if (event) {
                write.append(name + " " + password + "\n");
                write.close();
            }
        }
    }

    public ArrayList<Utenti> readUsers() throws FileNotFoundException {
        File file = new File("Save.txt");
        ArrayList<Utenti> users = new ArrayList<>();
        if (file.exists()) {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                Utenti i = new Utenti();
                String[] data = scanner.nextLine().split(" ");
                i.setName(data[0]);
                i.setPassword(data[1]);
                users.add(i);
            }
        }
        return users;
    }

    public void deleteUser(String nome, String password) throws FileNotFoundException, IOException {
        File file = new File("Save.txt");
        ArrayList<Utenti> users = new ArrayList<>();
        if (file.exists()) {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                Utenti i = new Utenti();
                String[] data = scanner.nextLine().split(" ");
                i.setName(data[0]);
                i.setPassword(data[1]);
                users.add(i);
            }
            for (int i = 0; i < users.size(); i++) {
                if (nome.equals(users.get(i).getName()) && password.equals(users.get(i).getPassword())) {
                    users.remove(i);
                    break;
                }
            }
            for (int i = 0; i < users.size(); i++) {
                FileWriter write = new FileWriter(file);
                write.append(users.get(i).getName() + " " + users.get(i).getPassword() + "\n");
                write.close();
            }
        }
    }
}
