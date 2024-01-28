package com.github.biggerball.mockrecorder.agent.advisor;

import com.github.biggerball.mockrecorder.agent.util.GlobalConfig;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpSender {

    static final OkHttpClient client = new OkHttpClient();

    public static void send(String content) {
        Request request = new Request.Builder()
                .url(GlobalConfig.serverAddr + "/db/write")
                .post(RequestBody.create(MediaType.parse("application/json"), content)).build();
        try (Response _response = client.newCall(request).execute()) {
            System.out.println(_response.code());
            //send success
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
