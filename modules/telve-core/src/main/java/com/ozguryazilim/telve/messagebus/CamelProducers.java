package com.ozguryazilim.telve.messagebus;

import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

@ApplicationScoped
public class CamelProducers {


    @Inject //@ContextName("telve")
    //private CdiCamelContext camelContext;
    private TelveCamelContext camelContext;

    @Produces
    public ProducerTemplate getProducerTemplate(){
        return camelContext.createProducerTemplate();
    }

    @Produces
    public ConsumerTemplate getConsumerTemplate(){
        return camelContext.createConsumerTemplate();
    }
}
