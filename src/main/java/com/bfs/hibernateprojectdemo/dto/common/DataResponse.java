package com.bfs.hibernateprojectdemo.dto.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataResponse {
    private String message;
    private Object data;
}

