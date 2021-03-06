/*
 * Copyright 2020 Ingyu Hwang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.resilience4j.timelimiter.autoconfigure;

import com.codahale.metrics.MetricRegistry;
import io.github.resilience4j.metrics.TimeLimiterMetrics;
import io.github.resilience4j.metrics.publisher.TimeLimiterMetricsPublisher;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.springframework.boot.actuate.autoconfigure.MetricRepositoryAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricsDropwizardAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({MetricRegistry.class, TimeLimiter.class, TimeLimiterMetricsPublisher.class})
@AutoConfigureAfter(MetricsDropwizardAutoConfiguration.class)
@AutoConfigureBefore(MetricRepositoryAutoConfiguration.class)
@ConditionalOnProperty(value = "resilience4j.timelimiter.metrics.enabled", matchIfMissing = true)
public class TimeLimiterMetricsAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = "resilience4j.timelimiter.metrics.legacy.enabled", havingValue = "true")
    @ConditionalOnMissingBean
    public TimeLimiterMetrics registerTimeLimiterMetrics(TimeLimiterRegistry timeLimiterRegistry,
        MetricRegistry metricRegistry) {
        return TimeLimiterMetrics.ofTimeLimiterRegistry(timeLimiterRegistry, metricRegistry);
    }

    @Bean
    @ConditionalOnProperty(value = "resilience4j.timelimiter.metrics.legacy.enabled", havingValue = "false", matchIfMissing = true)
    @ConditionalOnMissingBean
    public TimeLimiterMetricsPublisher timeLimiterMetricsPublisher(MetricRegistry metricRegistry) {
        return new TimeLimiterMetricsPublisher(metricRegistry);
    }

}
