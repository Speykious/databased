package com.based.app;

import javax.swing.ScrollPaneLayout.UIResource;
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
  public void parseCSV(InputStream inputstream, TableInfo table) throws IOException{
    // lire le csv -> transformer dans list<list<string>>
    InputStreamReader input = new InputStreamReader(inputstream);
    BufferedReader csvReader = new BufferedReader(input) ;
    String row;
    while ((row = csvReader.readLine()) != null) {
      // csv -> table
      /*
      row -> <List<List<String>> 
      */
      String[] data = row.split(",");
      // mettre la contenu de fichier dans la Table
    } 
    csvReader.close();
  }
}
