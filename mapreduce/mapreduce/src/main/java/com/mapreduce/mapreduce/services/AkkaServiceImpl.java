package com.mapreduce.mapreduce.services;

import org.springframework.stereotype.Service;

import com.mapreduce.mapreduce.actors.Mapper;
import com.mapreduce.mapreduce.actors.Reducer;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

@Service
public class AkkaServiceImpl implements AkkaServiceItf {
    ActorSystem mapperSystem = ActorSystem.create("mapperHost", ConfigFactory.load("system1.conf"));
    ActorSystem reducerSystem = ActorSystem.create("reducerHost", ConfigFactory.load("system2.conf"));
    ActorRef[] mappers;
    ActorRef[] reducers;

    @Override
    public void initSystems() {
        initMappers();
        initReducers();
    }

    @Override
    public void digestFile(String[] lines) {
        // distribute the lines
        for (int i = 0; i < lines.length; i++) {
            mappers[i % mappers.length].tell(lines[i], ActorRef.noSender());
        }
    }

    @Override
    public int getWordCount(String word) {
        // not sure about this
        ActorRef chosenReducer = reducers[Math.abs(word.hashCode()) % reducers.length];
        return ((Reducer) chosenReducer).getWordCount(word); // Ask the Reducer for the count
    }

    private void initMappers() {
        for (int i = 0; i < 3; i++) {
            mappers[i] = mapperSystem.actorOf(Props.create(Mapper.class), ("Mapper" + i));
        }
    }

    private void initReducers() {
        for (int i = 0; i < 2; i++) {
            reducers[i] = reducerSystem.actorOf(Props.create(Reducer.class), ("Reducer" + i));
        }
    }

}
