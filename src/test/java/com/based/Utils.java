package com.based;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;

import com.based.distrib.MachineTarget;
import com.based.distrib.Nodes;

public class Utils {
    public static String baseUrl = "http://localhost:8080/based";
    public static ResteasyClient client = Nodes.createHttpClient();

    public static MachineTarget getSelfTarget(String path) {
        return new MachineTarget(Nodes.getSelfIndex(), client.target(baseUrl + path));
    }
}
