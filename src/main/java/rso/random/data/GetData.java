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

    private static final String RESOURCES_URL = "src/main/resources/";
    private static final String MALE_NAMES_FILE = RESOURCES_URL + "names/male.txt";
    private static final String FEMALE_NAMES_FILE = RESOURCES_URL + "names/female.txt";
    private static final String LAST_NAMES_FILE = RESOURCES_URL + "names/last.txt";
    private static final String WOLA_WARSZAWA_FILE = RESOURCES_URL + "warszawa/wola.txt";

    private static final String FIRST_NAMES_LIST_PAGE_URL = "http://evacska.republika.pl/materialy/teoria/najpopularniejsze_imiona_w_polsce.htm";
    private static final String LAST_NAMES_LIST_PAGE_URL = "http://www.futrega.org/etc/nazwiska.html";
    private static final String WOLA_WARSZAWA_URL = "http://warszawa.wikia.com/wiki/Ulice_na_Woli";

    public static void main(String[] args) {
        putNamesInFiles();
    }

    private static void putNamesInFiles() {
        if (filesInPlace()) return;

        try {
            List<String> names = getFirstNamesFromTheWeb();
            putFemaleNamesInFile(names);
            putMaleNamesInFile(names);

            names = getLastNamesFromTheWeb();
            putLastNamesInFile(names);

            names = getWolaWarszawaFromTheWeb();
            putWolaWarszawaInFile(names);
        } catch (IOException e) {
            LOG.error("Failed to write names to files", e);
        }
    }

    private static boolean filesInPlace() {
        File maleFile = new File(MALE_NAMES_FILE);
        File femaleFile = new File(FEMALE_NAMES_FILE);
        File lastFile = new File(LAST_NAMES_FILE);
        File wolaFile = new File(WOLA_WARSZAWA_FILE);
        return maleFile.exists() && femaleFile.exists() && lastFile.exists() && wolaFile.exists();
    }

    private static List<String> getFirstNamesFromTheWeb() throws IOException {
        Document doc = Jsoup.connect(FIRST_NAMES_LIST_PAGE_URL).get();
        String[] split = doc.text().split(" ");

        List<String> words = new ArrayList<>(Arrays.asList(split));
        int namesStart = words.indexOf("1.");
        int namesEnd = words.indexOf("50.") + 6;
        return words.subList(namesStart, namesEnd).stream()
                .filter(GetData::firstCharNotNumeric)
                .map(GetData::leaveFirstLetterCapital)
                .collect(toList());
    }

    private static List<String> getLastNamesFromTheWeb() throws IOException {
        Document doc = Jsoup.connect(LAST_NAMES_LIST_PAGE_URL).get();
        String[] split = doc.text().replace("\n", " ").split(" ");

        List<String> words = new ArrayList<>(Arrays.asList(split));
        int start = words.indexOf("Nowak");
        int end = words.indexOf("Lęcznar") + 1;
        return words.subList(start, end).stream()
                .filter(GetData::firstCharNotNumeric)
                .collect(toList());
    }

    private static List<String> getWolaWarszawaFromTheWeb() throws IOException {
        Document doc = Jsoup.connect(WOLA_WARSZAWA_URL).get();
        return doc.getElementById("WikiaArticle").getElementsByTag("a").parallelStream()
                .filter(l -> l.attr("href").contains("Ulica"))
                .map(Element::text)
                .distinct()
                .collect(toList());
    }

    private static void putMaleNamesInFile(List<String> names) throws FileNotFoundException {
        List<String> maleNames = IntStream.range(0, names.size())
                .filter(GetData::odd)
                .mapToObj(names::get)
                .collect(toList());
        writeToFile(maleNames, MALE_NAMES_FILE);
    }

    private static void putFemaleNamesInFile(List<String> names) throws FileNotFoundException {
        List<String> femaleNames = IntStream.range(0, names.size())
                .filter(GetData::even)
                .mapToObj(names::get)
                .collect(toList());
        writeToFile(femaleNames, FEMALE_NAMES_FILE);
    }

    private static void putLastNamesInFile(List<String> names) throws FileNotFoundException {
        writeToFile(names, LAST_NAMES_FILE);
    }

    private static void putWolaWarszawaInFile(List<String> names) throws FileNotFoundException {
        writeToFile(names, WOLA_WARSZAWA_FILE);
    }

    private static void writeToFile(List<String> maleNames, String fileName) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(fileName);
        maleNames.forEach(out::println);
        out.close();
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
