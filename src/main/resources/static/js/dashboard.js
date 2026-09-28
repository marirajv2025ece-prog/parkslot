async function loadDashboard() {
    try {
        const data = await apiRequest('/api/dashboard');

        document.getElementById('totalSlots').textContent = data.totalSlots;
        document.getElementById('availableSlots').textContent = data.availableSlots;
        document.getElementById('occupiedSlots').textContent = data.occupiedSlots;
        document.getElementById('vehiclesInside').textContent = data.vehiclesInside;

        document.getElementById('slotGrid').innerHTML = data.slots.map(function (slot) {
            const css = slot.status === 'OCCUPIED' ? 'occupied' : 'available';
            return '<div class="slot ' + css + '">' +
                '<div class="slot-no">' + escapeHtml(slot.slotNumber) + '</div>' +
                '<div class="slot-status">' + escapeHtml(slot.status) + '</div>' +
                '<div class="slot-vehicle">' + escapeHtml(slot.vehicleNumber || '') + '</div>' +
                '</div>';
        }).join('');

        const rows = data.recentVisitors.map(function (r) {
            return '<tr><td>' + escapeHtml(r.vehicleNumber) + '</td><td>' + escapeHtml(r.flatNumber) +
                '</td><td>' + escapeHtml(r.slotNumber) + '</td><td>' + formatDateTime(r.entryTime) +
                '</td><td>' + statusBadge(r.status) + '</td></tr>';
        }).join('');
        document.getElementById('recentBody').innerHTML = rows || emptyRow(5, 'No visitors recorded yet.');
    } catch (error) {
        showMessage('message', error.message, 'error');
    }
}

initPage('dashboard').then(loadDashboard).catch(function () { /* redirected to login */ });
