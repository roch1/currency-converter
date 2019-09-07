package currencyconverter.web.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    // https://github.com/gocardless/http-api-design
    // https://www.vinaysahni.com/best-practices-for-a-pragmatic-restful-api
    // https://cloud.google.com/blog/products/api-management/restful-api-design-nouns-are-good-verbs-are-bad
    // https://www.alphavantage.co/documentation/#fx
    // https://github.com/exchangeratesapi/exchangeratesapi
    // https://stackoverflow.com/questions/30967822/when-do-i-use-path-params-vs-query-params-in-a-restful-api

    /*
    * path parameters vs query/request parameters
    * - use path params to identify a specific resource (or resources)
    * - use query params to sort/filter those resources
     */

    @RequestMapping("/")
    public String index() {
        return "Hello, Spring Boot World!";
    }

}
