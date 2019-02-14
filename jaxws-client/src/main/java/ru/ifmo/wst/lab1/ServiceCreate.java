package ru.ifmo.wst.lab1;

import lombok.Data;

@Data
public class ServiceCreate {
    private String businessKey;
    private String serviceName;
    private String wsdlUrl;
}
