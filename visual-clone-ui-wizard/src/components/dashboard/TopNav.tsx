import React from 'react';

export const TopNav: React.FC = () => {
  return (
    <header className="items-center border-b-[color:var(--Neutral-400,#EBEBEB)] flex w-full gap-6 flex-wrap px-[120px] py-3 border-b border-solid max-md:max-w-full max-md:px-5">
      <div className="self-stretch flex min-w-60 flex-col text-lg text-black font-medium text-center leading-none flex-1 shrink basis-[0%] my-auto max-md:max-w-full">
        <div className="items-center border border-[color:var(--Neutral-400,#EBEBEB)] flex gap-2 bg-neutral-100 px-4 py-3 rounded-lg border-solid">
          <div className="self-stretch my-auto">Personal account</div>
          <img src="https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/29991d0ed3b588a5247ea454c565daac0c766065?placeholderIfAbsent=true" alt="" className="aspect-[1] object-contain w-6 self-stretch shrink-0 my-auto" />
        </div>
      </div>
      
      <div className="self-stretch flex items-center gap-6 my-auto">
        <button className="justify-center items-center border-[color:var(--Neutral-600,#C9C9C9)] self-stretch flex min-h-12 gap-2 w-12 h-12 my-auto px-3 rounded-lg border-0 border-solid">
          <img src="https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/37cf3a797fa2e90022251108eebde06df6e39502?placeholderIfAbsent=true" alt="Notifications" className="aspect-[1] object-contain w-5 self-stretch my-auto" />
        </button>
        
        <div className="self-stretch flex flex-col text-base font-medium text-center w-[163px] my-auto">
          <div className="flex w-full max-w-[163px] items-center gap-3 pl-2 py-2 rounded-lg">
            <div className="self-stretch flex items-center gap-2 my-auto">
              <div className="text-neutral-50 self-stretch min-h-10 gap-2.5 whitespace-nowrap w-10 h-10 my-auto pl-2.5 pr-[9px] rounded-[100px]">
                NS
              </div>
              <div className="text-black self-stretch my-auto">Nacim Shehin</div>
            </div>
            <img src="https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/28d9ccc027d99eea71b0c06383fc9ddde3f5e080?placeholderIfAbsent=true" alt="" className="aspect-[1] object-contain w-5 self-stretch shrink-0 my-auto" />
          </div>
        </div>
      </div>
    </header>
  );
};