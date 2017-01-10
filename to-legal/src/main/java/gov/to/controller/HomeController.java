package gov.to.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class HomeController extends BaseController {

    @RequestMapping("/")
    public RedirectView index() {
        return new RedirectView("/cidadao/login", true);
    }
}
