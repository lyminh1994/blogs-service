package com.minhlq.blogsservice.controller;

import com.minhlq.blogsservice.service.JwtService;
import com.minhlq.blogsservice.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserService userService;
    @MockBean
    JwtService jwtService;
    @MockBean
    UserDetailsService userDetailsService;

    @WithUserDetails
    @Test
    @DisplayName("Should Get Current User With Token GET request to endpoint - /user")
    void shouldGetCurrentUserWithToken() throws Exception {
        // given - precondition or setup
        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/user"));

        // then - verify the result or output using assert statements
        response.andDo(print());
    }

    @Test
    @DisplayName("Should Get 401 Without Token GET request to endpoint - /user")
    void shouldGet401WithoutToken() {
    }

    @Test
    @DisplayName("Should Get 401 With Invalid Token GET request to endpoint - /user")
    void shouldGet401WithInvalidToken() {
    }

    @Test
    @DisplayName("Should Update Current User Profile PUT request to endpoint - /user")
    void shouldUpdateCurrentUserProfile() {
    }

    @Test
    @DisplayName(
            "Should Get Error If Email Exists When Update User Profile PUT request to endpoint - /user")
    void shouldGetErrorIfEmailExistsWhenUpdateUserProfile() {
    }

    @Test
    @DisplayName("Should Get 401 If Not Login PUT request to endpoint - /user")
    void shouldGet401IfNotLogin() {
    }

    @Test
    @DisplayName("Should Get User Profile Success GET request to endpoint - /profiles/{username}")
    void shouldGetUserProfileSuccess() {
    }

    @Test
    @DisplayName("Should Follow User Success POST request to endpoint - /profiles/{username}/follow")
    void shouldFollowUserSuccess() {
    }

    @Test
    @DisplayName(
            "Should Unfollow User Success DELETE request to endpoint - /profiles/{username}/follow")
    void shouldUnfollowUserSuccess() {
    }
}
