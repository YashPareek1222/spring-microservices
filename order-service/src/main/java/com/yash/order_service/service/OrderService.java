package com.yash.order_service.service;

import com.yash.order_service.dto.InventoryResponse;
import com.yash.order_service.dto.OrderLineItemsDto;
import com.yash.order_service.dto.OrderRequest;
import com.yash.order_service.event.OrderPlacedEvent;
import com.yash.order_service.model.Order;
import com.yash.order_service.model.OrderLineItems;
import com.yash.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItemsDto> orderLineItemsDtos = orderRequest.getOrderLineItemsDtoList();

        List<OrderLineItems> orderLineItemsList = orderLineItemsDtos.stream().map(this::mapToOrder).toList();
        order.setOrderLineItemsList(orderLineItemsList);

        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();
        // Call inventory service and place order if it is in stock
        InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                        .uri("http://inventory-service/api/inventory",
                                uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                                .retrieve()
                                        .bodyToMono(InventoryResponse[].class)
                                                .block();

        boolean allProductsinStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);
        log.debug(String.valueOf(allProductsinStock));
        if(allProductsinStock) {
            log.debug(order.toString());
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
            return "Order placed successfully";
        }
        else {
            throw new IllegalArgumentException("Product is not in stock. Please try again later!");
        }
    }

    private OrderLineItems mapToOrder(OrderLineItemsDto orderLineItemsDto) {
        return OrderLineItems.builder()
                .skuCode(orderLineItemsDto.getSkuCode())
                .price(orderLineItemsDto.getPrice())
                .quantity(orderLineItemsDto.getQuantity())
                .build();
    }
}
