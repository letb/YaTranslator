package tp.translator;

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

public class TranslateActivity extends AppCompatActivity implements LanguageBarFragment.OnLanguageChangeListener, TranslateAreaFragment.OnFragmentInteractionListener {

    public static final String LANGUAGE_MAP = "language_map";
    public static final String FROM_LANG = "from_lang";
    public static final String TO_LANG = "to_lang";
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
        UserInformer.showMessage(which, TranslateActivity.this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
