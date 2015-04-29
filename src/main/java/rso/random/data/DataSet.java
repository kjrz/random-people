package rso.random.data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author kjrz
 */
public enum DataSet {
    INSTANCE;

    private static final String MALE_NAMES_FILE = "names/male.txt";
    private static final String FEMALE_NAMES_FILE = "names/female.txt";
    private static final String LAST_NAMES_FILE = "names/last.txt";
    private static final String WARSZAWA_PATH = "warszawa/";

    public final List<String> MALE_NAMES;
    public final List<String> FEMALE_NAMES;
    public final List<String> LAST_NAMES;
    public final Map<String, List<String>> STREETS;

    private DataSet() {
        MALE_NAMES = readResourceFile(MALE_NAMES_FILE);
        FEMALE_NAMES = readResourceFile(FEMALE_NAMES_FILE);
        LAST_NAMES = readResourceFile(LAST_NAMES_FILE);

        STREETS = new HashMap<>();
        DataUrls urls = new DataUrlsOpener().urls;
        urls.districts.stream().forEach((d) -> this.getStreets(d.name, STREETS));
    }

    private List<String> readResourceFile(String fileName) {
        return new BufferedReader(
                new InputStreamReader(
                        getClass().getClassLoader().getResourceAsStream(fileName)))
                .lines().collect(toList());
    }

    private void getStreets(String name, Map<String, List<String>> districts) {
        List<String> streets = readResourceFile(WARSZAWA_PATH + name + ".txt");
        districts.put(name, streets);
    }
}
