/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telegramApi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Redaelli_Mattia03
 */
public class Api {

    private String user;
    private String userId;
    private ArrayList<String> paesi;
    private String paese;
    private int messageId;

    public Api() {
        user = "";
        userId = "";
        paesi = new ArrayList<>();
        messageId = 0;
        paese = "";
    }

    public String getUser() {
        return user;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessages() throws MalformedURLException, IOException {
        String messaggio = "";
        URL url = new URL("https://api.telegram.org/bot5174828278:AAHX9FMh6t8Q-QYI2NN8pXQzLq9MTPp0Ny8/getUpdates");
        Scanner inRemote = new Scanner(url.openStream());
        inRemote.useDelimiter("\u001a");
        String content = inRemote.next();
        inRemote.close();
        JSONObject js = new JSONObject(content);
        JSONArray jsA = js.getJSONArray("result");
        for (int i = 0; i < jsA.length(); i++) {
            messaggio += jsA.getJSONObject(i).getJSONObject("message").getString("text") + "\n";
        }
        return messaggio;
    }

    public String getMessage() throws MalformedURLException, IOException {
        String messaggio = "";
        URL url = new URL("https://api.telegram.org/bot5174828278:AAHX9FMh6t8Q-QYI2NN8pXQzLq9MTPp0Ny8/getUpdates?offset=-1");
        Scanner inRemote = new Scanner(url.openStream());
        inRemote.useDelimiter("\u001a");
        String content = inRemote.next();
        inRemote.close();
        JSONObject js = new JSONObject(content);
        JSONArray jsA = js.getJSONArray("result");
        try {
            messaggio += jsA.getJSONObject(0).getJSONObject("message").getString("text");
            user = jsA.getJSONObject(0).getJSONObject("message").getJSONObject("from").getString("username");
            userId = String.valueOf(jsA.getJSONObject(0).getJSONObject("message").getJSONObject("from").getLong("id"));
            return messaggio;
        } catch (Exception e) {
            return "";
        }
    }

    public String getLocation() throws MalformedURLException, IOException {
        String messaggio = "";
        URL url = new URL("https://api.telegram.org/bot5174828278:AAHX9FMh6t8Q-QYI2NN8pXQzLq9MTPp0Ny8/getUpdates?offset=-1");
        Scanner inRemote = new Scanner(url.openStream());
        inRemote.useDelimiter("\u001a");
        String content = inRemote.next();
        inRemote.close();
        JSONObject js = new JSONObject(content);
        JSONArray jsA = js.getJSONArray("result");
        try {
            messaggio += jsA.getJSONObject(0).getJSONObject("callback_query").getString("data");
            messaggio = messaggio.substring(6, 7);
            saveCoordinates(Integer.parseInt(messaggio.trim()) - 1);
            messageId = jsA.getJSONObject(0).getJSONObject("callback_query").getJSONObject("message").getInt("message_id");
            paese = paesi.get(Integer.parseInt(messaggio.trim()) - 1).split(";")[0];
            return paesi.get(Integer.parseInt(messaggio.trim()) - 1);
        } catch (Exception e) {
            return "";
        }
    }

    public String getPaese() {
        return paese;
    }

    public void sendMessage(String message) throws MalformedURLException, IOException {
        URL url = new URL("https://api.telegram.org/bot5174828278:AAHX9FMh6t8Q-QYI2NN8pXQzLq9MTPp0Ny8/sendmessage?chat_id=494814541&text=" + message);
        url.openStream();
    }

    public boolean isAdSet() throws FileNotFoundException {
        if (new File("Users.txt").exists()) {
            BufferedReader br = new BufferedReader(new FileReader("Users.txt"));
            try {
                String line = br.readLine();
                while (line != null) {
                    if (line.split(";")[0].equals(userId) && line.split(";")[1].trim().equals(user)) {
                        if (line.length() > 2) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        line = br.readLine();
                    }
                }
                return false;
            } catch (IOException exception) {
                System.out.println(exception);
            }
        }
        return false;
    }

    public void sendButton(String message, String buttons) throws MalformedURLException, IOException {
        URL url = new URL("https://api.telegram.org/bot5174828278:AAHX9FMh6t8Q-QYI2NN8pXQzLq9MTPp0Ny8/sendmessage?chat_id=494814541&text=" + message + buttons);
        url.openStream();
    }

    public void saveCoordinates(String city) throws MalformedURLException, IOException {
        URL url = new URL("https://nominatim.openstreetmap.org/search?q=" + city + "&format=");
        Scanner inRemote = new Scanner(url.openStream());
        inRemote.useDelimiter("\u001a");
        String content = inRemote.next();
        inRemote.close();
        JSONArray jsA = new JSONArray(content);
        String message = "";
        String buttons = "[[";
        for (int i = 0; i < jsA.length(); i++) {
            message += (i + 1) + ") Luogo: " + String.valueOf(jsA.getJSONObject(i).getString("display_name") + "\n" + "- Latitudine: " + jsA.getJSONObject(i).getString("lat") + "\n" + "- Longitudine: " + jsA.getJSONObject(i).getString("lon")) + "\n\n";
            paesi.add(jsA.getJSONObject(i).getString("display_name") + ";" + jsA.getJSONObject(i).getString("lat") + ";" + jsA.getJSONObject(i).getString("lon"));
            if (i % 8 != 0 || i == 0) {
                if (i + 1 != jsA.length()) {
                    buttons += "{\"text\": \"" + (i + 1) + " \", \"callback_data\": \"button" + (i + 1) + "\"},";
                } else {
                    buttons += "{\"text\": \"" + (i + 1) + " \", \"callback_data\": \"button" + (i + 1) + "\"}]]";
                }
            } else {
                buttons = buttons.substring(0, buttons.length() - 1);
                buttons += "],[{\"text\": \"" + (i + 1) + " \", \"callback_data\": \"button" + (i + 1) + "\"},";
            }
        }
        sendButton(URLEncoder.encode(message, StandardCharsets.UTF_8.toString()), "&reply_markup=" + "{\"inline_keyboard\":" + buttons + "}");
    }

    public void saveUser() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("Users.txt"));
        writer.append(userId.trim() + ";" + user.trim() + "\n");
        writer.close();
    }

    public void saveCoordinates(int pos) throws FileNotFoundException {
        if (new File("Users.txt").exists()) {
            BufferedReader br = new BufferedReader(new FileReader("Users.txt"));
            try {
                String line = br.readLine(), file = "";
                while (line != null) {
                    if (line.split(";")[0].equals(userId) && line.split(";")[1].trim().equals(user)) {
                        if (line.split(";").length < 3) {
                            line = line.trim();
                            line += ";" + paesi.get(pos).split(";")[1] + ";" + paesi.get(pos).split(";")[2] + "\n";
                            file += line;
                            line = br.readLine();
                        } else {
                            line = line.split(";")[0] + ";" + line.split(";")[1] + ";" + paesi.get(pos).split(";")[1] + ";" + paesi.get(pos).split(";")[2] + "\n";
                            file += line;
                            line = br.readLine();
                        }
                    } else {
                        file += line;
                        line = br.readLine();
                    }
                }
                BufferedWriter writer = new BufferedWriter(new FileWriter("Users.txt"));
                writer.write(file);
                writer.close();
            } catch (IOException exception) {
                System.out.println(exception);
            }
        }
    }

    public void deleteLast() throws MalformedURLException, IOException {
        URL url = new URL("https://api.telegram.org/bot5174828278:AAHX9FMh6t8Q-QYI2NN8pXQzLq9MTPp0Ny8/deleteMessage?chat_id=494814541&message_id=" + messageId);
        url.openStream();
    }

    public boolean SavedUsers() throws FileNotFoundException, IOException {
        if (new File("Users.txt").exists()) {
            BufferedReader br = new BufferedReader(new FileReader("Users.txt"));
            try {
                String line = br.readLine();
                while (line != null) {
                    if (line.split(";")[0].equals(userId) && line.split(";")[1].trim().equals(user)) {
                        return true;
                    } else {
                        line = br.readLine();
                    }
                }
                return false;
            } catch (IOException exception) {
                System.out.println(exception);
            }
        }
        return false;
    }
}
