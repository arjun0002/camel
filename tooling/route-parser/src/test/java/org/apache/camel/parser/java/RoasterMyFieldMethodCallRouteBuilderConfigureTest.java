/**
 *  Copyright 2005-2015 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package org.apache.camel.parser.java;

import java.io.File;
import java.util.List;

import org.apache.camel.parser.CamelJavaParserHelper;
import org.apache.camel.parser.ParserResult;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoasterMyFieldMethodCallRouteBuilderConfigureTest {

    private static final Logger LOG = LoggerFactory.getLogger(RoasterMyFieldMethodCallRouteBuilderConfigureTest.class);

    @Test
    public void parse() throws Exception {
        JavaClassSource clazz = (JavaClassSource) Roaster.parse(new File("src/test/java/org/apache/camel/parser/java/MyFieldMethodCallRouteBuilder.java"));
        MethodSource<JavaClassSource> method = clazz.getMethod("configure");

        List<ParserResult> list = CamelJavaParserHelper.parseCamelConsumerUris(method, true, true);
        for (ParserResult result : list) {
            LOG.info("Consumer: " + result.getElement());
        }
        Assert.assertEquals("netty-http:http://0.0.0.0:{{port}}/foo", list.get(0).getElement());
        Assert.assertEquals("netty-http:http://0.0.0.0:{{getNextPort}}/bar", list.get(1).getElement());

        list = CamelJavaParserHelper.parseCamelProducerUris(method, true, true);
        for (ParserResult result : list) {
            LOG.info("Producer: " + result.getElement());
        }
        Assert.assertEquals("mock:input1", list.get(0).getElement());
        Assert.assertEquals("netty-http:http://0.0.0.0:{{getNextPort}}/bar", list.get(1).getElement());
        Assert.assertEquals("mock:input2", list.get(2).getElement());
    }

}