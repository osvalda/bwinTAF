package io.github.osvalda.pages;

import com.microsoft.playwright.Page;
import io.github.osvalda.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionFactory;

import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class BasePage {

    protected Page page;
    protected ConditionFactory conditionFactory;

    public BasePage(Page page) {
        this.page = page;
        this.conditionFactory = Awaitility.await()
                .ignoreExceptions()
                .atMost(BaseTest.getTimeout(), TimeUnit.MILLISECONDS);
    }
}
