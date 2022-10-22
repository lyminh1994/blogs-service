package com.minhlq.blogsservice.config;

import com.minhlq.blogsservice.entity.Role;
import com.minhlq.blogsservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppInit implements ApplicationListener<ApplicationReadyEvent> {

  private final RoleRepository roleRepository;

  @Override
  public void onApplicationEvent(@NonNull ApplicationReadyEvent applicationReadyEvent) {
    if (roleRepository.findByName("ROLE_USER").isEmpty()) {
      Role role = new Role();
      role.setName("ROLE_USER");
      role.setDescription("Role for all user");

      roleRepository.save(role);
      log.info("Role user saved!");
    }
  }
}
