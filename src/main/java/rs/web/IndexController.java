package rs.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//@RestController
public class IndexController {

    @RequestMapping(method = RequestMethod.GET)
    public String sayHello() {
        return "hello";
    }
}
