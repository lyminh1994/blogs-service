package com.minhlq.blogsservice.utils;

import com.querydsl.core.util.ArrayUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

@UtilityClass
public class PagingUtils {

  public PageRequest toPageRequest(int pageNumber, int pageSize) {
    return PageRequest.of(pageNumber, pageSize);
  }

  public PageRequest toPageRequest(int pageNumber, int pageSize, String[] sort) {
    return PageRequest.of(pageNumber, pageSize, getSort(sort));
  }

  public Sort getSort(String[] sort) {
    if (ArrayUtils.isEmpty(sort)) {
      return Sort.unsorted();
    }

    List<Order> orders = new ArrayList<>();

    if (sort[0].contains(",")) {
      // will sort more than 2 fields
      // sortOrder="field, direction"
      for (String sortOrder : sort) {
        String[] sortStrings = sortOrder.split(",");
        orders.add(new Order(getSortDirection(sortStrings[1]), sortStrings[0]));
      }
    } else {
      // sort=[field, direction]
      orders.add(new Order(getSortDirection(sort[1]), sort[0]));
    }

    return Sort.by(orders);
  }

  public Sort.Direction getSortDirection(String direction) {
    if (direction.equals("asc")) {
      return Sort.Direction.ASC;
    } else if (direction.equals("desc")) {
      return Sort.Direction.DESC;
    }

    return Sort.Direction.ASC;
  }
}
