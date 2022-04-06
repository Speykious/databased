package com.based.app;

import javax.ws.rs.*;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.based.MachineTarget;
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
		MachineTarget[] machineTargets = Nodes.getOtherMachineTargets("/ppti/say-yes");

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
		
		return "Number of nodes that responded: " + machineResponses + "\n";
	}
}

class YesRequestRunnable implements Runnable {
	private MachineTarget machineTarget;
	private boolean hasResponded;

	public YesRequestRunnable(MachineTarget machineTarget) {
		this.machineTarget = machineTarget;
		this.hasResponded = false;
	}

	public boolean hasResponded() {
		return hasResponded;
	}

	@Override
	public void run() {
		try {
			ResteasyWebTarget target = machineTarget.getTarget();
			Builder request = target.request().accept(MediaType.TEXT_HTML);
			Response response = request.get();

			String responseString = response.readEntity(String.class);
			if (responseString.equals("yes")) {
				System.out.println("Machine " + machineTarget + " responded");
				hasResponded = true;
			} else {
				hasResponded = false;
			}
		} catch (Exception e) {
			// Do nothing to avoid exception noise
		}
	}
}
