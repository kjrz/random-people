package rso.random.data;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @author kjrz
 */
public class DataUrlsOpener {
    private static final String URLS_JSON = "urls.json";

    public final DataUrls urls;

    public DataUrlsOpener() {
        Reader json = new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(URLS_JSON));
        Gson gson = new Gson();
        urls = gson.fromJson(json, DataUrls.class);
    }
}
