/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.tokopedia.scrapper;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import config.WebClientConfig;

/**
 *
 * @author abdulsalam
 */
public class MainScrapper {

    public static void main(String[] args) {
        WebClient webClient = WebClientConfig.init(BrowserVersion.CHROME, args);
        String res = WebClientConfig.Scrap(webClient);

        System.out.println(res);
    }
}
