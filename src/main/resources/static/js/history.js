const searchForm = document.getElementById('searchForm');

async function loadHistory() {
    clearMessage('message');
    const params = new URLSearchParams();
    const vehicle = document.getElementById('vehicleNumber').value.replace(/\s+/g, '').trim();
    const flat = document.getElementById('flatNumber').value.trim();
    const date = document.getElementById('date').value;

    if (vehicle) params.append('vehicleNumber', vehicle);
    if (flat) params.append('flatNumber', flat);
    if (date) params.append('date', date);

    try {
        const records = await apiRequest('/api/parking/history?' + params.toString());
        const rows = records.map(function (r) {
            return '<tr><td>' + escapeHtml(r.vehicleNumber) + '</td><td>' + escapeHtml(r.visitorName) +
                '</td><td>' + escapeHtml(r.flatNumber) + '</td><td>' + escapeHtml(r.slotNumber) +
                '</td><td>' + formatDateTime(r.entryTime) + '</td><td>' + formatDateTime(r.exitTime) +
                '</td><td>' + statusBadge(r.status) + '</td></tr>';
        }).join('');
        document.getElementById('historyBody').innerHTML = rows || emptyRow(7, 'No records found.');
    } catch (error) {
        showMessage('message', error.message, 'error');
    }
}

searchForm.addEventListener('submit', function (event) {
    event.preventDefault();
    loadHistory();
});

document.getElementById('resetBtn').addEventListener('click', function () {
    searchForm.reset();
    loadHistory();
});

initPage('history').then(loadHistory).catch(function () { });
