package com.freded.file.server.common;

import com.freded.file.server.common.annotation.LoggedInUser;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserProducer {

  @Inject SecurityIdentity securityIdentity;

  @Produces
  @LoggedInUser
  @RequestScoped
  public LoggedInUserInfo getLoggedInUser() {
    String username = securityIdentity.getPrincipal().getName();
    return new LoggedInUserInfo(username);
  }
}
