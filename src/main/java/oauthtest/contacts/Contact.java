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
    "nsid",
    "username",
    "iconserver",
    "iconfarm",
    "ignored",
    "rev_ignored",
    "realname",
    "friend",
    "family",
    "path_alias",
    "location"
})
public class Contact {

  @JsonProperty("nsid")
  private String nsid;
  @JsonProperty("username")
  private String username;
  @JsonProperty("iconserver")
  private String iconserver;
  @JsonProperty("iconfarm")
  private Integer iconfarm;
  @JsonProperty("ignored")
  private Integer ignored;
  @JsonProperty("rev_ignored")
  private Integer revIgnored;
  @JsonProperty("realname")
  private String realname;
  @JsonProperty("friend")
  private Integer friend;
  @JsonProperty("family")
  private Integer family;
  @JsonProperty("path_alias")
  private Object pathAlias;
  @JsonProperty("location")
  private String location;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("nsid")
  public String getNsid() {
    return nsid;
  }

  @JsonProperty("nsid")
  public void setNsid(String nsid) {
    this.nsid = nsid;
  }

  @JsonProperty("username")
  public String getUsername() {
    return username;
  }

  @JsonProperty("username")
  public void setUsername(String username) {
    this.username = username;
  }

  @JsonProperty("iconserver")
  public String getIconserver() {
    return iconserver;
  }

  @JsonProperty("iconserver")
  public void setIconserver(String iconserver) {
    this.iconserver = iconserver;
  }

  @JsonProperty("iconfarm")
  public Integer getIconfarm() {
    return iconfarm;
  }

  @JsonProperty("iconfarm")
  public void setIconfarm(Integer iconfarm) {
    this.iconfarm = iconfarm;
  }

  @JsonProperty("ignored")
  public Integer getIgnored() {
    return ignored;
  }

  @JsonProperty("ignored")
  public void setIgnored(Integer ignored) {
    this.ignored = ignored;
  }

  @JsonProperty("rev_ignored")
  public Integer getRevIgnored() {
    return revIgnored;
  }

  @JsonProperty("rev_ignored")
  public void setRevIgnored(Integer revIgnored) {
    this.revIgnored = revIgnored;
  }

  @JsonProperty("realname")
  public String getRealname() {
    return realname;
  }

  @JsonProperty("realname")
  public void setRealname(String realname) {
    this.realname = realname;
  }

  @JsonProperty("friend")
  public Integer getFriend() {
    return friend;
  }

  @JsonProperty("friend")
  public void setFriend(Integer friend) {
    this.friend = friend;
  }

  @JsonProperty("family")
  public Integer getFamily() {
    return family;
  }

  @JsonProperty("family")
  public void setFamily(Integer family) {
    this.family = family;
  }

  @JsonProperty("path_alias")
  public Object getPathAlias() {
    return pathAlias;
  }

  @JsonProperty("path_alias")
  public void setPathAlias(Object pathAlias) {
    this.pathAlias = pathAlias;
  }

  @JsonProperty("location")
  public String getLocation() {
    return location;
  }

  @JsonProperty("location")
  public void setLocation(String location) {
    this.location = location;
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
