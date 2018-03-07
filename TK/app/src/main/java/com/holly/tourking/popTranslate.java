package com.holly.tourking;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Language;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.LanguageListOption;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.common.collect.ImmutableList;
import java.io.PrintStream;
import java.util.List;
import java.util.Optional;

import java.io.PrintStream;

// Imports the Google Cloud client library
/**
 * Created by Connor on 05/03/2018.
 */

public class popTranslate extends Activity {

    EditText MyInputText;
    Button MyTranslateButton;
    TextView MyOutputText;

    Translate translate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop_translate_window);

        DisplayMetrics dm = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width *.8),(int)(height*.6));

        MyInputText = findViewById(R.id.translate_input);
        MyTranslateButton = findViewById(R.id.TranslateButton);
        MyOutputText = findViewById(R.id.translate_output);

        MyTranslateButton.setOnClickListener(MyTranslateButtonOnClickListener);


    }

    private Button.OnClickListener MyTranslateButtonOnClickListener = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            String InputString;
            String OutputString;
            InputString = MyInputText.getText().toString();

            try {
                OutputString = textTranslate(InputString, "en", "fr");
            } catch (Exception ex) {
                ex.printStackTrace();
                OutputString = "There was an error trying to translate your phrase. Please ensure you are connected to the internet.";
                MyOutputText.setText(OutputString);
            }

        }
    };

    public Thread translateThread = new Thread(new Runnable () {
        @Override
        public void run() {
            try{
                translate = TranslateOptions.getDefaultInstance().getService();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    });


    public String textTranslate(final String input, String source, final String target) throws Exception {

        final String GOOGLE_API_KEY = "YOUR KEY GOES HERE";

            MyOutputText = findViewById(R.id.translate_output);
            final Handler textViewHandler = new Handler();
            String output = "";

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    Translate translate = TranslateOptions.newBuilder().setApiKey(GOOGLE_API_KEY).build().getService();
                    Log.i("Response:","works");
                    final Translation translation =
                            translate.translate(input,
                                    Translate.TranslateOption.targetLanguage(target));
                    textViewHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (MyOutputText != null) {
                                MyOutputText.setText(translation.getTranslatedText());
                            }
                        }
                    });
                    return null;
                }
            }.execute();

            return output;
        }
}

