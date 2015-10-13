package tp.translator.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import tp.translator.R;
import tp.translator.YandexAPIAdapter;
import tp.translator.utils.Parser;


public class TranslateAreaFragment extends Fragment {

    private OnTranslateAreaListener mListener;

    private EditText inputTextField;
    private TextView outputTextView;
    View view;

    public interface OnTranslateAreaListener {
        public void translateClicked();
    }

    public TranslateAreaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_translate_area, container, false);
        inputTextField = (EditText) view.findViewById(R.id.input_text_edit);
        outputTextView = (TextView) view.findViewById(R.id.output_text_view);

        Button translateButton  = (Button) view.findViewById(R.id.translate_button);
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.translateClicked();
            }
        });

        Button clearButton = (Button) view.findViewById(R.id.clear_button);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputTextField.setText("");
                outputTextView.setText("");
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;
        if (context instanceof Activity) {
            activity = (Activity) context;
            try {
                mListener = (OnTranslateAreaListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement OnTranslateAreaListener");
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // TODO: Запрещает ввод на русском


    public void translateText(String from_lang, String to_lang) throws IOException {
        String text = clearBadSymbols(inputTextField.getText().toString());
        try {
            YandexAPIAdapter.translateText(text, from_lang, to_lang);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void showTranslatedText(String response) throws IOException {
        String translation = "";
        if (response != null) {
            try {
                translation = Parser.parseTranslation(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            outputTextView.setText(translation);
        }
        else {
            throw new IOException();
        }
    }

    private String clearBadSymbols(String text) {
        return text.replace("\n", " ").replace("\r", " ");
    }
}
