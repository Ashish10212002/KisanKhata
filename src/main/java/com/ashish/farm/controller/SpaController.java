package com.ashish.farm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaController {

    // This Regex matches any path that does NOT have a file extension (like .js or .css)
    // and captures everything so React can handle the routing.
    @RequestMapping(value = "/{path:[^\\.]*}")
    public String redirect() {
        // Forward to the React index.html
        return "forward:/index.html";
    }
}