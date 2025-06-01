import React from "react";
import { Button } from "@/components/ui/button";

interface ApprovalButtonsProps {
  onApprove: () => void;
  onDeny: () => void;
}

const ApprovalButtons: React.FC<ApprovalButtonsProps> = ({ onApprove, onDeny }) => {
  return (
    <div className="flex justify-center gap-4 mt-8">
      <Button
        onClick={onDeny}
        variant="outline"
        className="px-8 py-2 border border-[#E9E9EB] text-[#2B2B2B] font-medium rounded-lg hover:bg-gray-100"
      >
        Deny
      </Button>
      <Button
        onClick={onApprove}
        className="px-8 py-2 bg-[rgba(0,122,255,1)] text-white font-medium rounded-lg hover:bg-blue-600"
      >
        Approve
      </Button>
    </div>
  );
};

export default ApprovalButtons;
