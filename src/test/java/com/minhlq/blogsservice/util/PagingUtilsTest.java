package com.minhlq.blogsservice.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

@SpringBootTest
class PagingUtilsTest {

  @Test
  @DisplayName("Should to page request success")
  void shouldToPageRequestSuccess() {
    // given - setup or precondition
    List<Order> orders = Collections.singletonList(new Order(Direction.DESC, "name"));
    Sort sort = Sort.by(orders);
    PageRequest expected = PageRequest.of(1, 10, sort);

    // when - action or the testing
    PageRequest actual = PagingUtils.toPageRequest(1, 10, new String[] {"name", "desc"});

    // then - very output
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Should get unsorted when sort empty")
  void shouldGetUnsortedWhenSortEmpty() {
    Sort expected = Sort.unsorted();
    Sort actual = PagingUtils.getSort(null);
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Should get sort when more than two fields")
  void shouldGetSortWhenMoreThanTwoFields() {
    List<Order> orders =
        Arrays.asList(new Order(Direction.ASC, "name"), new Order(Direction.DESC, "id"));
    Sort expected = Sort.by(orders);

    Sort actual = PagingUtils.getSort(new String[] {"name,asc", "id,desc"});
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Should get sort with one field")
  void shouldGetSortWithOneField() {
    List<Order> orders = Collections.singletonList(new Order(Direction.DESC, "id"));
    Sort expected = Sort.by(orders);

    Sort actual = PagingUtils.getSort(new String[] {"id", "desc"});
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Should get direction ASC")
  void shouldGetDirectionAsc() {
    assertEquals(Direction.ASC, PagingUtils.getSortDirection("asc"));
  }

  @Test
  @DisplayName("Should get direction DESC")
  void shouldGetDirectionDesc() {
    assertEquals(Direction.DESC, PagingUtils.getSortDirection("desc"));
  }

  @Test
  @DisplayName("Should get direction Default")
  void shouldGetDirectionDefault() {
    assertEquals(Direction.ASC, PagingUtils.getSortDirection("other"));
  }
}
