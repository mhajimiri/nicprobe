package com.mohaymen.registry.demoregistry.backend.elk;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GrayLogJson {
    private String level;

    private String host;

    private String _some_info;

    private String short_message;

    private String version;
}
