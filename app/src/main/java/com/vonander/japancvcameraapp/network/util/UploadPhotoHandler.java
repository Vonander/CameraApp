package com.vonander.japancvcameraapp.network.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.Callable;

public class UploadPhotoHandler implements Callable<String> {

    private File fileToUpload;

    public void setFileToUpload(File fileToUpload) {
        this.fileToUpload = fileToUpload;
    }

    @Override
    public String call() throws Exception {

        String credentialsToEncode = "acc_dff845dad777694" + ":" + "3d1dbc04655c9176e6fecf4478027c48";
        String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

        String endpoint = "/uploads";

        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary =  "Image Upload";

        URL urlObject = new URL("https://api.imagga.com/v2" + endpoint);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

        connection.setRequestProperty("Authorization", "Basic " + basicAuth);
        connection.setUseCaches(false);
        connection.setDoOutput(true);

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Cache-Control", "no-cache");
        connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

        DataOutputStream request = new DataOutputStream(connection.getOutputStream());

        request.writeBytes(twoHyphens + boundary + crlf);
        request.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + fileToUpload.getName() + "\"" + crlf);
        request.writeBytes(crlf);

        InputStream inputStream = new FileInputStream(fileToUpload);
        int bytesRead;
        byte[] dataBuffer = new byte[1024];
        while ((bytesRead = inputStream.read(dataBuffer)) != -1) {
            request.write(dataBuffer, 0, bytesRead);
        }

        request.writeBytes(crlf);
        request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
        request.flush();
        request.close();

        InputStream responseStream = new BufferedInputStream(connection.getInputStream());

        BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));

        String line = "";
        StringBuilder stringBuilder = new StringBuilder();

        while ((line = responseStreamReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        responseStreamReader.close();

        String apiResponse = stringBuilder.toString();
        System.out.println("response: " + apiResponse);

        responseStream.close();
        connection.disconnect();

        return (apiResponse);
    }
}
