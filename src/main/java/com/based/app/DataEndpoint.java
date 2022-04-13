package com.based.app;

import java.util.List;
import java.util.Map;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.based.entity.InsertRequest;
import com.based.entity.NbLinesResponse;
import com.based.model.Database;

@Path("/data")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DataEndpoint {
	@POST
	@Path("/{tableName}")
	public NbLinesResponse insert(@PathParam("tableName") String tableName, InsertRequest request) throws IllegalArgumentException {
		List<String> values = request.getValues();
		Database.insert(tableName, values);
		return new NbLinesResponse(values.size());
	}

	@GET
	@Path("/{tableName}")
	public Map<String, List<String>> select(@PathParam("tableName") String tableName) throws IllegalArgumentException {
		return Database.select(tableName);
	}
}
