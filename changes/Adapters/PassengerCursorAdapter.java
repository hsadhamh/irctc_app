package irctc.factor.app.irctcmadeeasy.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import irctc.factor.app.irctcmadeeasy.Adapters.RecycleCursorAdapter.RecyclerViewCursorAdapter;
import irctc.factor.app.irctcmadeeasy.Adapters.RecycleCursorAdapter.RecyclerViewCursorViewHolder;
import irctc.factor.app.irctcmadeeasy.Events.DeletePassengerEvent;
import irctc.factor.app.irctcmadeeasy.Events.EditPassengerInfo;
import irctc.factor.app.irctcmadeeasy.Events.SelectPassenger;
import irctc.factor.app.irctcmadeeasy.Events.UnselectPassenger;
import irctc.factor.app.irctcmadeeasy.Json.ChildJson;
import irctc.factor.app.irctcmadeeasy.Json.PassengerJson;
import irctc.factor.app.irctcmadeeasy.R;
import irctc.factor.app.irctcmadeeasy.database.PassengerInfo;
import irctc.factor.app.irctcmadeeasy.database.PassengerInfoDao;

/**
 * Created by hassanhussain on 7/11/2016.
 */

public class PassengerCursorAdapter extends RecyclerViewCursorAdapter<PassengerCursorAdapter.PassengerViewHolder> {

    // Because RecyclerView.Adapter in its current form doesn't natively
    // support cursors, we wrap a CursorAdapter that will do all the job
    // for us.
    Cursor mCursor;
    Context mContext;

    public PassengerCursorAdapter(Context context, Cursor c) {
        super(context);
        mContext = context;
        mCursor = c;
        setupCursorAdapter(c, 0, R.layout.layout_passenger_view, false);
    }

    @Override
    public void onBindViewHolder(PassengerViewHolder holder, int position) {
        // Move cursor to this position
        mCursorAdapter.getCursor().moveToPosition(position);
        // Set the ViewHolder
        setViewHolder(holder);
        // Bind this view
        mCursorAdapter.bindView(null, mContext, mCursorAdapter.getCursor());
    }

    @Override
    public PassengerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Passing the inflater job to the cursor-adapter
        View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        return new PassengerViewHolder(v);
    }

    static class PassengerViewHolder extends RecyclerViewCursorViewHolder{
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

        @Override
        public void bindCursor(Cursor cursor) {
            String passengerName = cursor.getString(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.Name.columnName));
            String passengerProof = cursor.getString(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.AadharCardNo.columnName));
            int passengerAge = cursor.getInt(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.Age.columnName));
            String passengerBerth = cursor.getString(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.Berth.columnName));
            String passengerChild = cursor.getString(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.Child.columnName));
            String passengerFood = cursor.getString(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.Food.columnName));
            String passengerGender = cursor.getString(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.Gender.columnName));
            int passengerId = cursor.getInt(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.Id.columnName));
            String passengerNationality = cursor.getString(cursor.getColumnIndexOrThrow(PassengerInfoDao.Properties.Nationality.columnName));

            String sText1 = "<b>"+passengerName + "</b>, <b><i>" + (passengerGender.equals("MALE")? "M" : "F") + "</i></b>, <b>Age:</b> " + passengerAge;
            String sText2 = passengerChild + "; <b>Berth: </b>" + passengerBerth + "; <b>Food:</b> " + (passengerFood.equals("NonVeg")? "NonVeg" : "Veg");
            String sText3 =  "<b>Nationality:</b> INDIAN; <b>ID Proof :</b> " + passengerProof;

            // Populate fields with extracted properties
            txtView1.setText(Html.fromHtml(sText1));
            txtView1.setTextSize(15.0f);
            txtView2.setText(Html.fromHtml(sText2));
            txtView3.setText(Html.fromHtml(sText3));
            txtView1.setTag(passengerId);
        }

        @OnClick(R.id.btn_edit_info)
        public void onClickEditPassenger(){
            EventBus.getDefault().post(new EditPassengerInfo((int)txtView1.getTag()));
        }

        @OnClick(R.id.btn_delete_info)
        public void onClickDeletePassenger(){
            EventBus.getDefault().post(new DeletePassengerEvent((int)txtView1.getTag()));
        }

        @OnCheckedChanged(R.id.chkbx_select_passenger)
        public void checkChanged()
        {
            if(cbSelectPassenger.isChecked())
                EventBus.getDefault().post(new SelectPassenger((int)txtView1.getTag()));
            else
                EventBus.getDefault().post(new UnselectPassenger((int)txtView1.getTag()));
        }
    }
}

