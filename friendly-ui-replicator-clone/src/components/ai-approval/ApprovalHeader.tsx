import React from "react";

const ApprovalHeader: React.FC = () => {
  return (
    <div className="justify-center items-center border-b-[color:var(--Background-Light-Gray,#E9E9EB)] flex gap-2 p-6 border-b border-solid max-md:px-5">
      <img
        src="https://cdn.builder.io/api/v1/image/assets/fce6f3940d0143078dd23de7ff861479/acb1a3149dc60aafe90be6ed5f1cbbd5cdec6c03?placeholderIfAbsent=true"
        alt="Multiwallet Logo"
        className="aspect-[3.69] object-contain w-[107px] self-stretch my-auto"
      />
    </div>
  );
};

export default ApprovalHeader;
