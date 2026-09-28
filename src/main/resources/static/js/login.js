const loginForm = document.getElementById('loginForm');

// If the guard is already logged in, go straight to the dashboard
apiRequest('/api/auth/me')
    .then(function () { window.location.href = '/index.html'; })
    .catch(function () { /* not logged in: stay on this page */ });

loginForm.addEventListener('submit', async function (event) {
    event.preventDefault();
    clearMessage('message');

    const body = {
        username: document.getElementById('username').value.trim(),
        password: document.getElementById('password').value
    };

    try {
        await apiRequest('/api/auth/login', { method: 'POST', body: JSON.stringify(body) });
        window.location.href = '/index.html';
    } catch (error) {
        showMessage('message', error.message, 'error', error.details);
    }
});
