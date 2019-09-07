package currencyconverter.web.app.controller;

import currencyconverter.data.Rates;
import currencyconverter.domain.CurrencyPair;
import currencyconverter.domain.Rate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import currencyconverter.service.Converter;

import java.math.BigDecimal;

@RestController
@RequestMapping(path = "/rates", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExchangeRateController {

    private final Rates rates;
    private final Converter currencyConverter;

    public ExchangeRateController(Rates rates, Converter currencyConverter) {
        this.rates = rates;
        this.currencyConverter = currencyConverter;
    }

    // @GetMapping for base path /rates

    @GetMapping("/{currencyCode}")
    public Rate rateByCode(@PathVariable("currencyCode") String currencyCode) {
        return rates.getCurrency(currencyCode);
    }

    @GetMapping("/currencypair")
    public CurrencyPair currencyPair(@RequestParam(name = "base") String baseCurrCode, @RequestParam(name = "quote") String quoteCurrCode) {
        return currencyConverter.convert(baseCurrCode, quoteCurrCode, BigDecimal.ONE);
    }

    @GetMapping("/conversion")
    public CurrencyPair conversion(@RequestParam(name = "base") String baseCurrCode, @RequestParam(name = "quote") String quoteCurrCode,
                                   @RequestParam(name = "amount") BigDecimal amount) {
        return currencyConverter.convert(baseCurrCode, quoteCurrCode, amount);
    }

}
