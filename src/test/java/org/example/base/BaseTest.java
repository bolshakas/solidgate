package org.example.base;

import com.codeborne.selenide.Configuration;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.config.ConfigLoader;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {

    protected static String publicKey;
    protected static String secretKey;

    @BeforeAll
    static void setUp() {
        WebDriverManager.chromedriver().setup();
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10000;

        ConfigLoader configLoader = new ConfigLoader();
        publicKey = configLoader.getProperty("publicKey");
        secretKey = configLoader.getProperty("secretKey");
    }
}
