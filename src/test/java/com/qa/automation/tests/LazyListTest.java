package com.qa.automation.tests;

import com.microsoft.playwright.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LazyListTest {
    private static final String BASE_URL = "https://claude.ai/public/artifacts/1e02a9a5-4f20-4f19-a7ba-6c3f16c6eab9";
    private Browser browser;
    private BrowserContext context;
    private Page page;

    @Before
    public void setUp() {
        browser = Playwright.create().chromium().launch();
        context = browser.newContext();
        page = context.newPage();
    }

    @After
    public void tearDown() {
        context.close();
        browser.close();
    }

    @Test
    public void testLoadAndVerifyListItems() {
        // Navigate to the page
        page.navigate(BASE_URL);

        // Navigate to the Timing Challenges tab
        page.click("text=Timing Challenges");
        page.waitForLoadState(LoadState.NETWORKIDLE);

        // Click "Load More Items" 3 times
        for (int i = 0; i < 3; i++) {
            // Wait for the button to be clickable
            page.click("button:has-text(\"Load More Items\")");

            // Wait for loading to complete after each click
            page.waitForLoadState(LoadState.NETWORKIDLE);

            // Give a brief moment for DOM to update
            page.waitForTimeout(500);
        }

        // Verify exactly 15 items are displayed
        Locator items = page.locator("[data-testid=\"list-item\"], .list-item, li");
        int itemCount = items.count();
        assertEquals("Should have exactly 15 items", 15, itemCount);

        // Verify at least one item has status "active"
        Locator activeItems = page.locator("[data-testid*=\"status\"], [class*=\"status\"]")
                .filter(new Locator.FilterOptions().setHasText("active"));
        int activeCount = activeItems.count();
        assertTrue("Should have at least one active item", activeCount >= 1);

        // Verify at least one item has status "pending"
        Locator pendingItems = page.locator("[data-testid*=\"status\"], [class*=\"status\"]")
                .filter(new Locator.FilterOptions().setHasText("pending"));
        int pendingCount = pendingItems.count();
        assertTrue("Should have at least one pending item", pendingCount >= 1);
    }
}
