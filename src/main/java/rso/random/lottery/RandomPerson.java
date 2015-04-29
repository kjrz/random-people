package rso.random.lottery;

import com.google.gson.Gson;

/**
 * @author kjrz
 */
public class RandomPerson {
    public static final String WARSZAWA = "Warszawa-";
    public static final Gson GSON = new Gson();

    public final String firstName;
    public final String lastName;
    public final String district;
    public final String street;

    public RandomPerson(String firstName, String lastName, String district, String street) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.district = district;
        this.street = street;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + ", " + street + ", " + WARSZAWA + district;
    }

    public String toJson() {
        return GSON.toJson(this);
    }
}
