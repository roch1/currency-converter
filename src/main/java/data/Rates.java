package data;

import model.Rate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Rates {

    private static final Logger LOGGER = LoggerFactory.getLogger(Rates.class);
    private static final int INITIAL_CAPACITY = 32;
    private static final ConcurrentMap<String, Rate> INSTANCES = new ConcurrentHashMap<>(INITIAL_CAPACITY);
    private final Map<Rate, BigDecimal> rates = new HashMap<>(INITIAL_CAPACITY);
    private LocalDate lastUpdated = LocalDate.now().minusDays(4);

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
                    putRate(currencyCode, fxRate);
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

    public boolean empty() {
        return rates.isEmpty();
    }

    public void putRate(String currencyCode, String fxRate) {
        rates.put(getCurrency(currencyCode), new BigDecimal(fxRate));
    }

    public Rate getCurrency(String currencyCode) {
        Rate instance = INSTANCES.get(currencyCode);
        if (instance != null) {
            return instance;
        }

        return createRate(currencyCode);
    }

    public BigDecimal getRate(Rate rate) {
        return rates.get(rate);
    }

    private Rate createRate(String currencyCode) {
        LOGGER.info("creating rate {}", currencyCode);
        Currency currency = Currency.getInstance(currencyCode);
        Rate rate = new Rate(currencyCode, currency.getSymbol(), currency.getDisplayName());
        Rate instance = INSTANCES.putIfAbsent(currencyCode, rate);
        return instance != null ? instance : rate;
    }

}
