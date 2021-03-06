package com.alibaba.metrics.tomcat;

import com.alibaba.metrics.MetricManager;
import com.alibaba.metrics.MetricName;
import com.alibaba.metrics.MetricRegistry;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.junit.Assert;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TestTomcatThreadMetrics {

    @Test
    public void testTomcatThreadMetrics() {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(22222);
        tomcat.addContext("", "/tmp");
        tomcat.addServlet("", "testServlet", TestServlet.class.getName());
        MetricManager.register("tomcat", MetricName.build("middleware.tomcat.thread"), new ThreadGaugeSet());
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }

        MetricRegistry registry = MetricManager.getIMetricManager().getMetricRegistryByGroup("tomcat");
        Assert.assertNotNull(registry.getGauges().get(MetricName.build("middleware.tomcat.thread.busy_count")));
        Assert.assertNotNull(registry.getGauges().get(MetricName.build("middleware.tomcat.thread.total_count")));
        Assert.assertNotNull(registry.getGauges().get(MetricName.build("middleware.tomcat.thread.min_pool_size")));
        Assert.assertNotNull(registry.getGauges().get(MetricName.build("middleware.tomcat.thread.max_pool_size")));
        Assert.assertNotNull(registry.getGauges().get(MetricName.build("middleware.tomcat.thread.thread_pool_queue_size")));
    }

    private class TestServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            super.doGet(req, resp);
        }
    }
}
