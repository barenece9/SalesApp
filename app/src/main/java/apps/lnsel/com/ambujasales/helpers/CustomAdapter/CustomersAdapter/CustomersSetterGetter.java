package apps.lnsel.com.ambujasales.helpers.CustomAdapter.CustomersAdapter;

/**
 * Created by apps2 on 5/13/2017.
 */
public class CustomersSetterGetter {
    private String cusId;
    private String cusCode;
    private String cusFirstName;
    private String cusLastName;
    private String cusShopName;
    private String cusAddress;
    private String cusCountry;
    private String cusState;
    private String cusCity;
    private String cusPinCode;
    private String cusEmail;
    private String cusMobileNo;
    private String cusAlternateNo;
    private String cusCustomerType;
    private String cusParentName;

    public CustomersSetterGetter(String cusId, String cusCode, String cusFirstName, String cusLastName, String cusShopName, String cusAddress, String cusCountry, String cusState, String cusCity, String cusPinCode, String cusEmail, String cusMobileNo, String cusAlternateNo, String cusCustomerType, String cusParentName) {
        this.cusId = cusId;
        this.cusCode = cusCode;
        this.cusFirstName = cusFirstName;
        this.cusLastName = cusLastName;
        this.cusShopName = cusShopName;
        this.cusAddress = cusAddress;
        this.cusCountry = cusCountry;
        this.cusState = cusState;
        this.cusCity = cusCity;
        this.cusPinCode = cusPinCode;
        this.cusEmail = cusEmail;
        this.cusMobileNo = cusMobileNo;
        this.cusAlternateNo = cusAlternateNo;
        this.cusCustomerType = cusCustomerType;
        this.cusParentName = cusParentName;
    }

    public String getCusId() {
        return this.cusId;
    }

    public String getCusCode() {
        return this.cusCode;
    }

    public String getCusFirstName() {
        return this.cusFirstName;
    }

    public String getCusLastName() {
        return this.cusLastName;
    }

    public String getCusShopName() {
        return this.cusShopName;
    }

    public String getCusAddress() {
        return this.cusAddress;
    }

    public String getCusCountry() {
        return this.cusCountry;
    }

    public String getCusState() {
        return this.cusState;
    }

    public String getCusCity() {
        return this.cusCity;
    }

    public String getCusPinCode() {
        return this.cusPinCode;
    }

    public String getCusEmail() {
        return this.cusEmail;
    }

    public String getCusMobileNo() {
        return this.cusMobileNo;
    }

    public String getCusAlternateNo() {
        return this.cusAlternateNo;
    }

    public String getCusCustomerType() {
        return this.cusCustomerType;
    }

    public String getCusParentName() {
        return this.cusParentName;
    }
}
