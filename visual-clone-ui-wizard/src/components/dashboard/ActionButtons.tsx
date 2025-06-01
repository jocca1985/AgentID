import React from 'react';

const actions = [
  { icon: 'https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/54d434ec438e99a84d7b31a830c84303e00efce5?placeholderIfAbsent=true', label: 'Pay or transfer', primary: true },
  { icon: 'https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/fe6054e9386dadee189c57ae189f80a24d2216a5?placeholderIfAbsent=true', label: 'Top up' },
  { icon: 'https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/e919ccfe4eb3681190b2389dd6167bf78b31252d?placeholderIfAbsent=true', label: 'Convert' },
  { icon: 'https://cdn.builder.io/api/v1/image/assets/5fef96f6a1464afbb033bd371ba8593b/edb6777fbc095f3daf9dc093076fdf42bcd75694?placeholderIfAbsent=true', label: 'Recipients' },
];

export const ActionButtons: React.FC = () => {
  return (
    <div className="flex gap-5 text-base text-[#5C58FF] font-medium text-center flex-wrap mt-6 max-md:max-w-full">
      {actions.map((action, index) => (
        <button
          key={index}
          className={`justify-center items-center flex gap-2 ${
            action.primary
              ? 'text-white font-normal bg-[#363636]'
              : 'bg-[#EBEAFF]'
          } pl-4 pr-6 py-3 rounded-lg max-md:pr-5`}
        >
          <img src={action.icon} alt="" className="aspect-[1] object-contain w-5 self-stretch shrink-0 my-auto" />
          <div className={action.primary ? 'text-white' : 'text-[#5C58FF]'}>
            {action.label}
          </div>
        </button>
      ))}
    </div>
  );
};