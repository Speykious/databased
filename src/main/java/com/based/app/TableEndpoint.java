package com.based.app;

import javax.ws.rs.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;

import com.based.distrib.MachineTarget;
import com.based.distrib.Nodes;
import com.based.distrib.RequestRunnable;
import com.based.entity.dto.TableDTO;
import com.based.exception.DuplicateTableException;
import com.based.exception.InvalidColumnFormatException;
import com.based.exception.InvalidTableFormatException;
import com.based.exception.MissingTableException;
import com.based.model.Database;
import com.based.model.Table;

@Path("/table")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TableEndpoint {
	@POST
	public TableDTO createTable(TableDTO request)
			throws DuplicateTableException, InvalidTableFormatException, InvalidColumnFormatException,
			InterruptedException {
		System.out.println("Creating a table: " + request.getName() + " (with broadcast)");
		Database.addTable(new Table(request));

		RequestRunnable.broadcastRequests(
				CreateTableRequestRunnable.class,
				Nodes.getOtherOnlineMachineTargets("/table"),
				(machineTarget) -> new CreateTableRequestRunnable(machineTarget, request));

		return request;
	}

	@PUT
	public TableDTO putTable(TableDTO request)
			throws DuplicateTableException, InvalidTableFormatException, InvalidColumnFormatException {
		System.out.println("Creating a table: " + request.getName());
		Database.addTable(new Table(request));
		return request;
	}

	@GET
	@Path("/{tableName}")
	public TableDTO getTable(@PathParam("tableName") String tableName) throws MissingTableException {
		System.out.println("Fetching information on a table");
		return Database.getTable(tableName).getDTO();
	}
}

class CreateTableRequestRunnable extends RequestRunnable {
	private TableDTO tableDTO;

	public CreateTableRequestRunnable(MachineTarget machineTarget, TableDTO tableDTO) {
		super(machineTarget);
		this.tableDTO = tableDTO;
	}

	@Override
	protected void sendRequest(Builder request) throws Exception {
		TableDTO response = (TableDTO) request.put(
				Entity.entity(TableDTO.class, MediaType.APPLICATION_JSON_TYPE)).getEntity();

		if (response.hashCode() != tableDTO.hashCode())
			throw new Exception("Response TableDTO is different from request TableDTO!");
	}
}