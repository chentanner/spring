package com.sss.entity.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface SimpleListItem extends ListItem {

    @JsonIgnore
    public boolean getSelectable();

    @JsonIgnore
    public void setSelectable(boolean selectable);


}
