package com.sss.app.core.user.model;

import java.util.List;

public interface User {
    public String getName();

    public String getUsername();

    public List<String> getRoles();

    public List<String> getGroups();
}
