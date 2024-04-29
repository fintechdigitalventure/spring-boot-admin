package com.oxxo.fintech.admin.config;

import com.oxxo.fintech.admin.converter.KubernetesSpinServiceInstanceConverter;
import de.codecentric.boot.admin.server.cloud.discovery.ServiceInstanceConverter;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMissingBean({ServiceInstanceConverter.class})
@ConditionalOnBean(KubernetesClient.class)
public class KubernetesSpinConverterConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.boot.admin.discovery.converter")
    public KubernetesSpinServiceInstanceConverter serviceInstanceConverter() {
        return new KubernetesSpinServiceInstanceConverter();
    }

}
