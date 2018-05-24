package oauthtest.contacts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "page",
    "pages",
    "per_page",
    "perpage",
    "total",
    "contact"
})
public class Contacts {

  @JsonProperty("page")
  private Integer page;
  @JsonProperty("pages")
  private Integer pages;
  @JsonProperty("per_page")
  private Integer perPage;
  @JsonProperty("perpage")
  private Integer entriesperpage;
  @JsonProperty("total")
  private Integer total;
  @JsonProperty("contact")
  private List<Contact> contact = null;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("page")
  public Integer getPage() {
    return page;
  }

  @JsonProperty("page")
  public void setPage(Integer page) {
    this.page = page;
  }

  @JsonProperty("pages")
  public Integer getPages() {
    return pages;
  }

  @JsonProperty("pages")
  public void setPages(Integer pages) {
    this.pages = pages;
  }

  @JsonProperty("per_page")
  public Integer getPerPage() {
    return perPage;
  }

  @JsonProperty("per_page")
  public void setPerPage(Integer perPage) {
    this.perPage = perPage;
  }

  @JsonProperty("perpage")
  public Integer getEntriesPerpage() {
    return entriesperpage;
  }

  @JsonProperty("perpage")
  public void setEntriesPerpage(Integer entriesperpage) {
    this.entriesperpage = entriesperpage;
  }

  @JsonProperty("total")
  public Integer getTotal() {
    return total;
  }

  @JsonProperty("total")
  public void setTotal(Integer total) {
    this.total = total;
  }

  @JsonProperty("contact")
  public List<Contact> getContact() {
    return contact;
  }

  @JsonProperty("contact")
  public void setContact(List<Contact> contact) {
    this.contact = contact;
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

