package apps.lnsel.com.ambujasales.views.Dashboard.activities.CustomerDetailsScreen;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import apps.lnsel.com.ambujasales.R;
import apps.lnsel.com.ambujasales.helpers.CustomAdapter.CustomerDetailsAdapter.CustomerDetailsBaseAdapter;
import apps.lnsel.com.ambujasales.helpers.CustomAdapter.CustomerDetailsAdapter.CustomerDetailsData;
import apps.lnsel.com.ambujasales.helpers.CustomAdapter.CustomersAdapter.CustomersData;
import apps.lnsel.com.ambujasales.presenters.CustomerDetailsPresenter;
import apps.lnsel.com.ambujasales.utils.ActivityUtil;
import apps.lnsel.com.ambujasales.utils.SharedManagerUtil;
import apps.lnsel.com.ambujasales.utils.UrlUtil;
import apps.lnsel.com.ambujasales.views.Dashboard.activities.MeetingDetailsScreen.MeetingDetailsActivity;

/**
 * Created by apps2 on 5/13/2017.
 */
public class CustomerDetailsActivity extends AppCompatActivity implements CustomerDetailsActivityView {

    private ProgressDialog progress;
    private CustomerDetailsPresenter presenter;

    CustomerDetailsBaseAdapter adapter;
    SharedManagerUtil session;

    private Dialog dialog;

    TextInputLayout til_next_visit_meeting_name, til_next_visit_meeting_date, til_next_visit_meeting_time;
    EditText et_next_visit_meeting_name, et_next_visit_meeting_date, et_next_visit_meeting_time;
    Spinner dialog_customer_meeting_spinner_meeting_type;
    ExpandableRelativeLayout expandableLayout1, expandableLayout2;

    TextView tv_customer_code, tv_customer_name, tv_customer_shop_name, tv_customer_address, tv_customer_country, tv_customer_state, tv_customer_city, tv_customer_pin_code, tv_customer_email, tv_customer_mobile_no, tv_customer_alternate_no, tv_customer_type;

    Button btn_back;

    ListView list;

    FloatingActionButton fab_add_customer_meeting;

    ArrayList<String> meetingTypeList = new ArrayList<String>();
    ArrayList<String> meetingTypeId = new ArrayList<String>();
    ArrayAdapter<String> meetingTypeAdapter;
    String meetingTypeid = "";
    String meetingTypeSelect="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Customer All Details");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCustomersActivity();
            }
        });

        // Session Manager
        session = new SharedManagerUtil(this);

        presenter = new CustomerDetailsPresenter(this);

        tv_customer_code = (TextView) findViewById(R.id.fragment_customer_info_detail_tv_customer_code);
        tv_customer_name = (TextView) findViewById(R.id.fragment_customer_info_detail_tv_customer_name);
        tv_customer_shop_name = (TextView) findViewById(R.id.fragment_customer_info_detail_tv_shop_name);
        tv_customer_address = (TextView) findViewById(R.id.fragment_customer_info_detail_tv_address);
        tv_customer_country = (TextView) findViewById(R.id.fragment_customer_info_detail_tv_country);
        tv_customer_state = (TextView) findViewById(R.id.fragment_customer_info_detail_tv_state);
        tv_customer_city = (TextView) findViewById(R.id.fragment_customer_info_detail_tv_city);
        tv_customer_pin_code = (TextView) findViewById(R.id.fragment_customer_info_detail_tv_pin_code);
        tv_customer_email = (TextView) findViewById(R.id.fragment_customer_info_detail_tv_email);
        tv_customer_mobile_no = (TextView) findViewById(R.id.fragment_customer_info_detail_tv_mobile_no);
        tv_customer_alternate_no = (TextView) findViewById(R.id.fragment_customer_info_detail_tv_alternate_no);
        tv_customer_type = (TextView) findViewById(R.id.fragment_customer_info_detail_tv_customer_type);

        //btn_back = (Button) v.findViewById(R.id.fragment_customer_info_detail_btn_back);
        list = (ListView) findViewById(R.id.list_view);
        expandableLayout1 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout1);
        expandableLayout2 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout2);

        fab_add_customer_meeting = (FloatingActionButton) findViewById(R.id.activity_customer_details_fab_add_meeting);

        fab_add_customer_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomerMeetingDialog();
            }
        });


        tv_customer_code.setText(CustomersData.selectedCustomerCode);
        tv_customer_name.setText(CustomersData.selectedCustomerFirstName + " "+ CustomersData.selectedCustomerLastName);
        tv_customer_shop_name.setText(CustomersData.selectedCustomerShopName);
        tv_customer_address.setText(CustomersData.selectedCustomerAddress);
        tv_customer_country.setText(CustomersData.selectedCustomerCountry);
        tv_customer_state.setText(CustomersData.selectedCustomerState);
        tv_customer_city.setText(CustomersData.selectedCustomerCity);
        tv_customer_pin_code.setText(CustomersData.selectedCustomerPinCode);
        tv_customer_email.setText(CustomersData.selectedCustomerEmail);
        tv_customer_mobile_no.setText(CustomersData.selectedCustomerMobileNo);
        tv_customer_alternate_no.setText(CustomersData.selectedCustomerAlternateNo);
        tv_customer_type.setText(CustomersData.selectedCustomerType);

        meetingTypeList.add("Select Type");
        meetingTypeList.add("Hot");
        meetingTypeList.add("Cold");
        getCustomerInfoDetails();

    }

    public void getCustomerInfoDetails(){
        if(isNetworkAvailable()){
            progress = new ProgressDialog(this);
            progress.setMessage("loading...");
            progress.show();
            progress.setCanceledOnTouchOutside(false);
            presenter.getCustomerInfoDetailService(UrlUtil.GET_CUSTOMER_MEETINGS+"?userId="+session.getUserID()+"&cusId="+CustomersData.selectedCustomerId);

        }else{
            Toast.makeText(this,"Internet Connection not Available", Toast.LENGTH_LONG).show();
        }
    }

    public void expandableButton1(View view) {
        expandableLayout1.toggle(); // toggle expand and collapse
    }

    public void expandableButton2(View view) {
        expandableLayout2.toggle(); // toggle expand and collapse
    }

    public void startCustomersActivity() {
        new ActivityUtil(this).startCustomersActivity();
    }

    @Override
    public void onBackPressed() {
        startCustomersActivity();
    }

    public void startGetCustomerInfoDetail() {
        progress.dismiss();

        adapter=new CustomerDetailsBaseAdapter(this, CustomerDetailsData.customerDetailsList);
        list.setAdapter(adapter);

        //expandableLayout2.toggle();

    }


    public void errorInfo(String msg){
        progress.dismiss();
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void openCustomerMeetingDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_customer_meeting);

        dialog_customer_meeting_spinner_meeting_type=(Spinner)dialog.findViewById(R.id.dialog_customer_meeting_spinner_meeting_type);
        et_next_visit_meeting_name = (EditText) dialog.findViewById(R.id.dialog_meeting_next_visit_et_meeting_name);
        et_next_visit_meeting_date = (EditText) dialog.findViewById(R.id.dialog_meeting_next_visit_et_meeting_date);
        et_next_visit_meeting_time = (EditText) dialog.findViewById(R.id.dialog_meeting_next_visit_et_meeting_time);

        til_next_visit_meeting_name = (TextInputLayout) dialog.findViewById(R.id.dialog_meeting_next_visit_til_meeting_name);
        til_next_visit_meeting_date = (TextInputLayout) dialog.findViewById(R.id.dialog_meeting_next_visit_til_meeting_date);
        til_next_visit_meeting_time = (TextInputLayout) dialog.findViewById(R.id.dialog_meeting_next_visit_til_meeting_time);

        ImageButton btn_cancel = (ImageButton) dialog.findViewById(R.id.dialog_meeting_next_visit_ib_cancel);
        Button btn_submit = (Button) dialog.findViewById(R.id.dialog_meeting_next_visit_btn_submit);

        et_next_visit_meeting_name.addTextChangedListener(new MyTextWatcher(et_next_visit_meeting_name));
        et_next_visit_meeting_date.addTextChangedListener(new MyTextWatcher(et_next_visit_meeting_date));
        et_next_visit_meeting_time.addTextChangedListener(new MyTextWatcher(et_next_visit_meeting_time));


        et_next_visit_meeting_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(CustomerDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                        DecimalFormat mFormat= new DecimalFormat("00");
                        mFormat.setRoundingMode(RoundingMode.DOWN);
                        selectedmonth = selectedmonth + 1;
                        String select_date =  selectedyear + "-" +  mFormat.format(Double.valueOf(selectedmonth)) + "-" +  mFormat.format(Double.valueOf(selectedday));
                        et_next_visit_meeting_date.setText("" + select_date);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                mDatePicker.show();
            }
        });



        et_next_visit_meeting_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CustomerDetailsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        DecimalFormat mFormat= new DecimalFormat("00");
                        mFormat.setRoundingMode(RoundingMode.DOWN);
                        String select_time =  mFormat.format(Double.valueOf(selectedHour)) + ":" +  mFormat.format(Double.valueOf(selectedMinute));
                        et_next_visit_meeting_time.setText(""+ select_time);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();


            }
        });


        meetingTypeAdapter = new ArrayAdapter<String>(CustomerDetailsActivity.this,R.layout.spinner_rows, meetingTypeList);
        meetingTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dialog_customer_meeting_spinner_meeting_type.setAdapter(meetingTypeAdapter);
        dialog_customer_meeting_spinner_meeting_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                //meetingTypeid = meetingTypeId.get(position);
                meetingTypeSelect=meetingTypeList.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateMeetingName()) {
                    return;
                }
                if (!validateMeetingDate()) {
                    return;
                }
                if (!validateMeetingTime()) {
                    return;
                }

                submitCustomerMeeting();


            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }


    public void submitCustomerMeeting() {

        String userId = session.getUserID();
        String userParentPath = session.getUserParentPath();
        String mtnCustomerId = CustomersData.selectedCustomerId;
        String mtnName = et_next_visit_meeting_name.getText().toString().trim();
        String mtnDate = et_next_visit_meeting_date.getText().toString().trim();
        String mtnTime = et_next_visit_meeting_time.getText().toString();

        if(isNetworkAvailable()){

                progress = new ProgressDialog(this);
                progress.setMessage("loading...");
                progress.show();
                progress.setCanceledOnTouchOutside(false);

                presenter.addCustomerMeetingService(UrlUtil.ADD_CUSTOMER_MEETING_URL, userId, mtnCustomerId, mtnName, mtnDate, mtnTime, userParentPath);

        }else{
            Toast.makeText(this,"Internet Connection not Available", Toast.LENGTH_LONG).show();
        }

    }


    public void successAddCustomerMeetingInfo(String msg){
        progress.dismiss();
        MDToast.makeText(this, msg, MDToast.LENGTH_LONG, MDToast.TYPE_SUCCESS);
        //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        dialog.dismiss();

        getCustomerInfoDetails();
    }

    public void errorAddCustomerMeetingInfo(String msg){
        progress.dismiss();
        MDToast.makeText(this, msg, MDToast.LENGTH_LONG, MDToast.TYPE_ERROR);
        //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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
                case R.id.dialog_meeting_next_visit_et_meeting_name:
                    validateMeetingName();
                    break;
                case R.id.dialog_meeting_next_visit_et_meeting_date:
                    validateMeetingDate();
                    break;
                case R.id.dialog_meeting_next_visit_et_meeting_time:
                    validateMeetingTime();
                    break;
            }
        }
    }


    private boolean validateMeetingName() {
        if (et_next_visit_meeting_name.getText().toString().trim().isEmpty()) {
            til_next_visit_meeting_name.setError("meeting name can not be blank");
            requestFocus(et_next_visit_meeting_name);
            return false;
        } else {
            til_next_visit_meeting_name.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateMeetingDate() {
        if (et_next_visit_meeting_date.getText().toString().trim().isEmpty()) {
            til_next_visit_meeting_date.setError("meeting date can not be blank");
            requestFocus(et_next_visit_meeting_date);
            return false;
        } else {
            til_next_visit_meeting_date.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateMeetingTime() {
        if (et_next_visit_meeting_time.getText().toString().trim().isEmpty()) {
            til_next_visit_meeting_time.setError("meeting time can not be blank");
            requestFocus(et_next_visit_meeting_time);
            return false;
        } else {
            til_next_visit_meeting_time.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
