package com.moviesREST;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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

@Path("/sfmservice")
public class SFMovieService {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * This returns all the movies and location.
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
     * Service with filters (title) and (location)
     *
     *
     * All queries are handled by this function. This API support filter attributes title and location.
     * You can get a list of all movies showing at a particular location or list of all movies by title.
     * @example:
     *  curl https://movies-rest-server.herokuapp.com/api_v1.0/sfmservice/query?location=200%20block%20Market%20Street
     *     return value: [{"title":"180","locations":"200 block Market Street"}]
     *
     *  Or in the browser, just paste this.
     *  https://movies-rest-server.herokuapp.com/api_v1.0/sfmservice/query?location=200 block Market Street
     *      return value: [{"title":"180","locations":"200 block Market Street"}]
     *
     * @return http response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/query")
    public Response GetMoviesByFilter(@Context UriInfo params) {

        String title = params.getQueryParameters().getFirst("title");
        String location = params.getQueryParameters().getFirst("location");
        String url;

        if(title != null && location == null){
            //getQueryParameters from UriInfo removes all tailing spaces and separator from the passed values
            //adding the encoding space character again.
            url = "https://data.sfgov.org/resource/wwmu-gmzc.json?title=" + title.replaceAll(" ", "%20");
        }else if(location != null) {
            url = "https://data.sfgov.org/resource/wwmu-gmzc.json?locations=" + location.replaceAll(" ", "%20");
        }else {
            //Both params null, panic to the user with empty query response.
            return  Response.ok().type("application/json").entity("Server Error: Empty query").build();
        }

        return remoteDataJSON(url);

    }

    /**
     * Extracting remote data with one function to prevent code duplications.
     * All necessary data will be extracted from the returned json response from remote server
     * title and location is important here.
     */
    private static Response remoteDataJSON(String url) {

        StringBuilder jsonText = new StringBuilder();
        String jsonstring;

        //This is needed to help order the Json elements. because JSONObject is a unorder map.
        HashMap<String,String> objt = new LinkedHashMap<>();

        try (BufferedReader in = new
                BufferedReader(new InputStreamReader((new URL(url)).openConnection().getInputStream()))) {

            String line;
            while ((line = in.readLine()) != null) {
                jsonText.append(line);
            }


            //Saving Json response in order to extract the necessary data from the response.
            //Only location and title is important here
            JSONArray list = new JSONArray();

            JSONArray jsonArray = new JSONArray(jsonText.toString());

            for (int i = 0; i < jsonArray.length(); i++) {

                //optString:  Don't break if elements not found, just return empty string
                String location = jsonArray.getJSONObject(i).optString("locations");
                String title = jsonArray.getJSONObject(i).optString("title");

                if(!location.isEmpty() && !title.isEmpty()){
                    objt.put("locations",location);
                    objt.put("title",title);
                    list.put(objt);
                }

            }
            jsonstring = Stream.of(list).map(JSONArray::toString).collect(Collectors.joining(","));

            return Response.status(200).type("application/json").entity(jsonstring).build();
        } catch (IOException e) {
            return Response.ok().type("application/json").entity("Server Error: " + e.getMessage()).build();
        }

    }
}
