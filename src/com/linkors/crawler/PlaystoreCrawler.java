/**
 * PlaystoreCrawler is a application to get application package name
 * from Google Play Store based on searching application name
 *
 * @author  Kelvin
 * @version 1.0
 * @since   2018-08-31
 */
package com.linkors.crawler;

import com.linkors.crawler.model.Apps;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class PlaystoreCrawler {

    static public void trustAllHost() {
        try {
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        // Install the all-trusting trust manager
        SSLContext sc = null;

        sc = SSLContext.getInstance("SSL");

        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    // Main Method to get list of application
    public static List<Apps> SearchApplication (String appName) {
        try {
            trustAllHost();

            // Get link from see more button
            String url = "https://play.google.com/store/search?q=" + appName + "&hl=en&authuser=0";
            Document doc = Jsoup.connect(url).post();
            String appUrl = doc.select(".see-more").get(0).attr("href");

            // Use link from see more button to get more list of application
            String baseUrl = "https://play.google.com";
            doc = Jsoup.connect(baseUrl + appUrl)
                    .method(org.jsoup.Connection.Method.POST)
                    .execute().parse();

            Elements appDiv = doc.select(".card.apps");

            List<Apps> appsList = new ArrayList<Apps>();
            String name;
            String packageName;
            String icon;
            String star;
            String price;
            Elements temp;

            // Parse each element into Apps object @see com.linkors.crawler.model.Apps
            for (Element src : appDiv) {
                packageName = src.attr("data-docid");
                name = src.select(".title").get(0).attr("title");
                icon = src.select(".cover-image").get(0).attr("data-cover-small").replace("-rw", "");

                temp = src.select(".tiny-star");
                star = temp.size() > 0 ? temp.get(0).attr("aria-label") : "";
                price = src.select(".display-price").get(0).html();
                appsList.add(new Apps(name ,packageName, icon, star, price));
            }
            return appsList;
        } catch (Exception e) {
            print("Playstore Crawler Error: %s", e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        String appName = "";
        if (args.length == 1) {
            appName = args[0];
        } else {
            appName = "instagram";
        }

        List<Apps> list = SearchApplication(appName);
        StringBuffer sb = new StringBuffer();
        for (Apps app: list) {
            print("-\tName\t\t\t: %s\n\tPackage Name\t: %s\n\trating\t\t\t: %s\n\n", app.getName(), app.getPackageName(), app.getStar());
        }

    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }

}
