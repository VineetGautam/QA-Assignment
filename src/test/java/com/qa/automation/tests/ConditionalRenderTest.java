package com.qa.automation.tests;

import com.microsoft.playwright.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConditionalRenderTest {
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
    public void testConditionalLoginFlow() {
        // Navigate to the page
        page.navigate(BASE_URL);

        // Navigate to the Flaky Selectors tab
        page.click("text=Flaky Selectors");
        page.waitForLoadState(LoadState.NETWORKIDLE);

       
        // Click "Admin User" login button
        page.click("button:has-text(\"Admin User\")");

        // Wait for dashboard to load and loading state to complete
        page.waitForLoadState(LoadState.NETWORKIDLE);

        // Verify the Admin Panel is visible
        Locator adminPanel = page.locator("text=/Admin Panel|admin-panel/i").first();
        assertTrue("Admin Panel should be visible", adminPanel.isVisible());

        // Verify the Standard Panel is NOT visible
        Locator standardPanel = page.locator("text=/Standard Panel|standard-panel/i").first();
        try {
            assertFalse("Standard Panel should not be visible", standardPanel.isVisible());
        } catch (PlaywrightException e) {
            // Element doesn't exist, which is expected
            assertTrue("Standard Panel should not exist or be visible", true);
        }

        // Click Logout
        try {
            page.click("button:has-text(\"Logout\")");
        } catch (PlaywrightException e) {
            page.click("button:has-text(\"Log out\")");
        }

        // Wait for logout to complete
        page.waitForLoadState(LoadState.NETWORKIDLE);

        
        // Click "Standard User" login button
        page.click("button:has-text(\"Standard User\")");

        // Wait for dashboard to load
        page.waitForLoadState(LoadState.NETWORKIDLE);

        // Verify the Standard Panel is visible
        Locator standardPanelAfterLogin = page.locator("text=/Standard Panel|standard-panel/i").first();
        assertTrue("Standard Panel should be visible after login", standardPanelAfterLogin.isVisible());

        // Verify the Admin Panel is NOT visible
        Locator adminPanelAfterLogout = page.locator("text=/Admin Panel|admin-panel/i").first();
        try {
            assertFalse("Admin Panel should not be visible after logout", adminPanelAfterLogout.isVisible());
        } catch (PlaywrightException e) {
            // Element doesn't exist, which is expected
            assertTrue("Admin Panel should not exist or be visible", true);
        }
    }
}
