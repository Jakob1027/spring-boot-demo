package com.jakob.springbootdemo.config;


import com.codahale.metrics.*;
import com.codahale.metrics.jmx.JmxReporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
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
}
