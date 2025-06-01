
import React from 'react';
import { Sidebar } from './Sidebar';
import { TopNav } from './TopNav';

interface DashboardLayoutProps {
  children: React.ReactNode;
}

export const DashboardLayout: React.FC<DashboardLayoutProps> = ({ children }) => {
  return (
    <div className="bg-white min-h-screen overflow-hidden">
      <div className="flex h-screen max-md:flex-col max-md:items-stretch">
        <div className="w-[16%] max-md:w-full max-md:ml-0">
          <Sidebar />
        </div>
        <div className="w-[84%] ml-5 max-md:w-full max-md:ml-0 overflow-auto h-screen">
          <div className="w-full max-md:max-w-full">
            <TopNav />
            <main className="w-full overflow-auto pt-8 px-[120px] pb-8 max-md:max-w-full max-md:px-5">
              {children}
            </main>
          </div>
        </div>
      </div>
    </div>
  );
};
