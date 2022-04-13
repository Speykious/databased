package com.based.app;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.based.entity.TableMetadata;

@Path("/table")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TableEndpoint {
	@POST
	public String createTable(TableMetadata request) {
		return "Received CreateTableRequest:\n" + request + "\n";
	}

	@GET
	public String getTable() {
		return "Return table metadata here";
	}
}
