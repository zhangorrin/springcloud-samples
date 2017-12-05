package com.orrin.sca.component.redis.config;

public enum Prefixes {


    SNAP_UP_PRODUCT_PRICE("snap_up:product:price", ""),

    SYSTEM_MENUS("system_menus", ""),
    RESOURCE_AND_AUTHORITIES_LOCK("resource_and_authorities_lock", ""),
    RESOURCE_AND_AUTHORITIES_SET("resource:authorit:resource_and_authorities_set",""),
    SESSION("session:","")

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
