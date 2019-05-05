package no.hvl.dat110.ac.rest;

import com.google.gson.Gson;

import static spark.Spark.*;

/**
 * Hello world!
 */
public class App {

    static AccessLog accesslog = null;
    static AccessCode accesscode = null;

    public static void main(String[] args) {

        if (args.length > 0) {
            port(Integer.parseInt(args[0]));
        } else {
            port(8080);
        }

        // objects for data stored in the service

        accesslog = new AccessLog();
        accesscode = new AccessCode();

        after((req, res) -> {
            res.type("application/json");
        });

        // for basic testing purposes
        get("/accessdevice/hello", (req, res) -> {

            Gson gson = new Gson();

            return gson.toJson("IoT Access Control Device");
        });

        // TODO: implement the routes required for the access control service

        get("/accessdevice/code", ((request, response) -> {
            Gson gson = new Gson();
            String code = gson.toJson(accesscode);
            return code;
        }));

        get("/accessdevice/log/", ((request, response) -> {
            //	accesslog.add("LOCKED"); // For testing purposes.
            return accesslog.toJson();
        }));

        get("/accessdevice/log/:id", ((request, response) -> {
            AccessEntry accessEntry = null;
            Gson gson = new Gson();
            try {
                int id = Integer.parseInt(request.params(":id"));
                accessEntry = accesslog.get(id);
            } catch (Exception e) {
            }
            return gson.toJson(accessEntry);
        }));

        post("/accessdevice/log/", (request, response) -> {
            Gson gson = new Gson();
            AccessMessage message = gson.fromJson(request.body(), AccessMessage.class);
            int id = accesslog.add(message.getMessage());
            return "{\"id\":" + id + ",\"message:\"" + "\"" + message.getMessage() + "\"}";
        });

        put("/accessdevice/code", ((request, response) -> {
            Gson gson = new Gson();
            AccessCode code = gson.fromJson(request.body(), AccessCode.class);
            accesscode = code;
            return gson.toJson(accesscode);
        }));

        delete("/accessdevice/log/", ((request, response) -> {
            accesslog.clear();
            return "{}";
        }));


    }

}
