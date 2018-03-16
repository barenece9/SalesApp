package apps.lnsel.com.ambujasales.helpers.CustomAdapter.CustomersAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import apps.lnsel.com.ambujasales.R;

/**
 * Created by apps2 on 5/13/2017.
 */
public class CustomersBaseAdapter extends BaseAdapter {


    Context context;
    private static LayoutInflater inflater=null;

    private List<CustomersSetterGetter> customersList = null;
    private ArrayList<CustomersSetterGetter> arraylist;

    public CustomersBaseAdapter(Activity context, List<CustomersSetterGetter> customersList) {

        this.context = context;

        this.customersList = customersList;
        this.arraylist = new ArrayList<CustomersSetterGetter>();
        this.arraylist.addAll(customersList);

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(final int position, final View view, ViewGroup parent) {
        View rowView=inflater.inflate(R.layout.cardview_customerinfo, null,true);

        TextView tv_customer_name = (TextView) rowView.findViewById(R.id.cardview_customerinfo_tv_customer_name);
        TextView tv_customer_shop_name = (TextView) rowView.findViewById(R.id.cardview_customerinfo_tv_customer_shop_name);
        TextView tv_customer_mobile_no = (TextView) rowView.findViewById(R.id.cardview_customerinfo_tv_customer_mobile_no);
        TextView tv_customer_address = (TextView) rowView.findViewById(R.id.cardview_customerinfo_tv_customer_address);

        tv_customer_name.setText(customersList.get(position).getCusFirstName()+" "+customersList.get(position).getCusLastName());
        tv_customer_shop_name.setText(customersList.get(position).getCusShopName());
        tv_customer_mobile_no.setText(customersList.get(position).getCusMobileNo());
        tv_customer_address.setText(customersList.get(position).getCusAddress());

        return rowView;

    }

    @Override
    public int getCount() {
        return customersList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    public Object getCustomerId(int position){
        return customersList.get(position).getCusId();
    }

    public Object getCustomerCode(int position){
        return customersList.get(position).getCusCode();
    }

    public Object getCustomerFirstName(int position){
        return customersList.get(position).getCusFirstName();
    }

    public Object getCustomerLastName(int position){
        return customersList.get(position).getCusLastName();
    }

    public Object getCustomerShopName(int position){
        return customersList.get(position).getCusShopName();
    }

    public Object getCustomerAddress(int position){
        return customersList.get(position).getCusAddress();
    }

    public Object getCustomerCountry(int position){
        return customersList.get(position).getCusCountry();
    }

    public Object getCustomerState(int position){
        return customersList.get(position).getCusState();
    }

    public Object getCustomerCity(int position){
        return customersList.get(position).getCusCity();
    }

    public Object getCustomerPinCode(int position){
        return customersList.get(position).getCusPinCode();
    }

    public Object getCustomerEmail(int position){
        return customersList.get(position).getCusEmail();
    }

    public Object getCustomerMobileNo(int position){
        return customersList.get(position).getCusMobileNo();
    }

    public Object getCustomerAlternateNo(int position){
        return customersList.get(position).getCusAlternateNo();
    }

    public Object getCustomerType(int position){
        return customersList.get(position).getCusCustomerType();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    // Filter Class
    public void filter(String charText, View btn_clear, TextView tv_search_message) {
        charText = charText.toLowerCase(Locale.getDefault());
        customersList.clear();
        if (charText.length() == 0||charText.equalsIgnoreCase("")) {
            customersList.addAll(arraylist);
            btn_clear.setVisibility(View.GONE);
            //tv_search_message.setVisibility(View.VISIBLE);
            //tv_search_message.setText("Search for customer details");
        }
        else
        {
            for (CustomersSetterGetter wp : arraylist)
            {
                if (wp.getCusFirstName().toLowerCase(Locale.getDefault()).contains(charText)||
                        wp.getCusLastName().toLowerCase(Locale.getDefault()).contains(charText)||
                        wp.getCusShopName().toLowerCase(Locale.getDefault()).contains(charText)||
                        wp.getCusMobileNo().toLowerCase(Locale.getDefault()).contains(charText)||
                        wp.getCusAddress().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    customersList.add(wp);
                }
            }
            if(customersList.size()>0) {
                tv_search_message.setVisibility(View.GONE);
            }else {
                tv_search_message.setVisibility(View.VISIBLE);
                tv_search_message.setText("No result found");
            }
            btn_clear.setVisibility(View.VISIBLE);
        }
        notifyDataSetChanged();
    }
}
