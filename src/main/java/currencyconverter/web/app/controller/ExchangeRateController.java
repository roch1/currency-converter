package currencyconverter.web.app.controller;

import currencyconverter.data.DataStore;
import currencyconverter.domain.ConverterResponse;
import currencyconverter.domain.Currency;
import currencyconverter.service.Converter;
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
    private final Converter currencyConverter;

    public ExchangeRateController(DataStore datastore, Converter currencyConverter) {
        this.datastore = datastore;
        this.currencyConverter = currencyConverter;
    }

    // @GetMapping for base path /rates

    @GetMapping("/{currencyCode}")
    public Currency rateByCode(@PathVariable("currencyCode") String currencyCode) {
        return datastore.getCurrency(currencyCode);
    }

    @GetMapping("/currencypair")
    public ConverterResponse currencyPair(@RequestParam(name = "base") String baseCurrCode, @RequestParam(name = "quote") String quoteCurrCode) {
        return currencyConverter.convert(baseCurrCode, quoteCurrCode, BigDecimal.ONE);
    }

    @GetMapping("/conversion")
    public ConverterResponse conversion(@RequestParam(name = "base") String baseCurrCode, @RequestParam(name = "quote") String quoteCurrCode,
                                   @RequestParam(name = "amount") BigDecimal amount) {
        return currencyConverter.convert(baseCurrCode, quoteCurrCode, amount);
    }

}
