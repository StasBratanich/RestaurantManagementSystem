package com.hit.algorithms;

import java.util.List;
import java.util.ArrayList;

public class MergeSort<T extends Comparable<T>> implements IAlgoSort<T> {
    public void sort(List<T> items) {
        mergeSort(items, 0, items.size() - 1);
    }

    private void mergeSort(List<T> items, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;
            mergeSort(items, left, middle);
            mergeSort(items, middle + 1, right);
            merge(items, left, middle, right);
        }
    }

    private void merge(List<T> items, int left, int middle, int right) {
        int n1 = middle - left + 1;
        int n2 = right - middle;

        List<T> leftArray = new ArrayList<>();
        List<T> rightArray = new ArrayList<>();

        for (int i = 0; i < n1; i++) {
            leftArray.add(items.get(left + i));
        }
        for (int j = 0; j < n2; j++) {
            rightArray.add(items.get(middle + 1 + j));
        }

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            if (leftArray.get(i).compareTo(rightArray.get(j)) <= 0) {
                items.set(k, leftArray.get(i));
                i++;
            } else {
                items.set(k, rightArray.get(j));
                j++;
            }
            k++;
        }

        while (i < n1) {
            items.set(k, leftArray.get(i));
            i++;
            k++;
        }

        while (j < n2) {
            items.set(k, rightArray.get(j));
            j++;
            k++;
        }
    }
}

