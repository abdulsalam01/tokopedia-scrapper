/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author abdulsalam
 */
public class WebClientConfig {

    public static WebClient init(BrowserVersion browser, Object[] proxy) {
        WebClient webClient = new WebClient(browser);

        // set proxy
        DefaultCredentialsProvider credentialsProvider = (DefaultCredentialsProvider) webClient.getCredentialsProvider();
        credentialsProvider.addCredentials("scrapper_proxy", "scrapperProxy135");
        
        webClient.getCurrentWindow().getJobManager().removeAllJobs();
        // default configuration
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);

        return webClient;
    }

    public static String Scrap(WebClient webClient) {
        String result = "";
        //fetching the web page
        Object[] proxy = new Object[]{"ProxyServer", 8080};
        //fetching the web page
        String scrapUrl = Constant.BASE_URL + Constant.CATEGORY_URL;

        // per-page is 60 items
        for (int i = 1; i <= 1; i++) {
            try {
                String params = "?ob=5&page=" + i;
                String query = "//div[@data-testid='lstCL2ProductList']/div";
                
                HtmlPage content = webClient.getPage(scrapUrl + params);
                List<?> list = content.getByXPath(query);
                for (int j = 0; j < list.size(); j++) {
                    DomNode node = (DomNode) list.get(i);
                    HtmlAnchor a = (HtmlAnchor) node.getFirstChild();                    
                    HtmlPage detail = a.click();
                    
                    result += detail.asXml();
                    System.out.println(a);
                    System.out.println(detail.asText());
                }
            } catch (IOException ex) {
                Logger.getLogger(WebClientConfig.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FailingHttpStatusCodeException ex) {
                Logger.getLogger(WebClientConfig.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        webClient.close();
        return result;
    }
}
