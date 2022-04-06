package com.based.app;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.based.exception.RuntimeExceptionMapper;
import com.based.filter.GsonProvider;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/based")
public class App extends Application {
	@Override
	public Set<Object> getSingletons() {
		Set<Object> sets = new HashSet<>(1);
		sets.add(new TestEndpoint());
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
