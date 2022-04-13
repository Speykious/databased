package com.based.app;

import javax.ws.rs.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.based.entity.TableInfo;

public class CsvEndpoint {
  @POST
  @Path("/csv")
  public void importCsv(InputStream inputstream, TableInfo table) throws IOException {
    InputStreamReader input = new InputStreamReader(inputstream);
    BufferedReader csvReader = new BufferedReader(input);
    String row;
    while ((row = csvReader.readLine()) != null) {
      String[] data = row.split(",");
    }
    csvReader.close();
  }
}
