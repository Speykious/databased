package com.based.app;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.based.model.Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

@Path("/csv")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CsvEndpoint {
  @POST
	@Path("/{tableName}")
  public void importCsv(@PathParam("tableName") String tableName, InputStream inputstream) throws IOException {
    InputStreamReader input = new InputStreamReader(inputstream);
    BufferedReader csvReader = new BufferedReader(input);
    String row;
    while ((row = csvReader.readLine()) != null) {
      List<String> values = Arrays.asList(row.split(","));
      Database.insert(tableName, values);
    }
    csvReader.close();
  }
}
