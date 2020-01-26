package currencyconverter.data.feeds;

import currencyconverter.data.DataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public class DataFeedManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataFeedManager.class);
    private final List<DataFeed> dataFeeds;

    public DataFeedManager(List<DataFeed> dataFeeds) {
        this.dataFeeds = dataFeeds;
    }

    public void populateExchangeRateDataStore(DataStore datastore) {
        for (DataFeed dataFeed : dataFeeds) {
            LocalDateTime feedLastUpdated = dataFeed.lastUpdated();
            if (newRatesPublished(feedLastUpdated, datastore.getLastUpdated())) {
                LOGGER.debug("new rates file published");
                File ratesFile = dataFeed.createDirs();
                dataFeed.download(ratesFile);
                dataFeed.ingest(ratesFile, datastore);
                datastore.setLastUpdated(feedLastUpdated);
            }
        }
    }

    private boolean newRatesPublished(LocalDateTime feedLastUpdated, LocalDateTime storedLastUpdated) {
        boolean published = false;
        if (feedLastUpdated != null) {
            published = feedLastUpdated.isAfter(storedLastUpdated);
        }
        return published;
    }

}
