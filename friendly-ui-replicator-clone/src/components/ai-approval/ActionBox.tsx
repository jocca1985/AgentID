import React from "react";

interface ActionBoxProps {
  actionText: string;
}

const ActionBox: React.FC<ActionBoxProps> = ({ actionText }) => {
  return (
    <div className="self-center w-[479px] max-w-full text-base text-[#20263D] font-medium leading-none mt-[30px]">
      <div className="w-full">
        <div className="bg-[rgba(242,248,255,1)] flex w-full flex-col items-stretch justify-center p-[35px] rounded-[18px] max-md:px-5">
          <div className="w-full max-w-[409px]">
            <div className="text-[#20263D] self-stretch flex-1 shrink basis-[0%] w-full gap-3">
              {actionText}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ActionBox;
