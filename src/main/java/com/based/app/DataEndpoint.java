package com.based.app;

import java.util.List;
import java.util.Map;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.based.entity.Database;
import com.based.entity.InsertRequest;

@Path("/data")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DataEndpoint {
	@POST
	@Path("/{tableName}")
	public String insert(@PathParam("tableName") String tableName, InsertRequest request) throws IllegalArgumentException {
		Database.insert(tableName, request.getValues());
		return "Inserted values for table '" + tableName + "':\n" + request + "\n";
	}

	@GET
	@Path("/{tableName}")
	public Map<String, List<String>> select(@PathParam("tableName") String tableName) throws IllegalArgumentException {
		return Database.select(tableName);
	}
}
