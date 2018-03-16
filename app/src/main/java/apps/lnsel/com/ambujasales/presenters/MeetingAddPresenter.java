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
import apps.lnsel.com.ambujasales.views.Dashboard.activities.MeetingAddScreen.MeetingAddActivityView;
import apps.lnsel.com.ambujasales.views.Dashboard.activities.MeetingAddScreen.MeetingAddSearchData;

/**
 * Created by apps2 on 5/12/2017.
 */
public class MeetingAddPresenter {

    private MeetingAddActivityView view;
    private static final String TAG = "REQ";
    private static final String TAG_DATA = "data";
    JSONObject e;
    JSONArray data;

    public MeetingAddPresenter(MeetingAddActivityView view) {
        this.view = view;
    }

    public void getCustomersService(String url) {

        StringRequest req = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, response.toString());

                        String str_response = response;

                        MeetingAddSearchData.cusId.clear();
                        MeetingAddSearchData.cusCode.clear();
                        MeetingAddSearchData.cusFirstName.clear();
                        MeetingAddSearchData.cusLastName.clear();
                        MeetingAddSearchData.cusShopName.clear();
                        MeetingAddSearchData.cusAddress.clear();
                        MeetingAddSearchData.cusCountry.clear();
                        MeetingAddSearchData.cusState.clear();
                        MeetingAddSearchData.cusCity.clear();
                        MeetingAddSearchData.cusPinCode.clear();
                        MeetingAddSearchData.cusEmail.clear();
                        MeetingAddSearchData.cusMobileNo.clear();
                        MeetingAddSearchData.cusCustomerType.clear();

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

                                    MeetingAddSearchData.cusId.add(cusId);
                                    MeetingAddSearchData.cusCode.add(cusCode);
                                    MeetingAddSearchData.cusFirstName.add(cusFirstName);
                                    MeetingAddSearchData.cusLastName.add(cusLastName);
                                    MeetingAddSearchData.cusShopName.add(cusShopName);
                                    MeetingAddSearchData.cusAddress.add(cusAddress);
                                    MeetingAddSearchData.cusCountry.add(cusCountry);
                                    MeetingAddSearchData.cusState.add(cusState);
                                    MeetingAddSearchData.cusCity.add(cusCity);
                                    MeetingAddSearchData.cusPinCode.add(cusPinCode);
                                    MeetingAddSearchData.cusEmail.add(cusEmail);
                                    MeetingAddSearchData.cusMobileNo.add(cusMobileNo);
                                    MeetingAddSearchData.cusCustomerType.add(cusCustomerType);

                                }

                                view.startGetCustomers();

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

    public void addNewCustomerMeetingService(String url, final String userId, final String userParentPath, final String cusCode, final String cusFirstName, final String cusLastName, final String cusShopName, final String cusAddress, final String cusCountryId, final String cusCountry, final String cusStateId, final String cusState, final String cusCityId, final String cusCity, final String cusPinCode, final String cusEmail, final String cusMobileNo, final String cusCustomerTypeId, final String cusCustomerType, final String mtnName, final String mtnDate, final String mtnTime) {

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
                params.put("mtnName", mtnName);
                params.put("mtnDate", mtnDate);
                params.put("mtnTime", mtnTime);

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
                Log.d("mtnName", mtnName);
                Log.d("mtnDate", mtnDate);
                Log.d("mtnTime", mtnTime);


                return params;
            }

        };

        //jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }
}
