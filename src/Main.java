import action.EnergySystem;
import database.ConsumerDB;
import database.DistributorDB;
import database.ProducerDB;
import filesystem.Input;
import filesystem.InputLoader;
import filesystem.OutputWriter;

/**
 * Entry point to the simulation.
 */
public final class Main {

    private Main() {
    }

    /**
     * Main function which reads the input file and starts simulation.
     *
     * @param args input and output files
     * @throws Exception might error when reading/writing/opening files, parsing JSON
     */
    public static void main(final String[] args) throws Exception {

        // Read data from input file
        InputLoader inputLoader = new InputLoader(args[0]);
        Input input = inputLoader.initializeInput();

        // Create databases for entities
        ConsumerDB consumersData = new ConsumerDB(input.setConsumersData());
        DistributorDB distributorsData = new DistributorDB(input.setDistributorsData());
        ProducerDB producersData = new ProducerDB(input.setProducersData());

        // Create the energy system simulation
        EnergySystem simulation = EnergySystem.getInstance();

        // Run the simulation
        simulation.energySystemSimulation(consumersData,
                distributorsData, producersData, input);

        // Write the results to the output file
        OutputWriter out = new OutputWriter(args[1], consumersData,
                distributorsData, producersData);
        out.createOutput();

    }
}
