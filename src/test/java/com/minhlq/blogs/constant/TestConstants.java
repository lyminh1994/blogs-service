package com.minhlq.blogs.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestConstants {

  public static final String AUTHENTICATION_TOKEN =
      "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyMDAxIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.aLAFz9x6OF4BSpUQxIKWn39sDp8Cr9ZEeVqcMTdwtBsEZwdCKyR2lkjo_2BzcOJz96N7Qdx7t7VUXerKShnbxg";

  public static final String BEARER_AUTHENTICATION_TOKEN =
      SecurityConstants.BEARER + StringUtils.SPACE + AUTHENTICATION_TOKEN;

  public static final String USER_DTO_MUST_NOT_BE_NULL = "User must not be null";
}
