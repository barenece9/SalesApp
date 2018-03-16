package apps.lnsel.com.ambujasales.views.Dashboard.activities.OrderEditScreen;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;

import apps.lnsel.com.ambujasales.R;
import apps.lnsel.com.ambujasales.helpers.CustomAdapter.OrdersAdapter.OrdersData;
import apps.lnsel.com.ambujasales.presenters.OrderEditPresenter;
import apps.lnsel.com.ambujasales.utils.ActivityUtil;
import apps.lnsel.com.ambujasales.utils.SharedManagerUtil;
import apps.lnsel.com.ambujasales.utils.UrlUtil;

/**
 * Created by apps2 on 5/26/2017.
 */
public class OrderEditActivity extends AppCompatActivity implements OrderEditActivityView {

    private OrderEditPresenter presenter;

    SharedManagerUtil session;

    private ProgressDialog progress;

    EditText et_order_name, et_order_amount, et_order_description,activity_order_edit_et_quantity;
    TextInputLayout til_order_name, til_order_amount, til_order_description,activity_order_edit_til_quantity;
    Button btn_cancel, btn_submit;
    Spinner activity_order_edit_spinner_venues,activity_order_edit_spinner_unit;

    ArrayList<String> venuesList = new ArrayList<String>();
    ArrayList<String> venuesId = new ArrayList<String>();
    ArrayAdapter<String> venuesAdapter;
    String venuesSelect="";
    String venuesid="";

    ArrayList<String> unitList = new ArrayList<String>();
    ArrayList<String> unitId = new ArrayList<String>();
    ArrayAdapter<String> unitAdapter;
    String unitSelect="";
    String unitid="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Order");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOrdersActivity();
            }
        });

        // Session Manager
        session = new SharedManagerUtil(this);


        presenter = new OrderEditPresenter(this);

        activity_order_edit_spinner_venues=(Spinner)findViewById(R.id.activity_order_edit_spinner_venues);
        activity_order_edit_spinner_unit=(Spinner)findViewById(R.id.activity_order_edit_spinner_unit);

        et_order_name = (EditText) findViewById(R.id.activity_order_edit_et_title);
        et_order_amount = (EditText) findViewById(R.id.activity_order_edit_et_amount);
        et_order_description = (EditText) findViewById(R.id.activity_order_edit_et_description);
        activity_order_edit_et_quantity=(EditText)findViewById(R.id.activity_order_edit_et_quantity);

        btn_cancel = (Button) findViewById(R.id.activity_order_edit_btn_cancel);
        btn_submit = (Button) findViewById(R.id.activity_order_edit_btn_submit);

        til_order_name = (TextInputLayout) findViewById(R.id.activity_order_edit_til_title);
        til_order_amount = (TextInputLayout) findViewById(R.id.activity_order_edit_til_amount);
        til_order_description = (TextInputLayout) findViewById(R.id.activity_order_edit_til_description);
        activity_order_edit_til_quantity=(TextInputLayout)findViewById(R.id.activity_order_edit_til_quantity);

        et_order_name.addTextChangedListener(new MyTextWatcher(et_order_name));
        et_order_amount.addTextChangedListener(new MyTextWatcher(et_order_amount));
        et_order_description.addTextChangedListener(new MyTextWatcher(et_order_description));
        activity_order_edit_et_quantity.addTextChangedListener(new MyTextWatcher(activity_order_edit_et_quantity));

        et_order_name.setText(OrdersData.current_order_name);
        et_order_amount.setText(OrdersData.current_order_amount);
        et_order_description.setText(OrdersData.current_order_description);



        venuesList.add("Select Venues");
        venuesList.add("Sdf,Kolkata");
        venuesList.add("Webeel,Kolkata");
        venuesList.add("Sector V,Kolkata");
        venuesAdapter = new ArrayAdapter<String>(OrderEditActivity.this,R.layout.spinner_rows, venuesList);
        venuesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activity_order_edit_spinner_venues.setAdapter(venuesAdapter);

        activity_order_edit_spinner_venues.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                //venuesid = venuesId.get(position);
                venuesSelect=venuesList.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });




        unitList.add("Select Unit");
        unitList.add("Unit I");
        unitList.add("Unit II");
        unitList.add("Unit III");
        unitAdapter = new ArrayAdapter<String>(OrderEditActivity.this,R.layout.spinner_rows, unitList);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activity_order_edit_spinner_unit.setAdapter(unitAdapter);

        activity_order_edit_spinner_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                //venuesid = venuesId.get(position);
                unitSelect=unitList.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOrdersActivity();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateOrderName()) {
                    return;
                }
                if (!validateOrderAmount()) {
                    return;
                }
                if (!validateOrderDescription()) {
                    return;
                }

                updateOrder();

            }
        });


    }


    public void updateOrder(){

        if(isNetworkAvailable()){

            progress = new ProgressDialog(this);
            progress.setMessage("loading...");
            progress.show();
            progress.setCanceledOnTouchOutside(false);

            final String userId = session.getUserID();
            String ordId = OrdersData.current_order_id;

            String ordName = et_order_name.getText().toString();
            String ordAmount = et_order_amount.getText().toString();
            String ordDescription = et_order_description.getText().toString();

            presenter.updateOrderService(UrlUtil.UPDATE_ORDER_URL, userId, ordId, ordName, ordAmount, ordDescription);


        }else{
            Toast.makeText(this,"Internet Connection not Available", Toast.LENGTH_LONG).show();
        }

    }


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onBackPressed() {
        startOrdersActivity();
    }

    public void startOrdersActivity() {
        new ActivityUtil(this).startOrdersActivity();
    }

    public void errorInfo(String msg){
        if(progress != null){
            progress.dismiss();
        }
        MDToast.makeText(this, msg, MDToast.LENGTH_LONG, MDToast.TYPE_ERROR);
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void successInfo(String msg){
        if(progress != null){
            progress.dismiss();
        }
        MDToast.makeText(this, msg, MDToast.LENGTH_LONG, MDToast.TYPE_SUCCESS);
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        startOrdersActivity();
    }



    //********** Text Watcher for Validation *******************//
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.activity_order_edit_et_title:
                    validateOrderName();
                    break;
                case R.id.activity_order_edit_et_amount:
                    validateOrderAmount();
                    break;
                case R.id.activity_order_edit_et_description:
                    validateOrderDescription();
                    break;
            }
        }
    }


    private boolean validateOrderName() {
        if (et_order_name.getText().toString().trim().isEmpty()) {
            til_order_name.setError("order name can not be blank");
            requestFocus(et_order_name);
            return false;
        } else {
            til_order_name.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateOrderAmount() {
        if (et_order_amount.getText().toString().trim().isEmpty()) {
            til_order_amount.setError("order amount can not be blank");
            requestFocus(et_order_amount);
            return false;
        } else {
            til_order_amount.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateOrderDescription() {
        if (et_order_description.getText().toString().trim().isEmpty()) {
            til_order_description.setError("order details can not be blank");
            requestFocus(et_order_description);
            return false;
        } else {
            til_order_description.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
