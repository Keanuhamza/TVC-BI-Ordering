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

    // get customer cost method
    public float getCustomerCost(Long a) {
        float totalCost = 0f; // initialize cost
        KeyValueIterator<Long, cOrder> all = orderCustomerStore().all();
        while (all.hasNext()) {
            cOrder next = all.next().value; 
            if (next.getCustID() == a) { // if the order is from the customer
                totalCost += next.getProdPrice() * (float)next.getQuantity(); // add cost
            }
                
        }
        return totalCost; // return total cost
    } 

    // method to get total customer orders
    public List<String> getCustomerOrdersList(Long a) {
        List<String> totalOrders = new ArrayList<>();
        KeyValueIterator<Long, cOrder> all = orderCustomerStore().all();
        while (all.hasNext()) {
            System.out.println("");
            cOrder next = all.next().value;
            if (next.getCustID() == a) {
                totalOrders.add(next.toString());
            }
                
        }
        return totalOrders;
    } 

    private ReadOnlyKeyValueStore<Long, cOrder> orderCustomerStore() {
        return this.interactiveQueryService.getQueryableStore(OrderStreamProcessing.ORDER_CUSTOMER_STATE_STORE,
                QueryableStoreTypes.keyValueStore());
    }

   



}
