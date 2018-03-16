package apps.lnsel.com.ambujasales.views.Dashboard.activities.CustomerAddScreen;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import apps.lnsel.com.ambujasales.R;
import apps.lnsel.com.ambujasales.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.ambujasales.presenters.CustomerAddPresenter;
import apps.lnsel.com.ambujasales.utils.ActivityUtil;
import apps.lnsel.com.ambujasales.utils.SharedManagerUtil;
import apps.lnsel.com.ambujasales.utils.UrlUtil;

/**
 * Created by apps2 on 5/15/2017.
 */
public class CustomerAddActivity extends AppCompatActivity implements CustomerAddActivityView {

    private CustomerAddPresenter presenter;

    SharedManagerUtil session;
    int india_position, west_bengal_position, kolkata_position;
    public static String customer_available_type;

    private ProgressDialog progress;

    private EditText et_customer_code, et_first_name, et_last_name, et_address, et_pin_code, et_email, et_mobile_no, et_meeting_name, et_meeting_date, et_meeting_time,
            activity_customer_add_et_customer_dob,activity_customer_add_et_customer_anniversary,activity_customer_add_et_customer_alternate_no,activity_customer_add_et_customer_landmark;

    private TextInputLayout tip_customer_code, tip_first_name, tip_last_name, activity_customer_add_customer_company_name, tip_address, tip_pin_code, tip_email, tip_mobile_no, tip_meeting_name, tip_meeting_date, tip_meeting_time,
            activity_customer_add_customer_industry_type,activity_customer_add_customer_department,activity_customer_add_customer_designation,activity_customer_add_customer_dob,
            activity_customer_add_customer_anniversary,activity_customer_add_customer_area,activity_customer_add_customer_alternate_no,activity_customer_add_customer_landmark;

    private Spinner spinner_country, spinner_state, spinner_city, spinner_customer_type;
    private Button btn_submit, btn_cancel;

    private AutoCompleteTextView activity_customer_add_act_customer_company_name,activity_customer_add_act_customer_industry_type,activity_customer_add_act_customer_department,activity_customer_add_act_customer_designation,activity_customer_add_act_customer_area;

    ArrayList<String> customerList = new ArrayList<String>();
    ArrayList<String> customerId = new ArrayList<String>();

    ArrayList<String> countryList = new ArrayList<String>();
    ArrayList<String> countryId = new ArrayList<String>();

    ArrayList<String> stateList = new ArrayList<String>();
    ArrayList<String> stateId = new ArrayList<String>();

    ArrayList<String> cityList = new ArrayList<String>();
    ArrayList<String> cityId = new ArrayList<String>();

    ArrayList<String> industryList = new ArrayList<String>();
    ArrayList<String> industryId = new ArrayList<String>();

    ArrayList<String> departmentList = new ArrayList<String>();
    ArrayList<String> departmentId = new ArrayList<String>();

    ArrayList<String> designationList = new ArrayList<String>();
    ArrayList<String> designationId = new ArrayList<String>();

    ArrayList<String> areaList = new ArrayList<String>();
    ArrayList<String> areaId = new ArrayList<String>();

    ArrayList<String> companyList = new ArrayList<String>();
    ArrayList<String> companyId = new ArrayList<String>();


    ArrayAdapter<String> customerAdapter;
    ArrayAdapter<String> countryAdapter;
    ArrayAdapter<String> stateAdapter;
    ArrayAdapter<String> cityAdapter;

    ArrayAdapter<String> industryAdapter;
    ArrayAdapter<String> departmentAdapter;
    ArrayAdapter<String> designationAdapter;
    ArrayAdapter<String> areaAdapter;
    ArrayAdapter<String> companyAdapter;

    String customerid = "";
    String countryid = "";
    String stateid = "";
    String cityid = "";



    String industryid = "";
    String departmentid = "";
    String designationid = "";
    String areaid = "";
    String companyid = "";



    String customerSelect="";
    String countrySelect="";
    String stateSelect="";
    String citySelect="";

    String industrySelect="";
    String departmentSelect="";
    String designationSelect="";
    String areaSelect="";
    String companySelect="";

    private boolean gpsEnabled;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_add);

        // Session Manager
        session = new SharedManagerUtil(this);

        presenter = new CustomerAddPresenter(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add Customer");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCustomersActivity();
            }
        });

        doCustomerTypeList();

        tip_customer_code = (TextInputLayout) findViewById(R.id.fragment_customers_tab_add_customer_tip_customer_code);
        tip_first_name = (TextInputLayout) findViewById(R.id.fragment_customers_tab_add_customer_tip_first_name);
        tip_last_name = (TextInputLayout) findViewById(R.id.fragment_customers_tab_add_customer_tip_last_name);

        tip_address = (TextInputLayout) findViewById(R.id.fragment_customers_tab_add_customer_tip_address);
        tip_pin_code = (TextInputLayout) findViewById(R.id.fragment_customers_tab_add_customer_tip_pin_code);
        tip_email = (TextInputLayout) findViewById(R.id.fragment_customers_tab_add_customer_tip_email);
        tip_mobile_no = (TextInputLayout) findViewById(R.id.fragment_customers_tab_add_customer_tip_mobile_no);
        tip_meeting_name = (TextInputLayout) findViewById(R.id.fragment_customers_tab_add_customer_tip_meeting_name);
        tip_meeting_date = (TextInputLayout) findViewById(R.id.fragment_customers_tab_add_customer_tip_meeting_date);
        tip_meeting_time = (TextInputLayout) findViewById(R.id.fragment_customers_tab_add_customer_tip_meeting_time);



        //////////////////////////////////////////////////////////////////////////////////////////////////
        activity_customer_add_customer_industry_type = (TextInputLayout) findViewById(R.id.activity_customer_add_customer_industry_type);
        activity_customer_add_customer_department = (TextInputLayout) findViewById(R.id.activity_customer_add_customer_department);
        activity_customer_add_customer_designation = (TextInputLayout) findViewById(R.id.activity_customer_add_customer_designation);
        activity_customer_add_customer_dob = (TextInputLayout) findViewById(R.id.activity_customer_add_customer_dob);
        activity_customer_add_customer_anniversary = (TextInputLayout) findViewById(R.id.activity_customer_add_customer_anniversary);
        activity_customer_add_customer_area = (TextInputLayout) findViewById(R.id.activity_customer_add_customer_area);
        activity_customer_add_customer_alternate_no = (TextInputLayout) findViewById(R.id.activity_customer_add_customer_alternate_no);
        activity_customer_add_customer_company_name = (TextInputLayout) findViewById(R.id.activity_customer_add_customer_company_name);
        activity_customer_add_customer_landmark = (TextInputLayout) findViewById(R.id.activity_customer_add_customer_landmark);



        activity_customer_add_act_customer_industry_type = (AutoCompleteTextView) findViewById(R.id.activity_customer_add_act_customer_industry_type);
        activity_customer_add_act_customer_department = (AutoCompleteTextView) findViewById(R.id.activity_customer_add_act_customer_department);
        activity_customer_add_act_customer_designation = (AutoCompleteTextView) findViewById(R.id.activity_customer_add_act_customer_designation);
        activity_customer_add_act_customer_area = (AutoCompleteTextView) findViewById(R.id.activity_customer_add_act_customer_area);
        activity_customer_add_act_customer_company_name = (AutoCompleteTextView) findViewById(R.id.activity_customer_add_act_customer_company_name);

        activity_customer_add_et_customer_dob = (EditText) findViewById(R.id.activity_customer_add_et_customer_dob);
        activity_customer_add_et_customer_anniversary = (EditText) findViewById(R.id.activity_customer_add_et_customer_anniversary);
        activity_customer_add_et_customer_alternate_no = (EditText) findViewById(R.id.activity_customer_add_et_customer_alternate_no);
        activity_customer_add_et_customer_landmark=(EditText)findViewById(R.id.activity_customer_add_et_customer_landmark);


        activity_customer_add_act_customer_industry_type.addTextChangedListener(new MyTextWatcher(activity_customer_add_act_customer_industry_type));
        activity_customer_add_act_customer_department.addTextChangedListener(new MyTextWatcher(activity_customer_add_act_customer_department));
        activity_customer_add_act_customer_designation.addTextChangedListener(new MyTextWatcher(activity_customer_add_act_customer_designation));
        activity_customer_add_act_customer_area.addTextChangedListener(new MyTextWatcher(activity_customer_add_act_customer_area));
        activity_customer_add_act_customer_company_name.addTextChangedListener(new MyTextWatcher(activity_customer_add_act_customer_company_name));

        activity_customer_add_et_customer_dob.addTextChangedListener(new MyTextWatcher(activity_customer_add_et_customer_dob));
        activity_customer_add_et_customer_anniversary.addTextChangedListener(new MyTextWatcher(activity_customer_add_et_customer_anniversary));
        activity_customer_add_et_customer_alternate_no.addTextChangedListener(new MyTextWatcher(activity_customer_add_et_customer_alternate_no));
        activity_customer_add_et_customer_landmark.addTextChangedListener(new MyTextWatcher(activity_customer_add_et_customer_landmark));

        ///////////////////////////////////////////////////////////////////////////////////////////////////////



        et_customer_code = (EditText) findViewById(R.id.fragment_customers_tab_add_customer_et_customer_code);
        et_first_name = (EditText) findViewById(R.id.fragment_customers_tab_add_customer_et_first_name);
        et_last_name = (EditText) findViewById(R.id.fragment_customers_tab_add_customer_et_last_name);
        et_address = (EditText) findViewById(R.id.fragment_customers_tab_add_customer_et_address);
        et_pin_code = (EditText) findViewById(R.id.fragment_customers_tab_add_customer_et_pin_code);
        et_email = (EditText) findViewById(R.id.fragment_customers_tab_add_customer_et_email);
        et_mobile_no = (EditText) findViewById(R.id.fragment_customers_tab_add_customer_et_mobile_no);

        et_customer_code.addTextChangedListener(new MyTextWatcher(et_customer_code));
        et_first_name.addTextChangedListener(new MyTextWatcher(et_first_name));
        et_last_name.addTextChangedListener(new MyTextWatcher(et_last_name));

        et_address.addTextChangedListener(new MyTextWatcher(et_address));
        et_pin_code.addTextChangedListener(new MyTextWatcher(et_pin_code));
        et_email.addTextChangedListener(new MyTextWatcher(et_email));
        et_mobile_no.addTextChangedListener(new MyTextWatcher(et_mobile_no));


        spinner_country = (Spinner) findViewById(R.id.fragment_customers_tab_add_customer_spinner_country);
        spinner_state = (Spinner) findViewById(R.id.fragment_customers_tab_add_customer_spinner_state);
        spinner_city = (Spinner) findViewById(R.id.fragment_customers_tab_add_customer_spinner_city);
        spinner_customer_type = (Spinner) findViewById(R.id.fragment_customers_tab_add_customer_spinner_customer_type);

        btn_cancel = (Button) findViewById(R.id.activity_meeting_add_btn_cancel);
        btn_submit = (Button) findViewById(R.id.activity_meeting_add_btn_submit);




        spinner_customer_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                customerid = customerId.get(position);
                customerSelect=customerList.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });

        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                countryid = countryId.get(position);
                countrySelect=countryList.get(position);

                if(!countryid.equalsIgnoreCase("-1"))
                    doStateList(countryid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });

        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                stateid = stateId.get(position);
                stateSelect=stateList.get(position);

                if(!stateid.equalsIgnoreCase("-1"))
                    doCityList(stateid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });

        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                cityid=cityId.get(position);
                citySelect=cityList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });

        customerAdapter = new ArrayAdapter<String>(CustomerAddActivity.this,R.layout.spinner_rows, customerList);
        customerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_customer_type.setAdapter(customerAdapter);

        countryAdapter = new ArrayAdapter<String>(CustomerAddActivity.this,R.layout.spinner_rows, countryList);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_country.setAdapter(countryAdapter);

        stateAdapter = new ArrayAdapter<String>(CustomerAddActivity.this,R.layout.spinner_rows, stateList);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_state.setAdapter(stateAdapter);

        cityAdapter = new ArrayAdapter<String>(CustomerAddActivity.this,R.layout.spinner_rows, cityList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_city.setAdapter(cityAdapter);


        //start company  =============================================================
        companyAdapter= new ArrayAdapter<String>(CustomerAddActivity.this, android.R.layout.simple_dropdown_item_1line, companyList);
        activity_customer_add_act_customer_company_name.setAdapter(companyAdapter);
        activity_customer_add_act_customer_company_name.setThreshold(1);
        activity_customer_add_act_customer_company_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                activity_customer_add_act_customer_company_name.showDropDown();
            }
        });
        activity_customer_add_act_customer_company_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                companySelect=activity_customer_add_act_customer_company_name.getText().toString();
            }
        });
        //end company ==============================================================

        //start industry type =============================================================
        industryAdapter= new ArrayAdapter<String>(CustomerAddActivity.this, android.R.layout.simple_dropdown_item_1line, industryList);
        activity_customer_add_act_customer_industry_type.setAdapter(industryAdapter);
        activity_customer_add_act_customer_industry_type.setThreshold(1);
        activity_customer_add_act_customer_industry_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                activity_customer_add_act_customer_industry_type.showDropDown();
            }
        });
        activity_customer_add_act_customer_industry_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                industrySelect=activity_customer_add_act_customer_industry_type.getText().toString();
            }
        });
        //end industry type==============================================================

        //start Department  =============================================================
        departmentAdapter= new ArrayAdapter<String>(CustomerAddActivity.this, android.R.layout.simple_dropdown_item_1line, departmentList);
        activity_customer_add_act_customer_department.setAdapter(departmentAdapter);
        activity_customer_add_act_customer_department.setThreshold(1);
        activity_customer_add_act_customer_department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                activity_customer_add_act_customer_department.showDropDown();
            }
        });
        activity_customer_add_act_customer_department.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                departmentSelect=activity_customer_add_act_customer_department.getText().toString();
            }
        });
        //end Department ==============================================================

        //start Designation  =============================================================
        designationAdapter= new ArrayAdapter<String>(CustomerAddActivity.this, android.R.layout.simple_dropdown_item_1line, designationList);
        activity_customer_add_act_customer_designation.setAdapter(designationAdapter);
        activity_customer_add_act_customer_designation.setThreshold(1);
        activity_customer_add_act_customer_designation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                activity_customer_add_act_customer_designation.showDropDown();
            }
        });
        activity_customer_add_act_customer_designation.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                designationSelect=activity_customer_add_act_customer_designation.getText().toString();
            }
        });
        //end Designation ==============================================================

        //start Area  =============================================================
        areaAdapter= new ArrayAdapter<String>(CustomerAddActivity.this, android.R.layout.simple_dropdown_item_1line, areaList);
        activity_customer_add_act_customer_area.setAdapter(areaAdapter);
        activity_customer_add_act_customer_area.setThreshold(1);
        activity_customer_add_act_customer_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                activity_customer_add_act_customer_area.showDropDown();
            }
        });
        activity_customer_add_act_customer_area.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                areaSelect=activity_customer_add_act_customer_area.getText().toString();
            }
        });
        //end Area ==============================================================

        // start dob date picker===================================================
        activity_customer_add_et_customer_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(CustomerAddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                        DecimalFormat mFormat= new DecimalFormat("00");
                        mFormat.setRoundingMode(RoundingMode.DOWN);
                        selectedmonth = selectedmonth + 1;
                        String select_date =  selectedyear + "-" +  mFormat.format(Double.valueOf(selectedmonth)) + "-" +  mFormat.format(Double.valueOf(selectedday));
                        activity_customer_add_et_customer_dob.setText("" + select_date);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                mDatePicker.show();
            }
        });
        // end dob date picker===================================================

        // start anniversary date picker===================================================
        activity_customer_add_et_customer_anniversary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(CustomerAddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                        DecimalFormat mFormat= new DecimalFormat("00");
                        mFormat.setRoundingMode(RoundingMode.DOWN);
                        selectedmonth = selectedmonth + 1;
                        String select_date =  selectedyear + "-" +  mFormat.format(Double.valueOf(selectedmonth)) + "-" +  mFormat.format(Double.valueOf(selectedday));
                        activity_customer_add_et_customer_anniversary.setText("" + select_date);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                mDatePicker.show();
            }
        });
        // end anniversary date picker===================================================


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateCustomerCode()) {
                    return;
                }
                if (!validateFirstName()) {
                    return;
                }

                if (!validateLastName()) {
                    return;
                }

                if (!validateShopeName()) {
                    return;
                }
                if (!validateAddress()) {
                    return;
                }
                if(countryid.isEmpty()||countryid.equalsIgnoreCase("-1")){
                    requestFocus(spinner_country);
                    Toast.makeText(CustomerAddActivity.this,"please select country",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(stateid.isEmpty()||stateid.equalsIgnoreCase("-1")){
                    requestFocus(spinner_state);
                    Toast.makeText(CustomerAddActivity.this,"please select state",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(cityid.isEmpty()||cityid.equalsIgnoreCase("-1")){
                    requestFocus(spinner_city);
                    Toast.makeText(CustomerAddActivity.this,"please select city",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!validatePinCode()) {
                    return;
                }

                if (!validateEmail()) {
                    return;
                }
                if (!validateMobile()) {
                    return;
                }

                if(customerid.isEmpty()||customerid.equalsIgnoreCase("-1")){
                    requestFocus(spinner_customer_type);
                    Toast.makeText(CustomerAddActivity.this,"please select customer type",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!validateEmail()) {
                    return;
                }

                submitNewCustomerMeeting();


            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCustomersActivity();
            }
        });


    }


    public void submitNewCustomerMeeting(){

        String userId = session.getUserID();
        String userParentPath = session.getUserParentPath();
        String cusCode = et_customer_code.getText().toString().trim();
        String cusFirstName = et_first_name.getText().toString().trim();
        String cusLastName = et_last_name.getText().toString().trim();
        String cusShopName = activity_customer_add_act_customer_company_name.getText().toString().trim();
        String cusAddress = et_address.getText().toString().trim();
        String cusCountryId = countryid;
        String cusCountry = countrySelect;
        String cusStateId = stateid;
        String cusState = stateSelect;
        String cusCityId = cityid;
        String cusCity = citySelect;
        String cusPinCode = et_pin_code.getText().toString().trim();
        String cusEmail = et_email.getText().toString().trim();
        String cusMobileNo = et_mobile_no.getText().toString().trim();
        String cusCustomerTypeId = customerid;
        String cusCustomerType = customerSelect;

        if(isNetworkAvailable()){

            LocationManager service = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            gpsEnabled = service
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            if(!gpsEnabled){
                buildAlertMessageNoGps();
            }else {
                progress = new ProgressDialog(this);
                progress.setMessage("loading...");
                progress.show();
                progress.setCanceledOnTouchOutside(false);

                presenter.addNewCustomerService(UrlUtil.ADD_NEW_CUSTOMER, userId, userParentPath, cusCode, cusFirstName, cusLastName, cusShopName, cusAddress, cusCountryId, cusCountry, cusStateId, cusState, cusCityId, cusCity, cusPinCode, cusEmail, cusMobileNo, cusCustomerTypeId, cusCustomerType);
            }

        }else{
            Toast.makeText(this,"Internet Connection not Available", Toast.LENGTH_LONG).show();

        }


    }

    public void startCustomersActivity() {new ActivityUtil(this).startCustomersActivity();}

    @Override
    public void onBackPressed() {
        startCustomersActivity();
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
        MDToast.makeText(this, msg, MDToast.LENGTH_LONG, MDToast.TYPE_SUCCESS);
        //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        startCustomersActivity();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
                case R.id.fragment_customers_tab_add_customer_et_email:
                    validateEmail();
                    break;
                case R.id.fragment_customers_tab_add_customer_et_mobile_no:
                    validateMobile();
                    break;
                case R.id.fragment_customers_tab_add_customer_et_first_name:
                    validateFirstName();
                    break;
                case R.id.fragment_customers_tab_add_customer_et_last_name:
                    validateLastName();
                    break;
                case R.id.fragment_customers_tab_add_customer_et_customer_code:
                    validateCustomerCode();
                    break;
                case R.id.activity_customer_add_act_customer_company_name:
                    validateShopeName();
                    break;
                case R.id.fragment_customers_tab_add_customer_et_address:
                    validateAddress();
                    break;
                case R.id.fragment_customers_tab_add_customer_et_pin_code:
                    validatePinCode();
                    break;
            }
        }
    }



    //*********** Functions for Validation *********************//
    private boolean validateCustomerCode() {
        if (et_customer_code.getText().toString().trim().isEmpty()) {
            tip_customer_code.setError("customer code can not be blank");
            requestFocus(et_customer_code);
            return false;
        } else {
            tip_customer_code.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateFirstName() {
        if (et_first_name.getText().toString().trim().isEmpty()) {
            tip_first_name.setError("first name can not be blank");
            requestFocus(et_first_name);
            return false;
        } else {
            tip_first_name.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateLastName() {
        if (et_last_name.getText().toString().trim().isEmpty()) {
            tip_last_name.setError("last name can not be blank");
            requestFocus(et_last_name);
            return false;
        } else {
            tip_last_name.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateShopeName() {
        if (activity_customer_add_act_customer_company_name.getText().toString().trim().isEmpty()) {
            activity_customer_add_customer_company_name.setError("company name can not be blank");
            requestFocus(activity_customer_add_act_customer_company_name);
            return false;
        } else {
            activity_customer_add_customer_company_name.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateAddress() {
        if (et_address.getText().toString().trim().isEmpty()) {
            tip_address.setError("address can not be blank");
            requestFocus(et_address);
            return false;
        } else {
            tip_address.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePinCode() {
        if (et_pin_code.getText().toString().trim().isEmpty()) {
            tip_pin_code.setError("pin code can not be blank");
            requestFocus(et_pin_code);
            return false;
        } else {
            tip_pin_code.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateEmail() {
        String email = et_email.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            tip_email.setError("email is not valid");
            requestFocus(et_email);
            return false;
        } else {
            tip_email.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateMobile() {
        String mobile_no=et_mobile_no.getText().toString().trim();

        if (mobile_no.isEmpty()||!isValidMobile(mobile_no)) {
            tip_mobile_no.setError("mobile no is not valid");
            requestFocus(et_mobile_no);
            return false;
        } else {
            tip_mobile_no.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidMobile(String phone) {
        boolean check=false;
        if(phone.length() < 10 || phone.length() > 10) {
            // if(phone.length() != 10) {
            check = false;
            //txtPhone.setError("Not Valid Number");
        } else {
            check = true;
        }
        return check;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    //*********** Web services for country ***********************//
    public void doCountryList(){
        String tag_json_arry = "json_array_req";

        String url = UrlUtil.GET_COUNTRIES;

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseCountryResponse(response);
                        Log.d("TAG", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(req, tag_json_arry);
    }

    public void parseCountryResponse(final JSONArray response){
        try {
            if(countryList.size()>0){

                countryList.clear();
                countryId.clear();
            }
            countryList.add("Select Country");
            countryId.add("-1");
            ///////////////////////////////
            industryList.add("Select Industry Type");
            departmentList.add("Select Department");
            designationList.add("Select Designation");
            areaList.add("Select Area");
            companyList.add("Select Company");
            ///////////////////////////////
            for (int i = 0; i < response.length(); i++) {

                JSONObject records = response.optJSONObject(i);

                if(records.optString("countryName").toString().equalsIgnoreCase("India")){
                    india_position = i+1;
                }



                countryList.add(records.optString("countryName"));
                countryId.add(records.optString("countryId"));

                ///////////////////////////////
                industryList.add(records.optString("countryName"));
                departmentList.add(records.optString("countryName"));
                designationList.add(records.optString("countryName"));
                areaList.add(records.optString("countryName"));
                companyList.add(records.optString("countryName"));
                ///////////////////////////////

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        countryAdapter.notifyDataSetChanged();
        spinner_country.setSelection(india_position);
    }


    //*********** Web services for State ***********************//
    public void doStateList(String countryid){
        String tag_json_arry = "json_array_req";

        String url = UrlUtil.GET_STATES_BY_ID+"?id="+countryid;

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseStateResponse(response);
                        Log.d("TAG", response.toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(req, tag_json_arry);
    }

    public void parseStateResponse(final JSONArray response){
        try {
            if(stateList.size()>0){

                stateList.clear();
                stateId.clear();
            }
            stateList.add("Select State");
            stateId.add("-1");
            for (int i = 0; i < response.length(); i++) {

                JSONObject records = response.optJSONObject(i);

                if(records.optString("stateName").toString().equalsIgnoreCase("West Bengal")){
                    west_bengal_position = i+1;
                }

                stateList.add(records.optString("stateName"));
                stateId.add(records.optString("stateId"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        stateAdapter.notifyDataSetChanged();
        spinner_state.setSelection(west_bengal_position);
    }


    //*********** Web services for city ***********************//
    public void doCityList(String stateid){
        String tag_json_arry = "json_array_req";

        String url = UrlUtil.GET_CITIES_BY_ID+"?id="+stateid;

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseCityResponse(response);
                        Log.d("TAG", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(req, tag_json_arry);
    }

    public void parseCityResponse(final JSONArray response){
        try {
            if(cityList.size()>0){

                cityList.clear();
                cityId.clear();
            }
            cityList.add("Select City");
            cityId.add("-1");
            for (int i = 0; i < response.length(); i++) {

                JSONObject records = response.optJSONObject(i);

                if(records.optString("cityName").toString().equalsIgnoreCase("Kolkata")){
                    kolkata_position = i+1;
                }

                cityList.add(records.optString("cityName"));
                cityId.add(records.optString("cityId"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        cityAdapter.notifyDataSetChanged();
        spinner_city.setSelection(kolkata_position);
    }


    //*********** Web services for Customer Type ***********************//
    public void doCustomerTypeList(){
        String tag_json_arry = "json_array_req";

        String url = UrlUtil.GET_CUSTOMER_TYPES;

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseCustomerTypeResponse(response);
                        Log.d("TAG", response.toString());
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req, tag_json_arry);
    }

    public void parseCustomerTypeResponse(final JSONArray response){
        try {
            if(customerList.size()>0){

                customerList.clear();
                customerId.clear();
            }
            customerList.add("Customer Type");
            customerId.add("-1");
            for (int i = 0; i < response.length(); i++) {

                JSONObject records = response.optJSONObject(i);
                Log.d("cusName: ", records.optString("custName"));

                customerList.add(records.optString("custName"));
                customerId.add(records.optString("custId"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        customerAdapter.notifyDataSetChanged();
        doCountryList();
    }
}
