package com.example.lab05.publisher;

import com.example.lab05.model.Sentence;
import com.example.lab05.model.Word;
import org.atmosphere.config.service.Get;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class WordPublisher {
    protected Word words;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public WordPublisher() {
        words = new Word();
    }

    @GetMapping("/addBad/{word}")
    public ArrayList<String> addBadWord(@PathVariable("word") String s) {
        words.badWords.add(s);
        return words.badWords;
    }

    @GetMapping("/delBad/{word}")
    public ArrayList<String> deleteBadWord(@PathVariable("word") String s) {
        words.badWords.remove(s);
        return words.badWords;
    }

    @GetMapping("/addGood/{word}")
    public ArrayList<String> addGoodWord(@PathVariable("word") String s) {
        words.goodWords.add(s);
        return words.goodWords;
    }

    @GetMapping("/delGood/{word}")
    public ArrayList<String> deleteGoodWord(@PathVariable("word") String s) {
        words.goodWords.remove(s);
        return words.goodWords;
    }

    @GetMapping("/proof/{sentence}")
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
}
