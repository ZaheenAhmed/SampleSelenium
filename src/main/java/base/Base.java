package base;

import com.paulhammant.ngwebdriver.NgWebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.*;
import org.testng.annotations.Optional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class Base {

    public static WebDriver driver = null;
    private long minimumTimeout = 3;
    private long maximumTimeout = 60;
    public WebDriverWait wait = null;
    public static Properties properties;

    /**
     * Load config.properties
     */
    @BeforeSuite
    public void configurationAndEnvironmentSetup(ITestContext context) throws IOException {
        loadProperties();
    }

    /**
     * Setup browser and Url from config.properties from individual module.
     * Supported Browser: Chrome
     * @throws IOException config.properties file must be present
     */
    @BeforeClass
    public void browserAndUrlSetup() throws IOException {
        String browser = properties.getProperty("browser");

        if (browser.equalsIgnoreCase("Chrome")) {
            initDriver("Chrome");
        } else if (browser.equalsIgnoreCase("Firefox")) {
            initDriver("Firefox");
        } else if (browser.equalsIgnoreCase("IE")) {
            initDriver("IE");
        } else if (browser.equalsIgnoreCase("Headless Chrome")) {
            initDriver("Headless Chrome");
        } else if (browser.equalsIgnoreCase("Headless Firefox")) {
            initDriver("Headless Firefox");
        } else System.out.println("INVALID BROWSER!");

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(25, TimeUnit.SECONDS);

        driver.manage().window().setPosition(new Point(100, 200));

        String environment = properties.getProperty("environment");
        if (environment.equalsIgnoreCase("prod")) {
            driver.get(properties.getProperty("prod_url"));
        }

        driver.manage().window().maximize();
    }

    /**
     * Initialize driver based on specified chrome.
     *
     * @param browser default Chrome.
     */
    private void initDriver(@Optional("Chrome") String browser) {
        if (driver == null) {
            if (browser.equalsIgnoreCase("Chrome")) {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--start-maximized");
                options.addArguments("--disable-notifications");
                options.addArguments("--disable-extensions");
                options.addArguments("--disable-web-security");
                options.addArguments("--no-proxy-server");
                options.addArguments("--disable-default-apps");
                options.addArguments("test-type");
                options.addArguments("no-sandbox");
                options.addArguments("--allow-running-insecure-content");
                Map<String, Object> prefs = new HashMap<String, Object>();
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                prefs.put("profile.default_content_settings.popups", 0);
                prefs.put("download.prompt_for_download", "false");
                options.setExperimentalOption("prefs", prefs);
                driver = new ChromeDriver(options);
            }
            if (browser.equalsIgnoreCase("Firefox")) {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
            }
        }
    }

    /**
     * Load config.properties file from module.
     *
     * @throws IOException Each module must contain config.properties
     */
    private static void loadProperties() throws IOException {
        properties = new Properties();
        InputStream inputStream = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/config.properties");
        properties.load(inputStream);
        inputStream.close();
    }


    /**
     * Method to check ui every 500 ms to check if element is present
     * @param xPath xpath of the element
     * @param lngTimeout amount of time it will check until timeout
     * @return as a WebElement
     */
    private WebElement fluentWait(final String xPath, long lngTimeout) {
        WebElement element = null;
        try {
            FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                    .withTimeout(Duration.ofSeconds(lngTimeout))
                    .pollingEvery(Duration.ofMillis(500))
                    .ignoring(WebDriverException.class)
                    .ignoring(NoSuchWindowException.class);
            try {
                element = wait.until(new Function<WebDriver, WebElement>() {
                    public WebElement apply(WebDriver driver) {
                        if (driver.findElement(By.xpath(xPath)).isDisplayed()) {
                            return driver.findElement(By.xpath(xPath));
                        } else
                            return null;
                    }
                });
            } catch (Exception e) {
                element = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return element;
    }

    /**
     * Click method
     * @param xPath
     */
    public void click(String xPath) {
        try {
            fluentWait(xPath, maximumTimeout).click();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send text to element on UI
     * @param xPath of element
     * @param text to be sent
     */
    public void sendKeys(String xPath, String text) {
        try {
            WebElement element = fluentWait(xPath, maximumTimeout);
            element.isDisplayed();
            element.clear();
            if (text.trim().length() > 0) {
                element.sendKeys(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Read text on UI
     * @param xPath of element to read text
     * @return text of element
     */
    public String getText(String xPath) {
        String strText = null;

        try {
            strText = fluentWait(xPath, maximumTimeout).getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strText;
    }

    /**
     * Get current window handle
     * @return
     */
    public String getWindowHandle() {
        String winHandleBefore = driver.getWindowHandle();
        return winHandleBefore;
    }

    /**
     * Switch to original window handle
     * @param winHandleBefore
     */
    public void switchToOriginalWindow(String winHandleBefore) {
        driver.switchTo().window(winHandleBefore);
    }

    /**
     * Switch to new window
     */
    public void switchWindow() {
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }
    }

    /**
     * Hard wait
     * @param timeToWaitInMiliSec
     */
    public static void wait(int timeToWaitInMiliSec) {
        try {
            Thread.sleep(timeToWaitInMiliSec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Wait based on javascript document ready state
     *
     */
    public void waitForPageToLoad() {
        wait(1);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String state = (String) js.executeScript("return document.readyState");
        while (!state.equals("complete")) {
            wait(2);
            state = (String) js.executeScript("return document.readyState");
        }
    }

    /**
     * Mouse over element
     * @param locatorKey
     */
    public void mouseOverElement(String locatorKey) {
        Actions actions = new Actions(driver);
        WebElement element = driver.findElement(By.xpath(locatorKey));
        actions.moveToElement(element);
        actions.perform();
    }

    public void untilAngularFinishHttpCalls() {
         final String javaScriptToLoadAngular =
                "var injector = window.angular.element('body').injector();" +
                        "var $http = injector.get('$http');" +
                        "return ($http.pendingRequests.length === 0)";

        ExpectedCondition<Boolean> pendingHttpCallsCondition = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript(javaScriptToLoadAngular).equals(true);
            }
        };
        WebDriverWait wait = new WebDriverWait(driver, 20); // timeout = 20 secs
        wait.until(pendingHttpCallsCondition);
    }

    public void waitForAngularRequestsToFinish(){
        JavascriptExecutor myExecutor = ((JavascriptExecutor) driver);
        new NgWebDriver(myExecutor).waitForAngularRequestsToFinish();
    }




}
