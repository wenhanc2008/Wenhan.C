package org.example;

import io.github.stefanbratanov.jvm.openai.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class OpenAI {
    public static String CallOpenAI(List<String> previousMessages, String userMessageString) {
        Properties properties = new Properties();
        try (InputStream input = OpenAI.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IOException("Sorry, unable to find config.properties");
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String key = properties.getProperty("openai.api.key");

        io.github.stefanbratanov.jvm.openai.OpenAI openAI = io.github.stefanbratanov.jvm.openai.OpenAI.newBuilder(key).build();
        //Creates a ChatClient instance chatClient from the openAI object, specifically for handling chat (ChatGPT) related requests.
        ChatClient chatClient = openAI.chatClient();
        var builder = CreateChatCompletionRequest.newBuilder();
        var model = builder.model("gpt-4o");//Selects the model (here, using "gpt-4o").
        //Converts the user message userMessageString into a ChatMessage object and adds it to the request.
        var userMessage = ChatMessage.userMessage(userMessageString);
        var requestBuilder = model.message(userMessage);

        //Finally, the createChatCompletionRequest object contains the complete request information.
        CreateChatCompletionRequest createChatCompletionRequest = requestBuilder.build();
        //Sends the request to the OpenAI server and receives the response.
        //Extracts the reply content from the choices in chatCompletion.
        ChatCompletion chatCompletion = chatClient.createChatCompletion(createChatCompletionRequest);
        var choices = chatCompletion.choices();
        //	•	Retrieves the first reply from the choices list and returns it to the caller.
        return choices.iterator().next().message().content();
    }
}
