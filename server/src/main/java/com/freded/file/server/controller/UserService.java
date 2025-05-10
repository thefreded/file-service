package com.freded.file.server.controller;


import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class UserService {
    @Inject
    SecurityIdentity securityIdentity;

    /**
     * Fetch the username from SecurityIdentity
     *
     * @return {@link String} user name of logged-in user.
     */
    public String getUsername() {
        return securityIdentity.getPrincipal().getName();
    }
}
