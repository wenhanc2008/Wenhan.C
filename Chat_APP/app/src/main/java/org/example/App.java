package org.example;

import io.github.stefanbratanov.jvm.openai.*;

import java.util.ArrayList;
import java.util.List;

public class App {
    //This code is designed to process user input messages and add specific requests to ensure concise replies.
    //The RequestCount variable: Each time a new request is sent, this counter can be incremented to help record or debug the number of requests.
    public static int RequestCount = 0;
    //This method takes a userMessage string as input and returns a modified string.
    public static String MakeSomeComplexAdditionToTheMessage(String userMessage) {
        userMessage = userMessage + ". Please be concise. Short answer only.";
        return userMessage;
    }
    //The purpose of this code is to send the user’s input message to OpenAI and receive a response, while also printing request information and request count.
    public static String AskOpenAIConcisely(List<String> previousMessages, String userMessage) {
        //Calls the above method to further process the user’s message, adding a prompt for a concise response.
        userMessage = MakeSomeComplexAdditionToTheMessage(userMessage);

        System.out.println();
        System.out.println("Request #: " + RequestCount);//Prints the current request number.
        System.out.println(previousMessages);//Prints the previous message history.
        System.out.println("user:" + userMessage);// Prints the user’s current message.
        String response = CallOpenAIStub(previousMessages, userMessage); //Calls the CallOpenAIStub method，sending the user’s message and message history to OpenAI and returning the AI’s response.
        System.out.println("System:" +response); //Prints the AI’s response.

        RequestCount++;//Increments the request counter to track the current count for the next request.

        return response;//Returns the AI’s response.
    }
    // Placeholder function for OpenAI
    //Sends the user’s message and the message history to OpenAI to get a response, and updates the message history to ensure the context of the conversation is preserved.
    public static String CallOpenAIStub(List<String> previousMessages, String userMessage){
        previousMessages.add(userMessage);
        var answer  = OpenAI.CallOpenAI(previousMessages, userMessage);
        //var answer = "This is a fake respond that's shorter from OpenAI";
        previousMessages.add(answer);
        return answer;
    }
    /* The two pieces of code work together,
    with AskOpenAIConcisely being a higher-level function responsible for handling user input and debug information,
    while CallOpenAIStub focuses on interacting with OpenAI and returning the response.*/
    public static void main(String[] args) {
        List<String> previousMessages = new ArrayList<>();
        String response0 = AskOpenAIConcisely(previousMessages,"Tell me how to be a better programmer");
        String response1 = AskOpenAIConcisely(previousMessages,"Tell me how to be a better manager");

        //System.out.println(AskOpenAIConcisely(previousMessages,"Tell"));
        //System.out.println(AskOpenAIConcisely(previousMessages,"Tell"));
    }
}
