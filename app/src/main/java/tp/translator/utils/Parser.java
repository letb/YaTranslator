package tp.translator.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Created by eugene on 03.10.15.
 */
public class Parser {
    private static List<String> getListFromJSON (JSONArray jArray) throws JSONException {
        List <String> languageList = new ArrayList<>();
        int length = jArray.length();
        for (int i=0; i < length; i++)
            languageList.add(jArray.getString(i));
        return languageList;
    }

    private static JSONArray getArrayFromString (String parseKey, String values) throws JSONException {
        JSONObject json = new JSONObject(values);
        return json.getJSONArray(parseKey);
    }

    public static HashMap<String, ArrayList<String>> parseLanguageList (String parseKey, String values) throws JSONException {

        List<String> languages = getListFromJSON(
                getArrayFromString(parseKey, values)
        );
        HashMap <String, ArrayList<String>> languageMap = new HashMap<>();
        String curLang = "";
        ArrayList<String> curLangTo = new ArrayList<>();
        for (String pair : languages) {
            String[] array = pair.split("-");
            if (languageMap.containsKey(array[0]))
                curLangTo.add(array[1]);
            else {
                languageMap.put(curLang, curLangTo);
                curLangTo = new ArrayList<>();
                curLang = array[0];
                curLangTo.add(array[1]);
            }
        }
        languageMap.remove("");
        return languageMap;
    }


    public static HashMap<String, String> parseLanguagesNames(String values) throws JSONException {
        HashMap<String, String> languagesNamesMap = new HashMap<String, String>();

        JSONObject valuesObject = new JSONObject(values);
        JSONObject langsObject = valuesObject.getJSONObject("langs");

        Iterator<String> keysIter = langsObject.keys();
        while (keysIter.hasNext()) {
            String key = keysIter.next();
            String value = langsObject.getString(key);
            languagesNamesMap.put(key, value);
            languagesNamesMap.put(value, key);
        }

        return languagesNamesMap;
    }

    public static String parseTranslation(String values) throws JSONException {
        JSONObject jsonString = new JSONObject(values);
        String text = jsonString.getJSONArray("text").get(0).toString();
        return text;
    }
}
