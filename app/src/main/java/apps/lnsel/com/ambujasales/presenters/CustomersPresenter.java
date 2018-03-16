package apps.lnsel.com.ambujasales.presenters;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import apps.lnsel.com.ambujasales.helpers.CustomAdapter.CustomersAdapter.CustomersData;
import apps.lnsel.com.ambujasales.helpers.CustomAdapter.CustomersAdapter.CustomersSetterGetter;
import apps.lnsel.com.ambujasales.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.ambujasales.views.Dashboard.activities.CustomersScreen.CustomersActivity;
import apps.lnsel.com.ambujasales.views.Dashboard.activities.CustomersScreen.CustomersActivityView;

/**
 * Created by apps2 on 5/13/2017.
 */
public class CustomersPresenter {

    private CustomersActivityView view;

    private static final String TAG = "CUSTOMER_INFO_REQ";
    private static final String TAG_DATA = "data";
    JSONObject e;
    JSONArray data;

    public CustomersPresenter(CustomersActivity view) {
        this.view = view;
    }

    public void getCustomersService(String url) {

        StringRequest req = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, response.toString());

                        String str_response = response;

                        CustomersData.customersList.clear();

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);

                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");

                            if(status.equals("failed")){
                                view.errorInfo(message);
                            }else{
                                data = jsonObj.getJSONArray(TAG_DATA);
                                for (int i = 0; i < data.length(); i++) {
                                    e = data.getJSONObject(i);
                                    String Pick_Id = e.getString("cusFirstName");
                                    Log.i("Author is", Pick_Id);

                                    String cusId = e.getString("cusId");
                                    String cusCode = e.getString("cusCode");
                                    String cusFirstName = e.getString("cusFirstName");
                                    String cusLastName = e.getString("cusLastName");
                                    String cusShopName = e.getString("cusShopName");
                                    String cusAddress = e.getString("cusAddress");
                                    String cusCountry = e.getString("cusCountry");
                                    String cusState = e.getString("cusState");
                                    String cusCity = e.getString("cusCity");
                                    String cusPinCode = e.getString("cusPinCode");
                                    String cusEmail = e.getString("cusEmail");
                                    String cusMobileNo = e.getString("cusMobileNo");
                                    String cusAlternateNo = e.getString("cusAlternateNo");
                                    String cusCustomerType = e.getString("cusCustomerType");
                                    String cusParentName = e.getString("cusParentName");

                                    CustomersSetterGetter wp = new CustomersSetterGetter(cusId, cusCode, cusFirstName, cusLastName, cusShopName, cusAddress, cusCountry, cusState, cusCity, cusPinCode, cusEmail, cusMobileNo, cusAlternateNo, cusCustomerType, cusParentName);

                                    // Binds all strings into an array
                                    CustomersData.customersList.add(wp);

                                }

                                view.startGetCustomerInfo();

                            }



                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                view.errorInfo("Server not Responding, Please check your Internet Connection");
            }
        });

        AppController.getInstance().addToRequestQueue(req);

    }
}
