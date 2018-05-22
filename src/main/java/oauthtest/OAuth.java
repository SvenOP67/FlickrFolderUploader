package oauthtest;

import java.io.UnsupportedEncodingException;
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

class OAuth {

  private String apiKey;
  private String signatureKey;

  private static final String REQUEST_VERB = "GET";
  private static final String HMAC_ALGORITHM = "HMAC-SHA1";
  private static final String CALLBACK_TARGET = "oob";

  private Map map = new HashMap();

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

  String generateTokenUrl(String target, TreeMap<String, String> map, String secret) {

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
        REQUEST_VERB + "&" + oauthEncode(target) + "&" + oauthEncode(unencBaseString3.toString());
    Logger.getGlobal().info(url2Sign);

    String signature = getSignature(signatureKey, url2Sign);
    String signatureParam = "oauth_signature=" + oauthEncode(signature);

    return target + "?" + unencBaseString3 + "&" + signatureParam;
  }

  private TreeMap<String, String> getParameterMap() {
    TreeMap<String, String> map = new TreeMap<>();
    map.put("oauth_consumer_key", apiKey);
    map.put("oauth_nonce", String.valueOf(System.currentTimeMillis()));
    map.put("oauth_signature_method", HMAC_ALGORITHM);
    map.put("oauth_timestamp", String.valueOf(System.currentTimeMillis() / 1000));
    map.put("oauth_version", "1.0");
    return map;
  }

  String generateAccessTokenUrl(String target, String verifier) {
    TreeMap<String, String> map = getParameterMap();
    map.put("oauth_token", getAuthToken());
    map.put("oauth_verifier", verifier);

    return generateTokenUrl(target, map, signatureKey + getAuthTokenSecret());
  }

  String generateRequestTokenUrl(String target) {
    TreeMap<String, String> map = getParameterMap();
    map.put("oauth_callback", oauthEncode(CALLBACK_TARGET));

    return generateTokenUrl(target, map, signatureKey);
  }


  void parseAndStoreResult(String result) {
    String[] pairs = result.split("&");
    for (String pair : pairs) {
      String[] kv = pair.split("=");
      map.put(kv[0], kv[1]);
    }
  }

  String getAuthToken() {
    return map.get("oauth_token").toString();
  }

  String getAuthTokenSecret() {
    return map.get("oauth_token_secret").toString();
  }
}
