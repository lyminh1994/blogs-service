package com.minhlq.blogsservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.minhlq.blogsservice.constant.SecurityConstants.BEARER;
import static io.netty.util.internal.StringUtil.SPACE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestConstants {

    public static final String ENCRYPTED_TOKEN =
            "rsZ/WlOikdMCoomZbQdyJFlraMy3Dk8Pyw5vbgUPJLXA6r4fwA29isOtQvzjq1W1vVscvchCS3ci3qCQ/pQTHvx8AiCvUrxqpIr3KBwSgTc4jw+92eTgRUG9zAYVDTE4UKSIENQ2jwNVI59Rpg4Iw5NJbhIhWgqU2lFIDFCv9xM4DkYwRt7E3W7K+7Py3MaHHRQ3SYRZGggqnif3aE51mNxTMqrRWmn8qQOKiJ8Y8OI8IzORlDCfeomyOFq2y0C5nt+nqzE2bm7cQdZ9LSDhx37lK5X7ZNE/9DaF8I/nQ/0=";

    public static final String AUTHENTICATION_TOKEN =
            "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyMDAxIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.aLAFz9x6OF4BSpUQxIKWn39sDp8Cr9ZEeVqcMTdwtBsEZwdCKyR2lkjo_2BzcOJz96N7Qdx7t7VUXerKShnbxg";

    public static final String BEARER_AUTHENTICATION_TOKEN = BEARER + SPACE + AUTHENTICATION_TOKEN;
}
