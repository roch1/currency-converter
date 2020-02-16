package currencyconverter.web.app.controller;

import currencyconverter.domain.ConverterResponse;
import currencyconverter.domain.Currency;
import currencyconverter.web.app.model.ExchangeRateModel;
import currencyconverter.web.app.service.ExchangeRateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/latest")
    public ExchangeRateModel latest() {
        return exchangeRateService.getLatestRates();
    }

    @GetMapping("/{currencyCode}")
    public Currency rateByCode(@PathVariable("currencyCode") String currencyCode) {
        return exchangeRateService.getCurrency(currencyCode);
    }

    @GetMapping("/currencypair")
    public ConverterResponse currencyPair(@RequestParam(name = "base") String baseCurrCode, @RequestParam(name = "quote") String quoteCurrCode) {
        return exchangeRateService.convertCurrency(baseCurrCode, quoteCurrCode, BigDecimal.ONE);
    }

    @GetMapping("/conversion")
    public ConverterResponse conversion(@RequestParam(name = "base") String baseCurrCode, @RequestParam(name = "quote") String quoteCurrCode,
                                   @RequestParam(name = "amount") BigDecimal amount) {
        return exchangeRateService.convertCurrency(baseCurrCode, quoteCurrCode, amount);
    }

}
