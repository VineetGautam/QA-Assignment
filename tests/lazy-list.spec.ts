import { test, expect } from '@playwright/test';

test.describe('Load and Verify List Items - Test 2', () => {
  test('should load 15 items with various statuses', async ({ page }) => {
    // Navigate to the page
    await page.goto('/');
    
    // Navigate to the Timing Challenges tab
    await page.click('text=Timing Challenges', { timeout: 5000 });
    await page.waitForLoadState('networkidle');
    
    // Click "Load More Items" 3 times
    for (let i = 0; i < 3; i++) {
      // Wait for the button to be clickable
      await page.click('button:has-text("Load More Items")', { timeout: 5000 });
      
      // Wait for loading to complete after each click
      // Check for loading indicator to disappear or network to be idle
      await page.waitForLoadState('networkidle');
      
      // Give a brief moment for DOM to update
      await page.waitForTimeout(500);
    }
    
    // Verify exactly 15 items are displayed
    const items = page.locator('[data-testid="list-item"], .list-item, li');
    const itemCount = await items.count();
    expect(itemCount).toBe(15);
    
    // Verify at least one item has status "active"
    const activeItems = page.locator('[data-testid*="status"], [class*="status"]').filter({ hasText: /active/i });
    const activeCount = await activeItems.count();
    expect(activeCount).toBeGreaterThanOrEqual(1);
    
    // Verify at least one item has status "pending"
    const pendingItems = page.locator('[data-testid*="status"], [class*="status"]').filter({ hasText: /pending/i });
    const pendingCount = await pendingItems.count();
    expect(pendingCount).toBeGreaterThanOrEqual(1);
  });
});
