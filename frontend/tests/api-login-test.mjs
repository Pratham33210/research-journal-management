import axios from 'axios';

const API = process.env.API_URL || 'http://localhost:8081/api';

async function run() {
  try {
    const ts = Date.now();
    const email = `api_test_${ts}@local.test`;
    const password = 'TestPass123!';

    console.log('Registering', email);
    const reg = await axios.post(`${API}/auth/register`, {
      email,
      password,
      confirmPassword: password,
      firstName: 'API',
      lastName: 'Tester',
      affiliation: 'Local',
      role: 'AUTHOR'
    }).catch(e => e.response ? e.response.data : e.message);

    console.log('Register response:', typeof reg === 'object' ? (reg.status || 'ok') : reg);

    console.log('Logging in', email);
    const loginResp = await axios.post(`${API}/auth/login`, { email, password });
    console.log('Login response status:', loginResp.status);
    console.log('Token length:', (loginResp.data?.token || '').length);
    console.log('User:', loginResp.data?.user?.email || JSON.stringify(loginResp.data?.user));

    process.exit(0);
  } catch (err) {
    console.error('API login test failed:', err.response?.data || err.message || err);
    process.exit(2);
  }
}

run();
