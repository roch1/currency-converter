package service;

import data.Rates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RateGetter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RateGetter.class);
    private static final String DAILY_RATES_URL = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    private static final String FILE_NAME = ".\\src\\main\\resources\\rates\\daily_fx_rates.xml";

    public void getRates(Rates rates) {
        try {
            final URL dailyRatesUrl = new URL(DAILY_RATES_URL);
            LocalDate fileDate = lastUpdated(dailyRatesUrl);
            if (published(fileDate, rates.getLastUpdated())) {
                File ratesFile = createDirs();
                refresh(ratesFile, dailyRatesUrl);
                rates.parse(ratesFile);
                rates.setLastUpdated(fileDate);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private LocalDate lastUpdated(URL dailyRates) {
        LocalDate updatedDate = null;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(dailyRates.openStream()))) {
            String tag = "<Cube time='";
            String input;
            while ((input = in.readLine()) != null) {
                if (input.contains(tag)) {
                    String date = input.substring(tag.length() + 2, input.length() - 2); // account for tab characters
                    updatedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return updatedDate;
    }

    private boolean published(LocalDate fileDate, LocalDate lastUpdated) {
        boolean published = false;
        if (fileDate != null) {
            published = fileDate.isAfter(lastUpdated);
        }
        return published;
    }

    private long refresh(File file, URL dailyRates) {
        long bytesTransferred = 0;
        try (ReadableByteChannel rbc = Channels.newChannel(dailyRates.openStream());
             FileOutputStream fos = new FileOutputStream(file)) {
            LOGGER.info("downloading new rates file");
            bytesTransferred = fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            LOGGER.info("new rates file downloaded");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytesTransferred;
    }

    private File createDirs() {
        Path path = Paths.get(FILE_NAME);
        if (Files.notExists(path)) {
            path.toFile().getParentFile().mkdirs();
        }
        return path.toFile();
    }

}
