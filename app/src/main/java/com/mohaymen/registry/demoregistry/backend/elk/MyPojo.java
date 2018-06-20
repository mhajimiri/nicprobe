package com.mohaymen.registry.demoregistry.backend.elk;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPojo
{
    private String level;

    private String host;

    private String _some_info;

    private String short_message;

    private String version;

    @Override
    public String toString()
    {
        return "ClassPojo [level = "+level+", host = "+host+", _some_info = "+_some_info+", short_message = "+short_message+", version = "+version+"]";
    }
}
