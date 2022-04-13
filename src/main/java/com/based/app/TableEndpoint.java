package com.based.app;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.based.entity.TableInfo;

@Path("/table")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TableEndpoint {
	@POST
	public String createTable(TableInfo request) {
		return "Received TableInfo:\n" + request + "\n";
	}

	@GET
	public String getTable() {
		return "Return table metadata here";
	}
}
