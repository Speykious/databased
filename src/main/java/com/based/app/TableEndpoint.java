package com.based.app;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.based.entity.TableInfo;
import com.based.model.Database;

@Path("/table")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TableEndpoint {
	@POST
	public String createTable(TableInfo request) {
		Database.createTable(request);
		return "Created table:\n" + request + "\n";
	}

	@GET
	@Path("/{tableName}")
	public TableInfo getTable(@PathParam("tableName") String tableName) {
		return Database.getTableInfo(tableName);
	}
}
