package ru.ifmo.wst.lab1.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class TestService {
    @WebMethod
    public String hello() {
        return "Hello, kitty!";
    }
}
