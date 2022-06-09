package com.based.distrib;

import java.lang.reflect.Array;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

public abstract class RequestRunnable implements Runnable {
	protected MachineTarget machineTarget;
	protected boolean hasResponded;

	public RequestRunnable(MachineTarget machineTarget) {
		this.machineTarget = machineTarget;
		this.hasResponded = false;
	}

	public final boolean hasResponded() {
		return hasResponded;
	}

	protected abstract void sendRequest(Builder request) throws Exception;

	@Override
	public final void run() {
		try {
			ResteasyWebTarget target = machineTarget.getTarget();
			Builder request = target.request().accept(MediaType.APPLICATION_JSON_TYPE);
			sendRequest(request);
			hasResponded = true;
		} catch (Exception e) {
			String emsg = e.getMessage();
			if (emsg.contains("timed out"))
				System.err.println(machineTarget + " timed out.");
			else if (emsg.contains("Connection refused"))
				System.err.println(machineTarget + " refused connection.");
			else
				System.err.println(machineTarget + " had error: " + e.getMessage());
		}
	}

	public static <T extends RequestRunnable> BroadcastedRequests<T> broadcastRequests(Class<T> clazz,
			MachineTarget[] machineTargets,
			RequestRunnableConstructor<T> constructor) throws InterruptedException {
		if (machineTargets.length == 0)
			return null;

		@SuppressWarnings("unchecked")
		T[] runnables = (T[]) Array.newInstance(clazz, machineTargets.length);
		Thread[] requests = new Thread[machineTargets.length];

		for (int i = 0; i < requests.length; i++) {
			runnables[i] = constructor.construct(machineTargets[i], i);
			requests[i] = new Thread(runnables[i]);
			requests[i].start();
		}

		int nSuccessfulRunnables = 0;
		for (int i = 0; i < requests.length; i++) {
			requests[i].join();
			if (runnables[i].hasResponded()) {
				nSuccessfulRunnables++;
				System.out.println("Machine " + machineTargets[i] + " responded!");
			}
		}

		@SuppressWarnings("unchecked")
		T[] successfulRunnables = (T[]) Array.newInstance(clazz, nSuccessfulRunnables);
		@SuppressWarnings("unchecked")
		T[] failedRunnables = (T[]) Array.newInstance(clazz, requests.length - nSuccessfulRunnables);

		int si = 0, fi = 0;
		for (T runnable : runnables) {
			if (runnable.hasResponded())
				successfulRunnables[si++] = runnable;
			else
				failedRunnables[fi++] = runnable;
		}

		return new BroadcastedRequests<>(successfulRunnables, failedRunnables);
	}
}