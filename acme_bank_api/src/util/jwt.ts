import jwt from 'jsonwebtoken';

// This would normally be stored in environment variables
const JWT_SECRET = 'your_jwt_secret_key_56480001234567890987654321';

export interface TokenPayload {
    sub: string;
    iat: number;
    type: string;
}

export interface VerificationResult {
    valid: boolean;
    payload?: TokenPayload;
    error?: string;
}

/**
 * Verify JWT token
 */
export const verifyToken = async (token: string): Promise<VerificationResult> => {
    try {
        const payload = jwt.verify(token, JWT_SECRET) as TokenPayload;
        return { valid: true, payload };
    } catch (error) {
        const errorMessage = error instanceof Error ? error.message : 'Unknown error';
        return { valid: false, error: errorMessage };
    }
};