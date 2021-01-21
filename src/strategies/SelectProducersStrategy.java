package strategies;

import entities.Distributor;
import entities.Producer;

import java.util.ArrayList;

/**
 * The interface defines the operations that can be performed on the
 * distributor's data. Each implementation uses its own algorithm.
 */
public interface SelectProducersStrategy {

    /**
     * Method through a distributor should choose its
     * producers using a custom strategy.
     *
     * @param distributor current distributor
     * @param producers   information about all producers
     */
    void chooseProducers(Distributor distributor, ArrayList<Producer> producers);

}
