package com.rollingstone.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rollingstone.UserDTO;
import com.rollingstone.dto.PasswordResponseDTO;
import org.json.JSONObject;
import org.keycloak.models.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RollingStoneRemoteStorageUtil {

    static Logger logger  = LoggerFactory.getLogger("RollingStoneRemoteStorageUtil");

    private static final String ACCESS_TOKEN_DATA = "grant_type=client_credentials&client_secret=AA3eMk6JUeVrx1nMovrVOgFEZTrQCtP9&client_id=rollingstone_ecommerce_product_api_client";
    private static final String ACCESS_TOKEN_URL = "http://localhost:8180/auth/realms/binitdattarealm/protocol/openid-connect/token";

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public static String getAccessToken() {
        HttpURLConnection accessTokenConnection = null;

        try {
            byte[] accessTokenDataBytes = ACCESS_TOKEN_DATA.getBytes("UTF8");

            URL tokenUrl = new URL(ACCESS_TOKEN_URL);
            accessTokenConnection = (HttpURLConnection) tokenUrl.openConnection();

            accessTokenConnection.setDoOutput(true);
            accessTokenConnection.setRequestMethod("POST");
            accessTokenConnection.setRequestProperty("User-Agent", "KeyCloak User SPD Client");
            accessTokenConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            try (DataOutputStream wr = new DataOutputStream(accessTokenConnection.getOutputStream())) {
                wr.write(accessTokenDataBytes);
            }
        } catch (Exception e) {
            logger.info("Exception Happened in Getting Access Token :"+e.getMessage());
        }

        StringBuilder content = new StringBuilder();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(accessTokenConnection.getInputStream()))) {

            String line;
            content = new StringBuilder();

            while ((line = br.readLine()) != null) {
                content.append(line);
                content.append(System.lineSeparator());
            }
        } catch (IOException ex) {
            logger.info("Exception Happened in Reading Access Token :"+ex.getMessage());
        }

        logger.info(content.toString());

        JSONObject responseJson = new JSONObject(content.toString());
        String accessToken = responseJson.getString("access_token");
        String tokenType = responseJson.getString("token_type");
        long expiry = responseJson.getLong("expires_in");
        logger.info(" accessToken :" + accessToken);
        logger.info(" tokenType :" + tokenType);
        logger.info(" expiry :" + expiry);

        return accessToken;
    }

    public static UserDTO getUser(String userName) throws IOException {
        HttpURLConnection conn = null;
        String accessToken = getAccessToken();
        logger.info(accessToken);
        URL url = new URL("http://localhost:8072/user/"+ userName);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty ("Authorization", "Bearer "+accessToken);

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        String apiOutput = null;
        apiOutput = br.readLine();
        logger.info("API Output :"+ apiOutput);
        conn.disconnect();
        ObjectMapper mapper = new ObjectMapper();

        UserDTO userDto = mapper.readValue(apiOutput, UserDTO.class);
        return userDto;
    }

    private static HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        logger.info(builder.toString());
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }

    public static PasswordResponseDTO getPasswordResponse(UserModel userModel, String plainTextPassword) {

        PasswordResponseDTO passwordResponseDTO = new PasswordResponseDTO();
        String accessToken = getAccessToken();

        Map<Object, Object> data = new HashMap<>();
        data.put("password", plainTextPassword);
        String extractedUserName = userModel.getId().substring(userModel.getId().lastIndexOf(":")+1);
        String extractedUserEmail = userModel.getEmail();

        logger.info("extractedUserName :" + extractedUserName);
        logger.info("extractedUserEmail :" + extractedUserEmail);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(buildFormDataFromMap(data))
                .uri(URI.create("http://localhost:8072/" + extractedUserName + "/validate-password"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer "+accessToken)
                .build();

        try
        {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("HTTP Response Status Code:"+ response.statusCode());
            logger.info("Response Body :"+ response.body());

            ObjectMapper mapper = new ObjectMapper();

            passwordResponseDTO = mapper.readValue(response.body(), PasswordResponseDTO.class);
        }
        catch (MalformedURLException me) {
            me.printStackTrace();
        }
        catch (IOException e)
        {
            logger.info("Exception Happened During Password Validation :"+e.getMessage());
        }
        catch (InterruptedException e)
        {
            logger.info("Exception Happened During Password Validation :"+e.getMessage());
        }

        if (passwordResponseDTO == null)
            return new PasswordResponseDTO();

        return passwordResponseDTO;
    }

    public static void logUser(UserModel userModel) {
        if (userModel != null) {
            logger.info("************* UserModel after calling createUserModel Username: " + userModel.getUsername());
            logger.info("************* UserModel after calling createUserModel Lastname : " + userModel.getLastName());
            logger.info("************* UserModel after calling createUserModel Email : " + userModel.getEmail());
            logger.info("************* UserModel after calling createUserModel Firstname: " + userModel.getFirstName());
        }
    }
}
