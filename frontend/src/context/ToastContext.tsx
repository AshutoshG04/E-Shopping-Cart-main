// PATH: frontend/src/context/ToastContext.tsx
import React, { createContext, useState, useContext, useCallback, useMemo } from 'react';
import type { ReactNode } from 'react';

// --- Types ---
export type ToastType = 'success' | 'error' | 'warning' | 'info';

export interface Toast {
  id: number;
  type: ToastType;
  message: string;
}

interface ToastContextType {
  toasts: Toast[];
  showToast: (type: ToastType, message: string) => void;
  removeToast: (id: number) => void;
}

const ToastContext = createContext<ToastContextType | undefined>(undefined);

// --- Provider ---
export const ToastProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [toasts, setToasts] = useState<Toast[]>([]);

  const removeToast = useCallback((id: number) => {
    setToasts(prev => prev.filter(t => t.id !== id));
  }, []);

  const showToast = useCallback((type: ToastType, message: string) => {
    const id = Date.now();
    const newToast = { id, type, message };
    
    setToasts(prev => [...prev, newToast]);
    
    // Auto-dismiss after 3 seconds
    setTimeout(() => removeToast(id), 3000);
  }, [removeToast]);

  const value = useMemo(() => ({
    toasts,
    showToast,
    removeToast
  }), [toasts, showToast, removeToast]);

  return (
    <ToastContext.Provider value={value}>
      {children}
    </ToastContext.Provider>
  );
};

// --- Hook ---
// eslint-disable-next-line react-refresh/only-export-components
export const useToast = () => {
  const context = useContext(ToastContext);
  if (context === undefined) {
    throw new Error('useToast must be used within a ToastProvider');
  }
  return context;
};