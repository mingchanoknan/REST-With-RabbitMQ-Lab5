package com.example.lab05.consumer;

import com.example.lab05.model.Sentence;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class SentenceConsumer {
    protected Sentence sentence;

    public SentenceConsumer() {
        sentence = new Sentence();
    }

    @RabbitListener(queues = "BadWordQueue")
    public void addBadSentence(String s) {
        sentence.badSentences.add(s);
        System.out.println("In addBadSentence Method : [" + String.join(", ", sentence.badSentences) + "]");

    }
    @RabbitListener(queues = "GoodWordQueue")
    public void addGoodSentence(String s) {
        sentence.goodSentences.add(s);
        System.out.println("In addGoodSentence Method : [" + String.join(", ", sentence.goodSentences) + "]");
    }

    @RabbitListener(queues = "GetQueue")
    public Sentence getSentence() {
        return sentence;
    }
}
