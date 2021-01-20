package documents;

import java.util.ArrayList;

/**
 * Class that contains general information about a monthly
 * report and methods of processing its data.
 */
public final class MonthlyReport {
    /**
     * The month in which the report was written.
     */
    private final int currentMonth;
    /**
     * Ids of all distributors assigned to the report.
     */
    private ArrayList<Long> distributorsIds;

    public MonthlyReport(int currentMonth, ArrayList<Long> distributorsIds) {
        this.currentMonth = currentMonth;
        this.distributorsIds = distributorsIds;
    }

    public long getCurrentMonth() {
        return currentMonth;
    }

    public ArrayList<Long> getDistributorsIds() {
        return distributorsIds;
    }

    public void setDistributorsIds(ArrayList<Long> distributorsIds) {
        this.distributorsIds = distributorsIds;
    }
}
