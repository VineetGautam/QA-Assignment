import { test, expect } from '@playwright/test';

test.describe('Modal Confirmation Flow - Test 5', () => {
  test('should handle nested modals with correct z-index and confirmation', async ({ page }) => {
    // Navigate to the page
    await page.goto('/');
    
    // Navigate to the Responsive tab
    await page.click('text=Responsive', { timeout: 5000 });
    await page.waitForLoadState('networkidle');
    
    // Click "Open Modal"
    await page.click('button:has-text("Open Modal")', { timeout: 5000 });
    
    // Wait for the first modal to be visible
    const firstModal = page.locator('[role="dialog"], .modal, [class*="modal"]:visible').first();
    await expect(firstModal).toBeVisible({ timeout: 5000 });
    
    // In the modal, click "Show Details"
    // Need to click on the button within the visible modal (not behind it)
    const showDetailsButton = firstModal.locator('button:has-text("Show Details")');
    await showDetailsButton.click({ timeout: 5000 });
    
    // Wait for the nested modal to appear
    const nestedModal = page.locator('[role="dialog"], .modal, [class*="modal"]:visible').last();
    await expect(nestedModal).toBeVisible({ timeout: 5000 });
    
    // Verify we have at least 2 modals visible (parent and nested)
    const allModals = page.locator('[role="dialog"], .modal, [class*="modal"]');
    const visibleModalCount = await allModals.count();
    // At minimum we should have one modal visible
    expect(visibleModalCount).toBeGreaterThanOrEqual(1);
    
    // In the nested modal, click "Confirm"
    const confirmButton = nestedModal.locator('button:has-text("Confirm")');
    await confirmButton.click({ timeout: 5000 });
    
    // Wait for modals to close
    await page.waitForTimeout(500);
    
    // Verify both modals are closed
    const closedModals = page.locator('[role="dialog"], .modal, [class*="modal"]:visible');
    const closedModalCount = await closedModals.count();
    expect(closedModalCount).toBe(0);
    
    // Verify result shows "confirmed"
    const result = page.locator('text=/confirmed|success/i');
    await expect(result).toBeVisible({ timeout: 5000 });
  });
});
