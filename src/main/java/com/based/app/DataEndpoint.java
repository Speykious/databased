package com.based.app;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.based.entity.NbLinesResponse;
import com.based.entity.dto.SelectRequestDTO;
import com.based.exception.InvalidGroupByException;
import com.based.exception.InvalidOperationException;
import com.based.exception.InvalidSelectException;
import com.based.exception.MissingColumnException;
import com.based.exception.MissingTableException;
import com.based.exception.NullRequestException;
import com.based.model.Row;
import com.based.services.InsertService;
import com.based.services.SelectService;

@Path("/data")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DataEndpoint {
	@POST
	@Path("/{tableName}")
	public NbLinesResponse insert(@PathParam("tableName") String tableName, String csvRow) throws MissingTableException, IllegalArgumentException, IOException {
		System.out.println("Inserting values into a table");
		InsertService insertService = new InsertService();
		insertService.insert(tableName, csvRow);
		return new NbLinesResponse(1);
	}

	@GET
	@Path("/{tableName}")
	public List<Row> select(@PathParam("tableName") String tableName, SelectRequestDTO selectRequest) throws MissingTableException, MissingColumnException, NullRequestException, InvalidSelectException, InvalidOperationException, InvalidGroupByException {
		System.out.println("Selecting some data from a table");
		SelectService selectService = new SelectService();
		
		if(selectRequest != null) {
			return selectService.select(tableName, selectRequest);
		}

		throw new NullRequestException("the request is empty");

	}
}
