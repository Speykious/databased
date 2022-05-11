package com.based.distrib;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.based.entity.dto.NodePingDTO;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

public class PingRequestRunnable implements Runnable {
	private MachineTarget machineTarget;
	private boolean hasResponded;
	private int nodeIndex;

	public PingRequestRunnable(MachineTarget machineTarget) {
		this.machineTarget = machineTarget;
		this.hasResponded = false;
	}

	public boolean hasResponded() {
		return hasResponded;
	}

	public int getNodeIndex() {
		return nodeIndex;
	}

	@Override
	public void run() {
		try {
			ResteasyWebTarget target = machineTarget.getTarget();
			Builder request = target.request().accept(MediaType.TEXT_HTML);
			Response response = request.get();

			NodePingDTO responseDto = response.readEntity(NodePingDTO.class);
			nodeIndex = responseDto.getNodeIndex();
			hasResponded = true;
		} catch (Exception e) {
			// Do nothing to avoid exception noise
		}
	}
}