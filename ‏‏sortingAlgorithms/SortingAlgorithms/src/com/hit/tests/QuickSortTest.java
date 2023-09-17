package com.hit.tests;

import com.hit.algorithms.QuickSort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuickSortTest {
    @Test
    public void testQuickSort() {
        List<Integer> numbers = new ArrayList<>(Arrays.asList(5, 3, 8, 2, 1, 4));
        QuickSort<Integer> quickSort = new QuickSort<>();

        quickSort.sort(numbers);

        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5, 8);
        Assertions.assertEquals(expected, numbers);
    }
}

