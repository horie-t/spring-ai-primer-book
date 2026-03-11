"use client";

import { createContext, useContext, useState, type ReactNode } from 'react';

interface RagContextType {
  ragEnabled: boolean;
  setRagEnabled: (enabled: boolean) => void;
}

const RagContext = createContext<RagContextType | undefined>(undefined);

export function RagProvider({ children }: { children: ReactNode }) {
  const [ragEnabled, setRagEnabled] = useState(false);

  return (
    <RagContext.Provider value={{ ragEnabled, setRagEnabled }}>
      {children}
    </RagContext.Provider>
  );
}

export function useRagContext() {
  const context = useContext(RagContext);
  if (!context) {
    throw new Error('useRagContext must be used within RagProvider');
  }
  return context;
}
