package ru.ifmo.wst.lab1;

import lombok.Data;

@Data
public class Box<T> {
    private T value;
}
