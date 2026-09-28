const reportDate = document.getElementById('reportDate');

async function loadReport() {
    clearMessage('message');
    if (!reportDate.value) {
        showMessage('message', 'Please select a date.', 'error');
        return;
    }
    try {
        const report = await apiRequest('/api/parking/report/daily?date=' + reportDate.value);

        document.getElementById('reportTitle').textContent = 'DAILY VISITOR LOG - Date: ' + formatDate(report.date + 'T00:00:00');
        document.getElementById('totalVisitors').textContent = report.totalVisitors;
        document.getElementById('completedVisits').textContent = report.completedVisits;
        document.getElementById('currentlyInside').textContent = report.currentlyInside;

        const rows = report.records.map(function (r) {
            return '<tr><td>' + escapeHtml(r.vehicleNumber) + '</td><td>' + escapeHtml(r.visitorName) +
                '</td><td>' + escapeHtml(r.flatNumber) + '</td><td>' + escapeHtml(r.slotNumber) +
                '</td><td>' + formatTime(r.entryTime) + '</td><td>' + formatTime(r.exitTime) +
                '</td><td>' + statusBadge(r.status) + '</td></tr>';
        }).join('');
        document.getElementById('reportBody').innerHTML = rows || emptyRow(7, 'No visitors on this date.');
    } catch (error) {
        showMessage('message', error.message, 'error');
    }
}

document.getElementById('showReportBtn').addEventListener('click', loadReport);
reportDate.addEventListener('change', loadReport);

initPage('report').then(function () {
    reportDate.value = todayString();
    return loadReport();
}).catch(function () { });
