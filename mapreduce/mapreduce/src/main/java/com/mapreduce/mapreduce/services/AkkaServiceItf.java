package com.mapreduce.mapreduce.services;

public interface AkkaServiceItf {
    public void initSystems();

    public void digestFile(String[] lines);

    public int getWordCount(String mot);
}
