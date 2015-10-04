package tp.translator;


import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLEngineResult;

/**
 * Created by dannie on 03.10.15.
 */
public class YandexAPIAdapter {
    static final String API_KEY = "trnsl.1.1.20151003T144534Z.40c93807d08d1478." +
                                        "dce04c61895b35b049d65073e98ecb8ef6e800d3";
    static final String PARAM_API_KEY = "key=",
            PARAM_LANG_PAIR = "&lang=",
            PARAM_TEXT = "&text=";

    public interface AdapterListener {
        public void onDataLoaded(String data);
    }

    private static AdapterListener listener;

    public static void setAdapterListener(AdapterListener newListener) {
        listener = newListener;
    }

    private static String readStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder(inputStream.available());
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream), 1000);
        for (String line = reader.readLine(); line != null; line =reader.readLine()){
            stringBuilder.append(line);
        }
        inputStream.close();
        return stringBuilder.toString();
    }

    private static String getRequest(final URL requestUrl) throws IOException {
        HttpsURLConnection urlConnection = (HttpsURLConnection) requestUrl.openConnection();
        urlConnection.setRequestMethod("GET");

        try {
            int responseCode = urlConnection.getResponseCode();
            InputStream responseStream =  new BufferedInputStream(urlConnection.getInputStream());
            String response = readStream(responseStream);

            if (responseCode != HttpURLConnection.HTTP_OK) {
                return ("Yandex API error occured: " + response);
            }
            return (response);
        } finally {
            urlConnection.disconnect();
        }
    }

    public static void getLanguages() throws IOException {
        final String URL_PREFIX = "https://translate.yandex.net/api/v1.5/tr.json/getLangs?";
        String url = URL_PREFIX + PARAM_API_KEY + API_KEY;

        URL requestUrl = new URL(url);

        String response = getRequest(requestUrl);
        Log.i("getLanguages", response);

        if (listener != null)
            listener.onDataLoaded(response);
    }

    public String detectLanguage(final String text) throws IOException {
        final String URL_PREFIX = "https://translate.yandex.net/api/v1.5/tr.json/getLangs?";
        String url = URL_PREFIX + PARAM_API_KEY + API_KEY + PARAM_TEXT + text;

        URL requestUrl = new URL(url);

        String response = getRequest(requestUrl);
        Log.i("detectLanguage", response);

        return response;
    }

    public String translateText(final String text, final String fromLang, final String toLang)
                                                                            throws IOException {
        final String URL_PREFIX = "https://translate.yandex.net/api/v1.5/tr.json/translate?";
        final String LANG_PAIR = fromLang + "-" + toLang;
        String url = URL_PREFIX +   PARAM_API_KEY + API_KEY +
                                    PARAM_TEXT + text +
                                    PARAM_LANG_PAIR + LANG_PAIR;

        URL requestUrl = new URL(url);

        String response = getRequest(requestUrl);
        Log.i("translateText", response);

        return response;
    }

}
