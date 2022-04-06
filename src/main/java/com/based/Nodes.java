package com.based;

import java.security.InvalidParameterException;
import java.util.concurrent.TimeUnit;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

public final class Nodes {
    private static int[] baseIp = new int[] { 132, 227, 115, 97 };
    private static int count = 24;
    private static int port = 8080;
    private static int selfIndex = 0;
    private static ResteasyClient httpClient = createHttpClient();
    
    public static ResteasyClient createHttpClient() {
        return new ResteasyClientBuilder()
            .connectTimeout(200, TimeUnit.MILLISECONDS)
            .build();
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

    public static ResteasyWebTarget getMachineTarget(int index, String path) {
        System.out.println("Getting target " + index + " with path " + path);
        return httpClient.target(getMachineUrl(index, path));
    }

    public static ResteasyWebTarget[] getOtherMachineTargets(String path) {
        return getOtherMachineTargets(0, count - 1, path);
    }

    public static ResteasyWebTarget[] getOtherMachineTargets(int beg, int end, String path) {
        int length = end - beg + 1;
        if (selfIndex >= beg && selfIndex <= end)
            length--;
        
        ResteasyWebTarget[] targets = new ResteasyWebTarget[length];
        for (int i = 0, targetIndex = 0; i <= end - beg; i++) {
            if (i == selfIndex) continue;
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
