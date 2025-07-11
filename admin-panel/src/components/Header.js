import { Zap, Info } from "lucide-react";

const pageLabels = {
  sessions: "Sessions",
  "session-details": "Session Details", 
  "action-details": "Action Plan Details",
};

export default function Header({ currentView }) {
  return (
    <header className="bg-blue-600 shadow">
      <div
        className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex items-center justify-between py-4"
        style={{ color: "white" }}
      >
        <div className="flex items-center gap-2 min-w-0">
          <Info className="w-7 h-7" style={{ color: "white" }} />
          <span className="font-semibold text-lg tracking-wide" style={{ color: "white" }}>Incode</span>
        </div>
        <div className="flex-1 flex justify-center">
          <h1 className="text-2xl font-bold" style={{ color: "white" }}>AEE Admin Panel</h1>
        </div>
        <div className="flex items-center gap-2 min-w-0 justify-end">
          <Zap className="w-5 h-5" style={{ color: "white" }} />
          <span className="font-medium text-base" style={{ color: "white" }}>
            {pageLabels[currentView] || ""}
          </span>
        </div>
      </div>
    </header>
  );
} 