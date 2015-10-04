package tp.translator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import tp.translator.utils.Parser;
import tp.translator.utils.ProgressBarViewer;
import tp.translator.utils.UserInformer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        YandexAPIAdapter.setAdapterListener(new YandexAPIAdapter.AdapterListener() {
            @Override
            public void onDataLoaded(String data) {
                proceedToTranslation(data);
            }
        });

//        TODO: Закомментить, если все повалится
        ProgressBarViewer.view(MainActivity.this, getResources().getString(R.string.language_loading_progress_bar_msg));
        try {
            YandexAPIAdapter.getLanguages();
        } catch (IOException e) {
            UserInformer.showMessage(getResources().getString(R.string.CONNECTION_ERROR), MainActivity.this);
        }
    }

    public void proceedToTranslation (String languages) {
        try {
            HashMap<String, ArrayList<String>> languageMap = Parser.parseLanguageList(
                    getResources().getString(R.string.api_langarray_name),
                    languages
            );
            Intent intent = new Intent(MainActivity.this, TranslateActivity.class);
            intent.putExtra(TranslateActivity.LANGUAGE_MAP, languageMap);
            ProgressBarViewer.hide();
            startActivity(intent);
        } catch (JSONException e) {
            UserInformer.showMessage(getResources().getString(R.string.PARSE_ERROR), MainActivity.this);
        }
    }

}
