package com.sss.app.test;

import java.util.List;

public abstract class BaseEntityBuilder<T, TSnapshot> {

    public abstract T build();

    public abstract T buildSavedEntity();

    public abstract List<T> buildSavedEntities(int numEntities);

    public abstract TSnapshot buildSnapshot();
}
