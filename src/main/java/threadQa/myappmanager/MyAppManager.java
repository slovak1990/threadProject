package threadQa.myappmanager;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.codeborne.selenide.Selenide.*;

public class MyAppManager {
    private final Properties properties;
    private final String browser;
    private BaseObject baseObject;

    public MyAppManager(String browser) {
        this.browser = browser;
        properties = new Properties();
    }

    public void init() throws IOException {
        String target = System.getProperty("target", "local");
        properties.load(new FileReader(String.format("src/test/resources/%s.properties", target)));
        Configuration.browser = browser;
        Configuration.timeout = 20000;
        Configuration.browserSize = properties.getProperty("browser.size");
        Configuration.baseUrl = System.getProperty("baseUrl", properties.getProperty("baseUrl"));
        Configuration.proxyEnabled = true;
        Configuration.pageLoadStrategy = "eager";
        Configuration.pageLoadTimeout = 60000;
        Configuration.fileDownload = FileDownloadMode.FOLDER;

        if ("".equals(properties.getProperty("selenoid.server"))) {
            Configuration.reopenBrowserOnFail = true;
            Configuration.savePageSource = false;
        } else {
            //Configuration.remote = "http://" + InetAddress.getLocalHost().getHostAddress() + properties.getProperty("selenoid.server");
            Configuration.remote = "http://" + properties.getProperty("selenoid.server");
            DesiredCapabilities capabilities = new DesiredCapabilities();
            Map<String, Object> selenoidOptions = new HashMap<>();
            selenoidOptions.put("enableVNC", true);
            selenoidOptions.put("enableVideo", false);
            selenoidOptions.put("enableLog", true);
            capabilities.setCapability("selenoid:options", selenoidOptions);
            Configuration.browserCapabilities = capabilities;
        }
        baseObject = new BaseObject(this);
        open(Configuration.baseUrl);
    }

    public void stop() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            if ("".equals(properties.getProperty("selenoid.server"))) {
                localStorage().clear();
                clearBrowserCookies();
            }
            closeWebDriver();
        }
    }

    public BaseObject base() {
        return baseObject;
    }
}