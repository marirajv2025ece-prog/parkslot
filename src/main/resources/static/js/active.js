const activeBody = document.getElementById('activeBody');

async function loadActive() {
    try {
        const records = await apiRequest('/api/parking/active');
        const rows = records.map(function (r) {
            return '<tr><td>' + escapeHtml(r.vehicleNumber) + '</td><td>' + escapeHtml(r.visitorName) +
                '</td><td>' + escapeHtml(r.flatNumber) + '</td><td>' + escapeHtml(r.slotNumber) +
                '</td><td>' + formatDateTime(r.entryTime) + '</td><td>' + statusBadge(r.status) +
                '</td><td><button class="danger small" data-id="' + r.id + '">Exit</button></td></tr>';
        }).join('');
        activeBody.innerHTML = rows || emptyRow(7, 'No vehicles are inside right now.');
    } catch (error) {
        showMessage('message', error.message, 'error');
    }
}

// One listener for all Exit buttons (event delegation)
activeBody.addEventListener('click', async function (event) {
    const button = event.target.closest('button[data-id]');
    if (!button) return;
    if (!confirm('Record exit for this vehicle?')) return;

    clearMessage('message');
    try {
        const record = await apiRequest('/api/parking/exit/' + button.dataset.id, { method: 'POST' });
        showMessage('message', 'Vehicle ' + record.vehicleNumber + ' exited at ' + formatTime(record.exitTime) +
            '. Slot ' + record.slotNumber + ' is now AVAILABLE.', 'success');
    } catch (error) {
        showMessage('message', error.message, 'error');
    }
    await loadActive();      // refresh the table
});

initPage('active').then(loadActive).catch(function () { });
