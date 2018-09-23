/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facebookreader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import org.json.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

/**
 *
 * @author Matth
 */
public class FacebookReader {

    static class comment {

        String timestamp;
        String comment;
    }

    static class friends {

        ArrayList<friend> friends;
        ArrayList<friend> friendRequests;
        ArrayList<friend> friendFormer;
        ArrayList<friend> friendUnwanted;
    }

    class friend {

        String name;
        String timestamp;
    }

    class likes {
    }

    static class people {

        String name;
        int wordCount = 0;
        ArrayList<message> messages;
    }

    static class message {

        String sender;
        String timestamp;
        String content;
    }

    static class searchHistory {

        String timeStamp;
        String search;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String fileLocation = "C:\\Users\\Matth\\facebook\\";
        ArrayList<comment> listOfComments = new ArrayList();
        listOfComments = getComments(fileLocation);
        HashMap<String, people> messageSet = getMessages(fileLocation);
        for (int i = 0; i < messageSet.get("Om Sharan").messages.size(); i++) {
            System.out.println(Long.parseLong(messageSet.get("Om Sharan").messages.get(0).timestamp) - Long.parseLong(messageSet.get("Om Sharan").messages.get(i).timestamp));
        }
        System.out.println(listOfComments.size());

    }

    private static ArrayList getComments(String baseDirectory) {
        ArrayList<comment> listOfComments = new ArrayList();
        String location = baseDirectory + "comments\\comments.json";
        File file = new File(location);
        String content = "";
        JSONObject tomJsonObject = null;
        try {
            content = FileUtils.readFileToString(file, "utf-8");
            tomJsonObject = new JSONObject(content);
        } catch (IOException ex) {
            Logger.getLogger(FacebookReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(FacebookReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            JSONArray comments = tomJsonObject.getJSONArray("comments");
            JSONArray commentData = null;
            JSONObject temp;
            for (int i = 0; i < comments.length(); i++) {
                //temp =(comments.get(i));
                temp = new JSONObject(comments.get(i).toString());
                commentData = temp.getJSONArray("data");
                temp = new JSONObject(commentData.get(0).toString());
                temp = temp.getJSONObject("comment");
                comment tempComment = new comment();
                try {
                    //System.out.println(temp.get("comment") + "," + temp.get("timestamp"));
                    tempComment.comment = temp.get("comment").toString();
                    tempComment.timestamp = temp.get("timestamp").toString();
                    listOfComments.add(tempComment);
                } catch (JSONException e) {

                };
                //temp.get("comment");
                //temp.get("timestamp");

            }
        } catch (JSONException ex) {
            Logger.getLogger(FacebookReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listOfComments;
    }

    private static void getFriends(String baseDirectory) {
        String[] fileNames = {"friends.json", "received_friend_requests.json", "removed_friends.json", "sent_friend_requests.json"};
        String location = baseDirectory + "friends\\" + fileNames[0];
        location = baseDirectory + "friends\\" + fileNames[1];
        location = baseDirectory + "friends\\" + fileNames[2];
        location = baseDirectory + "friends\\" + fileNames[3];
    }

    private static HashMap getMessages(String baseDirectory) {
        File[] directories = new File(baseDirectory + "\\messages").listFiles(File::isDirectory);
        HashMap<String, people> people = new HashMap<>();

        for (File directorie : directories) {
            String uri = directorie + "\\message.json";
            File file = new File(uri);
            String content = "";
            JSONObject tomJsonObject = null;
            try {
                content = FileUtils.readFileToString(file, "utf-8");
                tomJsonObject = new JSONObject(content);
                try {
                    JSONArray participants = tomJsonObject.getJSONArray("participants");
                    if (participants.length() > 2) {
                        //System.out.println("Too many people"); 
                    } else {
                        JSONObject temp = new JSONObject(participants.get(0).toString());
                        //System.out.print();
                        JSONArray messages = tomJsonObject.getJSONArray("messages");
                        ArrayList<message> listOfMessages = new ArrayList();
                        String nameOfPerson = temp.get("name").toString();
                        int countOfWords = 0;
                        int countOfSwearWords = 0;
                        for (int i = 0; i < messages.length(); i++) {
                            JSONObject temp2 = new JSONObject(messages.get(i).toString());
                            //System.out.println(temp2.get("sender_name"));
                            message transferMessage = new message();
                            try {
                                transferMessage.content = temp2.get("content").toString();
                                countOfWords = countOfWords + countWords(transferMessage.content);
                            } catch (JSONException ex) {
                            }
                            try {
                                transferMessage.sender = temp2.get("sender_name").toString();
                                if (!transferMessage.sender.contains("Matt Gaiser")) {
                                    countOfSwearWords = countOfSwearWords + countSwearWords(transferMessage.content);

                                }
                            } catch (JSONException ex) {
                            }
                            transferMessage.timestamp = temp2.get("timestamp_ms").toString();
                            listOfMessages.add(transferMessage);
                        }
                        people tempPeople = new people();
                        tempPeople.messages = listOfMessages;
                        tempPeople.name = nameOfPerson;
                        people.put(nameOfPerson, tempPeople);

                        //System.out.println(nameOfPerson + "," + listOfMessages.size() + "," + countOfWords + "," + countOfSwearWords);
                        //System.out.println(messages.length());
                        // begin adding messages to array here
                    }
                    //JSONArray commentData = null;
                    //JSONObject temp;
                } catch (JSONException ex) {
                    //Logger.getLogger(FacebookReader.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                //Logger.getLogger(FacebookReader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException ex) {
                //Logger.getLogger(FacebookReader.class.getName()).log(Level.SEVERE, null, ex);
            }
            //System.out.println(tomJsonObject);

        }
        return people;
    }

    private static void getSearch(String baseDirectory) {

    }

    public static int countWords(String s) {

        int wordCount = 0;

        boolean word = false;
        int endOfLine = s.length() - 1;

        for (int i = 0; i < s.length(); i++) {
            // if the char is a letter, word = true.
            if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
                word = true;
                // if char isn't a letter and there have been letters before,
                // counter goes up.
            } else if (!Character.isLetter(s.charAt(i)) && word) {
                wordCount++;
                word = false;
                // last word of String; if it doesn't end with a non letter, it
                // wouldn't count without this.
            } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
                wordCount++;
            }
        }
        return wordCount;
    }

    public static int countSwearWords(String s) {
        return countingWord(s, "sex"); //+ countingWord(s, "shit") + countingWord(s, "crap") + countingWord(s, "damn");
    }

    private static int countingWord(String value, String findWord) {
        int counter = 0;
        try {
            while (value.contains(findWord)) {
                int index = value.indexOf(findWord);
                value = value.substring(index + findWord.length(), value.length());
                counter++;
            }
            return counter;
        } catch (NullPointerException e) {
            return 0; 
        }
    }

}
