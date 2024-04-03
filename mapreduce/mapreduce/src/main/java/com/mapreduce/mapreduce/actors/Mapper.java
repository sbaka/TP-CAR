package com.mapreduce.mapreduce.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class Mapper extends UntypedActor {

    @Override
    public void onReceive(Object message) {
        if (message instanceof String) {
            String line = (String) message;
            String[] words = line.split(" ");
            for (String word : words) {
                partition(word, null).tell(words, getSender());
            }
        } else {
            unhandled(message);
        }
    }

    private ActorRef partition(String word, ActorRef[] reducers) {

        // Utiliser un simple partitionneur basé sur le hachage
        // calculer un numéro de bucket compris entre 0 et réducteurs.length - 1
        // (inclus) basé sur le mot.
        // Le numéro de bucket est ensuite utilisé pour déterminer quel acteur réducteur
        // doit gérer ce mot particulier.
        return reducers[Math.abs(word.hashCode()) % reducers.length];
    }

}
