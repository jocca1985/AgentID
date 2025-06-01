import { Context } from 'hono';

/**
 * Custom API error class
 */
export class ApiError extends Error {
    statusCode: number;

    constructor(message: string, statusCode: number) {
        super(message);
        this.name = 'ApiError';
        this.statusCode = statusCode;
    }
}

/**
 * Create an Unauthorized error
 */
export const unauthorized = (message: string = 'Authentication required'): ApiError => {
    return new ApiError(message, 401);
};

/**
 * Create a Bad Request error
 */
export const badRequest = (message: string = 'Invalid request'): ApiError => {
    return new ApiError(message, 400);
};

/**
 * Create a Not Found error
 */
export const notFound = (message: string = 'Resource not found'): ApiError => {
    return new ApiError(message, 404);
};

/**
 * Send error response in a consistent format
 * This helper uses direct number literals which are compatible with Hono's types
 */
export const sendErrorResponse = (c: Context, error: Error): Response => {
    console.error('API Error:', error);

    if (error instanceof ApiError) {
        // Using direct number literals instead of StatusCode type
        switch (error.statusCode) {
            case 400:
                return c.json({ error: error.message }, 400);
            case 401:
                return c.json({ error: error.message }, 401);
            case 403:
                return c.json({ error: error.message }, 403);
            case 404:
                return c.json({ error: error.message }, 404);
            case 409:
                return c.json({ error: error.message }, 409);
            case 422:
                return c.json({ error: error.message }, 422);
            default:
                return c.json({ error: error.message }, 500);
        }
    }

    // Generic error handling for non-ApiError types
    return c.json(
        { error: error.message || 'An unexpected error occurred' },
        500
    );
};