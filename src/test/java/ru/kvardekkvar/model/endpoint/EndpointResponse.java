package ru.kvardekkvar.model.endpoint;

import lombok.*;

@Data
@AllArgsConstructor
public class EndpointResponse {
    private String result;
    private String message;
}
