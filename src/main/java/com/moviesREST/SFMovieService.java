package com.moviesREST;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


/*
 *  Created by IntelliJ IDEA.
 *   User: addodennis
 *   Date: 8/7/18
 *   Time: 8:17 AM
 *   To change this template use File | Settings | File Templates.
 */


/**
 * Root resource (exposed at "sfmservice" path)
 */

@Path("sfmservice")
public class SFMovieService {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/json" media type.
     *
     * @return http response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response GetAllMovies() {
        String url = "https://data.sfgov.org/resource/wwmu-gmzc.json";

        return remoteDataJSON(url);

    }

    /**
     * Service with filters (Filmname)
     *
     * @return http response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{filmname}")
    public Response GetMoviesByFilter(@PathParam("filmname") String filmname) {

        String url;
        if (filmname != null) {
            url = "https://data.sfgov.org/resource/wwmu-gmzc.json?title=" + filmname.replaceAll(" ", "%20");
        } else {
            return Response.status(500).entity("{response: \"Empty filter params\"}").build();
        }

        System.out.println(url);

        return remoteDataJSON(url);

    }

    /**
     * Extracting remote data with one function to prevent code duplications.
     */
    private static Response remoteDataJSON(String url) {
        StringBuilder jsonText = new StringBuilder();

        try (BufferedReader in = new
                BufferedReader(new InputStreamReader((new URL(url)).openConnection().getInputStream()))) {

            String line;
            while ((line = in.readLine()) != null) {
                jsonText.append(line);
            }
            //jsonText = in.lines().collect(Collectors.joining(""));

            return Response.status(200).type("application/json").entity(jsonText.toString()).build();
        } catch (IOException e) {
            return Response.status(500).type("application/json").entity("Server Error: " + e.getMessage()).build();
        }

    }
}
