export interface Account {
    balance: number;
}

class AccountModel {
    private static instance: AccountModel;
    private readonly account: Account;

    private constructor() {
        // Initialize with a starting balance of $150,000
        this.account = {
            balance: 150000
        };
    }

    public static getInstance(): AccountModel {
        if (!AccountModel.instance) {
            AccountModel.instance = new AccountModel();
        }
        return AccountModel.instance;
    }

    public getAccount(): Account {
        return { ...this.account };
    }

    public updateBalance(newBalance: number): void {
        this.account.balance = newBalance;
    }
}

export default AccountModel;