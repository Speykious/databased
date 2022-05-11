package com.based.app;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.based.distrib.Nodes;
import com.based.exception.RuntimeExceptionMapper;
import com.based.filter.GsonProvider;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/based")
public class App extends Application {
	public App() {
		super();

		int selfIndex = Integer.parseInt(System.getProperty("index", "0"));
		Nodes.setSelfIndex(selfIndex);
		System.out.println("Set selfIndex to " + selfIndex);

		try {
			Nodes.pingOtherNodes();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Set<Object> getSingletons() {
		Set<Object> sets = new HashSet<>(1);
		sets.add(new TableEndpoint());
		sets.add(new DataEndpoint());
		sets.add(new CsvEndpoint());
		sets.add(new DistributedTestEndpoint());
		return sets;
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> sets = new HashSet<>(1);
		sets.add(GsonProvider.class);
		sets.add(RuntimeExceptionMapper.class);
		return sets;
	}
}
