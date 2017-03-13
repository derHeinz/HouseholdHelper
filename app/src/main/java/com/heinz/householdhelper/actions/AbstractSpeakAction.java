package com.heinz.householdhelper.actions;

import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Heinz on 20.10.2016.
 * Abstract base class for further speaking out something.
 */

public abstract class AbstractSpeakAction extends AbstractAsyncTaskAction {

    protected void speak(String aText) {
        // Prepare Text to speech
        finisher = new Finisher<Boolean>();
        TextToSpeech tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    finisher.finished(true);
                } else {
                    finisher.finished(false);
                }
            }
        }
        );
        waitForFinisher();

        // set language
        tts.setLanguage(Locale.GERMANY);

        // speak
        finisher = new Finisher<Boolean>();

        // new speak id
        final String id = UUID.randomUUID().toString();

        HashMap<String, String> params = new HashMap<>();
        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, id);

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                // started - that's nice but not inresting
            }

            @Override
            public void onDone(String utteranceId) {
                if (id.equals(utteranceId)) {
                    finisher.finished(true);
                }
            }

            @Override
            public void onError(String utteranceId) {
                if (id.equals(utteranceId)) {
                    finisher.finished(false);
                }
            }
        });
        // the text to be spoken!
        tts.speak(aText, TextToSpeech.QUEUE_ADD, params);

        waitForFinisher();
        tts.shutdown();
    }
}
