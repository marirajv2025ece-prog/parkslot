// Shared helper functions used by all pages

const NAV_LINKS = [
    { key: 'dashboard', text: 'Dashboard', href: '/index.html' },
    { key: 'register', text: 'New Visitor', href: '/register.html' },
    { key: 'slots', text: 'Parking Slots', href: '/slots.html' },
    { key: 'active', text: 'Vehicles Inside', href: '/active.html' },
    { key: 'report', text: "Today's Log", href: '/report.html' },
    { key: 'history', text: 'History', href: '/history.html' }
];

// Calls the backend. Returns the parsed JSON, or throws an Error with the server's message.
async function apiRequest(url, options) {
    const response = await fetch(url, Object.assign({
        headers: { 'Content-Type': 'application/json' }
    }, options));

    const text = await response.text();
    let data = null;
    if (text) {
        try { data = JSON.parse(text); } catch (e) { data = null; }
    }

    if (response.status === 401 && url !== '/api/auth/login') {
        // not logged in (or session expired) -> go to the login page (unless we are already there)
        if (window.location.pathname !== '/login.html') {
            window.location.href = '/login.html';
        }
        throw new Error('Please log in.');
    }
    if (!response.ok) {
        const error = new Error(data && data.message ? data.message : 'Request failed (' + response.status + ')');
        error.status = response.status;
        error.details = data && data.errors ? data.errors : null;
        throw error;
    }
    return data;
}

// Checks the login, then draws the navigation bar. Every page except login calls this first.
async function initPage(activeKey) {
    const user = await apiRequest('/api/auth/me');
    const links = NAV_LINKS.map(function (link) {
        const cls = link.key === activeKey ? ' class="active"' : '';
        return '<a href="' + link.href + '"' + cls + '>' + link.text + '</a>';
    }).join('');

    document.getElementById('navbar').innerHTML =
        '<div class="navbar">' +
        '<span class="brand">ParkSpot</span>' + links +
        '<span class="user-box">' + escapeHtml(user.fullName) +
        '<button class="secondary small" id="logoutBtn">Logout</button></span>' +
        '</div>';

    document.getElementById('logoutBtn').addEventListener('click', logout);
    return user;
}

async function logout() {
    try { await apiRequest('/api/auth/logout', { method: 'POST' }); } catch (e) { /* ignore */ }
    window.location.href = '/login.html';
}

function escapeHtml(value) {
    if (value === null || value === undefined) return '';
    return String(value)
        .replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;').replace(/'/g, '&#39;');
}

// "2026-09-28T09:15:30" -> parts
function splitIso(iso) {
    const pieces = iso.split('T');
    const date = pieces[0].split('-');
    const time = (pieces[1] || '00:00:00').split(':');
    return { year: date[0], month: date[1], day: date[2], hour: parseInt(time[0], 10), minute: time[1] };
}

function formatDate(iso) {
    if (!iso) return '-';
    const p = splitIso(iso);
    return p.day + '-' + p.month + '-' + p.year;
}

function formatTime(iso) {
    if (!iso) return '-';
    const p = splitIso(iso);
    const suffix = p.hour >= 12 ? 'PM' : 'AM';
    const hour12 = p.hour % 12 === 0 ? 12 : p.hour % 12;
    return String(hour12).padStart(2, '0') + ':' + p.minute + ' ' + suffix;
}

function formatDateTime(iso) {
    return iso ? formatDate(iso) + ' ' + formatTime(iso) : '-';
}

// today as YYYY-MM-DD (local time)
function todayString() {
    const d = new Date();
    return d.getFullYear() + '-' + String(d.getMonth() + 1).padStart(2, '0') + '-' + String(d.getDate()).padStart(2, '0');
}

function statusBadge(status) {
    return '<span class="badge badge-' + String(status).toLowerCase() + '">' + escapeHtml(status) + '</span>';
}

// Shows a success / error box. "details" is the field-error map sent by the backend (optional).
function showMessage(elementId, text, type, details) {
    const box = document.getElementById(elementId);
    let html = escapeHtml(text);
    if (details) {
        html += '<ul>' + Object.keys(details).map(function (field) {
            return '<li>' + escapeHtml(details[field]) + '</li>';
        }).join('') + '</ul>';
    }
    box.className = 'message ' + type;
    box.innerHTML = html;
}

function clearMessage(elementId) {
    const box = document.getElementById(elementId);
    box.className = 'message';
    box.innerHTML = '';
}

function emptyRow(columns, text) {
    return '<tr><td colspan="' + columns + '" class="empty">' + text + '</td></tr>';
}
