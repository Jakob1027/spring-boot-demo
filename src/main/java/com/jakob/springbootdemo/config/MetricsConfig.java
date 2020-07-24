package com.jakob.springbootdemo.config;


import com.cgs.rtd.rtdbus.RtdBus;
import com.cgs.rtd.rtdbus.TagPathSelector;
import com.cgs.rtd.rtdbus.config.ClientConfig;
import com.cgs.rtd.rtdbus.config.RtdBusConfig;
import com.cgs.rtd.rtdbus.impl.RtdBusFactory;
import com.codahale.metrics.*;
import com.codahale.metrics.jmx.JmxReporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class MetricsConfig {

    @Bean
    public MetricRegistry getMetrics() {
        return new MetricRegistry();
    }

    @Bean
    public Meter requests() {
        return getMetrics().meter("requests");
    }

    @Bean
    public Histogram responseSize() {
        return getMetrics().histogram("responseSize");
    }

    @Bean
    public Timer timer() {
        return getMetrics().timer("timer");
    }

    @PostConstruct
    public void report() {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(getMetrics())
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(10, TimeUnit.SECONDS);
        JmxReporter jmxReporter = JmxReporter.forRegistry(getMetrics()).build();
        jmxReporter.start();
    }

    //    @Bean
    public RtdBus rtdBus() {
        RtdBusFactory factory = new RtdBusFactory();

        ClientConfig clientConfig1 = new ClientConfig();
        clientConfig1.setId("rtdbus_test");
        clientConfig1.setPosts(Collections.singletonList("**"));

        ClientConfig clientConfig2 = new ClientConfig();
        clientConfig2.setId("test-rtdbus-client");
        clientConfig2.setPosts(Collections.singletonList("**"));

        RtdBusConfig config = new RtdBusConfig();
        config.setId("test-rtdbus-router");
        config.setOpenServerPort(true);
        config.setPort(10027);
        config.setPersistentPath("");
        config.setClients(Arrays.asList(clientConfig1, clientConfig2));

        RtdBus rtdBus = factory.buildRtdBus(config);
        rtdBus.regSyncListener((tag, event) -> System.out.println(event), new TagPathSelector("test"), Object.class);
        rtdBus.start();
        return rtdBus;
    }

    public static void main(String[] args) {
        List<Integer> l1 = new ArrayList<>();
        l1.add(1);
        List<Integer> l2 = new ArrayList<>();
        l2.add(1);
        System.out.println(l1.equals(l2));

    }


}
