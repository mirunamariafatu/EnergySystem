package entities;

import documents.Contract;
import strategies.EnergyChoiceStrategyType;
import strategies.SelectProducersStrategy;
import utils.Constants;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Class that contains general information about a distributor and
 * methods of processing its data
 */
@SuppressWarnings("deprecation")
public final class Distributor extends Entity implements Observer {
    private final EnergyChoiceStrategyType strategyTitle;
    private final ArrayList<Producer> currentProducers;
    private final SelectProducersStrategy strategy;
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
    /**
     * Distributor's current budget
     */
    private long budget;
    /**
     * The amount of energy the distributor needs
     */
    private long energyNeededKW;
    /**
     * Boolean flag that indicates whether the distributor
     * must choose its producers or not
     */
    private boolean hasToReapplyStrategy = true;

    public Distributor(final long id, final long initialBudget, final long contractLength,
                       final long initialInfrastructureCost, long energyNeededKW,
                       final String strategyTitle, final SelectProducersStrategy strategy) {
        super(id);
        this.budget = initialBudget;
        this.contractLength = contractLength;
        this.infrastructureCost = initialInfrastructureCost;
        this.energyNeededKW = energyNeededKW;
        this.strategyTitle = EnergyChoiceStrategyType.valueOf(strategyTitle);
        this.contracts = new ArrayList<>();
        this.currentProducers = new ArrayList<>();
        this.strategy = strategy;
    }

    public boolean getHasToReapplyStrategy() {
        return hasToReapplyStrategy;
    }

    public void setHasToReapplyStrategy(boolean hasToReapplyStrategy) {
        this.hasToReapplyStrategy = hasToReapplyStrategy;
    }

    public SelectProducersStrategy getStrategy() {
        return strategy;
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(final long budget) {
        this.budget = budget;
    }

    public long getInfrastructureCost() {
        return infrastructureCost;
    }

    public void setInfrastructureCost(final long infrastructureCost) {
        this.infrastructureCost = infrastructureCost;
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

    public long getEnergyNeededKW() {
        return energyNeededKW;
    }

    public void setEnergyNeededKW(long energyNeededKW) {
        this.energyNeededKW = energyNeededKW;
    }

    public EnergyChoiceStrategyType getStrategyTitle() {
        return strategyTitle;
    }

    public ArrayList<Producer> getCurrentProducers() {
        return currentProducers;
    }

    /**
     * Method that calculates distributor's profit.
     *
     * @return the profit
     */
    private long getProfit() {
        return Math.round(Math.floor(Constants.PERCENT * productionCost));
    }

    /**
     * Calculates the current price.
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
     * Calculates current production cost.
     */
    public void calculateProductionCost() {
        double cost = 0;

        // Compute the sum
        for (Producer p : currentProducers) {
            cost += p.getPriceKW() * p.getEnergyPerDistributor();
        }

        productionCost = Math.round(Math.floor(cost / Constants.TEN_CONSTANT));
    }

    /**
     * Method in which the distributor pays his monthly fees.
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

                // Remove the distributor from his producers' active
                // distributors
                for (Producer p : this.getCurrentProducers()) {
                    p.getAssignedDistributors().remove(this);
                }
            }
        }
    }

    /**
     * Method that removes bankrupt consumers' contracts.
     */
    public void removeBankruptConsumers() {
        contracts.removeIf(current -> current.getConsumer().getIsBankrupt());
    }

    /**
     * Method that removes invalid contracts (remainedContractMonths = 0).
     */
    public void removeExpiredContracts() {
        contracts.removeIf(current -> current.getRemainedContractMonths() == 0);
    }

    /**
     * Method that updates distributor's data with the received information.
     *
     * @param newInfrastructureCost new infrastructure cost from monthly update
     */
    public void getDistributorUpdates(final long newInfrastructureCost) {
        this.setInfrastructureCost(newInfrastructureCost);
    }

    @Override
    public void update(Observable o, Object producer) {
        // The producer that has updated its data
        Producer changedProducer = (Producer) producer;
        for (Producer p : currentProducers) {

            // Check if the changedProducer is assigned to
            // the distributor's current producers
            if (changedProducer.equals(p)) {
                // Set the flag
                this.hasToReapplyStrategy = true;
                break;
            }
        }
    }
}
