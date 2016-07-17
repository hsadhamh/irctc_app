package irctc.factor.app.irctcmadeeasy.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import irctc.factor.app.irctcmadeeasy.Adapters.RecycleCursorAdapter.RecyclerViewCursorAdapter;
import irctc.factor.app.irctcmadeeasy.Adapters.RecycleCursorAdapter.RecyclerViewCursorViewHolder;
import irctc.factor.app.irctcmadeeasy.Events.DeleteFormInfo;
import irctc.factor.app.irctcmadeeasy.Events.DeletePassengerEvent;
import irctc.factor.app.irctcmadeeasy.Events.EditFormInfo;
import irctc.factor.app.irctcmadeeasy.Events.EditPassengerInfo;
import irctc.factor.app.irctcmadeeasy.R;

import irctc.factor.app.irctcmadeeasy.database.TicketDetailsDao;

/**
 * Created by hassanhussain on 7/11/2016.
 */

public class TicketDetailsCursorAdapter extends RecyclerViewCursorAdapter<TicketDetailsCursorAdapter.TicketDetailsViewHolder> {

    // Because RecyclerView.Adapter in its current form doesn't natively
    // support cursors, we wrap a CursorAdapter that will do all the job
    // for us.
    Cursor mCursor;
    Context mContext;

    public TicketDetailsCursorAdapter(Context context, Cursor c) {
        super(context);
        mContext = context;
        mCursor = c;
        setupCursorAdapter(c, 0, R.layout.layout_forms_view, false);
    }

    @Override
    public void onBindViewHolder(TicketDetailsViewHolder holder, int position) {
        // Move cursor to this position
        mCursorAdapter.getCursor().moveToPosition(position);
        // Set the ViewHolder
        setViewHolder(holder);
        // Bind this view
        mCursorAdapter.bindView(null, mContext, mCursorAdapter.getCursor());
    }

    @Override
    public TicketDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Passing the inflater job to the cursor-adapter
        View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        return new TicketDetailsViewHolder(v);
    }

    static class TicketDetailsViewHolder extends RecyclerViewCursorViewHolder{
        @BindView(R.id.btn_form_edit_info)
        Button btnEdit;
        @BindView(R.id.btn_form_delete_info)
        Button btnDelete;

        @BindView(R.id.txt_source_destination)
        TextView txtView1;



        public TicketDetailsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void bindCursor(Cursor cursor) {

            int trainId=cursor.getInt(cursor.getColumnIndexOrThrow(TicketDetailsDao.Properties.Id.columnName));
            String source = cursor.getString(cursor.getColumnIndexOrThrow(TicketDetailsDao.Properties.Source.columnName));

            String sText1 = "<b>"+source + "</b>";


            // Populate fields with extracted properties
            txtView1.setText(Html.fromHtml(sText1));
            txtView1.setTextSize(15.0f);
            //txtView2.setText(Html.fromHtml(sText2));
            //txtView3.setText(Html.fromHtml(sText3));
            txtView1.setTag(trainId);
        }

        @OnClick(R.id.btn_form_edit_info)
        public void onClickEditForm(){
            EventBus.getDefault().post(new EditFormInfo((int)txtView1.getTag()));
        }

        @OnClick(R.id.btn_form_delete_info)
        public void onClickDeleteForm(){
            EventBus.getDefault().post(new DeleteFormInfo((int)txtView1.getTag()));
        }
    }
}

