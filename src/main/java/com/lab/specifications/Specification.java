package com.lab.specifications;

public interface Specification<T>{
    boolean isSatisfiedBy(T oval);
}
