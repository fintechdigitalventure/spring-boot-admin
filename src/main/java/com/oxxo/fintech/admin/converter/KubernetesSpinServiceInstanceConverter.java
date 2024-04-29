package com.oxxo.fintech.admin.converter;

import de.codecentric.boot.admin.server.cloud.discovery.DefaultServiceInstanceConverter;
import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.client.ServiceInstance;

public class KubernetesSpinServiceInstanceConverter extends DefaultServiceInstanceConverter {

    private static final String MARKETING_PREFIX = "marketing";

    private static final String MOBILE_PREFIX = "mobile";

    @NotNull
    @Override
    protected String getManagementPath(@NotNull ServiceInstance instance) {
        return contextPath(instance) + super.getManagementPath(instance);
    }

    private String contextPath(ServiceInstance instance) {
        String appName = instance.getMetadata().get("app");

        if ((appName.startsWith(MOBILE_PREFIX) || appName.startsWith(MARKETING_PREFIX)) && appName.contains("-")) {
            appName = appName.replaceFirst("-", "/");
        }

        return appName;
    }

}