package guru.qa.niffler.utils;

import java.util.UUID;

public class GrpcStringUtil {

    public static String safe(String value) {
        return value == null ? "" : value;
    }

    public static String safe(UUID value) {
        return value == null ? "" : value.toString();
    }

}
