package threadQa.tests;

import com.codeborne.selenide.Browsers;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import threadQa.myappmanager.MyAppManager;

import java.lang.reflect.Method;
import java.util.Arrays;

public class TestBase {
    protected static final MyAppManager app
            = new MyAppManager(System.getProperty("browser", Browsers.CHROME));
    Logger logger = LoggerFactory.getLogger(TestBase.class);

    @BeforeTest
    public void globalLogs(final ITestContext context) {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(true));
        logger.info("Cьют: " + context.getCurrentXmlTest().getSuite().getName());
    }

    @BeforeClass
    public void setUp(ITestContext context) throws Exception {
        app.init();
        context.setAttribute("app", app);
    }

    @BeforeMethod
    public void logTestStart(Method m, Object[] p) {
        logger.info("Тест " + m.getName() + " с параметрами " + Arrays.asList(p) + " запущен:-->");
    }

    @AfterClass(alwaysRun = true)
    @Step("Закрываем браузер")
    public void tearDown() {
        app.stop();
    }
}