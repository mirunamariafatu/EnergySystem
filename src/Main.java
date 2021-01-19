import action.EnergySystem;
import database.ConsumerDB;
import database.DistributorDB;
import filesystem.Input;
import filesystem.InputLoader;
import filesystem.OutputWriter;

/**
 * The entry point to this project.
 */
public final class Main {
    /**
     * For coding style
     */
    private Main() {
    }

    /**
     * Reads from input, process the data, run the simulation and
     * writes the results to the output file.
     *
     * @param args from command line
     * @throws Exception in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws Exception {

        // Read data from input file
        InputLoader inputLoader = new InputLoader(args[0]);
        Input input = inputLoader.initializeInput();

        // Create databases for entities
        ConsumerDB consumersData = new ConsumerDB(input.setConsumersData());
        DistributorDB distributorsData = new DistributorDB(input.setDistributorsData());

        // Create the energy system simulation
        EnergySystem simulation = EnergySystem.getInstance();

        // Run the simulation
        simulation.energySystemSimulation(consumersData,
                distributorsData, input);

        // Write the results to the output file
        OutputWriter out = new OutputWriter(args[1], consumersData, distributorsData);
        out.createOutput();

    }
}
