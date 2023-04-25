package br.com.core.utils.id;

import java.util.UUID;

public class IDUtils {

    public static String createCustomUuid(int max_length) {
       return UUID.randomUUID().toString().replaceAll("-", "").substring(0, max_length);
    }

    public static UUID generateDefaultUuid() {
        return UUID.randomUUID();
    }

}
