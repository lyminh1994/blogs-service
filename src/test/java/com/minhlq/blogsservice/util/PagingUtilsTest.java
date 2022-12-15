package com.minhlq.blogsservice.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class PagingUtilsTest {

    @Test
    void shouldToPageRequestSuccess() {
        List<Order> orders = Collections.singletonList(new Order(Direction.DESC, "name"));
        Sort sort = Sort.by(orders);
        PageRequest expected = PageRequest.of(1, 10, sort);

        PageRequest actual = PagingUtils.toPageRequest(1, 10, new String[]{"name", "desc"});

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldGetUnsortedWhenSortEmpty() {
        Sort expected = Sort.unsorted();
        Sort actual = PagingUtils.getSort(null);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldGetSortWhenMoreThanTwoFields() {
        List<Order> orders =
                Arrays.asList(new Order(Direction.ASC, "name"), new Order(Direction.DESC, "id"));
        Sort expected = Sort.by(orders);

        Sort actual = PagingUtils.getSort(new String[]{"name,asc", "id,desc"});
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldGetSortWithOneField() {
        List<Order> orders = Collections.singletonList(new Order(Direction.DESC, "id"));
        Sort expected = Sort.by(orders);

        Sort actual = PagingUtils.getSort(new String[]{"id", "desc"});
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldGetDirectionAsc() {
        Assertions.assertEquals(Direction.ASC, PagingUtils.getSortDirection("asc"));
    }

    @Test
    void shouldGetDirectionDesc() {
        Assertions.assertEquals(Direction.DESC, PagingUtils.getSortDirection("desc"));
    }

    @Test
    void shouldGetDirectionDefault() {
        Assertions.assertEquals(Direction.ASC, PagingUtils.getSortDirection("other"));
    }
}
