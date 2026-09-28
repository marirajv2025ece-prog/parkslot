async function loadSlots(url) {
    try {
        const slots = await apiRequest(url);
        const rows = slots.map(function (slot) {
            return '<tr><td><strong>' + escapeHtml(slot.slotNumber) + '</strong></td><td>' + statusBadge(slot.status) +
                '</td><td>' + escapeHtml(slot.vehicleNumber || '-') + '</td><td>' + escapeHtml(slot.flatNumber || '-') +
                '</td><td>' + formatDateTime(slot.entryTime) + '</td></tr>';
        }).join('');
        document.getElementById('slotsBody').innerHTML = rows || emptyRow(5, 'No slots found.');
    } catch (error) {
        showMessage('message', error.message, 'error');
    }
}

document.querySelectorAll('button[data-url]').forEach(function (button) {
    button.addEventListener('click', function () { loadSlots(button.dataset.url); });
});

initPage('slots').then(function () { return loadSlots('/api/slots'); }).catch(function () { });
