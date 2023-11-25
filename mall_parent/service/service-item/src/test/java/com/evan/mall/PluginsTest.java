package com.evan.mall;

public class PluginsTest {
    //获取一个数组中第k大的数
    public static int getKth(int[] arr, int k) {
        // write code here
        int len = arr.length;
        int[] heap = new int[k];
        for (int i = 0; i < k; i++) {
            heap[i] = arr[i];
        }
        buildHeap(heap);
        for (int i = k; i < len; i++) {
            if (arr[i] > heap[0]) {
                heap[0] = arr[i];
                heapify(heap, 0);
            }
        }
        return heap[0];
    }

    private static void buildHeap(int[] heap) {
        int len = heap.length;
        for (int i = len / 2 - 1; i >= 0; i--) {
            heapify(heap, i);
        }
    }

    private static void heapify(int[] heap, int i) {
        int len = heap.length;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int max = i;
        if (left < len && heap[left] < heap[max]) {
            max = left;
        }
        if (right < len && heap[right] < heap[max]) {
            max = right;
        }
        if (max != i) {
            swap(heap, max, i);
            heapify(heap, max);
        }
    }

    private static void swap(int[] heap, int max, int i) {
        int temp = heap[max];
        heap[max] = heap[i];
        heap[i] = temp;
    }

    //文件复制


    public static void main(String[] args) {
        int[] arr = {1, 3, 5, 2, 2, 6, 11, 21};
        System.out.println(getKth(arr, 3));

    }
}
