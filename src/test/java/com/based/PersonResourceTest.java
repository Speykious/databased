package com.based;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PersonResourceTest {
    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        // start the server
        server = App.startServer();
        // create the client
        Client c = ClientBuilder.newClient();

        target = c.target(App.BASE_URI);
    }

    @After
    public void tearDown() throws Exception {
        server.shutdownNow();
    }

    /**
     * Test to see that a Person is sent in response.
     */
    @Test
    public void testGetPerson() {
        Person responsePerson = target.path("person").request().get(Person.class);
        assertNotNull(responsePerson);
        assertEquals(responsePerson.getName(), "UwU");
        assertEquals(responsePerson.getAge(), 18);
    }
}
