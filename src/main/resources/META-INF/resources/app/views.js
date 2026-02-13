/**
 * View rendering functions for TV Series UI
 */

/**
 * Show an alert message
 * @param {string} message - Message text
 * @param {string} type - 'success' or 'error'
 */
export function showAlert(message, type = 'error') {
    const container = document.getElementById('alert-container');
    const alertDiv = document.createElement('div');
    alertDiv.className = type === 'success' ? 'success-message' : 'error-message';
    alertDiv.textContent = message;
    
    container.innerHTML = '';
    container.appendChild(alertDiv);
    
    // Auto-dismiss after 5 seconds
    setTimeout(() => {
        alertDiv.remove();
    }, 5000);
}

/**
 * Clear any existing alert
 */
export function clearAlert() {
    const container = document.getElementById('alert-container');
    container.innerHTML = '';
}

/**
 * Render the series list table
 * @param {Array} series - Array of TV series items
 */
export function renderSeriesList(series) {
    const container = document.getElementById('series-list');
    
    if (!series || series.length === 0) {
        container.innerHTML = '<div class="empty-state">No TV series found. Add one above!</div>';
        return;
    }

    const table = document.createElement('table');
    table.className = 'series-table';
    
    table.innerHTML = `
        <thead>
            <tr>
                <th>Title</th>
                <th>Year</th>
                <th>Genre</th>
                <th>Director</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            ${series.map(s => `
                <tr data-id="${s.id}">
                    <td><strong>${escapeHtml(s.titolo)}</strong></td>
                    <td>${s.anno}</td>
                    <td>${escapeHtml(s.genere)}</td>
                    <td>${escapeHtml(s.regista)}</td>
                    <td class="actions">
                        <button class="secondary btn-edit" data-id="${s.id}">Edit</button>
                        <button class="danger btn-delete" data-id="${s.id}">Delete</button>
                    </td>
                </tr>
            `).join('')}
        </tbody>
    `;
    
    container.innerHTML = '';
    container.appendChild(table);
}

/**
 * Show loading state
 */
export function showLoading() {
    const container = document.getElementById('series-list');
    container.innerHTML = '<div class="loading">Loading...</div>';
}

/**
 * Populate form with series data for editing
 * @param {Object} series - Series data
 */
export function populateForm(series) {
    document.getElementById('series-id').value = series.id;
    document.getElementById('titolo').value = series.titolo;
    document.getElementById('anno').value = series.anno;
    document.getElementById('genere').value = series.genere;
    document.getElementById('regista').value = series.regista;
    document.getElementById('sinossi').value = series.sinossi || '';
    
    // Update UI for edit mode
    document.getElementById('form-title').textContent = '✏️ Edit Series';
    document.getElementById('submit-btn').textContent = 'Update';
    document.getElementById('cancel-btn').style.display = 'inline-block';
    
    // Scroll to form
    document.querySelector('.form-section').scrollIntoView({ behavior: 'smooth' });
}

/**
 * Reset form to initial state
 */
export function resetForm() {
    const form = document.getElementById('series-form');
    form.reset();
    document.getElementById('series-id').value = '';
    
    // Reset UI to add mode
    document.getElementById('form-title').textContent = '➕ Add New Series';
    document.getElementById('submit-btn').textContent = 'Save';
    document.getElementById('cancel-btn').style.display = 'none';
}

/**
 * Get form data as object
 * @returns {Object} Form data
 */
export function getFormData() {
    return {
        titolo: document.getElementById('titolo').value.trim(),
        anno: parseInt(document.getElementById('anno').value, 10),
        genere: document.getElementById('genere').value.trim(),
        regista: document.getElementById('regista').value.trim(),
        sinossi: document.getElementById('sinossi').value.trim() || null,
    };
}

/**
 * Get current series ID from form (for edit mode)
 * @returns {string|null}
 */
export function getCurrentSeriesId() {
    const id = document.getElementById('series-id').value;
    return id || null;
}

/**
 * Escape HTML to prevent XSS
 * @param {string} text
 * @returns {string}
 */
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}
