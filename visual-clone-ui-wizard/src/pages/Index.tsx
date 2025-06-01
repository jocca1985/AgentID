import React from 'react';
import { DashboardLayout } from '@/components/dashboard/DashboardLayout';
import { AccountBalance } from '@/components/dashboard/AccountBalance';
import { ActionButtons } from '@/components/dashboard/ActionButtons';
import { CurrencyCard } from '@/components/dashboard/CurrencyCard';
import { TransactionTable } from '@/components/dashboard/TransactionTable';

const currencies = [
  {
    currency: 'USD',
    balance: '89,178.25',
    icon: 'https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/3894dcb7388dd3e8ecc137e7bbf3c00773ba09f5?placeholderIfAbsent=true',
    exchangeRate: '1 SGD = 0.74 USD'
  },
  {
    currency: 'SGD',
    balance: '241,891.12',
    icon: 'https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/a584370d9aec3bf965833610727b10013723bc83?placeholderIfAbsent=true',
    isBase: true
  },
  {
    currency: 'GBP',
    balance: '60,919.90',
    icon: 'https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/656b4a61e1a498501e0100384d77e918b7dffb94?placeholderIfAbsent=true',
    exchangeRate: '1 SGD = 0.57 GBP'
  },
  {
    currency: 'EUR',
    balance: '24,120.00',
    icon: 'https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/009f004088234a3c3463075bcf72bec61e3e642a?placeholderIfAbsent=true',
    exchangeRate: '1 SGD = 0.68 EUR'
  }
];

const Index: React.FC = () => {
  return (
    <DashboardLayout>
      <div className="flex w-full flex-col max-md:max-w-full">
        <section className="main-section">
          <AccountBalance />
          <ActionButtons />
        </section>

        <section className="flex w-full items-center gap-6 flex-wrap mt-7 max-md:max-w-full">
          {currencies.map((currency, index) => (
            <CurrencyCard key={index} {...currency} />
          ))}
          <CurrencyCard
            currency=""
            balance=""
            icon="https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/a961fea3b6ed3d008e9e78007eec0270e402ebf0?placeholderIfAbsent=true"
            isAdd
          />
        </section>

        <TransactionTable />
      </div>
    </DashboardLayout>
  );
};

export default Index;