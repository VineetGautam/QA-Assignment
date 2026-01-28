package com.qa.automation.tests;

import com.microsoft.playwright.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DynamicIdsTest {
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
    public void testDynamicIdHandling() {
        // Navigate to the page
        page.navigate(BASE_URL);

        // Navigate to the Flaky Selectors tab
        page.click("text=Flaky Selectors");
        page.waitForLoadState(LoadState.NETWORKIDLE);

        // Regenerate IDs multiple times to ensure the test is resilient
        for (int i = 0; i < 2; i++) {
            // Click "Regenerate All IDs"
            page.click("button:has-text(\"Regenerate All IDs\")");

            // Wait for IDs to be regenerated
            page.waitForTimeout(1000);
        }

        // Select the item named "Beta" using text selector (not id)
        Locator betaItem = page.locator("text=Beta").first();
        betaItem.click();

        // Verify "Beta" is shown as selected
        boolean isSelected = (Boolean) page.evaluate("() => {" +
                "  const betaElement = document.evaluate(" +
                "    \"//*[text()='Beta']\", " +
                "    document, " +
                "    null, " +
                "    XPathResult.FIRST_ORDERED_NODE_TYPE, " +
                "    null" +
                "  ).singleNodeValue;" +
                "  if (!betaElement) return false;" +
                "  const parent = betaElement.closest('[role=\"option\"], [role=\"listitem\"], .item, li');" +
                "  if (parent) {" +
                "    return parent.classList.contains('selected') || " +
                "           parent.classList.contains('active') || " +
                "           parent.getAttribute('aria-selected') === 'true';" +
                "  }" +
                "  return betaElement.classList.contains('selected') || " +
                "         betaElement.classList.contains('active');" +
                "}");

        assertTrue("Beta item should be selected", isSelected);
    }
}
