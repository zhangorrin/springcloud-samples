package com.orrin.sca.component.utils.string;

import java.util.UUID;

public class LocalStringUtils {
    public static String uuidLowerCase(){
        return UUID.randomUUID().toString().toLowerCase().replaceAll("-","");
    }
}
