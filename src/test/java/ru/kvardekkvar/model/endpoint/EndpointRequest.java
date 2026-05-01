package ru.kvardekkvar.model.endpoint;

import lombok.*;

@Data
@AllArgsConstructor
public class EndpointRequest {
    private String token;
    private String action;
}
