package com.lab.specifications;

import com.lab.entity.Oval;
import com.lab.service.OvalService;

class IsCircle implements Specification<Oval> {
    private final OvalService service = new OvalService();

    @Override
    public boolean isSatisfiedBy(Oval oval) {
        return service.isCircle(oval);
    }
}

