import React from "react";
import ActionBox from "./ActionBox";

interface ApprovalContentProps {
  title: string;
  description: string;
  actionText: string;
  timeLimit: string;
}

const ApprovalContent: React.FC<ApprovalContentProps> = ({
  title,
  description,
  actionText,
  timeLimit,
}) => {
  return (
    <div className="flex w-full flex-col items-stretch text-center justify-center mt-[42px] py-[30px] max-md:max-w-full max-md:mt-10">
      <div className="flex w-full flex-col items-stretch max-md:max-w-full">
        <div className="flex w-full flex-col items-stretch max-md:max-w-full">
          <div className="flex w-full flex-col items-stretch max-md:max-w-full">
            <h1 className="text-[#2B2B2B] text-4xl font-extrabold leading-none tracking-[-0.5px] max-md:max-w-full">
              {title}
            </h1>
            <p className="text-[rgba(96,102,124,1)] text-base font-medium leading-[21px] self-center w-[504px] mt-2 max-md:max-w-full">
              {description}
            </p>
          </div>
          <ActionBox actionText={actionText} />
        </div>
        <p className="text-[#20263d] text-base font-medium leading-[21px] self-center w-[452px] mt-[33px] max-md:max-w-full">
          If approved, the agent will have{" "}
          <span className="font-bold text-[rgba(0,122,255,1)]">{timeLimit} </span>
          , to complete this action, or until you manually revoke their access.
        </p>
      </div>
    </div>
  );
};

export default ApprovalContent;
