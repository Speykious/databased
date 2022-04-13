package com.based.app;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.based.entity.Account;
import com.based.entity.Table;

import java.io.InputStream;
import java.util.List;

@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TestEndpoint {
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String helloWorld() {
		return "Hello World\n";
	}

	@GET
	@Path("/list")
	public List<String> getListInParams(@QueryParam("ids") List<String> ids) {
		System.out.println(ids);
		return ids;
	}

	@POST
	@Path("/entity")
	public Account getAccount(Account account) {
		System.out.println("Received account " + account);
		account.setUpdated(System.currentTimeMillis());
		return account;
	}

	@POST
	@Path("/table")
	public Table createTable(Table table) {
		Database.putTable(table);
		System.out.print(table + " is created");
		return table;
	}

	@GET
	@Path("/exception")
	public Response exception() {
		throw new RuntimeException("Cringe runtime error");
	}

	@POST
	public String upload(InputStream input){
		//Lire scv inoutStream

		return "";
	}

	/**
	 * new class endPOint 	createTable, uploadFichier, requete select
	 * Stocker des strings	/	Select
	 * 
	 * list list string
	 */
}
