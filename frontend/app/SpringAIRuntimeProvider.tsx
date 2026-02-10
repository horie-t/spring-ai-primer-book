"use client";

import type { ReactNode } from "react";
import {
    AssistantRuntimeProvider,
    useLocalRuntime,
    type ChatModelAdapter,
} from "@assistant-ui/react";

const SpringAIModelAdapter: ChatModelAdapter = {
    async run({ messages, abortSignal }) {
        // 直近のユーザーメッセージを取得
        const lastMessage = messages.at(-1);
        const result = await fetch("http://localhost:8080/api/chat", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(lastMessage),
            signal: abortSignal,
        });
        const data = await result.json();

        return {
            content: [
                {
                    type: "text",
                    text: data.text,  // バックエンドから返却される AssistantUIContent.text (後述)
                },
            ],
        };
    },
};

export function SpringAIRuntimeProvider({ children }: Readonly<{ children: ReactNode; }>) {
    const runtime = useLocalRuntime(SpringAIModelAdapter);

    return (
        <AssistantRuntimeProvider runtime={runtime}>
            {children}
        </AssistantRuntimeProvider>
    );
}
