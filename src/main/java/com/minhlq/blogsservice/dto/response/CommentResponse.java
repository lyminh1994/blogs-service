package com.minhlq.blogsservice.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * This class models the format of the comment response produced.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Data
public class CommentResponse {

    private String id;

    private String body;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private ProfileResponse user;
}
