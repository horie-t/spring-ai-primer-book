package com.example.spring_ai_demo.adapter.in.web;

import com.example.spring_ai_demo.adapter.out.saas.CustomerSupportRoutingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/support")
public class CustomerSupportController {
    private final CustomerSupportRoutingService routingService;

    public CustomerSupportController(CustomerSupportRoutingService routingService) {
        this.routingService = routingService;
    }

    @PostMapping("/inquiry")
    public SupportResponse handleInquiry(@RequestBody SupportRequest request) {
        String response = routingService.route(request.message());

        return new SupportResponse(response);
    }

    public record SupportRequest(String message) {}
    public record SupportResponse(String response) {}
}
