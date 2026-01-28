package com.qa.automation.tests;

import com.microsoft.playwright.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DelayedButtonTest {
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
    public void testDelayedButtonFlow() {
        // Navigate to the application
        page.navigate(BASE_URL);

        // Navigate to the Timing Challenges tab
        page.click("text=Timing Challenges");
        page.waitForLoadState(LoadState.NETWORKIDLE);

        // Click "Start Process"
        page.click("button:has-text(\"Start Process\")");

        // Wait for the "Confirm Action" button to become enabled (not just visible)
        // Using waitForFunction to ensure button is enabled, not just present
        page.waitForFunction("() => {" +
                "  const button = document.querySelector('button');" +
                "  return button && !button.disabled && button.textContent.includes('Confirm Action');" +
                "}", new Page.WaitForFunctionOptions().setTimeout(5000));

        // Click the confirm button
        page.click("button:has-text(\"Confirm Action\")");

        // Verify the success message appears
        page.waitForSelector("text=/success|completed|confirmed/i", 
                new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5000));

        Locator successMessage = page.locator("text=/success|completed|confirmed/i");
        assertTrue("Success message should be visible", successMessage.isVisible());
    }
}
