package com.based.app;

import org.junit.Assert;
import org.junit.Test;

import com.based.Utils;
import com.based.distrib.SayYesRequestRunnable;

public class DistributedTestEndpointTest {
    @Test
    public void saysYes() {
        SayYesRequestRunnable runnable = new SayYesRequestRunnable(Utils.getSelfTarget("/ppti/say-yes"));
        runnable.run();
        Assert.assertTrue(runnable.hasResponded());
        Assert.assertTrue(runnable.hasSaidYes());
    }
}
