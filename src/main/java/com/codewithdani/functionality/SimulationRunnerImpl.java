package com.codewithdani.functionality;


import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SimulationRunnerImpl implements SimulationRunner {
    @Override
    @Async
    public void runSimulation(Simulation simulation, int amountOfSimulations) throws IOException {
        simulation.startSimulation(amountOfSimulations);
    }
}
