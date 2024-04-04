package com.mapreduce.mapreduce.actors;

import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.Map;

import com.mapreduce.mapreduce.messages.GetWordCountMessage;
import com.mapreduce.mapreduce.messages.Message;
import com.mapreduce.mapreduce.messages.WordCountMessage;

import akka.actor.UntypedActor;

public class Reducer extends UntypedActor {

    // private final Map<String, Integer> wordCounts = new HashMap<>();
    private final ArrayList<String> words = new ArrayList<>();

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Message) {
            String mot = ((Message) message).mot();
            words.add(mot);

            // wordCounts.put(mot, wordCounts.getOrDefault(mot, 0) + 1);
        } else if (message instanceof GetWordCountMessage) {
            int count = getWordCount(((GetWordCountMessage) message).word());
            getSender().tell(new WordCountMessage(count), getSender());
        }

        else {
            unhandled(message);
        }
    }

    public int getWordCount(String mot) {
        int count = 0;
        for (String word : words) {
            if (mot.equals(word)) {
                count++;
            }
        }
        return count;
    }
    // // Add a method to retrieve word counts for a given word
    // public int getWordCount(String word) {
    // return wordCounts.getOrDefault(word, 0);
    // }
}
