/**
 * Main application entry point
 * Coordinates API calls and view updates
 */

import * as api from './api.js';
import * as views from './views.js';

// Application state
let currentPage = 0;
const pageSize = 20;

/**
 * Load and display all series
 */
async function loadSeries() {
    try {
        views.showLoading();
        views.clearAlert();
        
        const response = await api.fetchAllSeries(currentPage, pageSize);
        views.renderSeriesList(response.items);
        
        // Attach event listeners to action buttons
        attachActionListeners();
    } catch (error) {
        console.error('Failed to load series:', error);
        if (error instanceof api.ApiError) {
            views.showAlert(error.getUserMessage(), 'error');
        } else {
            views.showAlert('Failed to load TV series. Please try again.', 'error');
        }
        document.getElementById('series-list').innerHTML = '';
    }
}

/**
 * Handle form submission (create or update)
 */
async function handleFormSubmit(event) {
    event.preventDefault();
    
    const submitBtn = document.getElementById('submit-btn');
    submitBtn.disabled = true;
    views.clearAlert();
    
    try {
        const formData = views.getFormData();
        const seriesId = views.getCurrentSeriesId();
        
        if (seriesId) {
            // Update existing series
            await api.updateSeries(seriesId, formData);
            views.showAlert(`Series "${formData.titolo}" updated successfully!`, 'success');
        } else {
            // Create new series
            await api.createSeries(formData);
            views.showAlert(`Series "${formData.titolo}" created successfully!`, 'success');
        }
        
        views.resetForm();
        await loadSeries();
    } catch (error) {
        console.error('Form submission error:', error);
        if (error instanceof api.ApiError) {
            views.showAlert(error.getUserMessage(), 'error');
        } else {
            views.showAlert('Failed to save series. Please try again.', 'error');
        }
    } finally {
        submitBtn.disabled = false;
    }
}

/**
 * Handle edit button click
 */
async function handleEdit(seriesId) {
    views.clearAlert();
    
    try {
        const series = await api.fetchSeriesById(seriesId);
        views.populateForm(series);
    } catch (error) {
        console.error('Failed to fetch series for edit:', error);
        if (error instanceof api.ApiError) {
            views.showAlert(error.getUserMessage(), 'error');
        } else {
            views.showAlert('Failed to load series details.', 'error');
        }
    }
}

/**
 * Handle delete button click
 */
async function handleDelete(seriesId, seriesTitle) {
    if (!confirm(`Are you sure you want to delete "${seriesTitle}"?`)) {
        return;
    }
    
    views.clearAlert();
    
    try {
        await api.deleteSeries(seriesId);
        views.showAlert(`Series "${seriesTitle}" deleted successfully!`, 'success');
        await loadSeries();
    } catch (error) {
        console.error('Failed to delete series:', error);
        if (error instanceof api.ApiError) {
            views.showAlert(error.getUserMessage(), 'error');
        } else {
            views.showAlert('Failed to delete series. Please try again.', 'error');
        }
    }
}

/**
 * Handle cancel button click
 */
function handleCancel() {
    views.resetForm();
    views.clearAlert();
}

/**
 * Attach event listeners to edit/delete buttons
 */
function attachActionListeners() {
    // Edit buttons
    document.querySelectorAll('.btn-edit').forEach(btn => {
        btn.addEventListener('click', () => {
            const seriesId = btn.dataset.id;
            handleEdit(seriesId);
        });
    });
    
    // Delete buttons
    document.querySelectorAll('.btn-delete').forEach(btn => {
        btn.addEventListener('click', () => {
            const seriesId = btn.dataset.id;
            const row = btn.closest('tr');
            const seriesTitle = row.querySelector('td strong').textContent;
            handleDelete(seriesId, seriesTitle);
        });
    });
}

/**
 * Initialize the application
 */
function init() {
    // Form submission
    document.getElementById('series-form').addEventListener('submit', handleFormSubmit);
    
    // Cancel button
    document.getElementById('cancel-btn').addEventListener('click', handleCancel);
    
    // Load initial data
    loadSeries();
}

// Start the app when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
} else {
    init();
}
