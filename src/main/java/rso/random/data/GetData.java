package rso.random.data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * @author kjrz
 */
public class GetData {
    private static final Logger LOG = LoggerFactory.getLogger(DataSet.class);

    private static final String RESOURCES_DIR = "src/main/resources/";
    private static final String MALE_NAMES_PATH = RESOURCES_DIR + "names/male.txt";
    private static final String FEMALE_NAMES_PATH = RESOURCES_DIR + "names/female.txt";
    private static final String LAST_NAMES_PATH = RESOURCES_DIR + "names/last.txt";
    private static final String WARSZAWA_PATH = RESOURCES_DIR + "warszawa/";

    public static void main(String[] args) {
        DataUrls urls = new DataUrlsOpener().urls;
        getFirstNames(urls.firstNamesUrl);
        getLastNames(urls.lastNamesUrl);
        urls.districts.stream().forEach(GetData::getDistrictStreets);
    }

    private static void getFirstNames(String firstNamesUrl) {
        try {
            List<String> names = getFirstNamesFromTheWeb(firstNamesUrl);
            putFemaleNamesInFile(names);
            putMaleNamesInFile(names);
        } catch (IOException e) {
            LOG.error("Failed to get first names", e);
        }
    }

    private static List<String> getFirstNamesFromTheWeb(String firstNamesUrl) throws IOException {
        Document doc = Jsoup.connect(firstNamesUrl).get();
        String[] split = doc.text().split(" ");

        List<String> words = new ArrayList<>(Arrays.asList(split));
        int namesStart = words.indexOf("1.");
        int namesEnd = words.indexOf("50.") + 6;
        return words.subList(namesStart, namesEnd).stream()
                .filter(GetData::firstCharNotNumeric)
                .map(GetData::leaveFirstLetterCapital)
                .collect(toList());
    }

    private static void putMaleNamesInFile(List<String> names) throws FileNotFoundException {
        List<String> maleNames = IntStream.range(0, names.size())
                .filter(GetData::odd)
                .mapToObj(names::get)
                .collect(toList());
        writeToFile(maleNames, MALE_NAMES_PATH);
    }

    private static void putFemaleNamesInFile(List<String> names) throws FileNotFoundException {
        List<String> femaleNames = IntStream.range(0, names.size())
                .filter(GetData::even)
                .mapToObj(names::get)
                .collect(toList());
        writeToFile(femaleNames, FEMALE_NAMES_PATH);
    }

    private static void getLastNames(String lastNamesUrl) {
        try {
            List<String> names = getLastNamesFromTheWeb(lastNamesUrl);
            putLastNamesInFile(names);
        } catch (IOException e) {
            LOG.error("Failed to get last names", e);
        }
    }

    private static List<String> getLastNamesFromTheWeb(String lastNamesUrl) throws IOException {
        Document doc = Jsoup.connect(lastNamesUrl).get();
        String[] split = doc.text().replace("\n", " ").split(" ");

        List<String> words = new ArrayList<>(Arrays.asList(split));
        int start = words.indexOf("Nowak");
        int end = words.indexOf("Lęcznar") + 1;
        return words.subList(start, end).stream()
                .filter(GetData::firstCharNotNumeric)
                .collect(toList());
    }

    private static void putLastNamesInFile(List<String> names) throws FileNotFoundException {
        writeToFile(names, LAST_NAMES_PATH);
    }

    private static void getDistrictStreets(DataUrls.District district) {
        try {
            List<String> streets = getStreetsFromUrl(district.url);
            writeDistrictToFile(streets, district.name);
        } catch (IOException e) {
            LOG.error("Failed to get streets of " + district.name, e);
        }
    }

    private static List<String> getStreetsFromUrl(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        return doc.getElementById("WikiaArticle").getElementsByTag("a").parallelStream()
                .filter(l -> l.attr("href").contains("Ulica"))
                .map(Element::text)
                .distinct()
                .collect(toList());
    }

    private static void writeDistrictToFile(List<String> streets, String name) throws FileNotFoundException {
        writeToFile(streets, WARSZAWA_PATH + name + ".txt");
    }

    private static void writeToFile(List<String> maleNames, String fileName) throws FileNotFoundException {
        createParentDirectory(fileName);
        PrintWriter out = new PrintWriter(fileName);
        maleNames.forEach(out::println);
        out.close();
    }

    private static void createParentDirectory(String fileName) {
        File f = new File(fileName);
        try {
            Files.createDirectories(f.getParentFile().toPath());
        } catch (IOException e) {
            LOG.error("Failed to create parent directory", e);
        }
    }

    private static boolean firstCharNotNumeric(String s) {
        return !s.substring(0, 1).matches("[0-9]");
    }

    private static String leaveFirstLetterCapital(String s) {
        return s.substring(0, 1) + s.substring(1).toLowerCase();
    }

    private static boolean even(int i) {
        return i % 2 == 0;
    }

    private static boolean odd(int i) {
        return !even(i);
    }
}
