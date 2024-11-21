package com.devchw.springai;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final OpenAiChatModel openAiChatModel;
//    private final VertexAiGeminiChatModel vertexAiGeminiChatModel;


    @PostMapping("/api/chat")
    public Map<String, String> chat(@RequestBody String message) {
        String command = "Tell me answer that question {}";
        Map<String, String> responses = new HashMap<>();

        String openAiResponse = openAiChatModel.call(message);
        responses.put("openai(chatGPT) 응답", openAiResponse);

//        String vertexAiGeminiResponse = vertexAiGeminiChatModel.call(message);
//        responses.put("vertexai(gemini) 응답", vertexAiGeminiResponse);
        return responses;
    }

//    @PostMapping("/api/prompt-template")
//    public PromptTemplate promptTemplate(@RequestBody String message) {
//
//    }
}
