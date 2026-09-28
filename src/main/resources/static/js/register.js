const visitorForm = document.getElementById('visitorForm');
const flatSelect = document.getElementById('flatNumber');
const slotSelect = document.getElementById('slotNumber');

// Flats come from the backend (GET /api/flats)
async function loadFlats() {
    const flats = await apiRequest('/api/flats');
    flatSelect.innerHTML = '<option value="">-- Select flat --</option>' + flats.map(function (flat) {
        return '<option value="' + escapeHtml(flat.flatNumber) + '">' +
            escapeHtml(flat.flatNumber) + ' - ' + escapeHtml(flat.residentName) + '</option>';
    }).join('');
}

// Load only slots that are free right now.
async function loadSlots() {
    const url = '/api/slots/available';
    const slots = await apiRequest(url);

    let options = '<option value="">-- Select slot --</option>';
    options += slots.map(function (slot) {
        const note = slot.status === 'OCCUPIED' ? ' (Occupied)' : '';
        return '<option value="' + escapeHtml(slot.slotNumber) + '">' + escapeHtml(slot.slotNumber) + note + '</option>';
    }).join('');
    slotSelect.innerHTML = options;

    if (slots.length === 0) {
        showMessage('message', 'No parking slots are available right now.', 'error');
    }
}

visitorForm.addEventListener('submit', async function (event) {
    event.preventDefault();
    clearMessage('message');

    const body = {
        visitorName: document.getElementById('visitorName').value.trim(),
        mobileNumber: document.getElementById('mobileNumber').value.trim(),
        vehicleNumber: document.getElementById('vehicleNumber').value.replace(/\s+/g, '').toUpperCase(),
        vehicleType: document.getElementById('vehicleType').value,
        flatNumber: flatSelect.value,
        slotNumber: slotSelect.value,
        purpose: document.getElementById('purpose').value.trim()
    };

    try {
        const record = await apiRequest('/api/parking/entry', { method: 'POST', body: JSON.stringify(body) });
        showMessage('message', 'Vehicle ' + record.vehicleNumber + ' registered. Slot ' + record.slotNumber +
            ' is now OCCUPIED (entry time ' + formatTime(record.entryTime) + ').', 'success');
        visitorForm.reset();
    } catch (error) {
        showMessage('message', error.message, 'error', error.details);
    }
    await loadSlots();       // refresh the slot list from the backend
});


initPage('register')
    .then(function () { return Promise.all([loadFlats(), loadSlots()]); })
    .catch(function (error) { showMessage('message', error.message, 'error'); });
