"use client";

import { Thread } from '@/components/assistant-ui/thread';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import { useRagContext } from './RagContext';

export default function Page() {
  const { ragEnabled, setRagEnabled } = useRagContext();

  return (
    <ProtectedRoute>
       <main className="h-screen flex items-center justify-center">
         <div className="w-full max-w-3xl h-[80vh] border rounded-xl p-4 overflow-hidden flex flex-col">
           <Thread />
           <div className="mb-3 flex items-center gap-2">
             <label className="flex items-center gap-2 cursor-pointer">
               <input
                 type="checkbox"
                 checked={ragEnabled}
                 onChange={(e) => setRagEnabled(e.target.checked)}
                 className="w-4 h-4"
               />
               <span className="text-sm">社内文書検索</span>
             </label>
           </div>
         </div>
       </main>
    </ProtectedRoute>
    );
}
