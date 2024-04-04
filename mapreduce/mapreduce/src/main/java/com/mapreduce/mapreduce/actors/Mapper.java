package com.mapreduce.mapreduce.actors;

import java.util.ArrayList;

import com.mapreduce.mapreduce.messages.Message;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class Mapper extends UntypedActor {

    private final ArrayList<ActorRef> reducers;

    public Mapper(ArrayList<ActorRef> reducers) {
        this.reducers = reducers;
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof String) {
            String line = (String) message;
            String[] words = line.split(" ");

            for (int i = 0; i < words.length; i++) {
                // System.out.println(words[i]);
                partition(words[i].trim(), reducers).tell(new Message(words[i].trim()), getSender());
            }
        } else {
            unhandled(message);
        }
    }

    private ActorRef partition(String word, ArrayList<ActorRef> reducers) {

        // Utiliser un simple partitionneur basé sur le hachage
        // calculer un numéro de bucket compris entre 0 et réducteurs.length
        // (inclus) basé sur le mot.
        // Le numéro de bucket est ensuite utilisé pour déterminer quel acteur réducteur
        // doit gérer ce mot particulier.
        return reducers.get(Math.abs(word.hashCode()) % reducers.size());
    }

}
