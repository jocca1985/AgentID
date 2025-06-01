
import React from 'react';
import { Avatar, AvatarImage, AvatarFallback } from "@/components/ui/avatar";
import { 
  Table, 
  TableHeader, 
  TableBody, 
  TableHead,
  TableRow, 
  TableCell 
} from "@/components/ui/table";
import { ArrowRight, ArrowDown } from "lucide-react";

interface Transaction {
  icon: string;
  name: string;
  avatar?: string;
  avatarText?: string;
  type: string;
  status: 'completed' | 'pending' | 'in progress';
  amount: string;
  isPositive?: boolean;
  additionalInfo?: string;
  useArrowDownIcon?: boolean;
}

const transactions: Transaction[] = [
  {
    icon: 'https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/d3202a771ceebc30f85b3cef1dcfd307cf75555f?placeholderIfAbsent=true',
    name: 'Roman Karachinsky',
    avatarText: 'RK',
    type: 'Outgoing',
    status: 'in progress',
    amount: '-USD 250.00'
  },
  {
    icon: 'https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/8fc596bf87b51b685aa0eaf89f939fb5d5653f88?placeholderIfAbsent=true',
    name: 'Payroll',
    avatar: 'https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/c7f9a591fd6f33b162eebffc676ca877032e49e9?placeholderIfAbsent=true',
    type: 'Incoming',
    status: 'completed',
    amount: '+USD 8,000.00',
    isPositive: true,
    useArrowDownIcon: true
  },
  {
    icon: 'https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/0fb8f50fa102c3f310dcfa7ff3f64cec1625d4cb?placeholderIfAbsent=true',
    name: 'Adobe Creative Cloud',
    avatar: 'https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/c66bf7fbb113e3f358ee7ca0de20f15f717c283b?placeholderIfAbsent=true',
    type: 'Outgoing | Card 1234',
    status: 'pending',
    amount: '-USD 90.00'
  },
  {
    icon: 'https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/0fb8f50fa102c3f310dcfa7ff3f64cec1625d4cb?placeholderIfAbsent=true',
    name: 'Adidas Originals',
    avatar: 'https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/1fa5077914d3968d4205c945e752bced967a087f?placeholderIfAbsent=true',
    type: 'Outgoing | Card 1234',
    status: 'pending',
    amount: '-USD 100.00'
  },
  {
    icon: 'https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/d3202a771ceebc30f85b3cef1dcfd307cf75555f?placeholderIfAbsent=true',
    name: 'Roman Karachinsky',
    avatarText: 'RK',
    type: 'Outgoing',
    status: 'completed',
    amount: '-USD 250.00'
  }
];

const StatusBadge: React.FC<{ status: Transaction['status'] }> = ({ status }) => {
  const styles = {
    completed: 'bg-[#9AEBBF] text-[#363636]',
    pending: 'bg-[#FFE6BF] text-[#363636]',
    'in progress': 'bg-[#FFE6BF] text-[#363636]'
  };

  return (
    <div className={`inline-flex items-center justify-center rounded px-2 py-1 text-xs font-medium ${styles[status]}`}>
      {status.charAt(0).toUpperCase() + status.slice(1)}
    </div>
  );
};

export const TransactionTable: React.FC = () => {
  return (
    <section className="border border-[color:var(--Neutral-400,#EBEBEB)] w-full overflow-hidden mt-7 p-6 rounded-lg border-solid max-md:max-w-full">
      <div className="flex w-full items-center gap-[40px_100px] justify-between flex-wrap max-md:max-w-full">
        <h2 className="text-black text-2xl font-semibold leading-none self-stretch my-auto">
          Recent transactions
        </h2>
        <button className="text-[#363636] self-stretch border border-[color:var(--Neutral-600,#C9C9C9)] gap-2 text-base font-medium text-center my-auto px-4 py-2 rounded-lg border-solid">
          View all
        </button>
      </div>

      <div className="w-full mt-4 max-md:max-w-full">
        <div className="text-black text-base font-semibold max-md:max-w-full">
          Today
        </div>
        
        <Table className="w-full mt-2">
          <TableBody>
            {transactions.map((transaction, index) => (
              <TableRow key={index} className="hover:bg-gray-50">
                <TableCell className="w-[118px] p-0">
                  <div className="flex w-full items-center justify-start p-4">
                    <div className="items-center border border-[color:var(--Neutral-400,#EBEBEB)] flex w-12 h-12 bg-neutral-50 p-3 rounded-full border-solid">
                      <img 
                        src={transaction.icon} 
                        alt="" 
                        className="w-6 h-6 object-contain max-w-full max-h-full" 
                      />
                    </div>
                  </div>
                </TableCell>
                
                <TableCell className="min-w-[240px]">
                  <div className="flex items-center gap-3">
                    <Avatar className="h-12 w-12 shrink-0">
                      {transaction.avatar ? (
                        <AvatarImage src={transaction.avatar} alt={transaction.name} />
                      ) : null}
                      <AvatarFallback className="bg-[#363636] text-white">
                        {transaction.avatarText}
                      </AvatarFallback>
                    </Avatar>
                    <div>
                      <div className="text-[#363636] text-xl font-medium leading-[1.4]">
                        {transaction.name}
                      </div>
                      {transaction.type && (
                        <div className="text-[#5E5E5E] text-sm font-normal leading-5 flex items-center gap-1">
                          {transaction.type}
                          {transaction.type.includes('Outgoing') && (
                            <ArrowRight className="h-4 w-4 text-[#5E5E5E] inline-block ml-0.5" />
                          )}
                          {transaction.useArrowDownIcon && (
                            <ArrowDown className="h-4 w-4 text-[#5E5E5E] inline-block ml-0.5" />
                          )}
                        </div>
                      )}
                    </div>
                  </div>
                </TableCell>
                
                <TableCell className="w-[120px]">
                  <StatusBadge status={transaction.status} />
                </TableCell>
                
                <TableCell className={`w-[160px] text-right text-xl font-medium ${transaction.isPositive ? 'text-[#3CB775]' : 'text-[#363636]'}`}>
                  {transaction.amount}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>
    </section>
  );
};
