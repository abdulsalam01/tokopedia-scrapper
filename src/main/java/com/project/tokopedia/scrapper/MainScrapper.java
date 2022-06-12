/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.tokopedia.scrapper;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import config.WebClientConfig;
import java.util.List;
import utils.GenerateFile;

/**
 *
 * @author abdulsalam
 */
public class MainScrapper {

    public static void main(String[] args) {
        Object[] proxy = new Object[]{"ProxyServer", 8080};
        WebClient webClient = WebClientConfig.init(BrowserVersion.CHROME, proxy);
        List<String[]>res = WebClientConfig.Scrap(webClient);

        // save to file
        String path = System.getProperty("user.dir") + "src/main/java/export";
        GenerateFile.writeToCsv(path + "/tokped_data_scrap.csv", res);
    }
}
