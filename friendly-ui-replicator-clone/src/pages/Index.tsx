
import React from "react";
import ApprovalDialog from "@/components/ai-approval/ApprovalDialog";

const Index: React.FC = () => {
  return (
    <div className="min-h-screen bg-gray-50 flex flex-col items-center justify-center p-4">
      <ApprovalDialog />
      <footer className="mt-8 text-center text-gray-500 text-sm">
        <p>© 2023 Multiwallet. All rights reserved.</p>
      </footer>
    </div>
  );
};

export default Index;
