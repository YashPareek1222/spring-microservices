package com.yash.order_service.dto;

import com.yash.order_service.model.OrderLineItems;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private List<OrderLineItemsDto> orderLineItemsDtoList;
}
