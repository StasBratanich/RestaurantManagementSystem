package com.hit.tests;

import com.hit.algorithms.BubbleSort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BubbleSortTest {
    @Test
    public void testBubbleSort() {
        List<Integer> numbers = new ArrayList<>(Arrays.asList(5, 3, 8, 2, 1, 4));
        BubbleSort<Integer> bubbleSort = new BubbleSort<>();

        bubbleSort.sort(numbers);

        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5, 8);
        Assertions.assertEquals(expected, numbers);
    }
}



