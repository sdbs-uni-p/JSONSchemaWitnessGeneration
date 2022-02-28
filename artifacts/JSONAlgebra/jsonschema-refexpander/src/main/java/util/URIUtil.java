package util;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import com.google.common.net.PercentEscaper;
import exception.InvalidIdentifierException;

/**
 * Offers methods to alter URIs.
 * 
 * @author Lukas Ellinger
 */
public class URIUtil {

  /**
   * If <code>uri</code> ends with # then # is removed.
   * 
   * @param uri to remove ending #.
   * @return <code>uri</code> with removed ending #.
   */
  public static URI removeTrailingHash(URI uri) {
    if (uri.toString().endsWith("#")) {
      String uriString = uri.toString();
      try {
        return new URI(uriString.substring(0, uriString.length() - 1));
      } catch (URISyntaxException e) {
        throw new IllegalArgumentException(uri.toString() + " is no valid uri");
      }
    } else {
      return uri;
    }
  }

  /**
   * Removes the fragment of <code>uri</code>.
   * 
   * @param uri to remove the fragment.
   * @return <code>uri</code> with removed fragment.
   */
  public static URI removeFragment(URI uri) {
    try {
      return new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), uri.getQuery(), null);
    } catch (URISyntaxException e) {
      throw new InvalidIdentifierException(uri.toString() + " without fragment is no valid uri");
    }
  }

  /**
   * Returns <code>URI</code> of <code>url</code>.
   * 
   * @param url to be converted.
   * @return <code>url</code> converted to <code>URI</code>.
   * @throws URISyntaxException if no valid <code>URI</code> can be created out of <code>url</code>.
   */
  public static URI urlToUri(URL url) throws URISyntaxException {
    return new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(),
        url.getPath(), url.getQuery(), url.getRef());
  }

  /**
   * Returns a percent-escaped <code>URI</code>. :/?#[]@!$&'()*+,;=-._~% are not escaped.
   * 
   * @param uri to create <code>URI</code> from.
   * @return percent-escaped <code>URI</code>. :/?#[]@!$&'()*+,;=-._~% are not escaped.
   * @throws URISyntaxException if no valid URI can be created out of <code>uri</code>.
   */
  public static URI toURI(String uri) throws URISyntaxException {
    PercentEscaper escaper = new PercentEscaper(":/?#[]@!$&'()*+,;=-._~%", false);
    return new URI(escaper.escape(uri));
  }

  /**
   * Returns parent <code>URI</code> of <code>uri</code>. Therefore <code>uri</code> is cloned but
   * its path is the parent path.
   * 
   * @param uri to get parent <code>URI</code>.
   * @return <code>URI</code> with parent path.
   */
  public static URI getParentURI(URI uri) {
    try {
      return new URI(uri.getScheme(), uri.getAuthority(), getParent(uri.getPath()), uri.getQuery(),
          uri.getFragment());
    } catch (URISyntaxException e) {
      throw new InvalidIdentifierException("Parent URI of " + uri + " is no valid URI");
    }
  }

  private static String getParent(String resourcePath) {
    int index = resourcePath.lastIndexOf('/');
    if (index > 0) {
      return resourcePath.substring(0, index);
    }
    return "/";
  }
}
