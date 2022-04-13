package com.based.app;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.based.entity.CreateTableRequest;

@Path("/table")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TableEndpoint {
	@POST
	public String createTable(CreateTableRequest request) {
		return "Received CreateTableRequest:\n" + request + "\n";
	}
}
