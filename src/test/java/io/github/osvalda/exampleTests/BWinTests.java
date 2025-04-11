package io.github.osvalda.exampleTests;

import com.microsoft.playwright.Locator;
import io.github.osvalda.BaseTest;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BWinTests extends BaseTest {

    @Test
    @Description("Adding pick to Betslip")
    @Severity(SeverityLevel.BLOCKER)
    public void firstTest() {
        openLiveBetting();

        Locator eventViewTabButton = page.locator("#ds-tab-id-1-2");
        assertThat(eventViewTabButton).isVisible();
        eventViewTabButton.click();

        Locator odds = page.locator(".result")
                .filter(new Locator.FilterOptions().setHasNot(page.locator(".offline")))
                .first();
        assertThat(odds).isVisible();
        odds.click();

        Locator betSlip = page.locator("//div[contains(text(),'Bet Slip')][@class='ds-tab-item']");
        assertThat(betSlip).isVisible();
        assertThat(betSlip).hasText("Bet Slip 1");
    }

    @Test
    @Description("Check Sport Sorting")
    @Severity(SeverityLevel.BLOCKER)
    public void thirdTest() {
        openLiveBetting();

        Locator azSports = page.getByTestId("all")
                .first();
        assertThat(azSports).isVisible();
        azSports.click();
        
        Locator sports = page.locator(".ds-list-tile-title");
        assertThat(sports.first()).isVisible();
        org.assertj.core.api.Assertions.assertThat(sports.count())
                .as("All sports count must be a positive number")
                .isPositive();
        sports.first().click();
        assertThat(page).hasURL("https://sports.bwin.com/en/sports/american-football-11");

        Locator americanFootballTab = page.getByTestId("11");
        assertThat(americanFootballTab).isVisible();
    }

    @Step("Open live betting")
    private void openLiveBetting() {
        Locator liveBettingButton = page.getByTestId("live")
                .first();

        assertThat(liveBettingButton).isVisible();
        liveBettingButton.click();
    }
}
