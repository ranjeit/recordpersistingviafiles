package com.substring.auth.helper;

import java.util.UUID;

public class UserHelpler {

    public static UUID parseUUID(String uuid){
        return UUID.fromString(uuid);
    }
}
