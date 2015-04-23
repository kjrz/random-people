package rso.random.api;

import rso.random.lottery.Ticket;

import static spark.Spark.get;

/**
 * @author kjrz
 */
public class RestApi {

    public static void main(String[] args) {
        get("/person", (req, res) -> new Ticket()
                .setDistrict(req.queryParams("district"))
                .setStreet(req.queryParams("street"))
                .scratchOff() + "\n");
    }
}
