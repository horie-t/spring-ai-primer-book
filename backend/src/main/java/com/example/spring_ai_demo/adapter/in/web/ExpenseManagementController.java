package com.example.spring_ai_demo.adapter.in.web;

import com.example.spring_ai_demo.adapter.out.saas.ExpenseManagementLLMService;
import com.example.spring_ai_demo.application.domain.model.ExpenseAnalysis;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExpenseManagementController {
    private final ExpenseManagementLLMService expenseManagementLLMService;

    public ExpenseManagementController(ExpenseManagementLLMService expenseManagementLLMService) {
        this.expenseManagementLLMService = expenseManagementLLMService;
    }

    @PostMapping("/api/expense:classify")
    public ExpenseAnalysis classifyExpenses(@RequestBody String expense) {
        return expenseManagementLLMService.classifyExpenses(expense);
    }
}
