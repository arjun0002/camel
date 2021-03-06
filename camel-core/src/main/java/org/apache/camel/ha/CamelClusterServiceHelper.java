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
package org.apache.camel.ha;

import java.util.Optional;
import java.util.function.Predicate;

import org.apache.camel.CamelContext;

public final class CamelClusterServiceHelper {
    private CamelClusterServiceHelper() {
    }

    public static Optional<CamelClusterService> lookupService(CamelContext context) {
        return Optional.ofNullable(context.hasService(CamelClusterService.class));
    }

    public static Optional<CamelClusterService> lookupService(CamelContext context, Predicate<CamelClusterService> selector) {
        for (CamelClusterService service: context.hasServices(CamelClusterService.class)) {
            if (selector.test(service)) {
                return Optional.of(service);
            }
        }

        return Optional.empty();
    }
}
