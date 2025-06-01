import { Context } from 'hono';
import accountService from '../service/accountService';
import {ApiError, sendErrorResponse} from '../util/errors';

/**
 * Get account balance
 */
export const getBalance = async (c: Context) => {
    try {
        const account = accountService.getBalance();
        return c.json({ balance: account.balance });
    } catch (error) {
        console.error('Error getting balance:', error);

        if (error instanceof ApiError) {
            return sendErrorResponse(c, error);
        }

        return c.json({ error: 'Failed to get balance' }, 500);
    }
};

/**
 * Deposit money into account
 */
export const deposit = async (c: Context) => {
    try {
        const { amount } = await c.req.json<{ amount: number }>();
        const result = accountService.deposit(amount);

        return c.json({
            success: true,
            amount: result.amount,
            newBalance: result.newBalance
        });
    } catch (error) {
        console.error('Error making deposit:', error);

        if (error instanceof ApiError) {
            return sendErrorResponse(c, error);
        }

        return c.json({ error: 'Failed to process deposit' }, 500);
    }
};

/**
 * Withdraw money from account
 */
export const withdraw = async (c: Context) => {
    try {
        const { amount } = await c.req.json<{ amount: number }>();
        const result = accountService.withdraw(amount);

        return c.json({
            success: true,
            amount: result.amount,
            newBalance: result.newBalance
        });
    } catch (error) {
        console.error('Error making withdrawal:', error);

        if (error instanceof ApiError) {
            return sendErrorResponse(c, error);
        }

        return c.json({ error: 'Failed to process withdrawal' }, 500);
    }
};