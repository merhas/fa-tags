package fr.xorus.software.fa;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xorus on 26/10/2015.
 */
public class ViewPage {
    Document document;

    public ViewPage(Document document) {
        this.document = document;
    }

    public List<String> parseImageTags() {
        Elements keywords = document.select("#keywords a");

        List<String> keywordList = new ArrayList<String>();
        for (Element keyword : keywords) {
            keywordList.add(keyword.text().toLowerCase());
        }

        return keywordList;
    }

    public String parseImageLink() {
        Elements elms = document.select("a:contains(Download)");
        if(elms.size() > 0) {
            return FurAffinity.FA_PROTOCOL + elms.first().attr("href");
        }
        return null;
    }
}
