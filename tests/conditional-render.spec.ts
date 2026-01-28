import { test, expect } from '@playwright/test';

test.describe('Conditional Login Flow - Test 4', () => {
  test('should handle different login flows with conditional rendering', async ({ page }) => {
    // Navigate to the page
    await page.goto('/');
    
    // Navigate to the Flaky Selectors tab
    await page.click('text=Flaky Selectors', { timeout: 5000 });
    await page.waitForLoadState('networkidle');
    
    // === Admin User Login Flow ===
    // Click "Admin User" login button
    await page.click('button:has-text("Admin User")', { timeout: 5000 });
    
    // Wait for dashboard to load and loading state to complete
    await page.waitForLoadState('networkidle');
    // Wait for any loading spinners to disappear
    await page.waitForSelector('text=Admin Panel, [data-testid="admin-panel"], .admin-panel', {
      state: 'visible',
      timeout: 5000,
    }).catch(() => {
      // If not found with standard selectors, the test may need adjustment
      // but we'll proceed with visibility check
    });
    
    // Verify the Admin Panel is visible
    const adminPanel = page.locator('text=/Admin Panel|admin-panel/i').first();
    await expect(adminPanel).toBeVisible({ timeout: 5000 });
    
    // Verify the Standard Panel is NOT visible
    const standardPanel = page.locator('text=/Standard Panel|standard-panel/i').first();
    const isStandardPanelVisible = await standardPanel.isVisible().catch(() => false);
    expect(isStandardPanelVisible).toBeFalsy();
    
    // Click Logout
    await page.click('button:has-text("Logout"), button:has-text("Log out")', { timeout: 5000 });
    
    // Wait for logout to complete
    await page.waitForLoadState('networkidle');
    
    // === Standard User Login Flow ===
    // Click "Standard User" login button
    await page.click('button:has-text("Standard User")', { timeout: 5000 });
    
    // Wait for dashboard to load
    await page.waitForLoadState('networkidle');
    
    // Verify the Standard Panel is visible
    const standardPanelAfterLogin = page.locator('text=/Standard Panel|standard-panel/i').first();
    await expect(standardPanelAfterLogin).toBeVisible({ timeout: 5000 });
    
    // Verify the Admin Panel is NOT visible
    const adminPanelAfterLogout = page.locator('text=/Admin Panel|admin-panel/i').first();
    const isAdminPanelVisible = await adminPanelAfterLogout.isVisible().catch(() => false);
    expect(isAdminPanelVisible).toBeFalsy();
  });
});
