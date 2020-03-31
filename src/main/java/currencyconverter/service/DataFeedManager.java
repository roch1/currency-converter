package currencyconverter.service;

import currencyconverter.data.feeds.DataFeed;

import java.util.List;

public class DataFeedManager {

    private final List<DataFeed> dataFeeds;

    public DataFeedManager(List<DataFeed> dataFeeds) {
        this.dataFeeds = dataFeeds;
    }

    public void populateExchangeRateDataStore() {
        for (DataFeed dataFeed : dataFeeds) {
            dataFeed.ingest();
        }
    }

}
