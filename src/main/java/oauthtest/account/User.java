package oauthtest.account;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "nsid",
    "username"
})
public class User {

  @JsonProperty("id")
  private String id;
  @JsonProperty("nsid")
  private String nsid;
  @JsonProperty("username")
  private Username username;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("id")
  public String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(String id) {
    this.id = id;
  }

  @JsonProperty("nsid")
  public String getNsid() {
    return nsid;
  }

  @JsonProperty("nsid")
  public void setNsid(String nsid) {
    this.nsid = nsid;
  }

  @JsonProperty("username")
  public Username getUsername() {
    return username;
  }

  @JsonProperty("username")
  public void setUsername(Username username) {
    this.username = username;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

}
