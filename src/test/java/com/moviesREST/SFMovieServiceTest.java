package com.moviesREST;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SFMovieServiceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(SFMovieService.class);
    }

    /**
     * Test to see that the movies result empty in the response.
     */
    @Test
    public void testGetMovies() {
        final String responseMsg = target().path("sfmservice").request().get(String.class);

        assertNotEquals(0,responseMsg.length());
    }

    /**
     * Test to see that the requested movie with title 180 returned something in the response.
     */
    @Test
    public void testGetMoviesByTitle() {
        final Response responseMsg = target().path("sfmservice/180").request().get();

        assertEquals("should return status 200", 200, responseMsg.getStatus());
    }

    /**
     * Test to see without pagenot found response works.
     */
    @Test
    public void testPageNotFound() {
        final Response responseMsg = target().path("sfmservic34").request().get();

        assertEquals("should return status 404", 404, responseMsg.getStatus());
    }
}
