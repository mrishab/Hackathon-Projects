package com.example.i864261.erp_hackathon;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;

import java.util.Locale;

public class SpeechToText {

    // const
    public static final int REQ_CODE = 100;


    Activity ctx;

    public SpeechToText(Activity ctx) {
        this.ctx = ctx;
    }

    public void run() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            ctx.startActivityForResult(intent, REQ_CODE);
        } catch (ActivityNotFoundException activityNotFoundExceptiona) {

        }
    }
}
