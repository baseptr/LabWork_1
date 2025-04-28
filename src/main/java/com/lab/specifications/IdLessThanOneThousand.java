package com.lab.specifications;

import com.lab.entity.Oval;

public class IdLessThanOneThousand implements Specification<Oval> {
    @Override
    public boolean isSatisfiedBy(Oval oval) {
        return oval.getId() < 1000;
    }
}