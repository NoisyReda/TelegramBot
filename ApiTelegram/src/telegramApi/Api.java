/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telegramApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

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

    public int getNumMes() throws MalformedURLException, IOException {
        URL url = new URL("https://api.telegram.org/bot5174828278:AAHX9FMh6t8Q-QYI2NN8pXQzLq9MTPp0Ny8/getUpdates");
        Scanner inRemote = new Scanner(url.openStream());
        inRemote.useDelimiter("\u001a");
        String content = inRemote.next();
        inRemote.close();
        JSONObject js = new JSONObject(content);
        JSONArray jsA = js.getJSONArray("result");
        try {
            return jsA.getJSONObject(0).getInt("update_id");
        } catch (JSONException exception) {
            return 0;
        }
    }

    public String getMessage() throws MalformedURLException, IOException {
        String messaggio = "";
        URL url = new URL("https://api.telegram.org/bot5174828278:AAHX9FMh6t8Q-QYI2NN8pXQzLq9MTPp0Ny8/getUpdates?offset=-1");
        Scanner inRemote = new Scanner(url.openStream());
        try {
            URL urldel = new URL("https://api.telegram.org/bot5174828278:AAHX9FMh6t8Q-QYI2NN8pXQzLq9MTPp0Ny8/getUpdates?offset=" + (getNumMes() + 1));
            urldel.openStream();
        } catch (IOException exception) {
            return "";
        }
        inRemote.useDelimiter("\u001a");
        String content = inRemote.next();
        inRemote.close();
        JSONObject js = new JSONObject(content);
        JSONArray jsA = js.getJSONArray("result");
        try {
            messaggio += jsA.getJSONObject(0).getJSONObject("message").getString("text");
            if (user == "" && userId == "") {
                try {
                    user = jsA.getJSONObject(0).getJSONObject("message").getJSONObject("from").getString("username");
                } catch (JSONException exception) {
                    try {
                        user = jsA.getJSONObject(0).getJSONObject("message").getJSONObject("from").getString("first_name") + " " + jsA.getJSONObject(0).getJSONObject("message").getJSONObject("from").getString("last_name");
                    } catch (JSONException exception1) {
                        user = jsA.getJSONObject(0).getJSONObject("message").getJSONObject("from").getString("first_name");
                    }
                }
                try {
                    userId = String.valueOf(jsA.getJSONObject(0).getJSONObject("message").getJSONObject("from").getLong("id"));
                } catch (JSONException exception) {
                    userId = String.valueOf(jsA.getJSONObject(0).getJSONObject("message").getJSONObject("chat").getLong("id"));
                }
            }
            return messaggio;
        } catch (Exception e) {
            return "";
        }
    }

    public void SaveAd(String city, String text, int range) throws IOException {
        if (Files.exists(Paths.get("advertisements.txt"))) {
            Files.write(Paths.get("advertisements.txt"), (city.trim() + ";" + text.trim() + ";" + range + "\n").getBytes(), StandardOpenOption.APPEND);
        } else {
            Files.createFile(Paths.get("advertisements.txt"));
            Files.write(Paths.get("advertisements.txt"), (city.trim() + ";" + text.trim() + ";" + range + "\n").getBytes(), StandardOpenOption.APPEND);
        }
    }

    public void sendToUser(String userId,String message) throws MalformedURLException, IOException {
        URL url = new URL("https://api.telegram.org/bot5174828278:AAHX9FMh6t8Q-QYI2NN8pXQzLq9MTPp0Ny8/sendmessage?chat_id=" + userId + "&text=" + message);
        url.openStream();
    }

    public ArrayList<ArrayList> checkCoordinates() throws FileNotFoundException {
        ArrayList<String> city = new ArrayList<>();
        ArrayList<String> text = new ArrayList<>();
        ArrayList<String> users = new ArrayList<>();
        if (new File("Users.txt").exists()) {
            BufferedReader bru = new BufferedReader(new FileReader("Users.txt"));
            try {
                String lineu = bru.readLine();
                while (lineu != null) {
                    if (new File("advertisements.txt").exists()) {
                        BufferedReader br = new BufferedReader(new FileReader("advertisements.txt"));
                        try {
                            String line = br.readLine();
                            while (line != null) {
                                double dlon = Math.toRadians(Double.parseDouble(line.split(";")[2])) - Math.toRadians(Double.parseDouble(lineu.split(";")[3]));
                                double dlat = Math.toRadians(Double.parseDouble(line.split(";")[1])) - Math.toRadians(Double.parseDouble(lineu.split(";")[2]));
                                double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(Math.toRadians(Double.parseDouble(lineu.split(";")[2]))) * Math.cos(Math.toRadians(Double.parseDouble(line.split(";")[1]))) * Math.pow(Math.sin(dlon / 2), 2);
                                double c = 2 * Math.asin(Math.sqrt(a));
                                if ((c * 6371) <= Double.parseDouble(line.split(";")[4])) {
                                    city.add(line.split(";")[0]);
                                    text.add(line.split(";")[3]);
                                    users.add(lineu.split(";")[0]);
                                }
                                line = br.readLine();
                            }
                            ArrayList<ArrayList> ar = new ArrayList<>();
                            ar.add(city);
                            ar.add(text);
                            ar.add(users);
                            return ar;
                        } catch (IOException exception) {
                            System.out.println(exception);
                        }
                    }
                }
            } catch (IOException exception) {
                System.out.println(exception);
            }
        }
        return null;
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
        paesi = new ArrayList<>();
        return paese;
    }

    public void sendMessage(String message) throws MalformedURLException, IOException {
        URL url = new URL("https://api.telegram.org/bot5174828278:AAHX9FMh6t8Q-QYI2NN8pXQzLq9MTPp0Ny8/sendmessage?chat_id=" + userId + "&text=" + message);
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
        URL url = new URL("https://api.telegram.org/bot5174828278:AAHX9FMh6t8Q-QYI2NN8pXQzLq9MTPp0Ny8/sendmessage?chat_id=" + userId + "&text=" + message + buttons);
        url.openStream();
    }

    public boolean saveCoordinates(String city) throws MalformedURLException, IOException {
        URL url = new URL("https://nominatim.openstreetmap.org/search?q=" + city + "&format=xml&addressdetails=1");
        Scanner inRemote = new Scanner(url.openStream());
        inRemote.useDelimiter("\u001a");
        String content = inRemote.next();
        inRemote.close();
        try {
            JSONArray jsA = XML.toJSONObject(content).getJSONObject("searchresults").getJSONArray("place");
            String message = "";
            String buttons = "[[";
            for (int i = 0; i < jsA.length(); i++) {
                message += (i + 1) + ") Luogo: " + String.valueOf(jsA.getJSONObject(i).getString("display_name") + "\n" + "- Latitudine: " + jsA.getJSONObject(i).getLong("lat") + "\n" + "- Longitudine: " + jsA.getJSONObject(i).getLong("lon")) + "\n\n";
                paesi.add(jsA.getJSONObject(i).getString("display_name") + ";" + jsA.getJSONObject(i).getLong("lat") + ";" + jsA.getJSONObject(i).getLong("lon"));
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
            return true;
        } catch (JSONException exception) {
            JSONObject js = XML.toJSONObject(content);
            paesi.add(js.getJSONObject("searchresults").getJSONObject("place").getString("display_name") + ";" + js.getJSONObject("searchresults").getJSONObject("place").getLong("lat") + ";" + js.getJSONObject("searchresults").getJSONObject("place").getLong("lon"));
            saveCoordinates(0);
            paese = paesi.get(0);
            return false;
        }
    }

    public void saveUser() throws IOException {
        Files.write(Paths.get("Users.txt"), (userId.trim() + ";" + user.trim() + "\n").getBytes(), StandardOpenOption.APPEND);
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
                        file += line + "\n";
                        line = br.readLine();
                    }
                }
                Files.write(Paths.get("Users.txt"), (file).getBytes(), StandardOpenOption.WRITE);
            } catch (IOException exception) {
                System.out.println(exception);
            }
        }
    }

    public void deleteLast() throws MalformedURLException, IOException {
        URL url = new URL("https://api.telegram.org/bot5174828278:AAHX9FMh6t8Q-QYI2NN8pXQzLq9MTPp0Ny8/deleteMessage?chat_id=" + userId + " &message_id=" + messageId);
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

    public void sendAdd() throws FileNotFoundException {
        if (new File("Users.txt").exists()) {
            BufferedReader br = new BufferedReader(new FileReader("Users.txt"));
            try {
                String line = br.readLine();
                while (line != null) {
                    userId = line.split(";")[0];
                    //invio messaggio
                    line = br.readLine();
                }
            } catch (IOException exception) {
                System.out.println(exception);
            }
        }
    }
}
