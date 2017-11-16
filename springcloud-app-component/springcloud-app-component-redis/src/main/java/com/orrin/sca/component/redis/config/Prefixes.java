package com.orrin.sca.component.redis.config;

public enum Prefixes {

    SYSTEM_MENUS("SYSTEM_MENUS", ""),
    RESOURCE_AND_AUTHORITIES_LOCK("RESOURCE_AND_AUTHORITIES_LOCK", ""),
    RESOURCE_AND_AUTHORITIES_SET("RESOURCE:AUTHORIT:RESOURCE_AND_AUTHORITIES_SET",""),
    SESSION("SESSION:","")

    ;


    private String value;

    private String name;

    Prefixes(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
