
package com.based.model;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import filter.GsonProvider;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreationTest {
  private ResteasyClient client;
  private final String baseUrl = "http://localhost:8081/api";
  private ResteasyWebTarget target;
  Gson gson = new GsonBuilder().setPrettyPrinting().create();

  @Before
  public void setUp() {
    this.client = new ResteasyClientBuilder().build();
    this.client.register(GsonProvider.class);
    this.target = client.target(UriBuilder.fromPath(baseUrl));
  }

  @After
  public void teardown() {
    this.client.close();
  }

  @Test
  public void testCreateTable() {
    ResteasyWebTarget target = client.target(UriBuilder.fromPath(baseUrl));
    
    String input = formatJSON(gson, "{\"tableName\":\"taxi\",\"columns\":{\"VendorID\":\"int\",\"tpep_pickup_datetime\":\"string\",\"tpep_dropoff_datetime\":\"string\",\"passenger_count\":\"int\",\"trip_distance\":\"string\",\"RatecodeID\":\"string\",\"store_and_fwd_flag\":\"string\",\"PULocationID\":\"int\",\"DOLocationID\":\"int\",\"payment_type\":\"string\",\"fare_amount\":\"string\",\"extra\":\"int\",\"mta_tax\":\"string\",\"tip_amount\":\"int\",\"tolls_amount\":\"string\",\"improvement_surcharge\":\"string\",\"total_amount\":\"string\",\"congestion_surcharge\":\"string\"}}");

    System.out.println("Sending request");

    Response response = target
      .path("/table")
      .queryParam("fromClient", true)
      .request()
      .post(Entity.entity(input, MediaType.APPLICATION_JSON));
    
    String actualResponse = response.readEntity(String.class);
    String expectedResponsde = "Creating a table: yellow_tripdata (with broadcast)";
    assertEquals(expected, actual);
    response.close();
  }
}

