package com.minhlq.blogsservice.dto.response;

import java.util.List;
import lombok.Value;

/**
 * This class base for paging response produced.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Value
public class PageResponse<T> {

  List<T> contents;

  long totalElements;
}
