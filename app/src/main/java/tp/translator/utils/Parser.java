package tp.translator.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
}