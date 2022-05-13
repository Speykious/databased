package com.based.distrib;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.based.filter.GsonProvider;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

public final class Nodes {
    private static int[] baseIp = new int[] { 132, 227, 115, 97 };

    private static int count = 24;
    private static int port = Integer.parseInt(System.getProperty("jetty.port", "8080"));
    private static int selfIndex = 0;
    private static ResteasyClient httpClient = createHttpClient();

    private static List<Integer> onlineNodeIndexes;

    public static ResteasyClient createHttpClient() {
        return new ResteasyClientBuilder()
                .connectionPoolSize(727)
                .connectTimeout(200, TimeUnit.MILLISECONDS)
                .build()
                .register(GsonProvider.class);
    }

    /**
     * Tells all other nodes that it exists and constructs back a list of active
     * nodes.
     * 
     * @throws InterruptedException
     */
    public static void pingOtherNodes() throws InterruptedException {
        onlineNodeIndexes = new ArrayList<>();

        MachineTarget[] machineTargets = Nodes.getOtherMachineTargets("/node/ping");

        PingRequestRunnable[] runnables = new PingRequestRunnable[machineTargets.length];
        Thread[] requests = new Thread[machineTargets.length];
        for (int i = 0; i < requests.length; i++) {
            runnables[i] = new PingRequestRunnable(machineTargets[i]);
            requests[i] = new Thread(runnables[i]);
            requests[i].start();
        }

        for (int i = 0; i < requests.length; i++) {
            requests[i].join();
            if (runnables[i].hasResponded()) {
                System.out.println("Machine " + machineTargets[i] + " responded!");
                onlineNodeIndexes.add(runnables[i].getNodeIndex());
            }
        }

        if (onlineNodeIndexes.size() == 0)
            System.out.println("Nobody responded :(");
        else {
            System.out.print("Registered " + onlineNodeIndexes.size() + " indexes:");
            for (int nodeIndex : onlineNodeIndexes)
                System.out.print(" " + nodeIndex);
            System.out.println();
        }
    }

    public static int getNextOnlineNodeIndex() throws IndexOutOfBoundsException {
        for (int index : onlineNodeIndexes) {
            if (index > selfIndex)
                return index;
        }

        return onlineNodeIndexes.get(0);
    }

    public static List<Integer> getOnlineNodeIndexes() {
        return onlineNodeIndexes;
    }

    public static String getMachineIp() {
        return getMachineIp(selfIndex);
    }

    public static String getMachineIp(int index) {
        if (index < 0 || index >= count)
            throw new InvalidParameterException("Index is out of range");

        return "" + baseIp[0] + "." + baseIp[1] + "." + baseIp[2] + "." + (baseIp[3] + index);
    }

    public static String getMachineUrl() {
        return getMachineUrl(selfIndex);
    }

    public static String getMachineUrl(String path) {
        return getMachineUrl(selfIndex, path);
    }

    public static String getMachineUrl(int index) {
        return getMachineUrl(index, "/");
    }

    public static String getMachineUrl(int index, String path) {
        return "http://" + getMachineIp(index) + ":" + port + "/based" + path;
    }

    public static MachineTarget getMachineTarget(int index, String path) {
        System.out.println("Getting target " + index + " with path " + path);
        return new MachineTarget(index, httpClient.target(getMachineUrl(index, path)));
    }

    public static MachineTarget getNextOnlineMachineTarget(String path) {
        while (true) {
            MachineTarget machineTarget = getMachineTarget(getNextOnlineNodeIndex(), path);
            PingRequestRunnable requestRunnable = new PingRequestRunnable(machineTarget);
            requestRunnable.run();
            if (!requestRunnable.hasResponded())
                onlineNodeIndexes.remove((Integer) machineTarget.getIndex());
            else
                return machineTarget;
        }
    }

    public static MachineTarget[] getOtherMachineTargets(String path) {
        return getOtherMachineTargets(0, count - 1, path);
    }

    public static MachineTarget[] getOtherMachineTargets(int beg, int end, String path) {
        int length = end - beg + 1;
        if (selfIndex >= beg && selfIndex <= end)
            length--;

        MachineTarget[] targets = new MachineTarget[length];
        for (int i = 0, targetIndex = 0; i <= end - beg; i++) {
            if (i == selfIndex)
                continue;
            targets[targetIndex++] = getMachineTarget(i, path);
        }

        return targets;
    }

    public static int getSelfIndex() {
        return selfIndex;
    }

    public static void setSelfIndex(int index) {
        selfIndex = index;
    }

    public static int getCount() {
        return count;
    }

    public static int getPort() {
        return port;
    }
}