package tp.translator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import tp.translator.fragments.LanguageBarFragment;
import tp.translator.fragments.TranslateAreaFragment;
import tp.translator.utils.ProgressBarViewer;
import tp.translator.utils.UserInformer;

public class TranslateActivity extends AppCompatActivity
        implements LanguageBarFragment.OnLanguageChangeListener,
                  TranslateAreaFragment.OnTranslateAreaListener {

    public static final String LANGUAGE_MAP = "language_map";
    public static final String LANGUAGES_NAMES = "languages_names";
    public static int FROM_LANGUAGE_REQUEST = 1;
    public static int TO_LANGUAGE_REQUEST = 2;
    private HashMap<String, ArrayList<String>> languageMap;
    private HashMap<String, String> languagesNames;
    private TranslateAreaFragment transFrag;
    private LanguageBarFragment langFrag;

    private void fetchIntentParams(Bundle bundle) {
        languageMap = (HashMap<String, ArrayList<String>>) bundle.getSerializable(LANGUAGE_MAP);
        languagesNames = (HashMap<String, String>) bundle.getSerializable(LANGUAGES_NAMES);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        Intent intent = getIntent();
        fetchIntentParams(intent.getExtras());
        if (savedInstanceState != null) {
            fetchIntentParams(savedInstanceState);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        transFrag = (TranslateAreaFragment)
                getSupportFragmentManager().findFragmentById(R.id.translate_area);
        langFrag = (LanguageBarFragment)
                getSupportFragmentManager().findFragmentById(R.id.language_bar);
        if ((transFrag.getView() != null) && (langFrag.getView() != null)) {
            YandexAPIAdapter.setAdapterListener(new YandexAPIAdapter.AdapterListener() {
                @Override
                public void onDataLoaded(String data) {
                    ProgressBarViewer.hide();
                    try {
                        transFrag.showTranslatedText(data);
                    } catch (IOException e) {
                        UserInformer.showMessage(getResources().getString(R.string.CONNECTION_ERROR), TranslateActivity.this);
                    } catch (JSONException e) {
                        UserInformer.showMessage(getResources().getString(R.string.PARSE_ERROR), TranslateActivity.this);
                    }
                }
            });
        }
    }

    private ArrayList<String> prepareLanguagesList(ArrayList<String> availableLangs) {
        for (int i = 0; i < availableLangs.size(); ++i) {
            availableLangs.set(i, languagesNames.get(availableLangs.get(i)));
        }
        Collections.sort(availableLangs);
        return availableLangs;
    }

    @Override
    public void languageChanged(String which) {
        if (which.equals(LanguageBarFragment.FROM_LANG)) {
            ArrayList <String> availableLangs = new ArrayList<>(languageMap.keySet());
            Intent intent = new Intent(this, LanguageList.class);
            intent.putExtra(LanguageList.LANGUAGE_LIST, prepareLanguagesList(availableLangs));
            startActivityForResult(intent, FROM_LANGUAGE_REQUEST);
        }
        else {
            String fromLang = languagesNames.get(langFrag.getLang(LanguageBarFragment.FROM_LANG));
            ArrayList <String> availableLangs = new ArrayList<>(languageMap.get(fromLang));
            Intent intent = new Intent(this, LanguageList.class);
            intent.putExtra(LanguageList.LANGUAGE_LIST, prepareLanguagesList(availableLangs));
            startActivityForResult(intent, TO_LANGUAGE_REQUEST);
        }
    }

    @Override
    public void languageReversed(String left, String right) {
        ArrayList <String> avaliableLangs = new ArrayList<>(languageMap.get(languagesNames.get(right)));
        if (avaliableLangs.contains(languagesNames.get(left))) {
            langFrag.setLang(R.id.from_field, right);
            langFrag.setLang(R.id.to_field, left);
        }
        else {
            langFrag.setLang(R.id.from_field, right);
            langFrag.setLang(R.id.to_field, languagesNames.get(avaliableLangs.get(0)));
        }
    }

    @Override
    public void translateClicked() {
        String fromLang = languagesNames.get(langFrag.getLang(LanguageBarFragment.FROM_LANG));
        String toLang   = languagesNames.get(langFrag.getLang(LanguageBarFragment.TO_LANG));

        ProgressBarViewer.view(TranslateActivity.this, getResources().getString(R.string.translation_progress_bar_msg));
        try {
            transFrag.translateText(fromLang, toLang);
        } catch (IOException | InterruptedException | ExecutionException e) {
            UserInformer.showMessage(getResources().getString(R.string.CONNECTION_ERROR), TranslateActivity.this);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        String language = data.getStringExtra(LanguageList.LANGUAGE);

        if (requestCode == FROM_LANGUAGE_REQUEST) {
            langFrag.setLang(R.id.from_field, language);
            ArrayList<String> availableLangs = new ArrayList<>(languageMap.get(languagesNames.get(language)));
            String toLangCode = languagesNames.get(langFrag.getLang(LanguageBarFragment.TO_LANG));
            if (!availableLangs.contains(toLangCode)) {
                String fromLangCode = languagesNames.get(language);
                langFrag.setLang(R.id.to_field, languagesNames.get(languageMap.get(fromLangCode).get(0)));
            }
        } else if (requestCode == TO_LANGUAGE_REQUEST) {
            langFrag.setLang(R.id.to_field, language);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(LANGUAGE_MAP, languageMap);
        savedInstanceState.putSerializable(LANGUAGES_NAMES, languagesNames);
        super.onSaveInstanceState(savedInstanceState);
    }
}
