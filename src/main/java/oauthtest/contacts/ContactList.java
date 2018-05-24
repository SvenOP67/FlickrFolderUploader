package oauthtest.contacts;

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
    "contacts",
    "stat"
})
public class ContactList {

  @JsonProperty("contacts")
  private Contacts contacts;
  @JsonProperty("stat")
  private String stat;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("contacts")
  public Contacts getContacts() {
    return contacts;
  }

  @JsonProperty("contacts")
  public void setContacts(Contacts contacts) {
    this.contacts = contacts;
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
