// Path: src/test/java/utils/AllureAttach.java
package utils;

import io.qameta.allure.Attachment;

public class AllureAttach {
    @Attachment(value = "{name}", type = "text/plain")
    public static String text(String name, String body) { return body; }

    @Attachment(value = "{name}", type = "application/json")
    public static String json(String name, String body) { return body; }
}
