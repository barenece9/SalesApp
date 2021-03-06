package apps.lnsel.com.ambujasales.presenters;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import apps.lnsel.com.ambujasales.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.ambujasales.views.LoginScreen.LoginActivityView;

/**
 * Created by apps2 on 5/5/2017.
 */
public class LoginPresenter {

    private LoginActivityView view;

    private static final String TAG = "REQ";
    JSONObject e;

    public LoginPresenter(LoginActivityView view) {
        this.view = view;
    }

    public void getLoginService(String url) {

        StringRequest req = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);

                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");

                            if(status.equals("success")){
                                String userId = jsonObj.getString("userId");
                                String userLoginRecordId = jsonObj.getString("userLoginRecordId");
                                String userName = jsonObj.getString("userName");
                                String userFirstName = jsonObj.getString("userFirstName");
                                String userLastName = jsonObj.getString("userLastName");
                                String userEmail = jsonObj.getString("userEmail");
                                String userContactNo = jsonObj.getString("userContactNo");
                                String userDesignation = jsonObj.getString("userDesignation");
                                String userParentId = jsonObj.getString("userParentId");
                                String userParentPath = jsonObj.getString("userParentPath");
                                String userFirebaseToken="";

                                view.updateSession(userId,userLoginRecordId,userName,userFirstName,userLastName,userEmail,userContactNo,userDesignation,userParentId,userParentPath,userFirebaseToken);

                                view.startMainActivity();

                            }else{
                                view.errorInfo(message);

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
