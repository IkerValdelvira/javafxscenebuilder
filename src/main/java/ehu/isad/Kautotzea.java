package ehu.isad;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.util.AuthStore;
import com.flickr4java.flickr.util.FileAuthStore;
import com.flickr4java.flickr.util.IOUtilities;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuth1Token;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

public class Kautotzea {

    private static Kautotzea nKautotzea;
    private final String nsid;
    private final Flickr flickr;
    private AuthStore authStore;

    private Kautotzea() throws IOException, FlickrException {
        Properties properties;
        InputStream in = null;
        try {
            in = Kautotzea.class.getResourceAsStream("/setup.properties");
            properties = new Properties();
            properties.load(in);
        } finally {
            IOUtilities.close(in);
        }

        flickr = new Flickr(properties.getProperty("apiKey"), properties.getProperty("secret"), new REST());
        this.nsid = properties.getProperty("nsid");

        File authsDir = new File(properties.getProperty("authsDir"));
        if (authsDir != null) {
            this.authStore = new FileAuthStore(authsDir);
        }
    }

    public static synchronized Kautotzea getKautotzea() throws IOException, FlickrException {
        if(Kautotzea.nKautotzea == null){
            Kautotzea.nKautotzea = new Kautotzea();
        }
        return Kautotzea.nKautotzea;
    }

    public void kautotu() throws Exception {
        RequestContext rc = RequestContext.getRequestContext();

        if (this.authStore != null) {
            Auth auth = this.authStore.retrieve(this.nsid);
            if (auth == null) {
                this.authorize(); // throws Exception
            } else {
                rc.setAuth(auth);
            }
        }
    }

    public void authorize() throws Exception {
        AuthInterface authInterface = flickr.getAuthInterface();
        OAuth1RequestToken requestToken = authInterface.getRequestToken();

        String url = authInterface.getAuthorizationUrl(requestToken, Permission.DELETE);
        System.out.println("Follow this URL to authorise yourself on Flickr");
        System.out.println(url);
        System.out.println("Paste in the token it gives you:");
        System.out.print(">>");

        String tokenKey = new Scanner(System.in).nextLine();

        OAuth1Token accessToken = authInterface.getAccessToken(requestToken, tokenKey);

        Auth auth = authInterface.checkToken(accessToken);
        RequestContext.getRequestContext().setAuth(auth);
        this.authStore.store(auth);
        System.out.println("Thanks.  You probably will not have to do this every time.  Now starting backup.");
    }

    public Flickr getFlickr(){
        return flickr;
    }

    public String getNsid(){
        return nsid;
    }

}
