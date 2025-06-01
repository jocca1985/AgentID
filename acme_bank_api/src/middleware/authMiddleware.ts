import { Context, MiddlewareHandler, Next } from 'hono';
import { extractToken } from '../util/helper';
import { verifyToken } from '../util/jwt';
import { unauthorized } from '../util/errors';

/**
 * Authentication middleware
 * Validates JWT token in Authorization header
 */
export const authMiddleware: MiddlewareHandler = async (c: Context, next: Next) => {
    try {
        const token = extractToken(c.req.header('Authorization'));

        if (!token) {
            return c.json({ error: 'Authentication required' }, 401);
        }

        const validation = await verifyToken(token);

        if (!validation.valid) {
            return c.json({ error: 'Invalid token' }, 401);
        }

        // Add user info to request context
        c.set('user', validation.payload);

        await next();
    } catch (error) {
        return c.json({ error: 'Authentication failed' }, 401);
    }
};