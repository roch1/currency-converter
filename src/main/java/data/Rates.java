package data;

import model.Rate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

public class Rates {

    private final Map<String, Rate> rates = new HashMap<>();
    private LocalDate lastUpdated = LocalDate.now().minusDays(10);

    public Rate getRate(String currencyCode) {
        return rates.get(currencyCode);
    }

    public void parse(File ratesFile) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            DocumentBuilder docbuilder = dbf.newDocumentBuilder();
            Document doc = docbuilder.parse(ratesFile);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("Cube");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element e = (Element) nodeList.item(i);
                if (e.hasAttribute("currency")) {
                    String currencyCode = e.getAttribute("currency");
                    String fxRate = e.getAttribute("rate");
                    rates.put(currencyCode, new Rate(Currency.getInstance(currencyCode), new BigDecimal(fxRate)));
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    private void putRate(String currencyCode, Rate rate) {
        rates.put(currencyCode, rate);
    }

}
