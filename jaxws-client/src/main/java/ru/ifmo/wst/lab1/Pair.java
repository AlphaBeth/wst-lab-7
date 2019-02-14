package ru.ifmo.wst.lab1;

import lombok.Value;

@Value
public class Pair<L, R> {
    private final L left;
    private final R right;

}
