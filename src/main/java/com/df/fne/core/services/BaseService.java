package com.df.fne.core.services;

import java.util.List;
import java.util.UUID;

public interface BaseService<T>{
    T create(T t);
    T update(T t, UUID id);
    T get(UUID id);
    List<T> getAll();
    void delete(UUID id);
}
