package com.mapreduce.mapreduce.services;

import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class AkkaService {
    ActorSystem mapperSystem = ActorSystem.create("mapperHost", ConfigFactory.load("system1.conf"));
    ActorSystem reducerSystem = ActorSystem.create("reducerHost", ConfigFactory.load("system2.conf"));
    ActorRef mapper1, mapper2, mapper3, reducer1, reducer2;
}
