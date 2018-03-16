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

import java.util.HashMap;
import java.util.Map;

import apps.lnsel.com.ambujasales.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.ambujasales.views.Dashboard.activities.CustomerAddScreen.CustomerAddActivityView;

/**
 * Created by apps2 on 5/15/2017.
 */
public class CustomerAddPresenter {

    private CustomerAddActivityView view;
    private static final String TAG = "REQ";
    private static final String TAG_DATA = "data";
    JSONObject e;
    JSONArray data;

    public CustomerAddPresenter(CustomerAddActivityView view) {
        this.view = view;
    }

    public void addNewCustomerService(String url, final String userId, final String userParentPath, final String cusCode, final String cusFirstName, final String cusLastName, final String cusShopName, final String cusAddress, final String cusCountryId, final String cusCountry, final String cusStateId, final String cusState, final String cusCityId, final String cusCity, final String cusPinCode, final String cusEmail, final String cusMobileNo, final String cusCustomerTypeId, final String cusCustomerType) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("failed")){
                                view.errorInfo(message);
                            }else{
                                view.successInfo(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                view.errorInfo("Server not Responding, Please check your Internet Connection");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", userId);
                params.put("userParentPath", userParentPath);
                params.put("cusCode", cusCode);
                params.put("cusFirstName", cusFirstName);
                params.put("cusLastName", cusLastName);
                params.put("cusShopName", cusShopName);
                params.put("cusAddress", cusAddress);
                params.put("cusCountryId", cusCountryId);
                params.put("cusCountry", cusCountry);
                params.put("cusStateId", cusStateId);
                params.put("cusState", cusState);
                params.put("cusCityId", cusCityId);
                params.put("cusCity", cusCity);
                params.put("cusPinCode", cusPinCode);
                params.put("cusEmail", cusEmail);
                params.put("cusMobileNo", cusMobileNo);
                params.put("cusCustomerTypeId", cusCustomerTypeId);
                params.put("cusCustomerType", cusCustomerType);

                Log.d("userId", userId);
                Log.d("userParentPath", userParentPath);
                Log.d("cusCode", cusCode);
                Log.d("cusFirstName", cusFirstName);
                Log.d("cusLastName", cusLastName);
                Log.d("cusShopName", cusShopName);
                Log.d("cusAddress", cusAddress);
                Log.d("cusCountryId", cusCountryId);
                Log.d("cusCountry", cusCountry);
                Log.d("cusStateId", cusStateId);
                Log.d("cusState", cusState);
                Log.d("cusCityId", cusCityId);
                Log.d("cusCity", cusCity);
                Log.d("cusPinCode", cusPinCode);
                Log.d("cusEmail", cusEmail);
                Log.d("cusMobileNo", cusMobileNo);
                Log.d("cusCustomerTypeId", cusCustomerTypeId);
                Log.d("cusCustomerType", cusCustomerType);


                return params;
            }

        };

        //jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }
}
