package com.hit.dao;

import java.util.List;

public interface IDAO<T> {
    void appendData(T object);
    List<T> readData();
    void deleteData(T object);
}
