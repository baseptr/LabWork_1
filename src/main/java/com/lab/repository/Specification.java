package com.lab.repository;

public interface Specification<T>{
    boolean isSatisfiedBy(T oval);
}
