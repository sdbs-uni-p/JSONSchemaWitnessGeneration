package util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.IOUtils;

/**
 * Offers utils for <code>URLs</code>.
 * 
 * @author Lukas Ellinger
 */
public class URLLoader {

  /**
   * Returns the data of a <code>URL</code> after following all redirects. If http response code 429
   * is returned, the time in retry-after header is waited. After that it is tried to load the data.
   * 
   * @param url of which the data should be returned. Its <code>scheme</code> has to be http or
   *        https.
   * @return data of url after following all redirects.
   * @throws IOException if data could not be fetched.
   */
  public synchronized static String loadWithRedirect(URL url) throws IOException {
    URLConnection conn = url.openConnection();

    if (conn instanceof HttpURLConnection) {
      HttpURLConnection HttpConn = (HttpURLConnection) conn;

      boolean redirect = false;
      int status = HttpConn.getResponseCode();

      if (status >= 300 && status < 400) {
        redirect = true;
      }

      if (status >= 400 && status < 600 && status != 429) {
        Log.info(status + " " + url);
      }

      if (status == 429) {
        String wait = HttpConn.getHeaderField("Retry-After");

        try {
          TimeUnit.SECONDS.sleep(Integer.parseInt(wait));
          loadWithRedirect(url);
        } catch (NumberFormatException e) {
          Log.info(status + " " + wait + " " + url);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

      if (redirect) {
        return loadWithRedirect(new URL(HttpConn.getHeaderField("Location")));
      } else {
        return IOUtils.toString(url, "UTF-8");
      }
    } else {
      return IOUtils.toString(url, "UTF-8");
    }
  }
}
