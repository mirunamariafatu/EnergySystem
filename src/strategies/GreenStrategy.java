package strategies;

import entities.Distributor;
import entities.Producer;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * The class contains methods implemented taking into
 * account green strategy's requirements.
 */
public final class GreenStrategy implements SelectProducersStrategy {

    public GreenStrategy() {
    }

    @Override
    public void chooseProducers(Distributor distributor, ArrayList<Producer> producers) {

        // Create the comparator & sort the producers
        Comparator<Producer> greenSortedComparator =
                Comparator.comparingDouble(Producer::getPriceKW).
                        thenComparing(Comparator.comparingLong(Producer::getEnergyPerDistributor).
                                reversed()).thenComparing(Producer::getId);
        producers.sort(greenSortedComparator);

        long energyRequirement = distributor.getEnergyNeededKW();

        // Search for suitable producers
        for (Producer p : producers) {
            // Prioritize producers with renewable energy
            if (energyRequirement > 0 && p.getEnergyType().isRenewable()
                    && p.getAssignedDistributors().size() < p.getMaxDistributors()) {
                distributor.getCurrentProducers().add(p);
                p.getAssignedDistributors().add(distributor);
                energyRequirement -= p.getEnergyPerDistributor();
            }
        }

        if (energyRequirement > 0) {
            for (Producer p : producers) {
                if (energyRequirement > 0 && !p.getEnergyType().isRenewable()
                        && p.getAssignedDistributors().size() < p.getMaxDistributors()) {
                    distributor.getCurrentProducers().add(p);
                    p.getAssignedDistributors().add(distributor);
                    energyRequirement -= p.getEnergyPerDistributor();
                }
            }
        }

        // Restores the list of producers to its original form (sorted by id)
        Comparator<Producer> idSortedComparator = Comparator.comparingLong(Producer::getId);
        producers.sort(idSortedComparator);
    }
}
