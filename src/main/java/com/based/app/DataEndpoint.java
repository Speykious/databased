package com.based.app;

import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.based.entity.NbLinesResponse;
import com.based.entity.dto.RowDTO;
import com.based.exception.MissingTableException;
import com.based.model.Row;
import com.based.services.InsertService;
import com.based.services.SelectService;

@Path("/data")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DataEndpoint {
	@POST
	@Path("/{tableName}")
	public NbLinesResponse insert(@PathParam("tableName") String tableName, RowDTO request) throws MissingTableException {
		System.out.println("Inserting values into a table");
		List<Object> values = request.getValues();
		InsertService insertService = new InsertService();
		insertService.insert(tableName, values);
		return new NbLinesResponse(1);
	}

	@GET
	@Path("/{tableName}")
	public List<Row> select(@PathParam("tableName") String tableName) throws MissingTableException {
		System.out.println("Selecting some data from a table");
		SelectService selectService = new SelectService();
		return selectService.selectAll(tableName);
	}
}
