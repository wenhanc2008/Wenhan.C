package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
public class YoutubeController {
    //The YoutubeCacheService is injected into the controller to interact with the caching and YouTube API services.
    @Autowired
    private YoutubeCacheService youtubeCacheService;
    //This retrieves the YouTube API key from the application.properties file
    @Value("${youtube.api.key}")

    @GetMapping("/news")
    //	•	Main method to handle the GET request.
    //	It checks the cache, calls the API if necessary, and returns the appropriate HTML response.
    public String getYoutubeVideos(@RequestParam String topic) {
        // 尝试从缓存中获取结果
        Optional<String> cachedResults = youtubeCacheService.getCacheForQuery(topic);

        // 如果缓存命中，直接返回缓存结果
        if (cachedResults.isPresent()) {
            System.out.println("Cache hit for topic: " + topic);  // 打印日志，确认缓存命中
            return formatHtmlResponse(cachedResults.get(), topic, true);  // 标记缓存命中
        }

        // 如果缓存未命中，查询 YouTube API 并缓存结果
        try {
            System.out.println("Cache miss for topic: " + topic);  // 打印日志，确认缓存未命中
            String apiResults = youtubeCacheService.fetchYoutubeResults(topic);

            // 保存到缓存
            youtubeCacheService.saveCache(topic, apiResults);

            // 返回 API 查询结果
            return formatHtmlResponse(apiResults, topic, false);  // 标记缓存未命中
        } catch (Exception e) {
            System.out.println("Error fetching data from YouTube API: " + e.getMessage());
            return "<html><body><h2>Error fetching data from YouTube API</h2><p>" + e.getMessage() + "</p></body></html>";
        }
    }
    private String formatHtmlResponse(String jsonResponse, String topic, boolean isCached) {
        //This method formats the API results (or cached data) into an HTML page,
        //It uses the Jackson library to parse the JSON response and extract the video details (title and video ID).
        //	•	For each video,
        //	it creates a clickable link to the video on YouTube and wraps the video title inside a styled div element.
        StringBuilder htmlResponse = new StringBuilder();
        htmlResponse.append("<html><head><style>")
                .append("body { font-family: Arial, sans-serif; margin: 20px; }")
                .append(".video-card { border: 1px solid #ddd; padding: 10px; margin-bottom: 10px; border-radius: 5px; }")
                .append(".video-title { font-size: 18px; color: #007bff; }")
                .append("</style></head><body>")
                .append("<h2>Search results for: ").append(topic).append("</h2>");
        if (isCached) {
            htmlResponse.append("<p><em>Results fetched from cache.</em></p>");
        } else {
            htmlResponse.append("<p><em>Results fetched from YouTube API.</em></p>");
        }

        try {
            JsonNode jsonNode = new ObjectMapper().readTree(jsonResponse);
            JsonNode itemsNode = jsonNode.get("items");
            if (itemsNode != null && itemsNode.isArray()) {
                System.out.println("html response: " + htmlResponse);
                for (JsonNode item : itemsNode) {
                    JsonNode title = item.get("title");
                    JsonNode videoId = item.get("videoId");
                    // System.out.println("title: " + title);
                    // System.out.println("videoId: " + videoId);
                    if (title != null && videoId != null) {
                        // String link = "https://www.youtube.com/watch?v=" + videoId;
                        StringBuilder linkBuilder = new StringBuilder("https://www.youtube.com/watch?v=");
                        String link = linkBuilder.append(videoId.asText()).toString();
                        System.out.println("link: " + link);
                        htmlResponse.append("<div class='video-card'>")
                                .append("<a class='video-title' href='").append(link).append("'>")
                                .append(title).append("</a></div>");
                    } else {
                        htmlResponse.append("<p>Missing video details for one of the items.</p>");
                    }
                }
                //	If no videos are found, it shows a message indicating that no results were found for the given topic.
            } else {
                htmlResponse.append("<p>No videos found for topic: ").append(topic).append("</p>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //If the JSON response parsing fails, it returns an error message.
            htmlResponse.append("<p>Error parsing YouTube API response.</p>");
        }
        htmlResponse.append("</body></html>");
        return htmlResponse.toString();
    }
}

