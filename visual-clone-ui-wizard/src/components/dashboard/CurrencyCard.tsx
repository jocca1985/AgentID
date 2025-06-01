import React from 'react';

interface CurrencyCardProps {
  currency: string;
  balance: string;
  icon: string;
  exchangeRate?: string;
  isBase?: boolean;
  isAdd?: boolean;
}

export const CurrencyCard: React.FC<CurrencyCardProps> = ({
  currency,
  balance,
  icon,
  exchangeRate,
  isBase,
  isAdd
}) => {
  if (isAdd) {
    return (
      <div className="self-stretch border border-[color:var(--Neutral-400,#EBEBEB)] overflow-hidden w-60 bg-neutral-50 p-6 rounded-lg border-solid max-md:px-5">
        <div className="flex w-10 items-center gap-3">
          <div className="justify-center items-center border border-[color:var(--Neutral-600,#C9C9C9)] self-stretch flex min-h-10 w-10 gap-2.5 overflow-hidden h-10 my-auto px-2 rounded-[100px] border-dashed">
            <img src={icon} alt="" className="aspect-[1] object-contain w-6 self-stretch my-auto" />
          </div>
        </div>
        <div className="w-full text-xl text-black font-medium leading-7 flex-1 mt-4">
          <div className="w-full">
            <div className="self-stretch flex-1 shrink basis-[0%] w-full gap-2.5">
              Open a new <br />
              currency account
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="border border-[color:var(--Neutral-400,#EBEBEB)] self-stretch overflow-hidden w-60 bg-neutral-50 my-auto p-6 rounded-lg border-solid max-md:px-5">
      <div className="flex w-10 items-center gap-3">
        <img src={icon} alt="" className="aspect-[1] object-contain w-10 self-stretch my-auto" />
      </div>
      <div className="w-full mt-4">
        <div className="w-full whitespace-nowrap">
          <div className="self-stretch flex-1 shrink basis-[0%] w-full gap-2.5 text-xl text-black font-medium leading-[1.4]">
            {currency}
          </div>
          <div className="text-[#363636] self-stretch flex-1 shrink basis-[0%] w-full gap-2 text-2xl font-bold leading-none">
            {balance}
          </div>
        </div>
        {isBase ? (
          <div className="items-center border border-[color:var(--Primary-Light-200,#DEDDFF)] flex w-full gap-1 text-xs text-black font-medium leading-none bg-[#EBEAFF] mt-1 px-2 py-1 rounded-[100px] border-solid">
            <div className="self-stretch w-[76px] my-auto rounded-[0px_0px_0px_0px]">
              Base currency
            </div>
          </div>
        ) : exchangeRate && (
          <div className="items-center border border-[color:var(--Primary-Light-200,#DEDDFF)] flex w-full gap-1 text-xs text-black font-normal leading-none bg-[#EBEAFF] mt-1 px-2 py-1 rounded-[100px] border-solid">
            <img src="https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/52a67b67afc84792ff522de34202709fc88ceb23?placeholderIfAbsent=true" alt="" className="aspect-[1] object-contain w-4 self-stretch shrink-0 my-auto" />
            <div className="self-stretch w-[93px] my-auto rounded-[0px_0px_0px_0px]">
              {exchangeRate}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};