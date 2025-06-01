import React, { useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import ApprovalHeader from "./ApprovalHeader";
import ApprovalContent from "./ApprovalContent";
import ApprovalFooter from "./ApprovalFooter";
import ApprovalButtons from "./ApprovalButtons";
import { toast } from "sonner";
import { Loader2, XCircle } from "lucide-react";

interface ApprovalDialogProps {
  title?: string;
  description?: string;
  actionText?: string;
  timeLimit?: string;
}

const ApprovalDialog: React.FC<ApprovalDialogProps> = ({
  title = "Approve AI agent access",
  description = "An AI agent requested access to Multiwallet services on your behalf to perform an action. If you approve, they will be able to:",
  actionText = "Do you approve transaction?",
  timeLimit = "15 min",
}) => {
  const [isProcessing, setIsProcessing] = useState(false);
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  const token = searchParams.get("token");
  const error = searchParams.get("error");

  const handleApprove = () => {
    setIsProcessing(true);
    setTimeout(() => {
      setIsProcessing(false);
      navigate('/access-granted');
    }, 1000);
  };

  const handleDeny = () => {
    setIsProcessing(true);
    setTimeout(() => {
      toast.info("Access request denied");
      setIsProcessing(false);
    }, 1000);
  };

  const renderContent = () => {
    if (token) {
      return (
        <>
          <ApprovalContent
            title={title}
            description={description}
            actionText={actionText}
            timeLimit={timeLimit}
          />
          <ApprovalButtons onApprove={handleApprove} onDeny={handleDeny} />
          <ApprovalFooter />
        </>
      );
    }
    if (error) {
      return (
        <div className="flex flex-col items-center justify-center text-center py-10 px-6 min-h-[300px]">
          <XCircle className="w-16 h-16 text-red-500 mb-4" />
          <p className="text-xl font-medium text-slate-700">Verification failed.</p>
        </div>
      );
    }
    return (
      <div className="flex flex-col items-center justify-center text-center py-10 px-6 min-h-[300px]">
        <Loader2 className="w-16 h-16 text-blue-500 animate-spin mb-4" />
        <p className="text-xl font-medium text-slate-700">Waiting for verification...</p>
      </div>
    );
  };

  return (
    <div className="border border-[color:var(--Background-Light-Gray,#E9E9EB)] max-w-[800px] w-full overflow-hidden bg-white rounded-lg border-solid">
      <ApprovalHeader />
      {renderContent()}
    </div>
  );
};

export default ApprovalDialog;
