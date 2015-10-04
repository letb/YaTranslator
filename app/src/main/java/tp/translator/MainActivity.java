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
            showMessage(getResources().getString(R.string.CONNECTION_ERROR));
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
            showMessage(getResources().getString(R.string.PARSE_ERROR));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void showMessage(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
