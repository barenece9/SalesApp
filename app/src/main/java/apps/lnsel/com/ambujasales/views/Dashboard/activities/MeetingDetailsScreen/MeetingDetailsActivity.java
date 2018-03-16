package apps.lnsel.com.ambujasales.views.Dashboard.activities.MeetingDetailsScreen;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import apps.lnsel.com.ambujasales.R;
import apps.lnsel.com.ambujasales.helpers.CustomAdapter.MeetingDetailsAdapter.MeetingDetailsBaseAdapter;
import apps.lnsel.com.ambujasales.helpers.CustomAdapter.MeetingDetailsAdapter.MeetingDetailsData;
import apps.lnsel.com.ambujasales.helpers.CustomAdapter.MeetingsAdapter.MeetingsData;
import apps.lnsel.com.ambujasales.helpers.Service.GPSService;
import apps.lnsel.com.ambujasales.presenters.MeetingDetailsPresenter;
import apps.lnsel.com.ambujasales.utils.ActivityUtil;
import apps.lnsel.com.ambujasales.utils.SharedManagerUtil;
import apps.lnsel.com.ambujasales.utils.UrlUtil;

/**
 * Created by apps2 on 5/4/2017.
 */
public class MeetingDetailsActivity extends AppCompatActivity implements MeetingDetailsActivityView {

    private static final int PHONE_CALL = 115;

    public static String callingNumber;

    MeetingDetailsBaseAdapter adapter;

    private ProgressDialog progress;
    SharedManagerUtil session;
    private MeetingDetailsPresenter presenter;

    ExpandableRelativeLayout expandableLayout3;

    public static String meeting_visited_message, meeting_remarks_message;

    boolean gpsEnabled;

    private Dialog dialog;

    ListView list;

    ImageButton ib_call, ib_order, ib_visited, ib_meeting_expense, ib_next_visit, ib_add_picture, ib_signature, ib_remarks, ib_near_shop, ib_get_direction;

    TextView tv_meeting_name, tv_customer_name, tv_company_name, tv_mobile_no, tv_address, tv_meeting_date, tv_meeting_time, tv_visited, tv_picture, tv_signature, tv_remarks, tv_next_visit, tv_meeting_history_message;

    TextInputLayout til_next_visit_meeting_name, til_next_visit_meeting_date, til_next_visit_meeting_time;
    EditText et_next_visit_meeting_name, et_next_visit_meeting_date, et_next_visit_meeting_time;
    private Spinner dialog_meeting_next_visit_spinner_meeting_type;

    ArrayList<String> meetingTypeList = new ArrayList<String>();
    ArrayList<String> meetingTypeId = new ArrayList<String>();
    ArrayAdapter<String> meetingTypeAdapter;
    String meetingTypeid = "";
    String meetingTypeSelect="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Meeting Details");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMeetingsActivity();
            }
        });

        // Session Manager
        session = new SharedManagerUtil(this);

        presenter = new MeetingDetailsPresenter(this);

        tv_meeting_name = (TextView) findViewById(R.id.activity_meeting_details_tv_meeting_name);
        tv_customer_name = (TextView) findViewById(R.id.activity_meeting_details_tv_customer_name);
        tv_company_name = (TextView) findViewById(R.id.activity_meeting_details_tv_company_name);
        tv_mobile_no = (TextView) findViewById(R.id.activity_meeting_details_tv_mobile_no);
        tv_address = (TextView) findViewById(R.id.activity_meeting_details_tv_address);
        tv_meeting_date = (TextView) findViewById(R.id.activity_meeting_details_tv_meeting_date);
        tv_meeting_time = (TextView) findViewById(R.id.activity_meeting_details_tv_meeting_time);

        //tv_test = (TextView) findViewById(R.id.activity_meeting_details_tv_test);

        tv_visited = (TextView) findViewById(R.id.activity_meeting_details_tv_visited);
        tv_picture = (TextView) findViewById(R.id.activity_meeting_details_tv_picture_updated);
        tv_signature = (TextView) findViewById(R.id.activity_meeting_details_tv_signature_updated);
        tv_remarks = (TextView) findViewById(R.id.activity_meeting_details_tv_remarks_updated);
        tv_next_visit = (TextView) findViewById(R.id.activity_meeting_details_tv_next_visit_required);

        tv_meeting_history_message = (TextView) findViewById(R.id.activity_meeting_details_tv_meeting_history_message);

        ib_call = (ImageButton) findViewById(R.id.activity_meeting_details_ib_call);
        ib_order = (ImageButton) findViewById(R.id.activity_meeting_details_ib_order);
        ib_visited = (ImageButton) findViewById(R.id.activity_meeting_details_ib_visited);
        ib_next_visit = (ImageButton) findViewById(R.id.activity_meeting_details_ib_next_visit);
        ib_add_picture = (ImageButton) findViewById(R.id.activity_meeting_details_ib_add_picture);
        ib_signature = (ImageButton) findViewById(R.id.activity_meeting_details_ib_signature);
        ib_remarks = (ImageButton) findViewById(R.id.activity_meeting_details_ib_remarks);
        ib_near_shop = (ImageButton) findViewById(R.id.activity_meeting_details_ib_near_shop);
        ib_meeting_expense = (ImageButton) findViewById(R.id.activity_meeting_details_ib_expense);
        ib_get_direction = (ImageButton) findViewById(R.id.activity_meeting_details_ib_get_direction);

        list = (ListView) findViewById(R.id.activity_meeting_details_lv_list_view);


        ib_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callingMethod(MeetingsData.get_current_mtnCustomerMobileNo);
            }
        });

        ib_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMeetingOrderActivity();
            }
        });

        ib_add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMeetingPictureActivity();
            }
        });

        ib_signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMeetingSignatureActivity();
            }
        });

        ib_visited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVisitedDialog();
            }
        });

        ib_next_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNextVisitDialog();
            }
        });

        ib_remarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMeetingRemarksDialog();
            }
        });

        ib_near_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMeetingNearShopActivity();
            }
        });

        ib_meeting_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMeetingExpenseActivity();
            }
        });

        ib_get_direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customer_address = MeetingsData.get_current_mtnCustomerAddress;
                startGoogleMapDirectionActivity(customer_address);
            }
        });


        meetingTypeList.add("Select Type");
        meetingTypeList.add("Hot");
        meetingTypeList.add("Cold");

        if(isNetworkAvailable()){

            getMeetingDetails();


        }else{
            Toast.makeText(this,"Internet Connection not Available", Toast.LENGTH_LONG).show();
        }
    }


    public void getMeetingDetails(){
        progress = new ProgressDialog(this);
        progress.setMessage("loading...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        presenter.getMeetingDetailsService(UrlUtil.GET_MEETING_DETAILS_URL+"?userId="+session.getUserID()+"&meetingId="+ MeetingsData.current_meeting_id);
    }

    public void getMeetingHistory(String cusId){
        if(isNetworkAvailable()){
            progress = new ProgressDialog(this);
            progress.setMessage("loading...");
            progress.show();
            progress.setCanceledOnTouchOutside(false);
            presenter.getMeetingHistoryService(UrlUtil.GET_CUSTOMER_COMPLETED_MEETINGS+"?userId="+session.getUserID()+"&cusId="+ cusId);

        }else{
            Toast.makeText(this,"Internet Connection not Available", Toast.LENGTH_LONG).show();
        }
    }

    public void startGetMeetingHistory() {
        progress.dismiss();
        tv_meeting_history_message.setVisibility(View.GONE);
        Log.d("GetMeetingHistory: ", "before adapter");
        adapter=new MeetingDetailsBaseAdapter(this, MeetingDetailsData.meetingDetailsList);
        list.setAdapter(adapter);
        Log.d("GetMeetingHistory: ", "after adapter");
        //tv_test.setText("Get Meeting History Test, Get Meeting History Test, Get Meeting History Test, Get Meeting History Test, Get Meeting History Test, Get Meeting History Test, Get Meeting History Test");

    }

    public void errorMeetingHistoryInfo(String msg){
        if(progress != null){
            progress.dismiss();
        }
        //MDToast.makeText(this, msg, MDToast.LENGTH_LONG, MDToast.TYPE_ERROR);

        tv_meeting_history_message.setVisibility(View.VISIBLE);

    }

    public void startMeetingsActivity() {
        new ActivityUtil(this).startMeetingsActivity();
    }

    public void startMeetingOrderActivity() {
        new ActivityUtil(this).startMeetingOrderActivity();
    }

    public void startMeetingPictureActivity() {
        new ActivityUtil(this).startMeetingPictureActivity();
    }

    public void startMeetingSignatureActivity() {
        new ActivityUtil(this).startMeetingSignatureActivity();
    }

    public void startMeetingNearShopActivity() {
        new ActivityUtil(this).startMeetingNearShopActivity();
    }

    public void startMeetingExpenseActivity() {
        new ActivityUtil(this).startMeetingExpenseActivity();
    }

    public void startGoogleMapDirectionActivity(String address){
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+address);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void errorInfo(String msg){
        if(progress != null){
            progress.dismiss();
        }
        MDToast.makeText(this, msg, MDToast.LENGTH_LONG, MDToast.TYPE_ERROR);
        //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void successInfo(String msg){

        if(progress != null){
            progress.dismiss();
        }

        //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

        tv_meeting_name.setText(MeetingsData.get_current_mtnName);
        tv_customer_name.setText(MeetingsData.get_current_mtnCustomerName);
        tv_company_name.setText(MeetingsData.get_current_mtnCustomerShopName);
        tv_mobile_no.setText(MeetingsData.get_current_mtnCustomerMobileNo);
        tv_address.setText(MeetingsData.get_current_mtnCustomerAddress);
        tv_meeting_date.setText(MeetingsData.get_current_mtnDate);
        tv_meeting_time.setText(MeetingsData.get_current_mtnTime);

        tv_visited.setText(MeetingsData.get_current_mtnVisited);
        tv_picture.setText(MeetingsData.get_current_mtnPicture);
        tv_signature.setText(MeetingsData.get_current_mtnSignature);
        tv_remarks.setText(MeetingsData.get_current_mtnRemarks);
        tv_next_visit.setText(MeetingsData.get_current_mtnNextVisit);

        getMeetingHistory(MeetingsData.get_current_mtnCustomerId);
/*
        if(MeetingsData.get_current_mtnVisited.equals("yes")){
            ib_visited.setBackgroundResource(R.drawable.rounded_bg_selected);
            ib_visited.setEnabled(false);
        }*/
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void callingMethod(String number){
        callingNumber = number;
        boolean hasPermissionCall = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionCall) {
            ActivityCompat.requestPermissions(MeetingDetailsActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    PHONE_CALL);
        }else{
            Intent phoneIntent = new Intent(Intent.ACTION_CALL);
            phoneIntent.setData(Uri.parse("tel:"+number));
            if (ActivityCompat.checkSelfPermission(MeetingDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(phoneIntent);
        }
    }



    public void openVisitedDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_meeting_visited);

        final EditText et_message = (EditText) dialog.findViewById(R.id.dialog_meeting_visited_et_message);
        ImageButton ib_cancel = (ImageButton) dialog.findViewById(R.id.dialog_meeting_visited_ib_cancel);
        Button btn_submit = (Button) dialog.findViewById(R.id.dialog_meeting_visited_btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meeting_visited_message = et_message.getText().toString();
                submitVisitedUpdate();

            }
        });

        ib_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }

    public void successMeetingVisitedInfo(String msg){
        progress.dismiss();
        MDToast.makeText(this, msg, MDToast.LENGTH_LONG, MDToast.TYPE_SUCCESS);
        //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
        getMeetingDetails();
    }

    public void errorMeetingVisitedInfo(String msg){
        progress.dismiss();
        MDToast.makeText(this, msg, MDToast.LENGTH_LONG, MDToast.TYPE_ERROR);
        //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


    public void openMeetingRemarksDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_meeting_remarks);

        final EditText et_message = (EditText) dialog.findViewById(R.id.dialog_meeting_remarks_et_message);
        ImageButton ib_cancel = (ImageButton) dialog.findViewById(R.id.dialog_meeting_remarks_ib_cancel);
        Button btn_submit = (Button) dialog.findViewById(R.id.dialog_meeting_remarks_btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meeting_remarks_message = et_message.getText().toString();
                submitMeetingRemarksUpdate();

            }
        });

        ib_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }

    public void successMeetingRemarksInfo(String msg){
        progress.dismiss();
        MDToast.makeText(this, msg, MDToast.LENGTH_LONG, MDToast.TYPE_SUCCESS);
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
        getMeetingDetails();
    }

    public void errorMeetingRemarksInfo(String msg){
        progress.dismiss();
        MDToast.makeText(this, msg, MDToast.LENGTH_LONG, MDToast.TYPE_ERROR);
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    public void submitVisitedUpdate() {

        if (isNetworkAvailable()) {

            LocationManager service = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            gpsEnabled = service
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!gpsEnabled) {
                buildAlertMessageNoGps();
            } else {

                final String userId = session.getUserID();
                final String mtnId = MeetingsData.get_current_mtnId;

                SimpleDateFormat stf = new SimpleDateFormat("HH:mm");
                String mtnVisitedTime = stf.format(new Date());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                final String mtnVisitedDate = sdf.format(new Date());

                GPSService mGPSService = new GPSService(this);
                mGPSService.getLocation();

                double latitude = mGPSService.getLatitude();
                double longitude = mGPSService.getLongitude();

                String mtnVisitedLat = Double.toString(latitude);
                String mtnVisitedLong = Double.toString(longitude);

                String mtnVisitedMessage = meeting_visited_message;


                if (mtnVisitedLat.equals("0.0")) {
                    Toast.makeText(this, "GPS Device not responding correctly", Toast.LENGTH_LONG).show();
                } else {
                    progress = new ProgressDialog(this);
                    progress.setMessage("loading...");
                    progress.show();
                    progress.setCanceledOnTouchOutside(false);
                    presenter.updateMeetingVisitedService(UrlUtil.MEETING_VISITED_UPDATE_URL, userId, mtnId, mtnVisitedDate, mtnVisitedTime, mtnVisitedLat, mtnVisitedLong, mtnVisitedMessage);
                }
            }
        } else {
            Toast.makeText(this, "Internet Connection not Available", Toast.LENGTH_LONG).show();
        }
    }

    public void submitMeetingRemarksUpdate() {

        if (isNetworkAvailable()) {

            LocationManager service = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            gpsEnabled = service
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!gpsEnabled) {
                buildAlertMessageNoGps();
            } else {

                final String userId = session.getUserID();
                final String mtnId = MeetingsData.get_current_mtnId;

                SimpleDateFormat stf = new SimpleDateFormat("HH:mm");
                String mtnRemarksTime = stf.format(new Date());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                final String mtnRemarksDate = sdf.format(new Date());

                GPSService mGPSService = new GPSService(this);
                mGPSService.getLocation();

                double latitude = mGPSService.getLatitude();
                double longitude = mGPSService.getLongitude();

                String mtnRemarksLat = Double.toString(latitude);
                String mtnRemarksLong = Double.toString(longitude);

                String mtnRemarksMessage = meeting_remarks_message;


                if (mtnRemarksLat.equals("0.0")) {
                    Toast.makeText(this, "GPS Device not responding correctly", Toast.LENGTH_LONG).show();
                } else {
                    progress = new ProgressDialog(this);
                    progress.setMessage("loading...");
                    progress.show();
                    progress.setCanceledOnTouchOutside(false);
                    presenter.updateMeetingRemarksService(UrlUtil.MEETING_REMARKS_UPDATE_URL, userId, mtnId, mtnRemarksDate, mtnRemarksTime, mtnRemarksLat, mtnRemarksLong, mtnRemarksMessage);
                }
            }
        } else {
            Toast.makeText(this, "Internet Connection not Available", Toast.LENGTH_LONG).show();
        }
    }

    public void submitNextVisitUpdate() {

        String userId = session.getUserID();
        String userParentPath = session.getUserParentPath();
        String mtnId = MeetingsData.get_current_mtnId;
        String mtnCustomerId = MeetingsData.get_current_mtnCustomerId;
        String mtnName = et_next_visit_meeting_name.getText().toString().trim();
        String mtnDate = et_next_visit_meeting_date.getText().toString().trim();
        String mtnTime = et_next_visit_meeting_time.getText().toString();

        if(isNetworkAvailable()){

            LocationManager service = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            gpsEnabled = service
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            if(!gpsEnabled){
                buildAlertMessageNoGps();
            }else{
                progress = new ProgressDialog(this);
                progress.setMessage("loading...");
                progress.show();
                progress.setCanceledOnTouchOutside(false);

                presenter.updateMeetingNextVisitService(UrlUtil.NEXT_MEETING_UPDATE_URL, userId, mtnId, mtnCustomerId, mtnName, mtnDate, mtnTime, userParentPath);
            }


        }else{
            Toast.makeText(this,"Internet Connection not Available", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        startMeetingsActivity();
    }

    public void successMeetingNextVisitInfo(String msg){
        progress.dismiss();
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
        getMeetingDetails();
    }

    public void errorMeetingNextVisitInfo(String msg){
        progress.dismiss();
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


    public void openNextVisitDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_meeting_next_visit);


        dialog_meeting_next_visit_spinner_meeting_type=(Spinner)dialog.findViewById(R.id.dialog_meeting_next_visit_spinner_meeting_type);
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
                mDatePicker = new DatePickerDialog(MeetingDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                mTimePicker = new TimePickerDialog(MeetingDetailsActivity.this, new TimePickerDialog.OnTimeSetListener() {
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



        meetingTypeAdapter = new ArrayAdapter<String>(MeetingDetailsActivity.this,R.layout.spinner_rows, meetingTypeList);
        meetingTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dialog_meeting_next_visit_spinner_meeting_type.setAdapter(meetingTypeAdapter);
        dialog_meeting_next_visit_spinner_meeting_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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

                submitNextVisitUpdate();


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



    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Activate GPS to use use location services")
                .setTitle("Location not available, Open GPS")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
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
