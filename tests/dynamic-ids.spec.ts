import { test, expect } from '@playwright/test';

test.describe('Dynamic ID Handling - Test 3', () => {
  test('should handle dynamic IDs without relying on id attributes', async ({ page }) => {
    // Navigate to the page
    await page.goto('/');
    
    // Navigate to the Flaky Selectors tab
    await page.click('text=Flaky Selectors', { timeout: 5000 });
    await page.waitForLoadState('networkidle');
    
    // Regenerate IDs multiple times to ensure the test is resilient
    for (let i = 0; i < 2; i++) {
      // Click "Regenerate All IDs"
      await page.click('button:has-text("Regenerate All IDs")', { timeout: 5000 });
      
      // Wait for IDs to be regenerated
      await page.waitForTimeout(1000);
    }
    
    // Select the item named "Beta" using text selector (not id)
    // Use a more specific approach to find Beta without relying on id
    const betaItem = page.locator('text=Beta').first();
    await betaItem.click({ timeout: 5000 });
    
    // Verify "Beta" is shown as selected
    // Check for visual indication of selection (e.g., highlighted, checked, active class)
    const selectedBeta = page.locator('text=Beta').locator('..').filter({ 
      has: page.locator('[class*="selected"], [class*="active"], [aria-selected="true"]')
    }).first();
    
    // Alternative: Check if it has selected styling
    const betaElement = page.locator('text=Beta').first();
    const isSelected = await betaElement.evaluate((el) => {
      const parent = el.closest('[role="option"], [role="listitem"], .item, li');
      if (parent) {
        return parent.classList.contains('selected') || 
               parent.classList.contains('active') ||
               parent.getAttribute('aria-selected') === 'true';
      }
      return el.classList.contains('selected') || 
             el.classList.contains('active');
    });
    
    expect(isSelected).toBeTruthy();
  });
});
