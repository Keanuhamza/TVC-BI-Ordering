package com.example.BIService.services;
import org.apache.catalina.connector.CoyoteReader;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.state.KeyValueStore;
import org.hibernate.criterion.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.example.BIService.models.*;


@Configuration
public class OrderStreamProcessing {
    public final static String ORDER_STATE_STORE = "order-store";
    public final static String PRODUCT_TOTAL_STATE_STORE = "product-total-store";

    @Bean
    public Function<KStream<?, cOrder>, KStream<String, cProductTotal>> process() {
        return inputStream -> {
            

            inputStream.map((k, v) -> {

                String new_key = v.getProductName();
                System.out.println(new_key + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                return KeyValue.pair(new_key, v);
            }).toTable(
                    Materialized.<String, cOrder, KeyValueStore<Bytes, byte[]>>as(ORDER_STATE_STORE).
                            withKeySerde(Serdes.String())
                            // a custom value serde for this state store
                           // withValueSerde(orderSerde())
            );
            
            KTable<String, Long> orderKTable = inputStream.
                    mapValues(cOrder::getProductName).
                    groupBy((keyIgnored, value) -> value).
                    count(
                            Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as(PRODUCT_TOTAL_STATE_STORE).
                                    withKeySerde(Serdes.String()).
                                    withValueSerde(Serdes.Long())
                    );

            KStream<String, cProductTotal> productQuantityStream = orderKTable.
                    toStream().
                    map((k, v) -> KeyValue.pair(k, new cProductTotal(k, v)));
            // use the following code for testing
            productQuantityStream.print(Printed.<String, cProductTotal>toSysOut().withLabel("Console Output"));
            
            return productQuantityStream;
        };
    }


    // Can compare the following configuration properties 
    // with those defined in application.yml
    public Serde<cOrder> orderSerde() {
        final JsonSerde<cOrder> orderJsonSerde = new JsonSerde<>();
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.example.BIService.cOrder");
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        orderJsonSerde.configure(configProps, false);
        return orderJsonSerde;
    }
}
