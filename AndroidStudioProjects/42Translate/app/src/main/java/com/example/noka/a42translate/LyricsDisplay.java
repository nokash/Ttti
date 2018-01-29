package com.example.noka.a42translate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LyricsDisplay extends AppCompatActivity {

    public String id_lyrics,title,language, body, translated_body, artist, featured, submittedby;
    TextView lang, _translated, titl, _body, artst, _submitted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyrics_display);

        id_lyrics = getIntent().getExtras().getString("id_key");
        title = getIntent().getExtras().getString("title_key");
        language = getIntent().getExtras().getString("language_key");
        body = getIntent().getExtras().getString("body_key");
        translated_body = getIntent().getExtras().getString("translated_body_key");
        artist = getIntent().getExtras().getString("artist_key");
        featured = getIntent().getExtras().getString("featured_key");
        submittedby = getIntent().getExtras().getString("submittedby_key");

        lang = (TextView)findViewById(R.id.langTxt);
        _translated = (TextView) findViewById(R.id.translated);
        _submitted = (TextView) findViewById(R.id.sub);
        titl = (TextView) findViewById(R.id.ttl);
        _body = (TextView) findViewById(R.id.wasae);
        artst = (TextView) findViewById(R.id.artist);
        //_submitted = (TextView) findViewById(R.id.by);

        lang.setText(language);
        _submitted.setText(submittedby);
        _body.setText(translated_body);
        _translated.setText(body);
        artst.setText(artist);
        titl.setText(title);

    }
}
