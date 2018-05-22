package oauthtest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class FlickrUpload {

  private static final String REQUEST_TOKEN_URL = "https://www.flickr.com/services/oauth/request_token";
  private static final String ACCESS_TOKEN_URL = "https://www.flickr.com/services/oauth/access_token";

  public static void main(String[] args) {
    try (FileInputStream input = new FileInputStream(
        new File(System.getProperty("user.home") + File.separator + ".flickr"))) {
      Properties prop = new Properties();
      prop.load(input);
      OAuth auth = new OAuth(prop.getProperty("API_KEY"), prop.getProperty("SIGNATURE_KEY"));
      String url = auth.generateRequestTokenUrl(REQUEST_TOKEN_URL);
      Logger.getGlobal().info(url);

      String result = executeUrl(url);
      auth.parseAndStoreResult(result);

      Logger.getGlobal().info(auth.getAuthToken());
      Logger.getGlobal().info(auth.getAuthTokenSecret());

      //      Logger.getGlobal().info(auth.generateAccessTokenUrl(ACCESS_TOKEN_URL));
    } catch (IOException e) {
      Logger.getGlobal().severe(e.getMessage());
    }


  }

  private static String executeUrl(String url) throws IOException {
    HttpClient client = HttpClientBuilder.create().build();
    HttpGet get = new HttpGet(url);

    HttpResponse response = client.execute(get);
    Logger.getGlobal().info(String.valueOf(response.getStatusLine().getStatusCode()));

    BufferedReader contentReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
    String result = contentReader.readLine();
    Logger.getGlobal().info(result);
    return result;
  }
}
