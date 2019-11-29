package org.cayambe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * A simple test client who consumes the admin service
 */
public class AdminClient {

    private String url;

    /**
     * Takes the URL representing the admin microservice
     *
     * @param url the url for the admin microservice
     */
    public AdminClient(String url) {
        this.url = url;
    }

    public Category getCategory(Integer categoryId) throws IOException {
        URIBuilder uriBuilder;
        try {
            uriBuilder = new URIBuilder(url).setPath("/admin/category" + categoryId);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String jsonResponse = Request.Get(uriBuilder.toString()).execute().returnContent().asString();

        if(jsonResponse.isEmpty()) {
            return null;
        }

        return new ObjectMapper().registerModule(new JavaTimeModule()).readValue(jsonResponse, Category.class);
    }

}
