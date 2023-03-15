package com.app.service;

import java.util.List;

public class OrderWorkFlow implements WorkFlow {

    private final List<WorkFlowStep> steps;

    public OrderWorkFlow(List<WorkFlowStep> steps) {
        this.steps = steps;
    }

    @Override
    public List<WorkFlowStep> getSteps() {
        return this.steps;
    }
}