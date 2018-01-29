package com.example.noka.a42translate;

/**
 * Created by noka on 5/2/17.
 */

public class LyricsModel {

    public LyricsModel(String id_lyrics, String title, String body, String translated_body, String artist,
                       String featured, String submittedby, String language ) {

        this.setId_lyrics(id_lyrics);
        this.setTitle(title);
        this.setBody(body);
        this.setTranslated_body(translated_body);
        this.setArtist(artist);
        this.setFeatured(featured);
        this.setSubmittedby(submittedby);
        this.setLanguage(language);

    }
    String id_lyrics;
    String title;
    String body;
    String translated_body;
    String artist;
    String featured;
    String submittedby;
    String language;


    public String getId_lyrics() {
        return id_lyrics;
    }

    public void setId_lyrics(String id_lyrics) {
        this.id_lyrics = id_lyrics;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTranslated_body() {
        return translated_body;
    }

    public void setTranslated_body(String translated_body) {
        this.translated_body = translated_body;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getFeatured() {
        return featured;
    }

    public void setFeatured(String featured) {
        this.featured = featured;
    }

    public String getSubmittedby() {
        return submittedby;
    }

    public void setSubmittedby(String submittedby) {
        this.submittedby = submittedby;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }



}
