package currencyconverter.web.app.configuration;

import currencyconverter.data.Rates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import currencyconverter.service.Converter;
import currencyconverter.service.RateGetter;
import currencyconverter.service.RateScheduler;

@Configuration
public class CurrencyConverterConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyConverterConfiguration.class);

    @Bean
    public Rates rates() {
        LOGGER.info("creating Rates object - should only happen once");
        return new Rates();
    }

    @Bean
    public Converter converter() {
        LOGGER.info("creating Converter object - should only happen once");
        return new Converter(rates());
    }

    @Bean
    public RateGetter rateGetter() {
        LOGGER.info("creating RateGetter object - should only happen once");
        return new RateGetter();
    }

    @Bean
    public void rateScheduler() {
        LOGGER.info("creating RateScheduler object - should only happen once");
        RateScheduler rateScheduler = new RateScheduler(rates(), rateGetter());
        rateScheduler.startScheduling();
    }

}
