package com.example.BIService.services;

import com.example.BIService.*;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderInteractiveQuery {

    private final InteractiveQueryService interactiveQueryService;

    //@Autowired
    public OrderInteractiveQuery(InteractiveQueryService interactiveQueryService) {
        this.interactiveQueryService = interactiveQueryService;
    }

    public long getProductOrderQuantity(String productName) {
        if (orderStore().get(productName) != null) {
            return orderStore().get(productName);
        } else {
            throw new NoSuchElementException(); //TODO: should use a customised exception.
        }
    }

  /*  public List<String> getBrandList() {
        List<String> brandList = new ArrayList<>();
        KeyValueIterator<String, Long> all = brandStore().all();
        while (all.hasNext()) {
            String next = all.next().key;
            brandList.add(next);
        }
        return brandList;
    } */


    private ReadOnlyKeyValueStore<String, Long> orderStore() {
        return this.interactiveQueryService.getQueryableStore(OrderStreamProcessing.ORDER_STATE_STORE,
                QueryableStoreTypes.keyValueStore());
    }



}
