package data;

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

    private final Map<Currency, BigDecimal> rates = new HashMap<>();
    private LocalDate lastUpdated = LocalDate.now().minusDays(10); // for testing the first time program runs, need to update this variable every time a file is download

    public BigDecimal getRate(String currencyCode) {
        Currency currency = Currency.getInstance(currencyCode);
        return rates.get(currency);
    }

    public void parse(File file) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            DocumentBuilder docbuilder = dbf.newDocumentBuilder();
            Document doc = docbuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("Cube");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element e = (Element) nodeList.item(i);
                if (e.hasAttribute("currency")) {
                    rates.put(Currency.getInstance(e.getAttribute("currency")), new BigDecimal(e.getAttribute("rate")));
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

    private void putRate(Currency currency, BigDecimal rate) {
        rates.put(currency, rate);
    }

}
