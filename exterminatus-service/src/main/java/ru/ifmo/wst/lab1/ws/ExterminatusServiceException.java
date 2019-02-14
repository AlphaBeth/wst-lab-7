package ru.ifmo.wst.lab1.ws;

import lombok.Getter;

import javax.xml.ws.WebFault;

@WebFault(faultBean = "ru.ifmo.wst.lab1.ws.ExterminatusServiceFault")
public class ExterminatusServiceException extends Exception {
    @Getter
    private final ExterminatusServiceFault faultInfo;

    public ExterminatusServiceException(String message, ExterminatusServiceFault faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    public ExterminatusServiceException(String message, Throwable cause, ExterminatusServiceFault faultInfo) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }
}
