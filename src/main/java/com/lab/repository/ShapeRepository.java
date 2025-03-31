package com.lab.repository;

import com.lab.entity.Oval;
import java.util.List;
import java.util.ArrayList;

public class ShapeRepository {
    private final List<Oval> ovals = new ArrayList<>();

    public void add(Oval oval) {
        ovals.add(oval);
    }

    public List<Oval> getAll() {
        return new ArrayList<>(ovals);
    }

    // Методы для спецификаций (позднее)
}
