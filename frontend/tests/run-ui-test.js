import { chromium } from 'playwright';

(async () => {
  const browser = await chromium.launch({ headless: true });
  const page = await browser.newPage();
  const base = process.env.FRONTEND_URL || 'http://localhost:3000';

  try {
    console.log('Opening register page...');
    await page.goto(`${base}/register`, { waitUntil: 'networkidle' });

    const ts = Date.now();
    const email = `test_ui_${ts}@local.test`;
    const password = 'TestPass123!';

    // Fill registration form
    await page.fill('input[name="firstName"]', 'UI');
    await page.fill('input[name="lastName"]', 'Tester');
    await page.fill('input[name="email"]', email);
    await page.fill('input[name="affiliation"]', 'Local University');
    await page.selectOption('select[name="role"]', 'AUTHOR');
    await page.fill('input[name="password"]', password);
    await page.fill('input[name="confirmPassword"]', password);

    console.log('Submitting registration for', email);
    await Promise.all([
      page.click('button[type="submit"]'),
      page.waitForNavigation({ waitUntil: 'networkidle', timeout: 5000 }).catch(() => {})
    ]);

    // Wait briefly for client-side storage update
    await page.waitForTimeout(1000);

    const token = await page.evaluate(() => localStorage.getItem('authToken'));
    if (!token) throw new Error('No authToken after registration');
    console.log('Registration OK, token length:', token.length);

    // Clear token to test login
    await page.evaluate(() => localStorage.removeItem('authToken'));

    console.log('Opening login page...');
    await page.goto(`${base}/login`, { waitUntil: 'networkidle' });

    await page.fill('input#email', email);
    await page.fill('input#password', password);

    console.log('Submitting login for', email);
    await Promise.all([
      page.click('button[type="submit"]'),
      page.waitForNavigation({ waitUntil: 'networkidle', timeout: 5000 }).catch(() => {})
    ]);

    await page.waitForTimeout(1000);
    const token2 = await page.evaluate(() => localStorage.getItem('authToken'));
    if (!token2) throw new Error('No authToken after login');
    console.log('Login OK, token length:', token2.length);

    const finalUrl = page.url();
    console.log('Final URL after login:', finalUrl);

    console.log('UI test passed');
    await browser.close();
    process.exit(0);
  } catch (err) {
    console.error('UI test failed:', err);
    await browser.close();
    process.exit(2);
  }
})();
