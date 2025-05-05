package com.esde.specifications;

public interface Specification<T>{
    boolean isSatisfiedBy(T oval);
}
