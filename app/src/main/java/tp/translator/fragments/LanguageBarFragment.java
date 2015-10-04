package tp.translator.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import tp.translator.R;

public class LanguageBarFragment extends Fragment implements View.OnClickListener {

    private OnLanguageChangeListener mListener;

    public static final String FROM_LANG = "from_lang";
    public static final String TO_LANG = "to_lang";
    View view;

    public LanguageBarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;
        if (context instanceof Activity){
            activity = (Activity) context;
            try {
                mListener = (OnLanguageChangeListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement TextClicked");
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_language_bar, container, false);
        Button fromBtn = (Button) view.findViewById(R.id.from_field);
        Button toBtn = (Button) view.findViewById(R.id.to_field);
        fromBtn.setOnClickListener(this);
        toBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        //do what you want to do when button is clicked
        switch (v.getId()) {
            case R.id.from_field:
                mListener.languageChanged(FROM_LANG);
                break;
            case R.id.to_field:
                mListener.languageChanged(TO_LANG);
                break;
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setLang (int which, String data) {
        ((Button) view.findViewById(which)).setText(data);
    }

    public String getLang (String which) {
        if (which.equals(FROM_LANG))
            return (String)((Button) view.findViewById(R.id.from_field)).getText();
        else
            return (String)((Button) view.findViewById(R.id.to_field)).getText();
    }


    public interface OnLanguageChangeListener {
        public void languageChanged(String which);
    }

}
