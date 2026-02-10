import "./globals.css";
import { TooltipProvider } from "@/components/ui/tooltip"
import { SpringAIRuntimeProvider } from "@/app/SpringAIRuntimeProvider";

export default function RootLayout({ children }: Readonly<{ children: React.ReactNode; }>) {
    return (
        <html lang="ja">
            <body>
            <TooltipProvider>
                <SpringAIRuntimeProvider>
                    {children}
                </SpringAIRuntimeProvider>
            </TooltipProvider>
            </body>
        </html>
    );
}
