package com.based.app;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.based.distrib.Nodes;
import com.based.entity.dto.NodePingDTO;

@Path("/node")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NodeEndpoint {
	@GET
	@Path("/ping")
	public NodePingDTO sayYes() {
		return new NodePingDTO(Nodes.getSelfIndex());
	}
}
