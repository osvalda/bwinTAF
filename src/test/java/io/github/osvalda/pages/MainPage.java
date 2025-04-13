package io.github.osvalda.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Slf4j
public class MainPage extends BasePage {

    private final String liveBettingLocator = "live";
    private final String allSportsLocator = "all";
    private final String eventViewLocator = "#ds-tab-id-1-2";
    private final String sportsListLocator = ".ds-list-tile-title";
    private final String oddsLocator = ".result";
    private final String betSlipLocator = "//div[contains(text(),'Bet Slip')][@class='ds-tab-item']";
    private final String americanFootballTabLocator = "11";

    public MainPage(Page page) {
        super(page);
    }

    @Step("Open live betting")
    public MainPage openLiveBetting() {
        log.info("Open live betting");
        Locator liveBettingButton = page.getByTestId(liveBettingLocator)
                .first();

        assertThat(liveBettingButton).isVisible();
        liveBettingButton.click();

        return this;
    }

    @Step("Click on event view button")
    public MainPage clickEventViewButton() {
        log.info("Click on event view button");
        Locator eventViewTabButton = page.locator(eventViewLocator);
        assertThat(eventViewTabButton).isVisible();
        eventViewTabButton.click();

        return this;
    }

    @Step("Click on the first live odd")
    public MainPage clickFirstLiveOdd() {
        log.info("Click on the first live odd");
        Locator odds = page.locator(oddsLocator)
                .filter(new Locator.FilterOptions().setHasNot(page.locator(".offline")))
                .first();
        assertThat(odds).isVisible();
        odds.click();

        return this;
    }

    @Step("Add  bet and verify its placement")
    public MainPage addBetAndVerifyIt() {
        log.info("Add  bet and verify its placement");
        Locator betSlip = page.locator(betSlipLocator);
        assertThat(betSlip).isVisible();
        assertThat(betSlip).hasText("Bet Slip 1");

        return this;
    }

    @Step("Click on All sports button")
    public MainPage clickOnAllSports() {
        log.info("Click on All sports button");
        Locator azSports = page.getByTestId(allSportsLocator)
                .first();
        assertThat(azSports).isVisible();
        azSports.click();

        return this;
    }

    @Step("Click on first available sport")
    public MainPage clickOnFirstAvailableSport() {
        log.info("Click on first available sport");
        Locator sports = page.locator(sportsListLocator);
        assertThat(sports.first()).isVisible();
        org.assertj.core.api.Assertions.assertThat(sports.count())
                .as("All sports count must be a positive number!")
                .isPositive();
        sports.first().click();
        assertThat(page).hasURL("https://sports.bwin.com/en/sports/american-football-11");

        return this;
    }

    @Step("Verify that the American Football Tab is visible on the page")
    public MainPage verifyAmericanFootballVisibility() {
        log.info("Verify that the American Football Tab is visible on the page");
        Locator americanFootballTab = page.getByTestId(americanFootballTabLocator);
        assertThat(americanFootballTab).isVisible();

        return this;
    }
}
