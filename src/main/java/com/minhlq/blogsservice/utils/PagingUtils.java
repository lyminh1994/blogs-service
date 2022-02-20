package com.minhlq.blogsservice.utils;

import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

@UtilityClass
public class PagingUtils {

  /**
   * Get {@link PageRequest}
   *
   * @param pageNumber number of page
   * @param pageSize total element of page
   * @param sort array string for sorted
   * @return {@link PageRequest}
   */
  public PageRequest toPageRequest(int pageNumber, int pageSize, String[] sort) {
    return PageRequest.of(pageNumber, pageSize, getSort(sort));
  }

  /**
   * Get {@link Sort} from array string
   *
   * @param sort array string with format ["field1,direction1", "field2,direction2"] or ["field",
   *     "direction"]
   * @return {@link Sort}
   */
  public Sort getSort(String[] sort) {
    if (ArrayUtils.isEmpty(sort)) {
      return Sort.unsorted();
    }

    List<Order> orders = new ArrayList<>();

    if (sort[0].contains(",")) {
      // will sort more than 2 fields
      // sort=["field1,direction1", "field2,direction2"]
      for (String sortOrder : sort) {
        String[] sortStrings = sortOrder.split(",");
        orders.add(new Order(getSortDirection(sortStrings[1]), sortStrings[0]));
      }
    } else {
      // sort=["field", "direction"]
      orders.add(new Order(getSortDirection(sort[1]), sort[0]));
    }

    return Sort.by(orders);
  }

  /**
   * Get {@link Sort.Direction} from direction string
   *
   * @param direction asc, desc or another string
   * @return {@link Sort.Direction}
   */
  public Sort.Direction getSortDirection(String direction) {
    if (direction.equals("asc")) {
      return Sort.Direction.ASC;
    } else if (direction.equals("desc")) {
      return Sort.Direction.DESC;
    }

    return Sort.Direction.ASC;
  }
}
