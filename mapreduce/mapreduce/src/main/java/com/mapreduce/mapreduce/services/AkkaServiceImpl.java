package com.mapreduce.mapreduce.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.stereotype.Service;

import com.mapreduce.mapreduce.actors.Mapper;
import com.mapreduce.mapreduce.actors.Reducer;
import com.mapreduce.mapreduce.messages.GetWordCountMessage;
import com.mapreduce.mapreduce.messages.WordCountMessage;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import scala.concurrent.duration.FiniteDuration;

@Service
public class AkkaServiceImpl implements AkkaServiceItf {
    ActorSystem mapperSystem = ActorSystem.create("mapperHost", ConfigFactory.load("system1.conf"));
    ActorSystem reducerSystem = ActorSystem.create("reducerHost", ConfigFactory.load("system2.conf"));
    Inbox inbox = Inbox.create(reducerSystem);
    ArrayList<ActorRef> mappers = new ArrayList<>();
    ArrayList<ActorRef> reducers = new ArrayList<>();

    @Override
    public void initSystems() {
        for (int i = 0; i < 2; i++) {
            reducers.add(reducerSystem.actorOf(Props.create(Reducer.class), ("Reducer" + i)));
        }
        if (reducers != null) {
            for (int i = 0; i < 3; i++) {
                mappers.add(mapperSystem.actorOf(Props.create(Mapper.class, (List<ActorRef>) reducers),
                        ("Mapper" + i)));
            }
        } else {
            System.err.println("Reducer init problem");
        }
    }

    @Override
    public void digestFile(String[] lines) {
        // distribute the lines
        for (int i = 0; i < lines.length; i++) {
            mappers.get(i % mappers.size()).tell(lines[i], ActorRef.noSender());
        }
    }

    @Override
    public int getWordCount(String word) {
        inbox.send(reducers.get(Math.abs(word.hashCode()) % reducers.size()), new GetWordCountMessage(word));
        Object reply;
        int count = 0;
        try {
            reply = inbox.receive(FiniteDuration.create(5, TimeUnit.SECONDS));
            if (reply instanceof WordCountMessage wordCountMessage) {
                count = wordCountMessage.count();
                System.out.println("Count : " + wordCountMessage.count());
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return count; // Ask the Reducer for the count
    }

    // public int getWordCount(String word) {
    // // ... (existing code)
    // ActorRef chosenReducer = reducers[Math.abs(word.hashCode()) %
    // reducers.length];
    // Future<Object> future = chosenReducer.ask(new GetWordCountMessage(word)); //
    // Send message with word
    // try {
    // return (Integer) future.result(); // Get the result (cast to Integer)
    // } catch (Exception e) {
    // // Handle potential exceptions during message sending/receiving
    // throw new RuntimeException(e); // Or handle appropriately
    // }
    // }

}
