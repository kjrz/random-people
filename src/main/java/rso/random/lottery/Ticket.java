package rso.random.lottery;

/**
 * @author kjrz
 */
public class Ticket {

    private String district;
    private String street;

    public Ticket setDistrict(String district) {
        this.district = district;
        return this;
    }

    public Ticket setStreet(String street) {
        this.street = street;
        return this;
    }

    public RandomPerson scratchOff() {
        if (street != null && district != null) return Lottery.INSTANCE.nextRandomPerson(district, street);
        else return Lottery.INSTANCE.nextRandomPerson();
    }
}
