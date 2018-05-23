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
    "user",
    "stat"
})
public class AccountInfo {

  @JsonProperty("user")
  private User user;
  @JsonProperty("stat")
  private String stat;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("user")
  public User getUser() {
    return user;
  }

  @JsonProperty("user")
  public void setUser(User user) {
    this.user = user;
  }

  @JsonProperty("stat")
  public String getStat() {
    return stat;
  }

  @JsonProperty("stat")
  public void setStat(String stat) {
    this.stat = stat;
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
