package com.example.lab05.model;

import java.util.ArrayList;

public class Word {
    public ArrayList<String> badWords;
    public ArrayList<String> goodWords;

    public Word() {
        this.badWords = new ArrayList<>();
        this.badWords.add("olo");
        this.badWords.add("fuck");
        this.goodWords = new ArrayList<>();
        this.goodWords.add("happy");
        this.goodWords.add("enjoy");
        this.goodWords.add("life");
    }
}
