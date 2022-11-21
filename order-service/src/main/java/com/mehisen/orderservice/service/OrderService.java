package com.mehisen.orderservice.service;

import com.mehisen.orderservice.dto.InventoryResponse;
import com.mehisen.orderservice.dto.OrderLineItemsDto;
import com.mehisen.orderservice.dto.OrderRequest;
import com.mehisen.orderservice.model.Order;
import com.mehisen.orderservice.model.OrderLineItems;
import com.mehisen.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDto().stream().map(this::mapFromDto).toList();
        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();
        // Call Inventory Service, and place order if product is in
        // stack

        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get().uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build()).retrieve().bodyToMono(InventoryResponse[].class).block();

        boolean allProductInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInSock);

        if (allProductInStock) {
            orderRepository.save(order);
            return "Order Placed Successfully";
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again");
        }
    }

    private OrderLineItems mapFromDto(OrderLineItemsDto orderLineItemsDto) {

        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        return orderLineItems;
    }
}
