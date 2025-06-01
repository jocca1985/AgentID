import { verifyToken } from '../util/jwt';

/**
 * Auth Service
 * Handles authentication-related business logic
 */
class AuthService {
    /**
     * Validate a JWT token
     */
    public async validateToken(token: string): Promise<boolean> {
        if (!token) return false;

        const validation = await verifyToken(token);
        return validation.valid;
    }
}

// Singleton instance
const authService = new AuthService();

export default authService;