package apps.lnsel.com.ambujasales.helpers.CustomAdapter.OrdersAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import apps.lnsel.com.ambujasales.R;
import apps.lnsel.com.ambujasales.utils.ActivityUtil;
import apps.lnsel.com.ambujasales.views.Dashboard.activities.OrdersScreen.OrdersActivity;

/**
 * Created by apps2 on 5/25/2017.
 */
public class OrdersBaseAdapter extends BaseAdapter {

    Context context;
    private static LayoutInflater inflater=null;

    private List<OrdersSetterGetter> ordersList = null;
    private ArrayList<OrdersSetterGetter> arraylist;

    public OrdersBaseAdapter(Activity context, List<OrdersSetterGetter> ordersList) {
        this.context = context;

        this.ordersList = ordersList;
        this.arraylist = new ArrayList<OrdersSetterGetter>();
        this.arraylist.addAll(ordersList);

        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public View getView(final int position, final View view, ViewGroup parent) {
        View rowView=inflater.inflate(R.layout.cardview_orders, null,true);

        TextView tv_order_name = (TextView) rowView.findViewById(R.id.cardview_orders_tv_order_name);
        TextView tv_order_amount = (TextView) rowView.findViewById(R.id.cardview_orders_tv_order_amount);
        TextView tv_order_description = (TextView) rowView.findViewById(R.id.cardview_orders_tv_order_description);
        TextView tv_order_description_large = (TextView) rowView.findViewById(R.id.cardview_orders_tv_order_description_large);
        TextView tv_order_customer_name = (TextView) rowView.findViewById(R.id.cardview_orders_tv_customer_name);
        TextView tv_order_meeting_name = (TextView) rowView.findViewById(R.id.cardview_orders_tv_meeting_name);

        ImageButton ib_edit_order = (ImageButton) rowView.findViewById(R.id.cardview_orders_ib_edit_order);
        ImageButton ib_delete_order = (ImageButton) rowView.findViewById(R.id.cardview_orders_ib_delete_order);

        int description_length = ordersList.get(position).getOrdDescription().toString().length();

        tv_order_name.setText(ordersList.get(position).getOrdName());
        tv_order_amount.setText(ordersList.get(position).getOrdAmount());
        tv_order_description.setText(ordersList.get(position).getOrdDescription());
        tv_order_description_large.setText(ordersList.get(position).getOrdDescription());
        tv_order_customer_name.setText(ordersList.get(position).getOrdCustomerName());
        tv_order_meeting_name.setText(ordersList.get(position).getOrdMeetingName());

        if(description_length > 24){
            tv_order_description.setVisibility(View.GONE);
            tv_order_description_large.setVisibility(View.VISIBLE);
        }else{
            tv_order_description.setVisibility(View.VISIBLE);
            tv_order_description_large.setVisibility(View.GONE);
        }


        ib_edit_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrdersData.current_order_id = ordersList.get(position).getOrdId();
                OrdersData.current_order_name = ordersList.get(position).getOrdName();
                OrdersData.current_order_amount= ordersList.get(position).getOrdAmount();
                OrdersData.current_order_description = ordersList.get(position).getOrdDescription();

                new ActivityUtil((Activity) context).startOrderEditActivity();
            }
        });

        ib_delete_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ordId = ordersList.get(position).getOrdId();
                deleteDialog(ordId);
            }
        });

        return rowView;

    }

    // Filter Class
    public void filter(String charText, View btn_clear) {
        charText = charText.toLowerCase(Locale.getDefault());
        ordersList.clear();
        if (charText.length() == 0||charText.equalsIgnoreCase("")) {
            ordersList.addAll(arraylist);
            btn_clear.setVisibility(View.GONE);
        }
        else
        {
            for (OrdersSetterGetter wp : arraylist)
            {
                if (wp.getOrdName().toLowerCase(Locale.getDefault()).contains(charText)||
                        wp.getOrdAmount().toLowerCase(Locale.getDefault()).contains(charText)||
                        wp.getOrdDescription().toLowerCase(Locale.getDefault()).contains(charText)||
                        wp.getOrdMeetingName().toLowerCase(Locale.getDefault()).contains(charText)||
                        wp.getOrdCustomerName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    ordersList.add(wp);
                }
            }
            btn_clear.setVisibility(View.VISIBLE);
        }
        notifyDataSetChanged();
    }

    private void deleteDialog(final String ordId){
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder((Activity) context);
        builder.setMessage("Are you sure, you want to delete?")
                .setTitle("Delete Expense")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                        ((OrdersActivity) ((Activity) context))
                                .deleteOrder(ordId);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public int getCount() {
        return ordersList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
