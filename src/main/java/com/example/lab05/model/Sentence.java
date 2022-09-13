package com.example.lab05.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Sentence implements Serializable {
    public ArrayList<String> badSentences;
    public ArrayList<String> goodSentences;

    public Sentence() {
        badSentences = new ArrayList<>();
        goodSentences = new ArrayList<>();
    }
}
