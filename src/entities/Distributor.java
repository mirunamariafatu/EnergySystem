package entities;

import documents.Contract;
import utils.Constants;

import java.util.ArrayList;

/**
 * Class that contains general information about a distributor and
 * methods of processing its data
 */
public final class Distributor extends Entity {
    /**
     * Length of the contract
     */
    private long contractLength;
    /**
     * Infrastructure cost
     */
    private long infrastructureCost;
    /**
     * Production cost
     */
    private long productionCost;
    /**
     * The current price
     */
    private long price;
    /**
     * Information about distributor's active contracts
     */
    private ArrayList<Contract> contracts;

    public Distributor(final long id, final long initialBudget, final long contractLength,
                       final long initialInfrastructureCost, final long initialProductionCost) {
        super(id, initialBudget);
        this.contractLength = contractLength;
        this.infrastructureCost = initialInfrastructureCost;
        this.productionCost = initialProductionCost;
        this.contracts = new ArrayList<>();
    }

    public long getInfrastructureCost() {
        return infrastructureCost;
    }

    public void setInfrastructureCost(final long infrastructureCost) {
        this.infrastructureCost = infrastructureCost;
    }

    public long getProductionCost() {
        return productionCost;
    }

    public void setProductionCost(final long productionCost) {
        this.productionCost = productionCost;
    }

    public ArrayList<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(final ArrayList<Contract> contracts) {
        this.contracts = contracts;
    }

    public long getContractLength() {
        return contractLength;
    }

    public void setContractLength(final long contractLength) {
        this.contractLength = contractLength;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(final long price) {
        this.price = price;
    }

    /**
     * Method that calculates distributor's profit
     *
     * @return the profit
     */
    private long getProfit() {
        return Math.round(Math.floor(Constants.PERCENT * productionCost));
    }

    /**
     * Calculates the current price
     */
    public void calculatePrice() {
        if (contracts.size() != 0) {
            // The distributor has active contracts
            price = Math.round(Math.floor(infrastructureCost / (float) contracts.size())
                    + productionCost + getProfit());
        } else {
            price = infrastructureCost + productionCost + getProfit();
        }

        // Remove all expired contracts
        removeExpiredContracts();
    }

    /**
     * Method in which the distributor pays his monthly fees
     */
    public void payTaxes() {

        if (!this.getIsBankrupt()) {
            long totalPayment = infrastructureCost
                    + productionCost * contracts.size();

            // Proceed with the payment
            this.setBudget(this.getBudget() - totalPayment);
            // Remove bankrupt consumers' contracts
            removeBankruptConsumers();

            // Check if the distributor still has available funds
            if (this.getBudget() < 0) {
                this.setIsBankrupt(true);
            }
        }
    }

    /**
     * Method that removes bankrupt consumers' contracts
     */
    public void removeBankruptConsumers() {
        contracts.removeIf(current -> current.getConsumer().getIsBankrupt());
    }

    /**
     * Method that removes invalid contracts (remainedContractMonths = 0)
     */
    public void removeExpiredContracts() {
        contracts.removeIf(current -> current.getRemainedContractMonths() == 0);
    }

    /**
     * Method that updates distributor's data with the received information
     *
     * @param newProductionCost     new production cost from monthly update
     * @param newInfrastructureCost new infrastructure cost from monthly update
     */
    public void getNewUpdates(final long newProductionCost, final long newInfrastructureCost) {
        this.setProductionCost(newProductionCost);
        this.setInfrastructureCost(newInfrastructureCost);
    }
}
