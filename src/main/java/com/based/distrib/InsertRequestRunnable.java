package com.based.distrib;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import com.based.entity.NbLinesResponse;

public class InsertRequestRunnable extends RequestRunnable {
	private int nbLines = 0;
	private byte[] byteStream;

	public InsertRequestRunnable(MachineTarget machineTarget, byte[] byteStream) {
		super(machineTarget);
		this.byteStream = byteStream;
	}

	public int getNbLines() {
		return nbLines;
	}

	@Override
	protected void sendRequest(Builder request) {
		MultipartFormDataOutput output = new MultipartFormDataOutput();
		output.addFormData("file", byteStream,
				MediaType.APPLICATION_OCTET_STREAM_TYPE);
		Response response = request.put(Entity.entity(output, MediaType.MULTIPART_FORM_DATA));
		nbLines = response.readEntity(NbLinesResponse.class).getNbLines();
	}
}