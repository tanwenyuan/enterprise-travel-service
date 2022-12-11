package com.thoughtworks.travel.infrastructure.client;

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
    private T result;
    private String message;
    private String errorCode;

    public boolean isSuccess(){
        return code == 200 && "success".equals(message);
    }

}
