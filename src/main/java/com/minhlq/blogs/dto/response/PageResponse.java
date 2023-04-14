package com.minhlq.blogs.dto.response;

import java.util.List;

/**
 * This class base for paging response produced.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public record PageResponse<T>(List<T> contents, long totalElements) {}
