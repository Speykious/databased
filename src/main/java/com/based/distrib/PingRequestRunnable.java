package com.based.distrib;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.based.entity.dto.NodePingDTO;

public class PingRequestRunnable extends RequestRunnable {
	private int nodeIndex = -1;

	public PingRequestRunnable(MachineTarget machineTarget) {
		super(machineTarget);
	}

	public int getNodeIndex() {
		return nodeIndex;
	}

	@Override
	protected void sendRequest(Builder request) {
		Entity<NodePingDTO> nodePing = Entity.entity(new NodePingDTO(Nodes.getSelfIndex()),
				MediaType.APPLICATION_JSON_TYPE);
		Response response = request.post(nodePing);

		NodePingDTO responseDto = response.readEntity(NodePingDTO.class);
		nodeIndex = responseDto.getNodeIndex();
	}
}