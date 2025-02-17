package entities;

import documents.Contract;
import utils.Constants;

import java.util.ArrayList;
import java.util.stream.IntStream;

/**
 * Class that contains general information about a consumer and
 * methods of processing its data.
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
    /**
     * Consumer's current budget
     */
    private long budget;
    /**
     * Boolean flag which indicates whether
     * the consumer is bankrupt or not.
     */
    private boolean isBankrupt = false;

    public Consumer(final long id, final long initialBudget, final long monthlyIncome) {
        super(id);
        this.budget = initialBudget;
        this.monthlyIncome = monthlyIncome;
    }

    public Contract getCurrentContract() {
        return currentContract;
    }

    public void setCurrentContract(final Contract currentContract) {
        this.currentContract = currentContract;
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(final long budget) {
        this.budget = budget;
    }

    public boolean getIsBankrupt() {
        return isBankrupt;
    }

    public void setIsBankrupt(boolean bankrupt) {
        isBankrupt = bankrupt;
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
                IntStream.range(i + 1, distributors.size()).filter(j -> !distributors.get(j)
                        .getIsBankrupt()).filter(j -> distributors
                        .get(j).getPrice() < currentDistributor.getPrice())
                        .forEach(j -> currentDistributor = distributors.get(j));

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

            // Proceed with the payment
            if (currentDistributor.equals(oldDistributor)) {
                if (this.getBudget() >= totalPayment) {
                    this.setBudget(this.getBudget() - totalPayment);
                    currentDistributor.setBudget(currentDistributor.getBudget()
                            + currentContract.getPrice());
                    oldDistributor.setBudget(oldDistributor.getBudget()
                            + Math.round(Math.floor(Constants.RATE * oldPayment)));

                    // The consumer has paid all his debts
                    oldDistributor = null;
                    oldPayment = 0;
                    hasBorrowed = false;
                } else {
                    // The consumer has insufficient funds and as a result,
                    // he will go bankrupt
                    setIsBankrupt(true);
                }
            } else {
                if (this.getBudget() >= totalPayment - currentContract.getPrice()) {
                    //  The distributor can only pay
                    this.setBudget(this.getBudget() - (totalPayment - currentContract.getPrice()));
                    if (this.getBudget() >= currentContract.getPrice()) {
                        this.setBudget(this.getBudget() - currentContract.getPrice());

                        // The consumer has paid all his debts
                        oldDistributor = null;
                        oldPayment = 0;
                        hasBorrowed = false;
                    } else {

                        // The distributor has insufficient funds
                        // to proceed his current contract price
                        oldDistributor = currentDistributor;
                        oldPayment = currentContract.getPrice();
                    }
                } else {
                    // The consumer has insufficient funds and as a result,
                    // he will go bankrupt
                    setIsBankrupt(true);
                }
            }
        }

        // Update the current contract (the remaining contract months)
        currentContract.contractUpdate();
    }
}
