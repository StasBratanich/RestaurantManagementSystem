package com.hit.algorithms;

import java.util.List;

public interface IAlgoSort<T extends Comparable<T>> {
    void sort(List<T> items);
}
