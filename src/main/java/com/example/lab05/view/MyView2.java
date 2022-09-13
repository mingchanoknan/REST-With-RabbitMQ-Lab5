package com.example.lab05.view;

import com.example.lab05.model.Sentence;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;

@Route("index2")
public class MyView2 extends HorizontalLayout {

    private VerticalLayout leftVertical, rightVertical;
    private TextField wordTf, sentence;
    private Button addGood, addBad, addSentence, showSentence;
    private ComboBox<String> listGoodWord, listBadWord;
    private TextArea goodSentences, badSentences;


    public MyView2 () {
        this.leftVertical = new VerticalLayout();
        this.leftVertical.setAlignItems(FlexComponent.Alignment.STRETCH);
        this.wordTf = new TextField();
        this.wordTf.setLabel("Add Word");
        this.addGood = new Button("Add Good Word");
        this.addBad = new Button("Add Bad Word");
        this.listGoodWord = new ComboBox<>("Good Words");
        this.listBadWord = new ComboBox<>("Bad Words");
        this.leftVertical.add(this.wordTf, this.addGood, this.addBad, this.listGoodWord, this.listBadWord);

        this.rightVertical = new VerticalLayout();
        this.rightVertical.setAlignItems(FlexComponent.Alignment.STRETCH);
        this.sentence = new TextField();
        this.sentence.setLabel("Add Sentence");
        this.addSentence = new Button("Add Sentence");
        this.goodSentences = new TextArea();
        this.goodSentences.setLabel("Good Sentences");
        this.badSentences = new TextArea();
        this.badSentences.setLabel("Bad Sentences");
        this.showSentence = new Button("Show Sentence");
        this.rightVertical.add(this.sentence, this.addSentence, this.goodSentences, this.badSentences, this.showSentence);
        this.add(this.leftVertical, this.rightVertical);

        this.getBadWords();
        this.getGoodWords();

        this.addBad.addClickListener(buttonClickEvent -> {
            this.updateBadWords();
        });

        this.addGood.addClickListener(buttonClickEvent -> {
            this.updateGoodWords();
        });

        this.addSentence.addClickListener(buttonClickEvent -> {
            this.addSentence();
        });

        this.showSentence.addClickListener(buttonClickEvent -> {
            this.getAllSentence();
        });
    }

    public void updateGoodWords() {
        WebClient.create().post().uri("http://localhost:8080/addGood/" + this.wordTf.getValue()).retrieve().bodyToMono(ArrayList.class).block();
        this.getGoodWords();
    }

    public void updateBadWords() {
        WebClient.create().post().uri("http://localhost:8080/addBad/" + this.wordTf.getValue()).retrieve().bodyToMono(ArrayList.class).block();
        this.getBadWords();
    }

    public void getBadWords() {
        ArrayList out = WebClient.create().get().uri("http://localhost:8080/getBad").retrieve().bodyToMono(ArrayList.class).block();
        ArrayList<String> keep = new ArrayList<>();
        for (Object text : out) {
            keep.add((String) text);
        }
        this.listBadWord.setItems(keep);
    }
    public void getGoodWords() {
        ArrayList out = WebClient.create().get().uri("http://localhost:8080/getGood").retrieve().bodyToMono(ArrayList.class).block();
        ArrayList<String> keep = new ArrayList<>();
        for (Object text : out) {
            keep.add((String) text);
        }
        this.listGoodWord.setItems(keep);
    }
    public void addSentence() {
        String out = WebClient.create().post().uri("http://localhost:8080/proof/" + this.sentence.getValue()).retrieve().bodyToMono(String.class).block();
        System.out.println(out);
        new Notification(out).open();
    }

    public void getAllSentence() {
        Sentence out = WebClient.create().get().uri("http://localhost:8080/getSentence").retrieve().bodyToMono(Sentence.class).block();

        this.badSentences.setValue(String.join(", ", out.badSentences));
        this.goodSentences.setValue(String.join(", ", out.goodSentences));

    }
}
