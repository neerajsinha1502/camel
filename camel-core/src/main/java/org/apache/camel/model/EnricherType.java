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
package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.camel.Processor;
import org.apache.camel.processor.Enricher;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.spi.RouteContext;

/**
 * @see Enricher
 */
@XmlRootElement(name = "enrich")
@XmlAccessorType(XmlAccessType.FIELD)
public class EnricherType extends OutputType<EnricherType> {

    @XmlAttribute(required = true)
    private String resourceUri;
    
    @XmlAttribute(required = false)
    private String aggregationStrategyRef;
    
    @XmlTransient
    private AggregationStrategy aggregationStrategy;
    
    public EnricherType() {
        this(null, null);
    }
    
    public EnricherType(String resourceUri) {
        this(null, resourceUri);
    }
    
    public EnricherType(AggregationStrategy aggregationStrategy, String resourceUri) {
        this.aggregationStrategy = aggregationStrategy;
        this.resourceUri = resourceUri;
    }
    
    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        Enricher enricher = new Enricher(null, resourceUri);
        if (aggregationStrategyRef != null) {
            aggregationStrategy = routeContext.lookup(aggregationStrategyRef, AggregationStrategy.class);
        }
        if (aggregationStrategy == null) {
            enricher.setDefaultAggregationStrategy();
        } else {
            enricher.setAggregationStrategy(aggregationStrategy);
        }
        return enricher;
    }
    
}
