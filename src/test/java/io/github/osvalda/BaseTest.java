package io.github.osvalda;

import com.google.common.collect.ImmutableMap;
import com.microsoft.playwright.*;
import io.qameta.allure.Attachment;
import io.qameta.allure.Description;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;

@Slf4j
public abstract class BaseTest implements IHookable {

    private static final String SITE_URL_KEY = "site.url";
    private static final String TIMEOUT_KEY = "timeout";

    protected Page page;
    private Playwright playwright;
    private BrowserType.LaunchOptions options;
    private BrowserContext context;
    @Getter
    private static long timeout;

    @Description("Precondition environment setup and main navigation.")
    @BeforeSuite(alwaysRun = true)
    public void environmentSetup(ITestContext ctx) {
        String environment = getEnvironmentName();
        Properties envProperties = getPropertiesFile(environment);
        String siteUrl = envProperties.getProperty(SITE_URL_KEY);
        timeout = Long.parseLong(envProperties.getProperty(TIMEOUT_KEY, "10000"));

        playwright = Playwright.create();

        String browserType = System.getenv("BROWSER");
        if (browserType == null){
            browserType = "default";
        }
        setupWebDriver(browserType);

        page = context.newPage();
        page.navigate(siteUrl);

        allureEnvironmentWriter(ImmutableMap.<String, String>builder()
                .put("Environment Name", environment)
                .put("URL", siteUrl)
                .put("Browser Type", browserType)
                .put("Suite", ctx.getCurrentXmlTest().getSuite().getName())
                .build(), System.getProperty("user.dir") + "/build/allure-results/");
    }

    private static String getEnvironmentName() {
        String environment = System.getenv("ENV");
        log.info("The {} environment is chosen.", environment);
        if (environment == null) {
            throw new VerifyError("The ENV environment variable must contain the ".concat(
                    "name of the pod where the tests are going to run!"));
        }
        return environment;
    }

    private static Properties getPropertiesFile(String fileName) {
        String filePath = "environments/" + fileName + ".properties";
        log.info("Open properties file: {}", filePath);
        try {
            InputStream propertiesStream = BaseTest.class.getClassLoader().getResourceAsStream(filePath);
            Properties prop = new Properties();
            prop.load(propertiesStream);
            return prop;
        } catch (IOException | NullPointerException e) {
            log.error(e.getLocalizedMessage());
            throw new VerifyError("The ".concat(filePath).concat(" file is corrupted or missing!"));
        }
    }

    private void setupWebDriver(String browserType) {
        options = new BrowserType.LaunchOptions()
                .setHeadless(false);

        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setViewportSize(1930, 1080);

        Browser browser = switch (browserType) {
            case "safari" -> playwright.webkit().launch(options);
            case "firefox" -> playwright.firefox().launch(options);
            case "firefox-headless" -> playwright.firefox().launch();
            case "chrome" -> playwright.chromium().launch(options);
            case "chrome-mobile" -> {
                contextOptions.setViewportSize(390,844);
                yield playwright.chromium().launch(options);
            }

            default -> playwright.chromium().launch();
        };
        context = browser.newContext(contextOptions);

    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        page.close();
    }

    @Override
    public void run(IHookCallBack callBack, ITestResult testResult) {
        callBack.runTestMethod(testResult);
        if (testResult.getThrowable() != null) {
            attachScreenShot();
            attachSiteUrl();
        }
    }

    @Attachment(value = "ScreenShot of failed page", type = "image/png")
    private byte[] attachScreenShot() {
        return page.screenshot();
    }

    @Attachment(value = "URL of failed page", type = "text/uri-list")
    private String attachSiteUrl() {
        return page.url();
    }
}
