package com.lab.repository;

import com.lab.entity.Oval;
import com.lab.pool.ShapeWarehouse;
import com.lab.service.OvalService;
import com.lab.specifications.Specification;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class ShapeRepository {
    private final List<Oval> ovals = new ArrayList<>();
    private final OvalService service = new OvalService();

    public void add(Oval oval) {
        if (oval != null && !ovals.contains(oval)) {
            ovals.add(oval);
            oval.addObserver(ShapeWarehouse.getInstance());
            ShapeWarehouse.getInstance().update(oval, null);
        }
    }

    public List<Oval> getAll() {
        return new ArrayList<>(ovals);
    }

    public void delete(Oval oval) {
        if (oval != null) {
            boolean removed = ovals.remove(oval);
            if (removed) {
                oval.deleteObserver(ShapeWarehouse.getInstance());
            }
        }
    }

    public List<Oval> sortById() {
        List<Oval> sorted = new ArrayList<>(ovals);
        sorted.sort(Comparator.comparingLong(Oval::getId));
        return sorted;
    }

    public List<Oval> sortByArea() {
        List<Oval> sorted = new ArrayList<>(ovals);
        sorted.sort(Comparator.comparingDouble(service::calculateArea));
        return sorted;
    }

    public List<Oval> sortByPerimeter() {
        List<Oval> sorted = new ArrayList<>(ovals);
        sorted.sort(Comparator.comparingDouble(service::calculatePerimeter));
        return sorted;
    }

    public List<Oval> query(Specification<Oval> specification) {
        List<Oval> result = new ArrayList<>();
        for (Oval oval : ovals) {
            if (specification.isSatisfiedBy(oval)) {
                result.add(oval);
            }
        }
        return result;
    }

}
