package rso.random.lottery;

import rso.random.data.DataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author kjrz
 */
public enum Lottery {
    INSTANCE;

    private final Random r = new Random();
    private final List<String> districts = new ArrayList<>(DataSet.INSTANCE.STREETS.keySet());

    private enum Gender {
        MALE, FEMALE
    }

    public RandomPerson nextRandomPerson() {
        String district = nextDistrict();
        String street = nextStreet(district);
        return nextRandomPerson(district, street);
    }

    public RandomPerson nextRandomPerson(String district, String street) {
        Gender gender = nextSex();
        String firstName = nextFirstName(gender);
        String lastName = nextLastName(gender);
        return new RandomPerson(firstName, lastName, district, street);
    }

    private Gender nextSex() {
        return r.nextBoolean() ? Gender.MALE : Gender.FEMALE;
    }

    private String nextFirstName(Gender gender) {
        List<String> names = gender == Gender.MALE ? DataSet.INSTANCE.MALE_NAMES : DataSet.INSTANCE.FEMALE_NAMES;
        return names.get(r.nextInt(names.size()));
    }

    private String nextLastName(Gender gender) {
        List<String> names = DataSet.INSTANCE.LAST_NAMES;
        String lastName = names.get(r.nextInt(names.size()));
        if (gender == Gender.FEMALE && lastNameMale(lastName)) lastName = makeLastNameFemale(lastName);
        return lastName;
    }

    private boolean lastNameMale(String lastName) {
        return lastName.substring(lastName.length() - 1).equals("i");
    }

    private String makeLastNameFemale(String lastName) {
        return lastName.substring(0, lastName.length() - 1) + "a";
    }

    private String nextDistrict() {
        return districts.get(r.nextInt(districts.size()));
    }

    private String nextStreet(String district) {
        List<String> streets = DataSet.INSTANCE.STREETS.get(district);
        return streets.get(r.nextInt(streets.size()));
    }
}
