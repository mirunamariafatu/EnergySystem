package filesystem;

import database.ConsumerDB;
import database.DistributorDB;
import documents.Contract;
import entities.Consumer;
import entities.Distributor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.Constants;

import java.io.FileWriter;
import java.io.IOException;

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

    public OutputWriter(final String outputTest, final ConsumerDB consumerData,
                        final DistributorDB distributorData) {
        this.outputTest = outputTest;
        this.consumerData = consumerData;
        this.distributorData = distributorData;
    }

    /**
     * Method that transforms the output in a JSONArray
     *
     * @return a JSONArray of distributors
     */
    public JSONArray getConsumersArray() {
        JSONArray consumersArray = new JSONArray();
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
     * Method that transforms the output in a JSONArray
     *
     * @return a JSONArray of distributors
     */
    public JSONArray getDistributorsArray() {
        JSONArray distributorsArray = new JSONArray();

        for (Distributor d : distributorData.getDistributors()) {
            JSONObject distributorStructure = new JSONObject();
            distributorStructure.put(Constants.ID, d.getId());
            distributorStructure.put(Constants.BUDGET, d.getBudget());
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
     * Method that transforms the output in a JSONObject
     *
     * @throws IOException in case of exceptions to writing
     */
    public void createOutput() throws IOException {
        JSONObject out = new JSONObject();
        JSONArray consumersOut = getConsumersArray();
        JSONArray distributorsOut = getDistributorsArray();

        out.put(Constants.CONSUMERS, consumersOut);
        out.put(Constants.DISTRIBUTORS, distributorsOut);

        // Write to the file
        configureJSON(out);
    }

    /**
     * Method that writes to the output file and closes it
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
