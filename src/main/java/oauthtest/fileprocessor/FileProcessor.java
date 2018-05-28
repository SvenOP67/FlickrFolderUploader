package oauthtest.fileprocessor;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.DosFileAttributeView;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author sven
 */
public class FileProcessor {
  private static String _startDir = "";

  protected static class MyFileFilter implements FileFilter{

    private String[] fileExt;

    public MyFileFilter(String[] fileExt) {
      super();
      this.fileExt = fileExt;
    }

    @Override
    public boolean accept(File file) {
      return file.isDirectory()|| fileNameHasValidExtension(file.getAbsolutePath());
    }

    private boolean fileNameHasValidExtension(String fileName){
      boolean extIsValid = false;

      for(String ext : fileExt){
        extIsValid = fileName.endsWith(ext);
        if(extIsValid)
          break;
      }
      return extIsValid;
    }

  }

  /**
   * @param args
   *            the command line arguments
   */
//  public static void main(String[] args) {
//    Pattern pattern = Pattern.compile(";");
//
//    String startDir = "G:\\Eigene Bilder\\";// args[0];
//    String[] fileExt = pattern.split("jpg"/*args[1]*/);
//
//    _startDir = startDir;
//
//    FileProcessor fp = new FileProcessor();
//    fp.process(startDir, fileExt);
//  }

  private void process(String startDir, String[] fileExt) {
    File f = new File(startDir);

    if (f.isDirectory()) {
      File[] dirContent = f.listFiles(new MyFileFilter(fileExt));

      for (File file : dirContent) {
        if (file.isDirectory()) {
          process(file.getAbsolutePath(), fileExt);
        } else {
          String albumName = trimAlbumName(file);
          uploadFile(albumName, file);
        }
      }
    }
  }

  private String trimAlbumName(File file) {
    String albumName = file.getParent().replace(_startDir, "");
    if (albumName.startsWith("/"))
      albumName = albumName.substring(1);

    albumName = albumName.replaceAll("/", " ");
    return albumName;
  }

  private void uploadFile(String albumName, File file) {
    // Upload as needed
    try {
      String checksum = getChecksumForSpecificFile(file);
      File f = getStorageFilenameWithChecksum(file, checksum);

      if(!f.exists()){
        System.out.println(albumName + " " + file.getName() + " " + file.length());
        // if upload was successful
        createSystemStatusStorageFile(file);
      } else {
				System.out.println(albumName + " " + file.getName() + " already uploaded!" );
      }
    } catch (Exception e) {
      Logger.getGlobal().severe(e.getMessage());
    }

  }

  private File getStorageFilenameWithChecksum(File file, String checksum) {
    File f = new File(file.getAbsolutePath() + ".FLICKR-" + checksum);
    return f;
  }

  public void createSystemStatusStorageFile(File file){
    try {
      String checkSum = getChecksumForSpecificFile(file);

      File f1 = getStorageFilenameWithChecksum(file, checkSum);

      f1.createNewFile();
      Path p = Paths.get(f1.getAbsolutePath());
      DosFileAttributeView fileAttributeView = Files.getFileAttributeView(p, DosFileAttributeView.class );

      fileAttributeView.setHidden(true);

      System.out.println(f1.getAbsolutePath());

    } catch (Exception ex) {
      Logger.getLogger(FileProcessor.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private String getChecksumForSpecificFile(File file) throws Exception {
    String checkSum = MD5Checksum.getMD5Checksum(file.getAbsolutePath());
    return checkSum;
  }
}

