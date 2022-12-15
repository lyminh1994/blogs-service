package com.minhlq.blogsservice.annotation.impl.validator;

import com.minhlq.blogsservice.annotation.UpdateUserConstraint;
import com.minhlq.blogsservice.dto.UpdateUserDto;
import com.minhlq.blogsservice.payload.UserPrincipal;
import com.minhlq.blogsservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * The implement for UpdateUserConstraint annotation.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@RequiredArgsConstructor
public class UpdateUserValidator
        implements ConstraintValidator<UpdateUserConstraint, UpdateUserDto> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(UpdateUserDto value, ConstraintValidatorContext context) {
        final String inputEmail = value.getParams().getEmail();
        final String inputPhone = value.getParams().getPhone();
        final UserPrincipal targetUser = value.getTargetUser();

        boolean isEmailValid =
                userRepository
                        .findByEmail(inputEmail)
                        .map(user -> Objects.equals(user.getId(), targetUser.getId()))
                        .orElse(true);
        if (!isEmailValid) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("email already using by another user")
                    .addPropertyNode("email")
                    .addConstraintViolation();
        }

        boolean isPhoneValid =
                userRepository
                        .findByPhone(inputPhone)
                        .map(user -> Objects.equals(user.getId(), targetUser.getId()))
                        .orElse(true);
        if (!isPhoneValid) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("phone number already using by another user")
                    .addPropertyNode("phone")
                    .addConstraintViolation();
        }

        return isEmailValid || isPhoneValid;
    }
}
