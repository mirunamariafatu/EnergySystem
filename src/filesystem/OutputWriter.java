package filesystem;

import database.ConsumerDB;
import database.DistributorDB;
import database.ProducerDB;
import documents.Contract;
import documents.MonthlyReport;
import entities.Consumer;
import entities.Distributor;
import entities.Producer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.Constants;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

/**
 * The class writes the output in files.
 */
@SuppressWarnings({"deprecation", "unchecked"})
public final class OutputWriter {
    /**
     * The output file name
     */
    private final String outputTest;
    /**
     * Information about consumers and
     * distributors database
     */
    private final ConsumerDB consumerData;
    private final DistributorDB distributorData;
    private final ProducerDB producerData;

    public OutputWriter(final String outputTest, final ConsumerDB consumerData,
                        final DistributorDB distributorData, ProducerDB producerData) {
        this.outputTest = outputTest;
        this.consumerData = consumerData;
        this.distributorData = distributorData;
        this.producerData = producerData;
    }

    /**
     * Method that transforms the output in a JSONArray.
     *
     * @return a JSONArray of consumers
     */
    public JSONArray getConsumersArray() {
        JSONArray consumersArray = new JSONArray();

        // Create the structure of a consumer output
        for (Consumer c : consumerData.getConsumers()) {
            JSONObject consumerStructure = new JSONObject();
            consumerStructure.put(Constants.ID, c.getId());
            consumerStructure.put(Constants.IS_BANKRUPT, c.getIsBankrupt());
            consumerStructure.put(Constants.BUDGET, c.getBudget());
            consumersArray.add(consumerStructure);
        }
        return consumersArray;
    }

    /**
     * Method that transforms the output in a JSONArray.
     *
     * @return a JSONArray of distributors
     */
    public JSONArray getDistributorsArray() {
        JSONArray distributorsArray = new JSONArray();

        // Create the structure of a distributor output
        for (Distributor d : distributorData.getDistributors()) {
            JSONObject distributorStructure = new JSONObject();
            distributorStructure.put(Constants.ID, d.getId());
            distributorStructure.put(Constants.CONTRACT_COST, d.getPrice());
            distributorStructure.put(Constants.ENERGY_NEEDED_KW, d.getEnergyNeededKW());
            distributorStructure.put(Constants.BUDGET, d.getBudget());
            distributorStructure.put(Constants.PRODUCER_STRATEGY, d.getStrategyTitle().getLabel());
            distributorStructure.put(Constants.IS_BANKRUPT, d.getIsBankrupt());

            if (d.getContracts().size() != 0) {
                JSONArray contractsArray = new JSONArray();
                for (Contract c : d.getContracts()) {
                    JSONObject contractStructure = new JSONObject();
                    contractStructure.put(Constants.CONSUMER_ID, c.getConsumerId());
                    contractStructure.put(Constants.PRICE, c.getPrice());
                    contractStructure.put(Constants.REMAINED_CONTRACT_MONTHS,
                            c.getRemainedContractMonths());
                    contractsArray.add(contractStructure);
                }
                distributorStructure.put(Constants.CONTRACTS, contractsArray);
            } else {
                distributorStructure.put(Constants.CONTRACTS, d.getContracts());
            }
            distributorsArray.add(distributorStructure);
        }

        return distributorsArray;
    }

    /**
     * Method that transforms the output in a JSONArray.
     *
     * @return a JSONArray of producers
     */
    public JSONArray getProducersArray() {
        JSONArray producersArray = new JSONArray();

        // Create the structure of a producer output
        for (Producer p : producerData.getProducers()) {
            JSONObject producerStructure = new JSONObject();
            producerStructure.put(Constants.ID, p.getId());
            producerStructure.put(Constants.MAX_DISTRIBUTORS, p.getMaxDistributors());
            producerStructure.put(Constants.PRICE_KW, p.getPriceKW());
            producerStructure.put(Constants.ENERGY_TYPE, (p.getEnergyType()).getLabel());
            producerStructure.put(Constants.ENERGY_PER_DISTRIBUTOR, p.getEnergyPerDistributor());

            if (p.getReports().size() != 0) {
                JSONArray reportsArray = new JSONArray();
                for (MonthlyReport r : p.getReports()) {
                    JSONObject reportStructure = new JSONObject();
                    reportStructure.put(Constants.MONTH, r.getCurrentMonth());
                    Collections.sort(r.getDistributorsIds());
                    reportStructure.put(Constants.DISTRIBUTORS_IDS, r.getDistributorsIds());
                    reportsArray.add(reportStructure);
                }
                producerStructure.put(Constants.MONTHLY_STATS, reportsArray);
            } else {
                producerStructure.put(Constants.MONTHLY_STATS, p.getReports());
            }

            producersArray.add(producerStructure);
        }

        return producersArray;
    }

    /**
     * Method that transforms the output in a JSONObject.
     *
     * @throws IOException in case of exceptions to writing
     */
    public void createOutput() throws IOException {
        JSONObject out = new JSONObject();
        JSONArray consumersOut = getConsumersArray();
        JSONArray distributorsOut = getDistributorsArray();
        JSONArray producersOut = getProducersArray();

        out.put(Constants.CONSUMERS, consumersOut);
        out.put(Constants.DISTRIBUTORS, distributorsOut);
        out.put(Constants.ENERGY_PRODUCERS, producersOut);

        // Write to the file
        configureJSON(out);
    }

    /**
     * Method that writes to the output file and closes it.
     *
     * @param out object of JSON to be written
     * @throws IOException in case of exceptions to writing
     */
    public void configureJSON(final JSONObject out) throws IOException {
        // The file where all the data will be written
        FileWriter file = new FileWriter(outputTest);
        file.write(out.toJSONString());
        file.flush();
        file.close();
    }
}
