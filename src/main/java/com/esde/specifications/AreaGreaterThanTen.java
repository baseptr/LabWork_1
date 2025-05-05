package com.esde.specifications;

import com.esde.entity.Oval;
import com.esde.service.OvalService;

public class AreaGreaterThanTen implements Specification<Oval> {
    private final OvalService service = new OvalService();

    @Override
    public boolean isSatisfiedBy(Oval oval) {
        return service.calculateArea(oval) > 10;
    }
}
