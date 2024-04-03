package com.mapreduce.mapreduce.actors;

import java.util.HashMap;
import java.util.Map;

import akka.actor.UntypedActor;

public class Reducer extends UntypedActor {

    private final Map<String, Integer> wordCounts = new HashMap<>();

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Message) {
            String mot = ((Message) message).mot();
            wordCounts.put(mot, wordCounts.getOrDefault(mot, 0) + 1);
        } else {
            unhandled(message);
        }
    }

    // Add a method to retrieve word counts for a given word
    public int getWordCount(String word) {
        return wordCounts.getOrDefault(word, 0);
    }
}
