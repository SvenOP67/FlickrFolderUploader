package oauthtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Scanner;
import oauthtest.account.AccountInfo;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.util.Properties;
import java.util.logging.Logger;

public class FlickrUpload {

  private static final String USER_HOME = "user.home";
  private static final String USERNAME = "SvenOP67";
  private static final String REST_METHOD_PARAM = "method";

  public static void main(String[] args) {
    try (FileInputStream input = new FileInputStream(
        new File(System.getProperty(USER_HOME) + File.separator + ".flickr"))) {
      Properties prop = new Properties();
      prop.load(input);
      OAuth auth = new OAuth(prop.getProperty("API_KEY"), prop.getProperty("SIGNATURE_KEY"));

      String result = executeUrl(
          "https://api.flickr.com/services/rest/?method=flickr.people.findByUsername&api_key="
              + prop.getProperty("API_KEY") + "&username=" + USERNAME
              + "&format=json&nojsoncallback=1");
      if (result.contains("\"stat\":\"ok\"")) {
        AccountInfo account = new ObjectMapper().readValue(result, AccountInfo.class);
        String authStoreFile = account.getUser().getNsid();

        String file = System.getProperty(USER_HOME) + File.separator + authStoreFile;
        String url;
        if (!new File(file).exists()) {

          url = auth.generateRequestTokenUrl();
          Logger.getGlobal().info(url);

          result = executeUrl(url);
          auth.parseAndStoreResult(result);

          // Open authorization link on flickr
          String authLink =
              "https://www.flickr.com/services/oauth/authorize?perms=delete&" + "oauth_token="
                  + auth
                  .getAuthToken();
          Logger.getGlobal().info(authLink);

          url = auth.generateAccessTokenUrl(new Scanner(System.in).next());
          Logger.getGlobal().info(url);

          result = executeUrl(url);
          auth.parseAndStoreResult(result);

          storeAuthInFile(result, file);

        }
        auth.setAuthStoreFile(file);

        String info = String.format("Account information %s", auth.getAccessMap().toString());
        Logger.getGlobal().info(info);

        callRestService(auth, "flickr.test.login");

        callRestService(auth, "flickr.contacts.getList");

        callRestService(auth, "flickr.favorites.getList");

      }
    } catch (IOException e) {
      Logger.getGlobal().severe(e.getMessage());
    }


  }

  private static void callRestService(OAuth auth, String method)
      throws IOException {
    HashMap<String, String> methodMap = new HashMap<>();
    methodMap.put("nojsoncallback", "1");
    methodMap.put("format", "json");
    methodMap.put(REST_METHOD_PARAM, method);

    String url;
    url = auth.generateRestApiUrl(methodMap);
    Logger.getGlobal().info(url);
    executeUrl(url);
  }

  private static void storeAuthInFile(String result, String authStoreFile) {
    if (!new File(authStoreFile).exists()) {
      try (FileWriter writer = new FileWriter(authStoreFile)) {
        writer.write(result);
      } catch (IOException e) {
        Logger.getGlobal().severe(e.getMessage());
      }
    }
  }

  private static String executeUrl(String url) throws IOException {
    HttpClient client = HttpClientBuilder.create().build();
    HttpGet get = new HttpGet(url);

    HttpResponse response = client.execute(get);
    String statusCode = Integer.toString(response.getStatusLine().getStatusCode());
    Logger.getGlobal().info(statusCode);

    BufferedReader contentReader = new BufferedReader(
        new InputStreamReader(response.getEntity().getContent()));
    String result = contentReader.readLine();
    Logger.getGlobal().info(result);
    return result;
  }
}
