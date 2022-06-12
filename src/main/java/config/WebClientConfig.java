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
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author abdulsalam
 */
public class WebClientConfig {

    public static WebClient init(BrowserVersion browser, Object[] proxy) {
//        WebClient webClient = new WebClient(browser, (String)proxy[0], (int)proxy[1]);
        WebClient webClient = new WebClient(browser);

        // set proxy
//        DefaultCredentialsProvider credentialsProvider = (DefaultCredentialsProvider) webClient.getCredentialsProvider();
//        credentialsProvider.addCredentials("scrapper_proxy", "scrapperProxy135");
        
        webClient.getCurrentWindow().getJobManager().removeAllJobs();
        // default configuration
        webClient.getOptions().setTimeout(0); // SET FOREVER
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);

        return webClient;
    }

    public static List<String[]> Scrap(WebClient webClient) {
        List<String[]> result = new ArrayList<>();
        //fetching the web page
        String scrapUrl = Constant.BASE_URL + Constant.CATEGORY_URL;

        // per-page is 60 items
        for (int i = 1; i <= 2; i++) {
            try {
                String params = "?ob=5&page=" + i;
                String query = "//div[@data-testid='lstCL2ProductList']/div";
                
                HtmlPage content = webClient.getPage(scrapUrl + params);
                List<?> list = content.getByXPath(query);
                for (int j = 0; j < list.size(); j++) {
                    DomNode node = (DomNode) list.get(j);
                    HtmlAnchor a = node.getFirstByXPath("//a[@data-testid='lnkProductContainer']");
                    HtmlPage detail = webClient.getPage(a.getHrefAttribute());

                    // wait the page to load
                    DomNode product = detail.getFirstByXPath("//h1[@data-testid='lblPDPDetailProductName']");
                    DomNode rating = detail.getFirstByXPath("//span[@data-testid='lblPDPDetailProductRatingNumber']");
                    DomNode price = detail.getFirstByXPath("//div[@data-testid='lblPDPDetailProductPrice']");
                    DomNode store = detail.getFirstByXPath("//a[@data-testid='llbPDPFooterShopName']/h2");
                    HtmlImage image = detail.getFirstByXPath("//img[@data-testid='PDPMainImage']");
                    
                    result.add(new String[]{product.asText(), rating.asText(), 
                        price.asText(), store.asText(), 
                        image.getSrcAttribute()});
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
