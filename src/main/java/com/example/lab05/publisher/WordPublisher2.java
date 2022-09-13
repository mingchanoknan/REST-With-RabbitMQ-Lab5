package com.example.lab05.publisher;

import com.example.lab05.model.Sentence;
import com.example.lab05.model.Word;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class WordPublisher2 {
    protected Word words;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public WordPublisher2() {
        words = new Word();
    }

    @GetMapping("/getBad")
    public ArrayList<String> getBad() {
        return words.badWords;
    }
    @GetMapping("/getGood")
    public ArrayList<String> getGood() {
        return words.goodWords;
    }

    @PostMapping("/addBad/{word}")
    public ArrayList<String> addBadWord(@PathVariable("word") String s) {
        words.badWords.add(s);
        return words.badWords;
    }

    @PostMapping("/delBad/{word}")
    public ArrayList<String> deleteBadWord(@PathVariable("word") String s) {
        words.badWords.remove(s);
        return words.badWords;
    }

    @PostMapping("/addGood/{word}")
    public ArrayList<String> addGoodWord(@PathVariable("word") String s) {
        words.goodWords.add(s);
        return words.goodWords;
    }

    @PostMapping("/delGood/{word}")
    public ArrayList<String> deleteGoodWord(@PathVariable("word") String s) {
        words.goodWords.remove(s);
        return words.goodWords;
    }

    @PostMapping("/proof/{sentence}")
    public String proofSentence(@PathVariable("sentence") String s) {
        // Check have good
        boolean isHaveGood = false;
        for (String word : words.goodWords) {
            if (s.contains(word)){
                isHaveGood = true;
                break;
            }
        }
        // Check have bad
        boolean isHaveBad = false;
        for (String word : words.badWords) {
            if (s.contains(word)) {
                isHaveBad = true;
                break;
            }
        }

        if (isHaveBad && isHaveGood) {
            rabbitTemplate.convertAndSend("Fanout", "", s);
            return "Found Good And Bad Word";
        }
        else if (isHaveGood) {
            rabbitTemplate.convertAndSend("Direct", "good", s);
            return "Found Good Word";
        }
        else if (isHaveBad) {
            rabbitTemplate.convertAndSend("Direct", "bad", s);
            return "Found Bad Word";

        }
        return "";
    }

    @GetMapping("/getSentence")
    public Sentence getSentence() {
        Object out = rabbitTemplate.convertSendAndReceive("Direct", "get", "");
        return (Sentence) out;
    }
}
