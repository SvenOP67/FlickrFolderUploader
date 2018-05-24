package oauthtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Logger;
import oauthtest.account.AccountInfo;
import oauthtest.contacts.ContactList;
import oauthtest.favourites.FavouriteList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;

public class FlickrUpload {

  private static final String USER_HOME = "user.home";
  private static final String USERNAME = "SvenOP67";
  private static final String REST_METHOD_PARAM = "method";
  private static final String STAT_OK = "\"stat\":\"ok\"";

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
      if (result.contains(STAT_OK)) {
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

        auth.uploadImage("G:\\Eigene Bilder\\zebra_etosha.jpg");
/*
        String info = String.format("Account information %s", auth.getAccessMap().toString());
        Logger.getGlobal().info(info);

        callRestService(auth, "flickr.test.login");

        String contacts = callRestService(auth, "flickr.contacts.getList");
        if (contacts.contains(STAT_OK)) {
          ContactList cl = new ObjectMapper().readValue(contacts, ContactList.class);
          Logger.getGlobal().info(Integer.toString(cl.getContacts().getContact().size()));
        }

        String favourites = callRestService(auth, "flickr.favorites.getList");
        if (favourites.contains(STAT_OK)) {
          FavouriteList cl = new ObjectMapper().readValue(favourites, FavouriteList.class);
          Logger.getGlobal().info(Integer.toString(cl.getPhotos().getPhoto().size()));
        }
*/
      }
    } catch (IOException e) {
      Logger.getGlobal().severe(e.getMessage());
    }


  }


  private static String callRestService(OAuth auth, String method)
      throws IOException {
    HashMap<String, String> methodMap = new HashMap<>();
    methodMap.put("nojsoncallback", "1");
    methodMap.put("format", "json");
    methodMap.put(REST_METHOD_PARAM, method);

    String url;
    url = auth.generateRestApiUrl(methodMap);
    Logger.getGlobal().info(url);
    return executeUrl(url);
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
