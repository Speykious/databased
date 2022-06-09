package com.based.distrib;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class SayYesRequestRunnable extends RequestRunnable {
    private boolean hasSaidYes = false;

    public SayYesRequestRunnable(MachineTarget machineTarget) {
		super(machineTarget);
	}

	public boolean hasSaidYes() {
        return hasSaidYes;
    }

	@Override
	protected void sendRequest(Builder request) {
		Response response = request.accept(MediaType.TEXT_HTML).get();

		String responseDto = response.readEntity(String.class);
        hasSaidYes = "yes".equals(responseDto);
	}
}
