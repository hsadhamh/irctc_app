package irctc.factor.app.irctcmadeeasy.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import irctc.factor.app.irctcmadeeasy.Adapters.RecycleCursorAdapter.RecyclerViewCursorAdapter;
import irctc.factor.app.irctcmadeeasy.Adapters.RecycleCursorAdapter.RecyclerViewCursorViewHolder;
import irctc.factor.app.irctcmadeeasy.Events.DeleteFormInfo;
import irctc.factor.app.irctcmadeeasy.Events.EditFormInfo;
import irctc.factor.app.irctcmadeeasy.Events.StartBookingEvent;
import irctc.factor.app.irctcmadeeasy.Json.ChildJson;
import irctc.factor.app.irctcmadeeasy.Json.PassengerJson;
import irctc.factor.app.irctcmadeeasy.Json.TicketJson;
import irctc.factor.app.irctcmadeeasy.Json.flJsonParser;
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
        @BindView(R.id.btn_book_now)
        Button btnBookNow;

        @BindView(R.id.txt_source_destination)
        TextView txtView1;

        String msMsg = "";

        Context mContext = null;

        public TicketDetailsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mContext = view.getContext();
        }

        @Override
        public void bindCursor(Cursor cursor) {
            int nTicketId = cursor.getInt(cursor.getColumnIndexOrThrow(TicketDetailsDao.Properties.Id.columnName));
            String sJsonTicket = cursor.getString(cursor.getColumnIndexOrThrow(TicketDetailsDao.Properties.Json.columnName));

            if(sJsonTicket != null && sJsonTicket.length() > 0) {
                TicketJson oJson = GetJsonObject(sJsonTicket);
                if (oJson != null) {
                    String sText = "";
                    if(oJson.getTrainno() != null && oJson.getTrainno().length() > 0)
                        sText += "<br> Train Number: <b>" + oJson.getTrainno() + "</b>";
                    if (oJson.getSource() != null && oJson.getSource().length() > 0)
                        sText += "<br> From <b>" + oJson.getSource() + "</b>";
                    if (oJson.getDestination() != null && oJson.getDestination().length() > 0)
                        sText += " To <b>" + oJson.getDestination() + "</b>";
                    if (oJson.getBoarding() != null && oJson.getBoarding().length() > 0)
                        sText += "<br> Boarding at <b>" + oJson.getBoarding() + "</b>";
                    if (oJson.getDatejourney() != null && oJson.getDatejourney().length() > 0)
                        sText += "<br> On <b>" + oJson.getDatejourney() + "</b>";

                    sText += "<br> No of Adult Passengers : " +
                            (oJson.getPassengerinfo() == null ? 0 : oJson.getPassengerinfo().size());

                    sText += "<br> No of Child Passengers : " +
                            (oJson.getChildinfo() == null ? 0 : oJson.getChildinfo().size());
                    // Populate fields with extracted properties
                    txtView1.setText(Html.fromHtml(sText));
                    txtView1.setTextSize(16.0f);
                    txtView1.setTag(nTicketId);
                    msMsg = getJsonMessage(oJson);
                    txtView1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ShowInfoMessage();
                        }
                    });
                }
            }
        }

        public String getJsonMessage(TicketJson oJson){
            String sText = "<i>Journey Info:</i><br>";
            if(oJson.getTrainno() != null && oJson.getTrainno().length() > 0)
                sText += "<br> Train Number: <b>" + oJson.getTrainno() + "</b>";
            if(oJson.getSource() != null && oJson.getSource().length() > 0)
                sText += " From <b>"+ oJson.getSource() +"</b>";
            if(oJson.getDestination() != null && oJson.getDestination().length() > 0)
                sText += " To <b>"+ oJson.getDestination() +"</b>";
            if(oJson.getBoarding() != null && oJson.getBoarding().length() > 0)
                sText += "<br> Boarding at <b>"+ oJson.getBoarding() +"</b>";
            if(oJson.getDatejourney() != null && oJson.getDatejourney().length() > 0)
                sText += "<br> On <b>"+ oJson.getDatejourney() +"</b>";
            if(oJson.getMobile() != null && oJson.getMobile().length() > 0)
                sText += "<br> Mobile: <b>" + oJson.getMobile() + "</b>";
            if(oJson.getBclass() != null && oJson.getBclass().length() > 0)
                sText += "<br> Class: <b>" + oJson.getBclass() + "</b>";

            sText += "<br> Preferred Coach set: " +
                    (oJson.isPrefercoach() ? "YES " : "NO ")+
                    (oJson.getCoachid() == null? "" : oJson.getCoachid());
            sText += "<br> Quota: " + oJson.getQuota();

            if(oJson.getUsername() != null && oJson.getUsername().length() > 0) {
                sText += "<br> UserName: " + oJson.getUsername();
                sText += " & Password : " + (oJson.getPassword().length() > 0 ? "" : "*****");
            }

            sText += "<br> Payment: " + oJson.getPaymentmode();
            sText += "<br> Payment Option: " + oJson.getPaymentmodeid();

            sText += ("\nAuto-Upgrade: " + (oJson.isAutoupgrade() ? "Yes" : "No"));
            sText += "\nConfirm Ticket Option: " + (oJson.isBookconfirm() ? "YES - " : "NO - ")
                    + oJson.getBookidcond();

            if(oJson.getCardnovalue() != null && oJson.getCardnovalue().length() > 0) {
                sText += "<br> Card#: " + oJson.getCardnovalue();
                sText += " Card Type: " + oJson.getCardtype();
                sText += " Exp Mon: " + oJson.getExpirymon();
                sText += " Exp Yr: " + oJson.getExpiryyear();
                sText += " Card Name: " + oJson.getNamecard();
                sText += " CCV : " + (oJson.getCardcvv().length() > 0 ? "***" : "");
            }

            int n = 0;
            if(oJson.getPassengerinfo() != null && oJson.getPassengerinfo().size()>0) {
                for(PassengerJson p : oJson.getPassengerinfo()) {
                    n++;
                    sText += ("<br> Passenger " + n + ": " + p.getName());
                }
            }
            n = 0;
            if(oJson.getChildinfo() != null && oJson.getChildinfo().size()>0) {
                for(ChildJson p : oJson.getChildinfo()) {
                    n++;
                    sText += ("<br> Child " + n + ": " + p.getName());
                }
            }
            return sText;
        }

        public TicketJson GetJsonObject(String sJsonTicket){
            try {
                TicketJson o = flJsonParser.getTicketDetailObject(sJsonTicket);
                return o;
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public void ShowInfoMessage() {
            new MaterialDialog
                    .Builder(mContext)
                    .content(Html.fromHtml(msMsg))
                    .positiveText("OK")
                    .show();
        }

        @OnClick(R.id.btn_form_edit_info)
        public void onClickEditForm(){
            EventBus.getDefault().post(new EditFormInfo((int)txtView1.getTag()));
        }

        @OnClick(R.id.btn_form_delete_info)
        public void onClickDeleteForm(){
            EventBus.getDefault().post(new DeleteFormInfo((int)txtView1.getTag()));
        }

        @OnClick(R.id.btn_book_now)
        public void startBookNow(){
            EventBus.getDefault().post(new StartBookingEvent((int)txtView1.getTag()));
        }
    }
}

