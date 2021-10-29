package com.example.BIService.controllers;

import com.example.BIService.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.example.BIService.models.*;
import com.example.BIService.services.OrderInteractiveQuery;

@RestController
public class OrderQueryController {
  @Autowired
  private OrderInteractiveQuery orderInteractiveQuery;


    // get total cost for all orders from a customer
  @GetMapping("/totalCostForCust/{id}")
  float getTotalCost(@PathVariable Long id) {
    return orderInteractiveQuery.getCustomerCost(id);
  }

  // get all the orders from a customer
  @GetMapping("/getOrdersForCust/{id}")
  List<String> oneCustomer(@PathVariable Long id) {
    return orderInteractiveQuery.getCustomerOrdersList(id);
  }

}
