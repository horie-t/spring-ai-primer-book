"use client";

import type { ReactNode } from "react";
import {
    AssistantRuntimeProvider,
    useLocalRuntime,
    type ChatModelAdapter,
} from "@assistant-ui/react";
import { RagProvider, useRagContext } from "./RagContext";

function createSpringAIModelAdapter(ragEnabled: boolean): ChatModelAdapter {
    return {
        async run({ messages, abortSignal }) {
            // 直近のユーザーメッセージを取得
            const lastMessage = messages.at(-1);
            const result = await fetch("http://localhost:8080/api/chat", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    ...lastMessage,
                    ragEnabled,
                }),
                signal: abortSignal,
                credentials: 'include',
            });
            const data = await result.json();

            return {
                content: [
                    {
                        type: "text",
                        text: data.text,
                    },
                ],
            };
        },
    };
}

function SpringAIRuntimeProviderInner({ children }: Readonly<{ children: ReactNode; }>) {
    const { ragEnabled } = useRagContext();
    const runtime = useLocalRuntime(createSpringAIModelAdapter(ragEnabled));

    return (
        <AssistantRuntimeProvider runtime={runtime}>
            {children}
        </AssistantRuntimeProvider>
    );
}

export function SpringAIRuntimeProvider({ children }: Readonly<{ children: ReactNode; }>) {
    return (
        <RagProvider>
            <SpringAIRuntimeProviderInner>
                {children}
            </SpringAIRuntimeProviderInner>
        </RagProvider>
    );
}
