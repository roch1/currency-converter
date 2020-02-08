package currencyconverter.web.app.controller;

import currencyconverter.data.DataStore;
import currencyconverter.domain.ConverterResponse;
import currencyconverter.domain.Currency;
import currencyconverter.service.ConversionManager;
import currencyconverter.service.CurrencyConverter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping(path = "/rates", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExchangeRateController {

    private final DataStore datastore;
    private final ConversionManager conversionManager;

    public ExchangeRateController(DataStore datastore, ConversionManager conversionManager) {
        this.datastore = datastore;
        this.conversionManager = conversionManager;
    }

    // @GetMapping for base path /rates

    @GetMapping("/{currencyCode}")
    public Currency rateByCode(@PathVariable("currencyCode") String currencyCode) {
        return datastore.getCurrency(currencyCode);
    }

    @GetMapping("/currencypair")
    public ConverterResponse currencyPair(@RequestParam(name = "base") String baseCurrCode, @RequestParam(name = "quote") String quoteCurrCode) {
        return conversionManager.convertCurrency(baseCurrCode, quoteCurrCode, BigDecimal.ONE);
    }

    @GetMapping("/conversion")
    public ConverterResponse conversion(@RequestParam(name = "base") String baseCurrCode, @RequestParam(name = "quote") String quoteCurrCode,
                                   @RequestParam(name = "amount") BigDecimal amount) {
        return conversionManager.convertCurrency(baseCurrCode, quoteCurrCode, amount);
    }

}
