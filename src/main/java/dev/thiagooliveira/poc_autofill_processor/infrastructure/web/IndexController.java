package dev.thiagooliveira.poc_autofill_processor.infrastructure.web;

import dev.thiagooliveira.poc_autofill_processor.config.security.AuthenticatedUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {

    @GetMapping
    public String index(@AuthenticationPrincipal AuthenticatedUser user, Model model) {
        model.addAttribute("user", user.getUser());
        return "index";
    }
}
