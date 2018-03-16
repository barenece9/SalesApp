package apps.lnsel.com.ambujasales.helpers.CustomAdapter.OrdersAdapter;

/**
 * Created by apps2 on 5/25/2017.
 */
public class OrdersSetterGetter {
    private String ordId;
    private String ordName;
    private String ordAmount;
    private String ordDescription;
    private String ordDate;
    private String ordTime;
    private String ordMeetingName;
    private String ordCustomerName;


    public OrdersSetterGetter(String ordId, String ordName, String ordAmount, String ordDescription, String ordDate, String ordTime, String ordMeetingName, String ordCustomerName) {
        this.ordId = ordId;
        this.ordName = ordName;
        this.ordAmount = ordAmount;
        this.ordDescription = ordDescription;
        this.ordDate = ordDate;
        this.ordTime = ordTime;
        this.ordMeetingName = ordMeetingName;
        this.ordCustomerName = ordCustomerName;
    }

    public String getOrdId() {
        return this.ordId;
    }

    public String getOrdName() {
        return this.ordName;
    }

    public String getOrdAmount() {
        return this.ordAmount;
    }

    public String getOrdDescription() {
        return this.ordDescription;
    }

    public String getOrdDate() {
        return this.ordDate;
    }

    public String getOrdTime() {
        return this.ordTime;
    }

    public String getOrdMeetingName() {
        return this.ordMeetingName;
    }

    public String getOrdCustomerName() {
        return this.ordCustomerName;
    }
}
