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
	@Produces(MediaType.TEXT_HTML)
	public String sayYes() {
		return "yes";
	}

	@GET
	@Path("/broadcast")
	@Produces(MediaType.TEXT_HTML)
	public String broadcast() {
		int machineResponses = 0;
		ResteasyWebTarget[] machineTargets = Nodes.getOtherMachineTargets("/ppti/say-yes");

		YesRequestRunnable[] runnables = new YesRequestRunnable[machineTargets.length];
		Thread[] requests = new Thread[machineTargets.length];
		for (int i = 0; i < requests.length; i++) {
			runnables[i] = new YesRequestRunnable(machineTargets[i]);
			requests[i] = new Thread(runnables[i]);
			requests[i].start();
		}
		
		for (int i = 0; i < requests.length; i++) {
			try {
				requests[i].join();
				if (runnables[i].hasResponded())
					machineResponses++;
			} catch (Exception e) {
				System.err.println("ERROR: Request thread got interrupted: " + e);
			}
		}
		
		return "Number of nodes that responded: " + machineResponses;
	}
}

class YesRequestRunnable implements Runnable {
	private ResteasyWebTarget machineTarget;
	private boolean hasResponded;

	public YesRequestRunnable(ResteasyWebTarget machineTarget) {
		this.machineTarget = machineTarget;
		this.hasResponded = false;
	}

	public boolean hasResponded() {
		return hasResponded;
	}

	@Override
	public void run() {
		System.out.println("Requesting to " + machineTarget.getUri());
		try {
			var request = machineTarget.request();
			request.accept(MediaType.TEXT_HTML);
			Response response = request.get();
			String responseString = response.readEntity(String.class);
			if (responseString.equals("yes")) {
				System.out.println("They said yes! :D");
				hasResponded = true;
			} else {
				System.out.println("They didn't say yes :(");
				hasResponded = false;
			}
		} catch (Exception e) {
			// Do nothing to avoid exception noise
		}
	}
}
