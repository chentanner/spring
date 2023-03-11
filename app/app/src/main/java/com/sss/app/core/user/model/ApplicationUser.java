package com.sss.app.core.user.model;

import java.util.ArrayList;
import java.util.List;

public class ApplicationUser implements User {
    private final String name;
    private String username;
    private List<String> roles = new ArrayList<>();
    private List<String> groups = new ArrayList<>();

    private boolean isAdministrator = false;

    public ApplicationUser(String name) {
        this.name = name;
        this.username = name;
    }

    public ApplicationUser(String name, String username, List<String> roles, List<String> groups) {
        this.name = name;
        this.username = username;
        this.roles = roles;
        this.groups = groups;
    }

    public ApplicationUser(String name, String username, List<String> roles, List<String> groups, boolean isAdministrator) {
        this.name = name;
        this.username = username;
        this.roles = roles;
        this.groups = groups;
        this.isAdministrator = isAdministrator;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public List<String> getRoles() {
        return roles;
    }

    @Override
    public List<String> getGroups() {
        return groups;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public boolean isAdministrator() {
        return isAdministrator;
    }

    public void setAdministrator(boolean administrator) {
        isAdministrator = administrator;
    }
}