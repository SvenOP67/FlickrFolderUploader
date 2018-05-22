package oauthtest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class FlickrUpload {

  private static final String REQUEST_TOKEN_URL = "https://www.flickr.com/services/oauth/request_token";
  private static final String ACCESS_TOKEN_URL = "https://www.flickr.com/services/oauth/access_token";

  public static void main(String[] args) {
    try (FileInputStream input = new FileInputStream(
        new File(System.getProperty("user.home") + File.separator + ".flickr"))) {
      Properties prop = new Properties();
      prop.load(input);
      OAuth auth = new OAuth(prop.getProperty("API_KEY") ,prop.getProperty("SIGNATURE_KEY") );
      Logger.getGlobal().info(auth.genUrl(REQUEST_TOKEN_URL));
      Logger.getGlobal().info(auth.genUrl(ACCESS_TOKEN_URL));
    } catch (IOException e) {
      Logger.getGlobal().severe(e.getMessage());
    }


  }
}
