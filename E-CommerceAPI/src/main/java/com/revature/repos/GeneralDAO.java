package com.revature.repos;

import java.util.List;

/*
 THIS IS Generic interface (using abstraction), with a CRUD methods, witch I'm  going to be able to use
 in all of my interfaces asn then by the implementing that interfaces
 Here we are going to use overriding (polymorphism) and
 */

public interface GeneralDAO <T>{
    // All the methods here have by default public and abstract as there access and nor access modifiers
    T create(T obj);
    List<T> getAll();
    T getById(int id);
    T update(T obj);
    boolean deleteById(int id);
}
