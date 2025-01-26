package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.example.generated.tables.YoutubeCache;
import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class YoutubeCacheService {
    @Value("${youtube.api.key}")  // 从 application.properties 中获取 API Key
    private String youtubeApiKey;
    @Autowired
    private DSLContext dsl;  // JOOQ 的 DSLContext，用于操作数据库

    // 	This method checks if the search query results are already cached in the database.
    public Optional<String> getCacheForQuery(String query) {
        System.out.println("Querying database for: " + query);
        Optional<String> result = dsl.select(YoutubeCache.YOUTUBE_CACHE.RESULTS)
                .from(YoutubeCache.YOUTUBE_CACHE)
                .where(YoutubeCache.YOUTUBE_CACHE.QUERY.eq(query))
                .fetchOptionalInto(String.class);
        //If the result is found, it returns the cached result; otherwise, it indicates a cache miss and implies that an API call will be made.
        if (result.isPresent()) {
            System.out.println("Cache hit: Returning results from the database.");
        //it indicates a cache miss and implies that an API call will be made.
        } else {
            System.out.println("Cache miss: Querying YouTube API.");
        }
        return result;// 返回结果封装为 Optional
    }
    //•	This method makes an HTTP GET request to the YouTube Data API using the search query.
    public String fetchYoutubeResults(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

            // 	It constructs the API request URL with the provided query and API key,

            String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=" + encodedQuery + "&key=" + youtubeApiKey;
            System.out.println("Constructed URL: " + url);

            // then sends the request and parses the response as JSON.
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // If videos are found, it processes the API response
            // and returns the results in JSON format, containing video titles and video IDs.
            System.out.println("API Response: " + response.body());
            // 解析 JSON 响应
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body());

            // If no videos are found or if an error occurs, it returns a default empty JSON array or an error message.
            JsonNode itemsNode = jsonNode.path("items");
            if (!itemsNode.isArray() || itemsNode.size() == 0) {
                System.out.println("No videos found in the API response.");
                return "{\"items\": []}"; // 返回一个空 JSON 数组，避免错误
            }
            // 构建 JSON 格式的结果
            ArrayNode resultArray = objectMapper.createArrayNode();
            for (JsonNode item : itemsNode) {
                ObjectNode videoObject = objectMapper.createObjectNode();
                videoObject.put("title", item.path("snippet").path("title").asText());
                videoObject.put("videoId", item.path("id").path("videoId").asText());
                resultArray.add(videoObject);
            }

            // 返回 JSON 字符串
            ObjectNode resultObject = objectMapper.createObjectNode();
            resultObject.set("items", resultArray);
            String resultJson = objectMapper.writeValueAsString(resultObject);

            System.out.println("Processed JSON Result: " + resultJson);
            return resultJson;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "{\"error\": \"Failed to fetch data from YouTube API.\"}"; // 返回一个描述错误的 JSON
        }
    }

    // This method checks if the search query has already been cached.
    // If not, it calls the fetchYoutubeResults() method to retrieve data from YouTube.
    public void saveCache(String query, String resultsJson) {
        System.out.println("saveCache called with query: " + query);
        Optional<String> cachedResults = getCacheForQuery(query);
        if (cachedResults.isEmpty()) {
            String apiResults = fetchYoutubeResults(query);
            dsl.insertInto(YoutubeCache.YOUTUBE_CACHE)
                    .set(YoutubeCache.YOUTUBE_CACHE.QUERY, query)
                    .set(YoutubeCache.YOUTUBE_CACHE.RESULTS, JSONB.valueOf(apiResults))  // 转换为 JSONB
                    .set(YoutubeCache.YOUTUBE_CACHE.CREATED_AT, LocalDateTime.now())       // 插入时间
                    .execute();
            System.out.println("Cache saved to database for: " + query);
        } else {
            System.out.println("Cache hit: Returning cached results for: " + query);
        }
        }
    }
