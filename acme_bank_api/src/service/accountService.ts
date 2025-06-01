import AccountModel, { Account } from '../model/Account';
import { badRequest } from '../util/errors';

/**
 * Account Service
 * Handles business logic for account operations
 */
class AccountService {
    private accountModel: AccountModel;

    constructor() {
        this.accountModel = AccountModel.getInstance();
    }

    /**
     * Get account balance
     */
    public getBalance(): Account {
        return this.accountModel.getAccount();
    }

    /**
     * Deposit money into account
     */
    public deposit(amount: number): { amount: number; newBalance: number } {
        // Validate amount
        if (isNaN(amount) || amount <= 0) {
            throw badRequest('Invalid amount');
        }

        const account = this.accountModel.getAccount();
        const newBalance = account.balance + amount;

        this.accountModel.updateBalance(newBalance);

        return {
            amount,
            newBalance
        };
    }

    /**
     * Withdraw money from account
     */
    public withdraw(amount: number): { amount: number; newBalance: number } {
        // Validate amount
        if (isNaN(amount) || amount <= 0) {
            throw badRequest('Invalid amount');
        }

        const account = this.accountModel.getAccount();

        // Check if account has sufficient funds
        if (amount > account.balance) {
            throw badRequest('Insufficient funds');
        }

        const newBalance = account.balance - amount;

        this.accountModel.updateBalance(newBalance);

        return {
            amount,
            newBalance
        };
    }
}

// Singleton instance
const accountService = new AccountService();

export default accountService;