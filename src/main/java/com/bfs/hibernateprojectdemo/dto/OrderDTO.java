package com.bfs.hibernateprojectdemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDTO {

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("date_placed")
    private LocalDateTime datePlaced;

    @JsonProperty("order_status")
    private String orderStatus;

    @JsonProperty("user_id")
    private String userId;
}
