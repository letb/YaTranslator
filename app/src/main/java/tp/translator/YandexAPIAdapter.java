package tp.translator;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by dannie on 03.10.15.
 */
public class YandexAPIAdapter {
    static final String API_KEY = "trnsl.1.1.20151003T144534Z.40c93807d08d1478." +
                                    "dce04c61895b35b049d65073e98ecb8ef6e800d3",
                        UI      = "ru";

    static final String PARAM_API_KEY   =   "key=",
                        PARAM_LANG_PAIR =   "&lang=",
                        PARAM_TEXT      =   "&text=",
                        PARAM_UI        =   "&ui=";

    static final String PREFIX = "https://translate.yandex.net/api/v1.5/tr.json/";

    static final String PARAM_GET_LANGS =   "getLangs?",
                        PARAM_DETECT    =   "detect?",
                        PARAM_TRANSLATE =   "translate?";


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


    private static class GetRequestTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            try {
                URL requestUrl = urls[0];
                HttpsURLConnection urlConnection = (HttpsURLConnection) requestUrl.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(5000);

                try {
                    int responseCode = urlConnection.getResponseCode();
                    InputStream responseStream = new BufferedInputStream(urlConnection.getInputStream());
                    String response = readStream(responseStream);

                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        return ("Yandex API error occured: " + response);
                    }

                    return (response);
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(this.getClass().toString(), e.getMessage(), e);
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (listener != null)
                listener.onDataLoaded(result);
        }
    }

    public static void getLanguages() throws IOException {
        String url = PREFIX + PARAM_GET_LANGS + PARAM_API_KEY + API_KEY + PARAM_UI + UI;
        URL requestUrl = new URL(url);

        GetRequestTask getRequestTask = new GetRequestTask();
        getRequestTask.execute(requestUrl);
    }

    public static void detectLanguage(final String text) throws IOException {
        String url = PREFIX + PARAM_DETECT + PARAM_API_KEY + API_KEY + PARAM_TEXT + text;
        URL requestUrl = new URL(url);

        GetRequestTask getRequestTask = new GetRequestTask();
        getRequestTask.execute(requestUrl);
    }

    public static void translateText(final String text, final String fromLang, final String toLang)
            throws IOException, ExecutionException, InterruptedException {
        final String LANG_PAIR = fromLang + "-" + toLang;
        final String TEXT = text.replaceAll(" ", "%20");
        String url = PREFIX + PARAM_TRANSLATE + PARAM_API_KEY + API_KEY +
                                                PARAM_TEXT + TEXT +
                                                PARAM_LANG_PAIR + LANG_PAIR;
        URL requestUrl = new URL(url);

        GetRequestTask getRequestTask = new GetRequestTask();
        getRequestTask.execute(requestUrl);
    }




}
