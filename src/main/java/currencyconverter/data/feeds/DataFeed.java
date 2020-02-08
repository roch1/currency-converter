package currencyconverter.data.feeds;

import currencyconverter.data.DataStore;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public abstract class DataFeed {

    private final String name;
    private final String localFilePath;

    public DataFeed(String name, String localFilePath) {
        this.name = name;
        this.localFilePath = localFilePath;
    }

    abstract void ingest(File localRatesFile, DataStore datastore);
    abstract long download(File localRatesFile);
    abstract LocalDateTime lastUpdated();

    public File createDirs() {
        Path path = Paths.get(localFilePath);
        if (Files.notExists(path)) {
            path.toFile().getParentFile().mkdirs();
        }
        return path.toFile();
    }

}
