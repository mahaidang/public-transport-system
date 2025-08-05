package com.hdda.services.external;

import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GoongService {

    @Value("${goong.api_key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public double getWalkingDistanceKm(double lat1, double lng1, double lat2, double lng2) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://rsapi.goong.io/Direction")
                .queryParam("origin", lat1 + "," + lng1)
                .queryParam("destination", lat2 + "," + lng2)
                .queryParam("vehicle", "bike")
                .queryParam("api_key", apiKey)
                .toUriString();

        String json = restTemplate.getForObject(url, String.class);
        JSONObject root = new JSONObject(json);
        double meters = root.getJSONArray("routes")
                .getJSONObject(0)
                .getJSONArray("legs")
                .getJSONObject(0)
                .getJSONObject("distance")
                .getDouble("value");
        return meters / 1000.0;
    }
}
