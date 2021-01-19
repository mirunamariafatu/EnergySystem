package entities;

import documents.Contract;
import utils.Constants;

import java.util.ArrayList;

/**
 * Class that contains general information about a consumer and
 * methods of processing its data
 */
public final class Consumer extends Entity {
    /**
     * Consumer's monthly income
     */
    private final long monthlyIncome;
    /**
     * Consumer's current contract
     */
    private Contract currentContract;
    /**
     * Consumer's current distributor
     */
    private Distributor currentDistributor;
    /**
     * Boolean flag to check if the consumer
     * has unpaid payments
     */
    private boolean hasBorrowed = false;
    /**
     * The unpaid payment (if hasBorrowed == true)
     */
    private long oldPayment;
    /**
     * The unpaid distributor (if hasBorrowed == true)
     */
    private Distributor oldDistributor;

    public Consumer(final long id, final long initialBudget, final long monthlyIncome) {
        super(id, initialBudget);
        this.monthlyIncome = monthlyIncome;
    }

    public Contract getCurrentContract() {
        return currentContract;
    }

    public void setCurrentContract(final Contract currentContract) {
        this.currentContract = currentContract;
    }

    /**
     * Method in which the consumer searches for a new distributor,
     * choosing the one with the lowest price available.
     *
     * @param distributors information all distributors
     * @return the chosen distributor
     */
    public Distributor chooseDistributor(final ArrayList<Distributor> distributors) {
        for (int i = 0; i < distributors.size(); i++) {
            if (!distributors.get(i).getIsBankrupt()) {
                currentDistributor = distributors.get(i);
                for (int j = i + 1; j < distributors.size(); j++) {
                    if (!distributors.get(j).getIsBankrupt()) {
                        if (distributors.get(j).getPrice() < currentDistributor.getPrice()) {
                            currentDistributor = distributors.get(j);
                        }
                    }
                }
                return currentDistributor;
            }
        }
        return null;
    }

    /**
     * The consumer collects his monthly income.
     */
    public void getSalary() {
        this.setBudget(this.getBudget() + monthlyIncome);
    }

    /**
     * The consumer signs a new contract.
     *
     * @param distributor the distributor with whom he signs the contract
     */
    public void signContract(final Distributor distributor) {
        // Create a new contract
        this.currentContract = new Contract(this.getId(), distributor.getPrice(),
                distributor.getContractLength(), this);

        // Add the contract to the distributor's active contracts
        distributor.getContracts().add(currentContract);
    }

    /**
     * Method in which the consumer pays the current contract's price
     * to his current distributor.
     * If he has an unpaid payment, he must pay that payment as well
     * otherwise, the consumer will go bankrupt.
     */
    public void payPrice() {
        if (!hasBorrowed) {
            if (this.getBudget() >= currentContract.getPrice()) {
                // Proceed with the payment
                this.setBudget(this.getBudget() - currentContract.getPrice());
                currentDistributor.setBudget(currentDistributor.getBudget()
                        + currentContract.getPrice());
            } else {
                // The consumer has insufficient funds
                hasBorrowed = true;
                oldPayment = currentContract.getPrice();
                oldDistributor = currentDistributor;
            }
        } else {
            // The consumer has unpaid payments from last month
            long totalPayment = Math.round(Math.floor(Constants.RATE * oldPayment))
                    + currentContract.getPrice();
            if (this.getBudget() >= totalPayment) {
                // Proceed with the payment
                this.setBudget(this.getBudget() - totalPayment);
                currentDistributor.setBudget(currentDistributor.getBudget()
                        + currentContract.getPrice());
                oldDistributor.setBudget(oldDistributor.getBudget()
                        + Math.round(Math.floor(Constants.RATE * oldPayment)));

                oldDistributor = null;
                oldPayment = 0;
                hasBorrowed = false;
            } else {
                // The consumer has insufficient funds and as a result,
                // he will go bankrupt
                setIsBankrupt(true);
            }
        }

        // Update the current contract (the remaining contract months)
        currentContract.contractUpdate();
    }
}
