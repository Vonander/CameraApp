package com.vonander.japancvcameraapp.network.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.Callable;

public class SearchTagsHandler implements Callable<String> {

    private String image_upload_id = "";

    public void setImageUploadId(String image_upload_id) {
        this.image_upload_id = image_upload_id;
    }

    @Override
    public String call() throws Exception {

        String credentialsToEncode = "acc_dff845dad777694" + ":" + "3d1dbc04655c9176e6fecf4478027c48";
        String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

        String endpoint_url = "https://api.imagga.com/v2/tags";

        String url = endpoint_url + "?image_upload_id=" + image_upload_id;
        URL urlObject = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

        connection.setRequestProperty("Authorization", "Basic " + basicAuth);

        int responseCode = connection.getResponseCode();

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader connectionInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String jsonResponse = connectionInput.readLine();

        connectionInput.close();

        return jsonResponse;
    }
}
