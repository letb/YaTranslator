package tp.translator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class LanguageList extends AppCompatActivity {
    public static final String LANGUAGE_LIST = "language_list";
    public static final String LANGUAGE = "language";
    private ArrayList<String> languages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_list);
        Intent intent = getIntent();
        languages = (ArrayList<String>) intent.getSerializableExtra(LANGUAGE_LIST);
        ArrayAdapter<String> langsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, languages);
        final ListView listView = (ListView) findViewById(R.id.languageListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                String selectedFromList = listView.getItemAtPosition(position).toString();
                Intent intent = new Intent();
                intent.putExtra(LANGUAGE, selectedFromList);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        listView.setAdapter(langsAdapter);
    }
}
