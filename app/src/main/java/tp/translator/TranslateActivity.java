package tp.translator;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;

import tp.translator.fragments.LanguageBarFragment;
import tp.translator.fragments.TranslateAreaFragment;
import tp.translator.utils.UserInformer;

public class TranslateActivity extends AppCompatActivity
        implements LanguageBarFragment.OnLanguageChangeListener,
                  TranslateAreaFragment.OnTranslateAreaListener {

    public static final String LANGUAGE_MAP = "language_map";
    public static int FROM_LANGUAGE_REQUEST = 1;
    public static int TO_LANGUAGE_REQUEST = 2;
    private HashMap<String, ArrayList<String>> languageMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        Intent intent = getIntent();
        fetchParams(intent);
    }

    public void fetchParams (Intent intent) {
        languageMap = (HashMap<String, ArrayList<String>>) intent.getSerializableExtra(LANGUAGE_MAP);
    }

    @Override
    public void languageChanged(String which) {
        if (which.equals(LanguageBarFragment.FROM_LANG)) {
            ArrayList <String> avaliableLangs = new ArrayList<>(languageMap.keySet());
            Intent intent = new Intent(this, LanguageList.class);
            intent.putExtra(LanguageList.LANGUAGE_LIST, avaliableLangs);
            startActivityForResult(intent, FROM_LANGUAGE_REQUEST);
        }
        else {
            LanguageBarFragment langFrag = (LanguageBarFragment)
                    getSupportFragmentManager().findFragmentById(R.id.language_bar);
            String fromLang = langFrag.getLang(LanguageBarFragment.FROM_LANG);
            ArrayList <String> avaliableLangs = new ArrayList<>(languageMap.get(fromLang));
            Intent intent = new Intent(this, LanguageList.class);
            intent.putExtra(LanguageList.LANGUAGE_LIST, avaliableLangs);
            startActivityForResult(intent, TO_LANGUAGE_REQUEST);
        }
    }

    @Override
    public void translateClicked() {
        LanguageBarFragment langFrag = (LanguageBarFragment)
                getSupportFragmentManager().findFragmentById(R.id.language_bar);
        TranslateAreaFragment transFrag = (TranslateAreaFragment)
                getSupportFragmentManager().findFragmentById(R.id.translate_area);

        String fromLang = langFrag.getLang(langFrag.FROM_LANG);
        String toLang   = langFrag.getLang(langFrag.TO_LANG);
        transFrag.translate(fromLang, toLang);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        String language = data.getStringExtra(LanguageList.LANGUAGE);
        LanguageBarFragment langFrag = (LanguageBarFragment)
                getSupportFragmentManager().findFragmentById(R.id.language_bar);
        if (requestCode == FROM_LANGUAGE_REQUEST) {
            langFrag.setLang(R.id.from_field, language);
            langFrag.setLang(R.id.to_field, languageMap.get(language).get(0));
        }
        else if (requestCode == TO_LANGUAGE_REQUEST)
            langFrag.setLang(R.id.to_field, language);
    }


}
