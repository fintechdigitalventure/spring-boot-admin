package com.oxxo.fintech.admin.config;

import de.codecentric.boot.admin.server.domain.values.InstanceId;
import de.codecentric.boot.admin.server.domain.values.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import de.codecentric.boot.admin.server.config.AdminServerProperties;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.services.InstanceRegistry;

@Configuration
public class AdminServerConfiguration {

    private final static Logger log = LoggerFactory.getLogger(AdminServerConfiguration.class);
    private final DiscoveryClient discoveryClient;
    private final InstanceRegistry registry;

    public AdminServerConfiguration(DiscoveryClient discoveryClient, InstanceRegistry registry) {
        this.discoveryClient = discoveryClient;
        this.registry = registry;
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> onApplicationReadyEventListener(AdminServerProperties adminServerProperties) {
        return event -> discoveryClient.getServices().forEach(serviceId -> {
            discoveryClient.getInstances(serviceId).forEach(serviceInstance -> {
                String managementUrl = serviceInstance.getUri().toString() + "/actuator";
                log.info("managementUrl: {}", managementUrl);
                Instance instance = Instance.create(InstanceId.of(serviceInstance.getServiceId() + "-" + serviceInstance.getInstanceId()))
                        .register(Registration.builder()
                                .name(serviceInstance.getServiceId())
                                .serviceUrl(serviceInstance.getUri().toString())
                                .managementUrl(managementUrl)
                                .healthUrl(managementUrl + "/health").build());
                registry.register(instance.getRegistration());
            });
        });
    }
}

