package oauthtest;

import java.io.*;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;

class OAuth {

  private static final String URL_OAUTH_ACCESS_TOKEN = "https://www.flickr.com/services/oauth/access_token";
  private static final String URL_OAUTH_REQUEST_TOKEN = "https://www.flickr.com/services/oauth/request_token";
  private static final String URL_SERVICES_REST = "https://api.flickr.com/services/rest";
  private static final String URL_UPLOAD = "https://api.flickr.com/services/upload/";

  private static final String OAUTH_TOKEN_PARAM = "oauth_token";
  private static final String OAUTH_NONCE = "oauth_nonce";
  private static final String OAUTH_TIMESTAMP = "oauth_timestamp";

  private String apiKey;
  private String signatureKey;

  private static final String REQUEST_VERB_GET = "GET";
  private static final String REQUEST_VERB_POST = "POST";
  private static final String HMAC_ALGORITHM = "HMAC-SHA1";
  private static final String CALLBACK_TARGET = "oob";

  private Map<String, String> accessMap = new HashMap<>();

  OAuth(String apiKey, String signatureKey) {
    this.apiKey = apiKey;
    this.signatureKey = signatureKey;

  }

  private String oauthEncode(String input) {
    Map<String, String> oathEncodeMap = new HashMap<>();
    oathEncodeMap.put("\\*", "%2A");
    oathEncodeMap.put("\\+", "%20");
    oathEncodeMap.put("%7E", "~");
    String encoded = "";
    try {
      encoded = URLEncoder.encode(input, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      Logger.getGlobal().severe(e.getMessage());
    }
    for (Map.Entry<String, String> entry : oathEncodeMap.entrySet()) {
      encoded = encoded.replaceAll(entry.getKey(), entry.getValue());
    }
    return encoded;
  }

  private String getSignature(String key, String data) {
    SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "HmacSHA1");
    Mac macInstance = null;
    try {
      macInstance = Mac.getInstance("HmacSHA1");
      macInstance.init(keySpec);
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      Logger.getGlobal().severe(e.getMessage());
    }
    byte[] signedBytes = macInstance != null ? macInstance.doFinal(data.getBytes()) : new byte[0];
    return Base64.getEncoder().encodeToString(signedBytes);
  }

  private String generateTokenUrl(String target, TreeMap<String, String> map, String secret) {

    StringBuilder unencBaseString3 = new StringBuilder();
    int i = 1;
    for (Map.Entry<String, String> entry : map.entrySet()) {
      unencBaseString3.append(entry.getKey());
      unencBaseString3.append("=");
      unencBaseString3.append(entry.getValue());
      if (i < map.size()) {
        unencBaseString3.append("&");
      }
      i++;
    }

    String url2Sign =
        REQUEST_VERB_GET + "&" + oauthEncode(target) + "&" + oauthEncode(
            unencBaseString3.toString());
    Logger.getGlobal().info(url2Sign);

    String signature = getSignature(secret, url2Sign);
    String signatureParam = "oauth_signature=" + oauthEncode(signature);

    return target + "?" + unencBaseString3 + "&" + signatureParam;
  }

  private TreeMap<String, String> getParameterMap() {
    TreeMap<String, String> map = new TreeMap<>();
    map.put("oauth_consumer_key", apiKey);
    map.put(OAUTH_NONCE, String.valueOf(System.currentTimeMillis()));
    map.put("oauth_signature_method", HMAC_ALGORITHM);
    map.put(OAUTH_TIMESTAMP, String.valueOf(System.currentTimeMillis() / 1000));
    map.put("oauth_version", "1.0");
    return map;
  }

  String generateAccessTokenUrl(String verifier) {
    TreeMap<String, String> map = getParameterMap();
    map.put(OAUTH_TOKEN_PARAM, oauthEncode(getAuthToken()));
    map.put("oauth_verifier", oauthEncode(verifier));

    return generateTokenUrl(URL_OAUTH_ACCESS_TOKEN, map, signatureKey + getAuthTokenSecret());
  }

  String generateRequestTokenUrl() {
    TreeMap<String, String> map = getParameterMap();
    map.put("oauth_callback", oauthEncode(CALLBACK_TARGET));

    return generateTokenUrl(URL_OAUTH_REQUEST_TOKEN, map, signatureKey);
  }


  String generateRestApiUrl(HashMap<String, String> methodMap) {
    TreeMap<String, String> map = getParameterMap();
    map.put(OAUTH_TOKEN_PARAM, oauthEncode(getAuthToken()));

    for (Map.Entry<String, String> entry : methodMap.entrySet()) {
      map.put(entry.getKey(), entry.getValue());
    }

    return generateTokenUrl(URL_SERVICES_REST, map, signatureKey + getAuthTokenSecret());
  }


  void parseAndStoreResult(String result) {
    String[] pairs = result.split("&");
    for (String pair : pairs) {
      String[] kv = pair.split("=");
      accessMap.put(kv[0], kv[1]);
    }
  }

  String getAuthToken() {
    return accessMap.get(OAUTH_TOKEN_PARAM);
  }

  private String getAuthTokenSecret() {
    return accessMap.get("oauth_token_secret");
  }

  Map getAccessMap() {
    return accessMap;
  }

  void setAuthStoreFile(String authStoreFile) {
    if (new File(authStoreFile).exists()) {
      try (FileReader reader = new FileReader(authStoreFile)) {
        BufferedReader br = new BufferedReader(reader);
        parseAndStoreResult(br.readLine());
      } catch (IOException e) {
        Logger.getGlobal().severe(e.getMessage());
      }
    }
  }

  private String generateSignatureForUploadUrl(TreeMap<String, String> map, String secret) {

    StringBuilder unencBaseString3 = new StringBuilder();
    int i = 1;
    for (Map.Entry<String, String> entry : map.entrySet()) {
      unencBaseString3.append(entry.getKey());
      unencBaseString3.append("=");
      unencBaseString3.append(entry.getValue());
      if (i < map.size()) {
        unencBaseString3.append("&");
      }
      i++;
    }

    String url2Sign =
        REQUEST_VERB_POST + "&" + oauthEncode(URL_UPLOAD) + "&" + oauthEncode(
            unencBaseString3.toString());
    Logger.getGlobal().info(url2Sign);

    return getSignature(secret, url2Sign);
  }


  String uploadImage(String imageFilename) throws IOException {
    TreeMap<String, String> map = getParameterMap();
    map.put(OAUTH_TOKEN_PARAM, oauthEncode(getAuthToken()));

    String signature = generateSignatureForUploadUrl(map, signatureKey + getAuthTokenSecret());

    HttpClient client = HttpClientBuilder.create().build();
    HttpPost httppost = new HttpPost(URL_UPLOAD);

    HttpEntity reqEntity = MultipartEntityBuilder
        .create()
        .addPart("oauth_consumer_key", new StringBody(oauthEncode(apiKey), ContentType.DEFAULT_TEXT))
        .addPart(OAUTH_NONCE, new StringBody(map.get(OAUTH_NONCE), ContentType.DEFAULT_TEXT))
        .addPart("oauth_signature", new StringBody(signature, ContentType.DEFAULT_TEXT))
        .addPart("oauth_signature_method", new StringBody(HMAC_ALGORITHM, ContentType.DEFAULT_TEXT))
        .addPart(OAUTH_TIMESTAMP, new StringBody(map.get(OAUTH_TIMESTAMP), ContentType.DEFAULT_TEXT))
        .addPart(OAUTH_TOKEN_PARAM, new StringBody(oauthEncode(getAuthToken()), ContentType.DEFAULT_TEXT))
        .addPart("oauth_version", new StringBody(oauthEncode("1.0"), ContentType.DEFAULT_TEXT))
        .addBinaryBody("photo", new File(imageFilename), ContentType.create("image/jpeg"),
            imageFilename)
        .build();

    httppost.setEntity(reqEntity);

    HttpResponse response = client.execute(httppost);
    String statusCode = Integer.toString(response.getStatusLine().getStatusCode());
    Logger.getGlobal().info(statusCode);

    BufferedReader contentReader = new BufferedReader(
        new InputStreamReader(response.getEntity().getContent()));

    String line;
    StringBuilder result = new StringBuilder();
    while ((line = contentReader.readLine()) != null) {
      result.append(line);

    }
    Logger.getGlobal().info(result.toString());
    return result.toString();

  }
}
