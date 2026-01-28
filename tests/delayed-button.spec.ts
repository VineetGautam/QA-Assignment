import { test, expect } from '@playwright/test';

test.describe('Delayed Button Flow - Test 1', () => {
  test('should handle delayed button activation without hardcoded waits', async ({ page }) => {
    // Navigate to the Timing Challenges tab
    await page.goto('/');
    await page.click('text=Timing Challenges', { timeout: 5000 });
    
    // Wait for the content to load
    await page.waitForLoadState('networkidle');
    
    // Click "Start Process"
    await page.click('button:has-text("Start Process")', { timeout: 5000 });
    
    // Wait for the "Confirm Action" button to become enabled (not just visible)
    // Using waitForFunction to ensure button is enabled, not just present
    await page.waitForFunction(() => {
      const button = document.querySelector('button:has-text("Confirm Action")') as HTMLButtonElement;
      return button && !button.disabled;
    }, { timeout: 5000 });
    
    // Click the confirm button
    await page.click('button:has-text("Confirm Action")', { timeout: 5000 });
    
    // Verify the success message appears
    const successMessage = page.locator('text=/success|completed|confirmed/i');
    await expect(successMessage).toBeVisible({ timeout: 5000 });
  });
});
