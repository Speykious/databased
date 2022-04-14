package com.based.app;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.based.exception.MissingTableException;
import com.based.services.InsertService;

import java.io.IOException;
import java.io.InputStream;

@Path("/csv")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CsvEndpoint {
    @POST
    @Path("/{tableName}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void importCsv(@PathParam("tableName") String tableName, InputStream csv) throws IOException, MissingTableException {
        System.out.println("Importing some CSV file");
        InsertService insertService = new InsertService();
        insertService.insertCsv(tableName, csv);
    }
}
