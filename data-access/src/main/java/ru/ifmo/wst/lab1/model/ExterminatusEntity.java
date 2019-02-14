package ru.ifmo.wst.lab1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExterminatusEntity {
    private Long id;
    private String initiator;
    private String reason;
    private String method;
    private String planet;
    private Date date;
}
