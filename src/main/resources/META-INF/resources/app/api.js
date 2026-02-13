/**
 * API client for TV Series backend
 * Handles all HTTP communication and RFC7808 Problem Details error handling
 */

const API_BASE = '/api/series';

class ApiError extends Error {
    constructor(problemDetail, response) {
        super(problemDetail.detail || problemDetail.title || 'An error occurred');
        this.name = 'ApiError';
        this.problemDetail = problemDetail;
        this.status = response.status;
    }

    getUserMessage() {
        const pd = this.problemDetail;
        
        // Validation errors with field details
        if (pd.errors && Object.keys(pd.errors).length > 0) {
            const fieldErrors = Object.entries(pd.errors)
                .map(([field, messages]) => `${field}: ${messages.join(', ')}`)
                .join('; ');
            return `Validation failed: ${fieldErrors}`;
        }

        // Standard error message
        return pd.detail || pd.title || this.message;
    }
}

async function handleResponse(response) {
    const contentType = response.headers.get('content-type') || '';

    // Handle RFC7808 Problem Details
    if (contentType.includes('application/problem+json')) {
        const problemDetail = await response.json();
        throw new ApiError(problemDetail, response);
    }

    // Success with content
    if (response.ok && contentType.includes('application/json')) {
        return await response.json();
    }

    // Success without content (e.g., 204 No Content)
    if (response.ok) {
        return null;
    }

    // Unexpected error without Problem Details
    throw new Error(`HTTP ${response.status}: ${response.statusText}`);
}

/**
 * Fetch all TV series with pagination
 * @param {number} page - Page number (0-indexed)
 * @param {number} size - Page size
 * @returns {Promise<{items: Array, page: number, size: number, total: number}>}
 */
export async function fetchAllSeries(page = 0, size = 20) {
    const response = await fetch(`${API_BASE}?page=${page}&size=${size}`);
    return handleResponse(response);
}

/**
 * Fetch single TV series by ID
 * @param {string} id - Series UUID
 * @returns {Promise<Object>}
 */
export async function fetchSeriesById(id) {
    const response = await fetch(`${API_BASE}/${id}`);
    return handleResponse(response);
}

/**
 * Create a new TV series
 * @param {Object} data - Series data {titolo, anno, genere, regista, sinossi}
 * @returns {Promise<Object>} Created series with id
 */
export async function createSeries(data) {
    const response = await fetch(API_BASE, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    });
    return handleResponse(response);
}

/**
 * Update an existing TV series
 * @param {string} id - Series UUID
 * @param {Object} data - Series data {titolo, anno, genere, regista, sinossi}
 * @returns {Promise<Object>} Updated series
 */
export async function updateSeries(id, data) {
    const response = await fetch(`${API_BASE}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    });
    return handleResponse(response);
}

/**
 * Delete a TV series
 * @param {string} id - Series UUID
 * @returns {Promise<null>}
 */
export async function deleteSeries(id) {
    const response = await fetch(`${API_BASE}/${id}`, {
        method: 'DELETE',
    });
    return handleResponse(response);
}

export { ApiError };
