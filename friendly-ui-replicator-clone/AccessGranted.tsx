import * as React from "react";
import { useEffect } from "react";
import ApprovalHeader from "./src/components/ai-approval/ApprovalHeader";
import ApprovalFooter from "./src/components/ai-approval/ApprovalFooter";

/**
 * AccessGranted component that displays a confirmation screen
 * when a user has been granted access
 */
function AccessGranted() {
  useEffect(() => {
    const timer = setTimeout(() => {
      window.location.href = "claude://";
    }, 2400);
    return () => clearTimeout(timer); // Cleanup the timer if the component unmounts
  }, []);

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col items-center justify-center p-4">
      <div className="border border-[color:var(--Background-Light-Gray,#E9E9EB)] max-w-[800px] w-full overflow-hidden bg-white rounded-lg border-solid">
        <ApprovalHeader />
        <section className="flex flex-col items-center justify-center text-center py-[30px] px-6">
          <img
            src="https://cdn.builder.io/api/v1/image/assets/fce6f3940d0143078dd23de7ff861479/ea0aaacb459b84225411b62f977718fcf04821a7?placeholderIfAbsent=true"
            alt="Success icon"
            className="object-contain w-20 aspect-square mt-[42px]"
          />
          <h1 className="mt-6 text-[#2B2B2B] text-4xl font-extrabold leading-none tracking-[-0.5px] max-md:text-4xl">
            Access granted
          </h1>
          <p className="mt-4 text-[rgba(96,102,124,1)] text-base font-medium leading-[21px] self-center max-w-[504px]">
            You'll be redirected automatically...
          </p>
        </section>
        <ApprovalFooter />
      </div>
    </div>
  );
}

export default AccessGranted; 