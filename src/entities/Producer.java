package entities;

import documents.MonthlyReport;

import java.util.ArrayList;

/**
 * Class that contains general information about a producer and
 * methods of processing its data
 */
public final class Producer extends Entity {
    /**
     * The type of energy produced
     */
    private EnergyType energyType;
    /**
     * The maximum number of distributors that the producer can have
     */
    private long maxDistributors;
    /**
     * Price/KW
     */
    private double priceKW;
    /**
     * The amount of energy that the producer provides to a distributor
     */
    private long energyPerDistributor;
    /**
     * Information about producer's monthly stats
     */
    private final ArrayList<MonthlyReport> reports;
    /**
     * Information about producer's current assigned distributors
     */
    private final ArrayList<Distributor> assignedDistributors;

    public Producer(final long id, final EnergyType energyType, final long maxDistributors,
                    final double priceKW, final long energyPerDistributor) {
        super(id);
        this.energyType = energyType;
        this.maxDistributors = maxDistributors;
        this.priceKW = priceKW;
        this.energyPerDistributor = energyPerDistributor;
        this.reports = new ArrayList<>();
        this.assignedDistributors = new ArrayList<>();
    }

    public ArrayList<Distributor> getAssignedDistributors() {
        return assignedDistributors;
    }

    public ArrayList<MonthlyReport> getReports() {
        return reports;
    }

    public long getMaxDistributors() {
        return maxDistributors;
    }

    public void setMaxDistributors(long maxDistributors) {
        this.maxDistributors = maxDistributors;
    }

    public double getPriceKW() {
        return priceKW;
    }

    public void setPriceKW(double priceKW) {
        this.priceKW = priceKW;
    }

    public long getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public void setEnergyPerDistributor(long energyPerDistributor) {
        this.energyPerDistributor = energyPerDistributor;
    }

    public EnergyType getEnergyType() {
        return energyType;
    }

    public void setEnergyType(EnergyType energyType) {
        this.energyType = energyType;
    }

    /**
     * Method that updates producer's data with the received information.
     *
     * @param newQuantity new amount of energy from monthly update
     */
    public void getProducerUpdates(long newQuantity) {
        this.setEnergyPerDistributor(newQuantity);
    }
}
