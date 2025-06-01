
import React from 'react';

const navItems = [
  { icon: 'https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/98c2f8a918a28e9a7ca4cecc23f3d1113e4989d2?placeholderIfAbsent=true', label: 'Home', active: true },
  { icon: 'https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/54d434ec438e99a84d7b31a830c84303e00efce5?placeholderIfAbsent=true', label: 'Pay or transfer' },
  { icon: 'https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/d8ee414b7e9d8d2c450993884b3edc4e7ef00290?placeholderIfAbsent=true', label: 'Top up' },
  { icon: 'https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/579c15d6361e39106e5c93a0a55a9b66269a8f20?placeholderIfAbsent=true', label: 'Accounts' },
  { icon: 'https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/669773dc3e4e7c3deb141d81944d16e503b4f2b6?placeholderIfAbsent=true', label: 'Cards' },
  { icon: 'https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/25358264860f2579fe0ae758cd8f70fb5391fb46?placeholderIfAbsent=true', label: 'Convert' },
  { icon: 'https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/c637871c090d7882203b80930aabe98176f2925f?placeholderIfAbsent=true', label: 'Recipients' },
];

export const Sidebar: React.FC = () => {
  return (
    <nav className="w-full h-screen overflow-hidden bg-[#363636] mx-auto px-8 py-14 max-md:px-5 flex flex-col" role="navigation">
      <div className="flex w-full items-center gap-3 justify-center">
        <img
          src="/lovable-uploads/9abb0c22-a7f0-4db1-a6c7-291d6f0bd45d.png"
          alt="MW Logo"
          className="aspect-square object-contain w-[90px] fill-[linear-gradient(251deg,#0295FF_0.47%,var(--Primary,#5C58FF)_71.4%)] self-stretch my-auto"
        />
      </div>
      
      <div className="w-full text-base text-neutral-50 font-normal mt-16 max-md:mt-10">
        {navItems.map((item, index) => (
          <div
            key={index}
            className={`flex min-h-12 w-full items-center gap-3 ${
              item.active ? 'bg-[rgba(250,250,250,0.15)] font-semibold' : 'bg-[#363636]'
            } ${index > 0 ? 'mt-2' : ''} px-4 py-3 ${item.active ? 'rounded-lg' : ''}`}
          >
            <img src={item.icon} alt="" className="aspect-[1] object-contain w-5 self-stretch shrink-0 my-auto" />
            <div className="text-neutral-50 self-stretch my-auto">{item.label}</div>
          </div>
        ))}
      </div>

      <div className="w-full text-base text-neutral-50 font-normal whitespace-nowrap mt-auto mb-10">
        <div className="flex min-h-12 w-full gap-3 bg-[#363636] px-4 py-3">
          <img src="https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/d8dd5341f37f343bb3752c79341dd5b06fb214a1?placeholderIfAbsent=true" alt="" className="aspect-[1] object-contain w-5 shrink-0" />
          <div className="text-neutral-50">Settings</div>
        </div>
      </div>
    </nav>
  );
};
