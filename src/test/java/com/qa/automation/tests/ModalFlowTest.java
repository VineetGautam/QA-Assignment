package com.qa.automation.tests;

import com.microsoft.playwright.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ModalFlowTest {
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
    public void testModalConfirmationFlow() {
        // Navigate to the page
        page.navigate(BASE_URL);

        // Navigate to the Responsive tab
        page.click("text=Responsive");
        page.waitForLoadState(LoadState.NETWORKIDLE);

        // Click "Open Modal"
        page.click("button:has-text(\"Open Modal\")");

        // Wait for the first modal to be visible
        Locator firstModal = page.locator("[role=\"dialog\"], .modal, [class*=\"modal\"]:visible").first();
        assertTrue("First modal should be visible", firstModal.isVisible());

        // In the modal, click "Show Details"
        // Need to click on the button within the visible modal (not behind it)
        Locator showDetailsButton = firstModal.locator("button:has-text(\"Show Details\")");
        showDetailsButton.click();

        // Wait for the nested modal to appear
        Locator nestedModal = page.locator("[role=\"dialog\"], .modal, [class*=\"modal\"]:visible").last();
        assertTrue("Nested modal should be visible", nestedModal.isVisible());

        // Verify we have at least 1 modal visible (could be same modal updated or separate)
        Locator allModals = page.locator("[role=\"dialog\"], .modal, [class*=\"modal\"]");
        int visibleModalCount = allModals.count();
        assertTrue("Should have at least 1 modal visible", visibleModalCount >= 1);

        // In the nested modal, click "Confirm"
        Locator confirmButton = nestedModal.locator("button:has-text(\"Confirm\")");
        confirmButton.click();

        // Wait for modals to close
        page.waitForTimeout(500);

        // Verify both modals are closed
        Locator closedModals = page.locator("[role=\"dialog\"], .modal, [class*=\"modal\"]:visible");
        int closedModalCount = closedModals.count();
        assertEquals("Both modals should be closed", 0, closedModalCount);

        // Verify result shows "confirmed"
        Locator result = page.locator("text=/confirmed|success/i");
        assertTrue("Result should show confirmed or success", result.isVisible());
    }
}
