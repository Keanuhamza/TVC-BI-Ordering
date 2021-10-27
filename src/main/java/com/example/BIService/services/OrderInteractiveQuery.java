package com.example.BIService.services;

import com.example.BIService.*;
import com.example.BIService.models.cOrder;

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

    public float getCustomerCostList(Long a) {
        float totalCost = 0f;
        KeyValueIterator<Long, cOrder> all = orderCustomerStore().range(a, a);
        while (all.hasNext()) {
            cOrder next = all.next().value;

            totalCost += next.getProdPrice();
        }
        return totalCost;
    } 

    public List<String> getCustomerOrdersList(Long a) {
        List<String> totalOrders = new ArrayList<>();
        KeyValueIterator<Long, cOrder> all = orderCustomerStore().range(a, a);
        while (all.hasNext()) {
            cOrder next = all.next().value;

            totalOrders.add(next.toString());
        }
        return totalOrders;
    } 

    


    private ReadOnlyKeyValueStore<String, cOrder> orderStore() {
        return this.interactiveQueryService.getQueryableStore(OrderStreamProcessing.ORDER_STATE_STORE,
                QueryableStoreTypes.keyValueStore());
    }

    private ReadOnlyKeyValueStore<Long, cOrder> orderCustomerStore() {
        return this.interactiveQueryService.getQueryableStore(OrderStreamProcessing.ORDER_CUSTOMER_STATE_STORE,
                QueryableStoreTypes.keyValueStore());
    }

   



}
