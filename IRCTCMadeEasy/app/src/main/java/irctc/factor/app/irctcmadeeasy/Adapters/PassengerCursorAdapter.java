package irctc.factor.app.irctcmadeeasy.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import irctc.factor.app.irctcmadeeasy.R;
import irctc.factor.app.irctcmadeeasy.database.PassengerInfoDao;

/**
 * Created by hassanhussain on 7/11/2016.
 */

public class PassengerCursorAdapter extends RecyclerView.Adapter<PassengerCursorAdapter.PassengerViewHolder> {

    // Because RecyclerView.Adapter in its current form doesn't natively
    // support cursors, we wrap a CursorAdapter that will do all the job
    // for us.
    CursorAdapter mCursorAdapter;
    Context mContext;

    public PassengerCursorAdapter(Context context, Cursor c) {
        mContext = context;
        mCursorAdapter = new CursorAdapter(mContext, c, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View v = LayoutInflater.from(context).inflate(R.layout.layout_passenger_view, parent, false);
                PassengerViewHolder passenger = new PassengerViewHolder(v);
                v.setTag(passenger);
                return v;
            }

            // The bindView method is used to bind all data to a given view
            // such as setting the text on a TextView.
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                PassengerViewHolder viewHolder = null;
                if(view.getTag() instanceof PassengerViewHolder)
                    viewHolder = (PassengerViewHolder) view.getTag();
                else {
                    viewHolder = new PassengerViewHolder(view);
                    view.setTag(viewHolder);
                }

                String passengerName = cursor.getString(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.Name.columnName));
                String passengerProof = cursor.getString(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.AadharCardNo.columnName));
                int passengerAge = cursor.getInt(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.Age.columnName));
                String passengerBerth = cursor.getString(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.Berth.columnName));
                int passengerChild = cursor.getInt(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.Child.columnName));
                String passengerFood = cursor.getString(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.Food.columnName));
                String passengerGender = cursor.getString(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.Gender.columnName));
                int passengerId = cursor.getInt(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.Id.columnName));
                String passengerNationality = cursor.getString(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.Nationality.columnName));
                int passengerTransaction = cursor.getInt(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.TransactionId.columnName));


                String sText1 = passengerName + ", " + passengerGender == "MALE"? "M" : "F" + ", " + passengerAge;
                String sText2 = passengerBerth+ ", " + passengerFood == "MALE"? "M" : "F" ;
                String sText3 = passengerChild > 0 ? "CHILD" : passengerAge > 60 ? "SENIOR" : "";
                sText3 += ", ID Proof : " + passengerProof;

                // Populate fields with extracted properties
                viewHolder.txtView1.setText(sText1);
                viewHolder.txtView2.setText(sText2);
                viewHolder.txtView3.setText(sText3);
            }
        };
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }

    @Override
    public void onBindViewHolder(PassengerViewHolder holder, int position) {
        // Passing the binding operation to cursor loader
        mCursorAdapter.getCursor().moveToPosition(position); //EDITED: added this line as suggested in the comments below, thanks :)
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());

    }

    @Override
    public PassengerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Passing the inflater job to the cursor-adapter
        View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        return new PassengerViewHolder(v);
    }

    static class PassengerViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.btn_edit_info)
        Button btnEdit;
        @BindView(R.id.btn_delete_info)
        Button btnDelete;

        @BindView(R.id.txt_name_age_gender)
        TextView txtView1;
        @BindView(R.id.txt_berth_food)
        TextView txtView2;
        @BindView(R.id.txt_child_senior_berth_bed)
        TextView txtView3;

        @BindView(R.id.chkbx_select_passenger)
        CheckBox cbSelectPassenger;

        public PassengerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
/*
public class PassengerCursorAdapter extends CursorAdapter {
    public PassengerCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_passenger_view, parent, false);
        PassengerViewHolder passenger = new PassengerViewHolder(v);
        v.setTag(passenger);
        return v;
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        PassengerViewHolder viewHolder = null;
        if(view.getTag() instanceof PassengerViewHolder)
            viewHolder = (PassengerViewHolder) view.getTag();
        else {
            viewHolder = new PassengerViewHolder(view);
            view.setTag(viewHolder);
        }

        String passengerName = cursor.getString(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.Name.columnName));
        String passengerProof = cursor.getString(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.AadharCardNo.columnName));
        int passengerAge = cursor.getInt(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.Age.columnName));
        String passengerBerth = cursor.getString(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.Berth.columnName));
        int passengerChild = cursor.getInt(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.Child.columnName));
        String passengerFood = cursor.getString(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.Food.columnName));
        String passengerGender = cursor.getString(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.Gender.columnName));
        int passengerId = cursor.getInt(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.Id.columnName));
        String passengerNationality = cursor.getString(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.Nationality.columnName));
        int passengerTransaction = cursor.getInt(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.TransactionId.columnName));


        String sText1 = passengerName + ", " + passengerGender == "MALE"? "M" : "F" + ", " + passengerAge;
        String sText2 = passengerBerth+ ", " + passengerFood == "MALE"? "M" : "F" ;
        String sText3 = passengerChild > 0 ? "CHILD" : passengerAge > 60 ? "SENIOR" : "";
        sText3 += ", ID Proof : " + passengerProof;

        // Populate fields with extracted properties
        viewHolder.txtView1.setText(sText1);
        viewHolder.txtView2.setText(sText2);
        viewHolder.txtView3.setText(sText3);
    }


}*/
