package rso.random.lottery;

/**
 * @author kjrz
 */
public class RandomPerson {
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

    public RandomPerson(String firstName, String lastName, String street) {
        this(firstName, lastName, "Wola", street);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + ", " + street + ", " + "Warszawa" + "-" + district;
    }
}
