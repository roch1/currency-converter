package currencyconverter.data.feeds;

import currencyconverter.data.DataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class EuropeanCentralBank extends DataFeed {

    private static final Logger LOGGER = LoggerFactory.getLogger(EuropeanCentralBank.class);
    private static final String DAILY_RATES_URL = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    private static final String FILE_NAME = ".\\src\\main\\resources\\rates\\ecb_daily_fx_rates.xml";
    private final URL feedUrl;

    public EuropeanCentralBank() {
        super("European Central Bank", FILE_NAME);
        this.feedUrl = createUrl();
    }

    @Override
    LocalDateTime lastUpdated() {
        LocalDateTime updatedDate = null;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(feedUrl.openStream()))) {
            String tag = "<Cube time='";
            String input;
            while ((input = in.readLine()) != null) {
                if (input.contains(tag)) {
                    String date = input.substring(tag.length() + 2, input.length() - 2); // account for tab characters
                    LocalDate feedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    updatedDate = LocalDateTime.of(feedDate, LocalTime.now());
                    break;
                }
            }
        } catch (IOException e) {
            LOGGER.error("error opening URL stream or reading line", e);
        }
        return updatedDate;
    }

    @Override
    long download(File localRatesFile) {
        long bytesTransferred = 0;
        try (ReadableByteChannel rbc = Channels.newChannel(feedUrl.openStream());
             FileOutputStream fos = new FileOutputStream(localRatesFile)) {
            LOGGER.info("downloading new rates file");
            bytesTransferred = fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            LOGGER.info("downloaded new rates file");
        } catch (IOException e) {
            LOGGER.error("error with channels or streams", e);
        }
        return bytesTransferred;
    }

    @Override
    void ingest(File localRatesFile, DataStore datastore) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            DocumentBuilder docbuilder = dbf.newDocumentBuilder();
            Document doc = docbuilder.parse(localRatesFile);
            doc.getDocumentElement().normalize();

            LOGGER.info("ingesting rates file");

            NodeList nodeList = doc.getElementsByTagName("Cube");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element e = (Element) nodeList.item(i);
                if (e.hasAttribute("currency")) {
                    String currencyCode = e.getAttribute("currency");
                    String fxRate = e.getAttribute("rate");
                    datastore.putRate(currencyCode, fxRate);
                }
            }

            LOGGER.info("rates file ingestion completed");
        } catch (ParserConfigurationException | IOException | SAXException e) {
            LOGGER.error("error setting feature or ingesting rates file", e);
        }
    }

    private URL createUrl() {
        URL dailyRatesUrl = null;
        try {
            dailyRatesUrl = new URL(DAILY_RATES_URL);
        } catch (IOException e) {
            LOGGER.error("error creating URL from String", e);
        }
        return dailyRatesUrl;
    }

}
