package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class JiraParser {

    private String jiraLimit;
    private String jiraCurrentUsage;
    private String jiraMonthlyPercentage;
    private String jsmLimit;
    private String jsmCurrentUsage;
    private String jsmMonthlyPercentage;

    public void parse(String login, String password) {

        // Setup ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();

        // Set ChromeOptions
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        WebDriver driver = new ChromeDriver(options);

        String loginUrl = "https://solbeg.atlassian.net/";

        try {
            // Open the application URL that uses Microsoft SSO
            driver.get(loginUrl);

            // Create an explicit wait instance
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            // Wait for the SSO button and click it
            WebElement ssoButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("microsoft-auth-button")));
            ssoButton.click();

            // Wait for the Microsoft email field and enter the email
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("i0116")));
            emailField.sendKeys(login);

            // Click Next
            WebElement nextButton = driver.findElement(By.id("idSIButton9"));
            nextButton.click();

            // Wait for the password field and enter the password
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("i0118")));
            passwordField.sendKeys(password);

            // Click Sign In
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("idSIButton9")));
            signInButton.click();

            // Handle "Stay signed in" prompt
            WebElement staySignedInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("idBtn_Back")));
            staySignedInButton.click();

            // Wait for the page to load after successful login
            wait.until(ExpectedConditions.urlToBe("https://solbeg.atlassian.net/jira/your-work"));

            driver.get("https://solbeg.atlassian.net/jira/settings/automation#/tab/usage");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody[1]//td[1]")));

            List<String> remaining = driver.findElements(By.xpath("//tbody[1]//td[4]"))
                    .stream()
                    .map(WebElement::getText)
                    .toList();

            List<String> limit = driver.findElements(By.xpath("//tbody[1]//td[5]"))
                    .stream()
                    .map(WebElement::getText)
                    .toList();

            List<String> usagePercentage = driver.findElements(By.xpath("//tbody[1]//td[3]"))
                    .stream()
                    .map(WebElement::getText)
                    .toList();

            Integer jiraLimitInt = Integer.valueOf(limit.get(0).replace(",", ""));
            Integer jsmLimitInt = Integer.valueOf(limit.get(1).replace(",", ""));
            Integer jiraRemainingInt = Integer.valueOf(remaining.get(0).replace(",", ""));
            Integer jsmRemainingInt = Integer.valueOf(remaining.get(1).replace(",", ""));

            jiraLimit = limit.get(0).replace(",", "");
            jiraCurrentUsage = String.valueOf(jiraLimitInt - jiraRemainingInt);
            jiraMonthlyPercentage = usagePercentage.get(0).replace("%", "");
            jsmLimit = limit.get(1).replace(",", "");
            jsmCurrentUsage = String.valueOf(jsmLimitInt - jsmRemainingInt);
            jsmMonthlyPercentage = usagePercentage.get(1).replace("%", "");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }
    }

    public String getJiraLimit() {
        return jiraLimit;
    }

    public String getJiraCurrentUsage() {
        return jiraCurrentUsage;
    }

    public String getJiraMonthlyPercentage() {
        return jiraMonthlyPercentage;
    }

    public String getJsmLimit() {
        return jsmLimit;
    }

    public String getJsmCurrentUsage() {
        return jsmCurrentUsage;
    }

    public String getJsmMonthlyPercentage() {
        return jsmMonthlyPercentage;
    }

}
