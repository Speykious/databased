package com.based.app;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.based.entity.InsertRequest;

@Path("/data")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DataEndpoint {
	@POST
	@Path("/{tableName}")
	public String insert(@PathParam("tableName") String tableName, InsertRequest request) {
		return "Received InsertRequest for table '" + tableName + "':\n" + request + "\n";
	}

	@GET
	@Path("/{tableName}")
	public String select(@PathParam("tableName") String tableName) {
		// TODO: add `SelectRequest` for anything like 'where' and alike
		return "Return all data from table '" + tableName + "' here\n";
	}
}
