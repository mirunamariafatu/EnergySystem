package strategies;

import entities.Distributor;
import entities.Producer;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * The class contains methods implemented taking into
 * account price strategy's requirements.
 */
public final class PriceStrategy implements SelectProducersStrategy {

    public PriceStrategy() {
    }

    @Override
    public void chooseProducers(Distributor distributor, ArrayList<Producer> producers) {

        // Create the comparator & sort the producers
        Comparator<Producer> priceSortedComparator = Comparator.
                comparingDouble(Producer::getPriceKW).
                thenComparing(Comparator.comparingLong(Producer::getEnergyPerDistributor).
                        reversed()).thenComparing(Producer::getId);
        producers.sort(priceSortedComparator);

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
