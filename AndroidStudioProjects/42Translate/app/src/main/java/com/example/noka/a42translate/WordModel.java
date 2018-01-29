package com.example.noka.a42translate;

/**
 * Created by noka on 5/1/17.
 */

public class WordModel {
    public WordModel(String text, String translated, String context_raw, String contenxt_translated, String language, String id_word) {

        this.setContenxt_translated(contenxt_translated);
        this.setContext_raw(context_raw);
        this.setId_word(id_word);
        this.setLanguage(language);
        this.setText(text);
        this.setTranslated(translated);

    }
    String text;
    String translated;
    String id_word;
    String context_raw;
    String language;
    String contenxt_translated;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTranslated() {
        return translated;
    }

    public void setTranslated(String translated) {
        this.translated = translated;
    }

    public String getId_word() {
        return id_word;
    }

    public void setId_word(String id_word) {
        this.id_word = id_word;
    }

    public String getContext_raw() {
        return context_raw;
    }

    public void setContext_raw(String context_raw) {
        this.context_raw = context_raw;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getContenxt_translated() {
        return contenxt_translated;
    }

    public void setContenxt_translated(String contenxt_translated) {
        this.contenxt_translated = contenxt_translated;
    }


}
