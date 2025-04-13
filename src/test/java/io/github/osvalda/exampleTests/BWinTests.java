package io.github.osvalda.exampleTests;

import com.microsoft.playwright.Locator;
import io.github.osvalda.BaseTest;
import io.github.osvalda.pages.MainPage;
import io.qameta.allure.*;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BWinTests extends BaseTest {

    private MainPage mainPage;

    @BeforeMethod(alwaysRun = true)
    public void testSetup() {
        mainPage = new MainPage(page);
    }

    @Test
    @Description("Adding pick to Betslip")
    @Severity(SeverityLevel.BLOCKER)
    public void firstTest() {
        mainPage.openLiveBetting()
                .clickEventViewButton()
                .clickFirstLiveOdd()
                .addBetAndVerifyIt();
    }

    @Test
    @Description("Check Sport Sorting")
    @Severity(SeverityLevel.BLOCKER)
    public void thirdTest() {
        mainPage.openLiveBetting()
                .clickOnAllSports()
                .clickOnFirstAvailableSport()
                .verifyAmericanFootballVisibility();
    }

}
