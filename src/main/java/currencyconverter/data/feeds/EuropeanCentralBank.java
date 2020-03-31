package currencyconverter.data.feeds;

import currencyconverter.http.CurrencyConverterHttpClient;
import currencyconverter.service.DataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class EuropeanCentralBank implements DataFeed {

    private static final Logger LOGGER = LoggerFactory.getLogger(EuropeanCentralBank.class);
    private static final String DAILY_RATES_URL = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    private static final String FILE_NAME = ".\\src\\main\\resources\\rates\\ecb_daily_fx_rates.xml";
    private final DataStore dataStore;
    private final HttpClient httpClient;
    private final HttpRequest httpRequest;

    public EuropeanCentralBank(DataStore dataStore, CurrencyConverterHttpClient currencyConverterHttpClient) {
        this.dataStore = dataStore;
        this.httpClient = currencyConverterHttpClient.getHttpClient();
        this.httpRequest = createHttpRequest(); // request is always the same so only needs to be created once
    }

    @Override
    public void ingest() {
        HttpResponse<String> response = sendHttpRequest(httpRequest);
        String ratesData = response.body();
        LocalDateTime feedLastUpdated = lastUpdated(ratesData);

        if (newRatesPublished(feedLastUpdated, dataStore.getLastUpdated())) {
            LOGGER.debug("new ECB rates file published");
            File ratesFile = createDirs();
            writeToFile(ratesData, ratesFile);
            saveToDb(ratesData);
            dataStore.setLastUpdated(feedLastUpdated);
        }
    }

    private HttpRequest createHttpRequest() {
        return HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(DAILY_RATES_URL))
                .build();
    }

    private HttpResponse<String> sendHttpRequest(HttpRequest httpRequest) {
        return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .join();
    }

    private LocalDateTime lastUpdated(String responseBody) {
        String[] lines = responseBody.split("\n");
        LocalDateTime updatedDate = null;
        String tag = "<Cube time='";

        for (String line : lines) {
            if (line.contains(tag)) {
                String date = line.substring(tag.length() + 2, line.length() - 2); // account for 2 tab characters at start of string
                LocalDate feedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                updatedDate = LocalDateTime.of(feedDate, LocalTime.now());
                break;
            }
        }

        return updatedDate;
    }

    private boolean newRatesPublished(LocalDateTime feedLastUpdated, LocalDateTime storedLastUpdated) {
        boolean published = false;
        if (feedLastUpdated != null) {
            published = feedLastUpdated.isAfter(storedLastUpdated);
        }
        return published;
    }

    private File createDirs() {
        Path path = Paths.get(FILE_NAME);
        if (Files.notExists(path)) {
            path.toFile().getParentFile().mkdirs();
        }
        return path.toFile();
    }

    private void writeToFile(String ratesData, File fileToWriteTo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToWriteTo))) {
            writer.write(ratesData);
        } catch (IOException e) {
            LOGGER.error("error writing to file", e);
        }
    }

    private void saveToDb(String ratesData) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try (InputStream is = new ByteArrayInputStream(ratesData.getBytes(StandardCharsets.UTF_8))) {
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            DocumentBuilder docbuilder = dbf.newDocumentBuilder();
            Document doc = docbuilder.parse(is);
            doc.getDocumentElement().normalize();

            LOGGER.info("starting rates data saving");

            NodeList nodeList = doc.getElementsByTagName("Cube");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element e = (Element) nodeList.item(i);
                if (e.hasAttribute("currency")) {
                    String currencyCode = e.getAttribute("currency");
                    String fxRate = e.getAttribute("rate");
                    dataStore.putRate(currencyCode, fxRate);
                }
            }

            LOGGER.info("finished rates data saving");
        } catch (ParserConfigurationException | IOException | SAXException e) {
            LOGGER.error("error setting feature or saving rates data", e);
        }
    }

}
