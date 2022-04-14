package com.based.app;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.based.entity.dto.TableDTO;
import com.based.exception.DuplicateTableException;
import com.based.exception.InvalidTableDTOException;
import com.based.exception.MissingTableException;
import com.based.model.Database;
import com.based.model.Table;

@Path("/table")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TableEndpoint {
	@POST
	public String createTable(TableDTO request) throws DuplicateTableException, InvalidTableDTOException {
		Database.addTable(new Table(request));
		return "Created table:\n" + request;
	}

	@GET
	@Path("/{tableName}")
	public TableDTO getTable(@PathParam("tableName") String tableName) throws MissingTableException {
		return Database.getTable(tableName).getDTO();
	}
}
