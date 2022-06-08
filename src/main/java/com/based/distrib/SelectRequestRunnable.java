package com.based.distrib;

import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.based.entity.dto.SelectRequestDTO;
import com.based.model.Row;

public class SelectRequestRunnable extends RequestRunnable {
	private SelectRequestDTO select;
	private List<Row> responseDto;

	public SelectRequestRunnable(MachineTarget machineTarget, SelectRequestDTO select) {
		super(machineTarget);
		this.select = select;
	}

	public List<Row> getResponseDto() {
		return responseDto;
	}

	public void setResponseDto(List<Row> responseDto) {
		this.responseDto = responseDto;
	}

	@Override
	protected void sendRequest(Builder request) {
		Entity<SelectRequestDTO> selectRequest = Entity.entity(select,
				MediaType.APPLICATION_JSON_TYPE);
		Response response = request.put(selectRequest);

		responseDto = response.readEntity(new GenericType<List<Row>>() {});
	}
}