package org.stockmaster3000.stockmaster3000.client;

import org.springframework.stereotype.Component;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class OpenAIClient {

    private static final String API_KEY = "your-openai-api-key";
    private static final String API_URL = "https://api.openai.com/v1/completions";

    public String generateReport(String prompt) throws Exception {
        String requestBody = String.format(
            "{\"model\": \"text-davinci-003\", \"prompt\": \"%s\", \"max_tokens\": 500}",
            prompt
        );

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_URL))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + API_KEY)
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}
