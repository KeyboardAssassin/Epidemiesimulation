package com.codewithdani.functionality;


import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SimulationRunnerImpl implements SimulationRunner {
    @Override
    @Async
    public void runSimulation(Simulation simulation, int amountOfSimulations) {
        simulation.startSimulation(amountOfSimulations);
    }
}
