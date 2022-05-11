package com.based.app;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.based.entity.NbLinesResponse;
import com.based.exception.MissingTableException;
import com.based.services.InsertService;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Path("/csv")
@Produces(MediaType.APPLICATION_JSON)
public class CsvEndpoint {
    @POST
    @Path("/{tableName}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public NbLinesResponse importCsv(@PathParam("tableName") String tableName, MultipartFormDataInput input)
            throws IOException, MissingTableException, IllegalArgumentException, InterruptedException {
        System.out.println("Importing some CSV file");
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("file");

        List<InputStream> inputStreams = new ArrayList<>();
        for (InputPart inputPart : inputParts)
            inputStreams.add(inputPart.getBody(InputStream.class, null));
        InputStream csv = new SequenceInputStream(Collections.enumeration(inputStreams));

        InsertService insertService = new InsertService();
        int nbLines = insertService.insertCsv(tableName, csv);

        return new NbLinesResponse(nbLines);
    }
}
