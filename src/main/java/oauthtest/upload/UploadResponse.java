package oauthtest.upload;

public class UploadResponse {
  private String photoid;
  private String stat;

  public UploadResponse(String photoid, String stat) {
    this.photoid = photoid;
    this.stat = stat;
  }

// Getter Methods

  public String getPhotoid() {
    return photoid;
  }

  public String getStat() {
    return stat;
  }

}
