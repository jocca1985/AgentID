import React, { useState, useEffect } from 'react';

export const AccountBalance: React.FC = () => {
  const [balance, setBalance] = useState<string | null>(null);
  const [lastUpdated, setLastUpdated] = useState<string | null>(null);

  const fetchBalance = async () => {
    try {
      const response = await fetch('https://acme-web-incode-demo.ngrok.app/api/balance');
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const data = await response.json();
      // API returns a number, convert to string for consistent state type
      setBalance(String(data.balance)); 
      setLastUpdated(new Date().toLocaleTimeString());
    } catch (error) {
      console.error("Failed to fetch balance:", error);
      setBalance("Error loading balance");
    }
  };

  useEffect(() => {
    fetchBalance();
    const intervalId = setInterval(fetchBalance, 2000);
    return () => clearInterval(intervalId);
  }, []);

  const formatBalanceDisplay = (value: string | null | number): string => {
    if (value === null || value === undefined) {
      return 'Loading...';
    }
    if (value === "Error loading balance") {
      return value;
    }
    
    let valueAsString: string;
    if (typeof value === 'number') {
      valueAsString = String(value);
    } else {
      valueAsString = value;
    }

    try {
      // Remove existing commas before parsing - valueAsString is definitely a string here
      const numericValue = parseFloat(valueAsString.replace(/,/g, ''));
      if (isNaN(numericValue)) {
        console.warn("Balance format error, could not parse to number:", valueAsString);
        return "Invalid balance"; 
      }
      return `$` + numericValue.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    } catch (e) {
      console.error("Error formatting balance:", e, "Original value:", value);
      return "Error displaying balance";
    }
  };

  return (
    <section className="bg-[rgba(154,235,191,0.43)] border inline-block items-stretch justify-center p-[30px] rounded-[13px] border-[rgba(81,209,137,1)] border-solid max-md:px-5">
      <div className="flex flex-col">
        <div className="self-stretch gap-1 text-base text-black font-medium">
          Total account balance
        </div>
        <div className="flex items-center gap-[22px] text-[50px] text-black font-bold whitespace-nowrap leading-none mt-1 max-md:text-[40px]">
          <div className="self-stretch gap-3.5 my-auto max-md:text-[40px]">
            {formatBalanceDisplay(balance)}
          </div>
        </div>
        <div className="text-[#919191] self-stretch gap-2.5 text-sm font-normal leading-none mt-1">
          {lastUpdated ? `Your total balance in USD at ${lastUpdated}` : 'Fetching latest balance...'}
        </div>
      </div>
    </section>
  );
};
