package com.based.app;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.based.Nodes;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

@Path("/ppti")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DistributedTestEndpoint {
	@GET
	@Path("/say-yes")
	public String sayYes() {
		return "yes";
	}

	@GET
	@Path("/broadcast")
	public String broadcast() {
		int machineResponses = 0;
		ResteasyWebTarget[] machineTargets = Nodes.getOtherMachineTargets("/ppti/say-yes");
		for (var machineTarget : machineTargets) {
			Response response = machineTarget.request().get();
			if (response.readEntity(String.class) == "yes")
			machineResponses++;
		}
		return "Number of nodes that responded: " + machineResponses;
	}
}
