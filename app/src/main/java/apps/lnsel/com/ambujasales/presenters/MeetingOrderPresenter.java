package apps.lnsel.com.ambujasales.presenters;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import apps.lnsel.com.ambujasales.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.ambujasales.views.Dashboard.activities.MeetingOrderScreen.MeetingOrderActivityView;

/**
 * Created by apps2 on 5/25/2017.
 */
public class MeetingOrderPresenter {
    private MeetingOrderActivityView view;

    private static final String TAG = "REQ";

    public MeetingOrderPresenter(MeetingOrderActivityView view) {
        this.view = view;
    }

    public void addMeetingOrderRequest(String url, final String ordMeetingId, final String userId, final String userParentPath, final String ordName, final String ordAmount, final String ordDescription, final String ordDate, final String ordTime){

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

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
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        view.errorInfo("Server not Responding, Please check your Internet Connection");
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("userId",userId);
                params.put("userParentPath",userParentPath);
                params.put("ordMeetingId",ordMeetingId);
                params.put("ordName",ordName);
                params.put("ordAmount",ordAmount);
                params.put("ordDescription",ordDescription);
                params.put("ordDate",ordDate);
                params.put("ordTime",ordTime);
                return params;
            }
        };

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);

    }

}
