package in.mcxiv.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class LinkedListTest {

    @Test
    void test() {

        ArrayList<Integer> numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6));

        LinkedList<Integer, Integer> doubles = new LinkedList<>(numbers, i -> 2 * i);

        Assertions.assertEquals(6, doubles.size());

        numbers.add(10);

        Assertions.assertTrue(doubles.contains(20));

    }
}