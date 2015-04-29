package rso.random.data;

import java.util.Collection;

/**
 * @author kjrz
 */
class DataUrls {
    String firstNamesUrl;
    String lastNamesUrl;
    Collection<District> districts;

    class District {
        String name;
        String url;
    }
}
