package com.bftcom.ws.handlers.yandextranslate;

import com.bftcom.ws.handlers.AbstractHandler;
import com.bftcom.ws.objmodel.Message;
import com.bftcom.ws.objmodel.TextMessage;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artem on 26.09.2016.
 */
public class YandexTranslateRuEnHandler extends AbstractHandler {
  @Override
  public boolean handleMessage(Message inMessage) {
    if (inMessage.getMessageType() == Message.MESSAGE_TYPE_TEXT) {

      try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
        String key = "trnsl.1.1.20160925T185539Z.b069bd6665aa90fd.a6144a205921c3a88f66f6b80464265db8c2936b";
        String url = "https://translate.yandex.net/api/v1.5/tr.json/translate?" + "key=" + key + "&lang=en";
        HttpPost post = new HttpPost(url);

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("text", ((TextMessage) inMessage).getText()));

        post.setEntity(new UrlEncodedFormEntity(urlParameters, StandardCharsets.UTF_8));

        try (CloseableHttpResponse response = client.execute(post)) {
          if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                + response.getStatusLine().getStatusCode());
          }

          HttpEntity ht = response.getEntity();
          BufferedHttpEntity buf = new BufferedHttpEntity(ht);
          String responseContent = EntityUtils.toString(buf, StandardCharsets.UTF_8);
          JsonParser parser = new JsonParser();
          JsonObject o = parser.parse(responseContent).getAsJsonObject();
          ((TextMessage) inMessage).setText(o.get("text").getAsString());
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return true;
  }
}
