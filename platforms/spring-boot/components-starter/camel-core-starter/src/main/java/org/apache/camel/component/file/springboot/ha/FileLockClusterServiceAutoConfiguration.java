/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.file.springboot.ha;


import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.camel.component.file.ha.FileLockClusterService;
import org.apache.camel.converter.TimePatternConverter;
import org.apache.camel.ha.CamelClusterService;
import org.apache.camel.spring.boot.CamelAutoConfiguration;
import org.apache.camel.spring.boot.ha.ClusteredRouteControllerAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AutoConfigureBefore({ ClusteredRouteControllerAutoConfiguration.class, CamelAutoConfiguration.class })
@ConditionalOnProperty(prefix = "camel.component.file.cluster.service", name = "enabled")
@EnableConfigurationProperties(FileLockClusterServiceConfiguration.class)
public class FileLockClusterServiceAutoConfiguration {
    @Autowired
    private FileLockClusterServiceConfiguration configuration;

    @Bean(initMethod = "start", destroyMethod = "stop")
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @ConditionalOnMissingBean
    public CamelClusterService consulClusterService() throws Exception {
        FileLockClusterService service = new FileLockClusterService();

        Optional.ofNullable(configuration.getId())
            .ifPresent(service::setId);
        Optional.ofNullable(configuration.getRoot())
            .ifPresent(service::setRoot);
        Optional.ofNullable(configuration.getAcquireLockDelay())
            .map(TimePatternConverter::toMilliSeconds)
            .ifPresent(v -> service.setAcquireLockDelay(v, TimeUnit.MILLISECONDS));
        Optional.ofNullable(configuration.getAcquireLockInterval())
            .map(TimePatternConverter::toMilliSeconds)
            .ifPresent(v -> service.setAcquireLockInterval(v, TimeUnit.MILLISECONDS));

        return service;
    }
}
