package com.thoughtworks.enterprise.travel.infrastructure.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientBaseResponse<T> {
    private int code;
    private T data;
    private String message;
    private String errorCode;

    public boolean isSuccess(){
        return code == 200;
    }

}
