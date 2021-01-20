package strategies;

import entities.Distributor;
import entities.Producer;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * The class contains methods implemented regarding
 * quantity strategy's particularities.
 */
public final class QuantityStrategy implements SelectProducersStrategy {

    public QuantityStrategy() {
    }

    @Override
    public void chooseProducers(Distributor distributor, ArrayList<Producer> producers) {

        // Create the comparator & sort the producers
        Comparator<Producer> quantitySortedComparator = Comparator.
                comparing(Producer::getEnergyPerDistributor).reversed().
                thenComparing(Producer::getId);
        producers.sort(quantitySortedComparator);

        long energyRequirement = distributor.getEnergyNeededKW();

        // Search for suitable producers
        for (Producer p : producers) {
            if (energyRequirement > 0
                    && p.getAssignedDistributors().size() < p.getMaxDistributors()) {
                distributor.getCurrentProducers().add(p);
                p.getAssignedDistributors().add(distributor);
                energyRequirement -= p.getEnergyPerDistributor();
            }
        }

        // Restores the list of producers to its original form (sorted by id)
        Comparator<Producer> idSortedComparator = Comparator.comparingLong(Producer::getId);
        producers.sort(idSortedComparator);
    }
}
