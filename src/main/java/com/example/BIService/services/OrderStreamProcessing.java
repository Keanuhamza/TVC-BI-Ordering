package com.example.BIService.services;
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
    public final static String BRAND_STATE_STORE = "brand-store";
    public final static String EQUIPMENT_STATE_STORE = "equipment-store";

    @Bean
    public Function<KStream<?, Appliance>, KStream<String, OrderQuantity>> process() {
        return inputStream -> {

            inputStream.map((k, v) -> {
                String equipment_name = v.getEquipment();
                String brand_name = v.getBrand();
                Equipment equipment = new Equipment();
                equipment.setEquipment(equipment_name);
                equipment.setBrand(brand_name);
                String new_key = brand_name + equipment_name;
                return KeyValue.pair(new_key, equipment);
            }).toTable(
                    Materialized.<String, Equipment, KeyValueStore<Bytes, byte[]>>as(EQUIPMENT_STATE_STORE).
                            withKeySerde(Serdes.String()).
                            // a custom value serde for this state store
                            withValueSerde(equipmentSerde())
            );

            KTable<String, Long> brandKTable = inputStream.
                    mapValues(Appliance::getBrand).
                    groupBy((keyIgnored, value) -> value).
                    count(
                            Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as(BRAND_STATE_STORE).
                                    withKeySerde(Serdes.String()).
                                    withValueSerde(Serdes.Long())
                    );

            KStream<String, BrandQuantity> brandQuantityStream = brandKTable.
                    toStream().
                    map((k, v) -> KeyValue.pair(k, new BrandQuantity(k, v)));
            // use the following code for testing
            brandQuantityStream.print(Printed.<String, BrandQuantity>toSysOut().withLabel("Console Output"));

            return brandQuantityStream;
        };
    }


    // Can compare the following configuration properties with those defined in application.yml
    public Serde<Equipment> equipmentSerde() {
        final JsonSerde<Equipment> equipmentJsonSerde = new JsonSerde<>();
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "csci318.demo.model.Equipment");
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        equipmentJsonSerde.configure(configProps, false);
        return equipmentJsonSerde;
    }
}
