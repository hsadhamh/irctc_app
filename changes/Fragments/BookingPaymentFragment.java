package irctc.factor.app.irctcmadeeasy.Fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.rey.material.widget.CheckBox;
import com.rey.material.widget.EditText;
import com.rey.material.widget.RadioButton;
import com.rey.material.widget.Spinner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.Unbinder;
import irctc.factor.app.irctcmadeeasy.Events.AddFormsEvent;
import irctc.factor.app.irctcmadeeasy.Events.BookingPaymentEvent;
import irctc.factor.app.irctcmadeeasy.Json.TicketJson;
import irctc.factor.app.irctcmadeeasy.R;
import irctc.factor.app.irctcmadeeasy.View.ShowHidePasswordEditText;

/**
 * Created by hassanhussain on 7/8/2016.
 */
public final class BookingPaymentFragment extends Fragment  {
    @BindView(R.id.id_saved_card_list)
    public Spinner mSpSavedCards;

    @BindView(R.id.id_radio_banking)
    public RadioButton mRbBanking;
    @BindView(R.id.id_radio_credit)
    public RadioButton mRbCreditCard;
    @BindView(R.id.id_radio_debit)
    public RadioButton mRbDebitCard;
    @BindView(R.id.id_radio_cash)
    public RadioButton mRbCashCards;
    @BindView(R.id.id_radio_irctc)
    public RadioButton mRbIrctcCard;

    @BindView(R.id.id_spinner_payment)
    public Spinner mSpPaymentOptions;

    @BindView(R.id.section_id_card_type)
    public LinearLayout mvCardTypeSection;
    @BindView(R.id.spinner_card_type)
    public Spinner mSpCardType;

    @BindView(R.id.id_card_section)
    public LinearLayout mvCardSection;

    @BindView(R.id.section_id_card_details)
    public LinearLayout mvCardDetailsSection;
    @BindView(R.id.spn_month)
    public Spinner mSpMonth;
    @BindView(R.id.specify_card_number)
    public EditText mvCardNumber;
    @BindView(R.id.specify_card_name)
    public EditText mvCardName;
    @BindView(R.id.txt_year_no)
    public android.widget.EditText mvYear;
    @BindView(R.id.txt_cvv_no)
    public EditText mvCVVNumber;
    @BindView(R.id.txt_atm_pin)
    public EditText mvAtmPinNo;

    @BindView(R.id.id_internet_section)
    public LinearLayout mvInternetSection;
    @BindView(R.id.txt_internet_username)
    public EditText mvInternetUsername;
    @BindView(R.id.txt_internet_password)
    public ShowHidePasswordEditText mvInternetPassword;

    @BindView(R.id.specify_checkbox_name)
    public CheckBox mCbSaveCardInfo;
    @BindView(R.id.specify_name_txt)
    public EditText mvSaveCardWithName;

    private Unbinder unbinder;
    ArrayAdapter<String> mSpPaymentOption;
    Context mContext;
    int[] mArrBankingIDs = {0,1,10,22,29,28,31,34,35,38,39,36,37,42,43,40,46,44,45,50,48,54,53,52,56,60,64,67};
    int[] mArrCreditIDs = {0, 4, 17, 21, 27, 30, 58, 72,};
    int[] mArrDebitIDs = {0, 3, 5, 9, 15, 16, 19, 25, 26, 32, 41, 57, 66};
    int[] mArrCashIDs = {0,  23, 33, 55, 68, 70, 71};
    int[] mArrIrctIDs = {0,  59};
    String mPaymentMode;
    TicketJson oJsonTicket;
    IGetPaymentValue iGet;

    List<String> arrayAdpaterList;
    ArrayList<String> al;

    public static BookingPaymentFragment newInstance()
    {
        return new BookingPaymentFragment();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_irctc_payment, container, false);
        unbinder = ButterKnife.bind(this, view);

        mContext = container.getContext();

        arrayAdpaterList=  Arrays.asList(getResources().getStringArray(R.array.net_banking_options));
        al = new ArrayList<String>(arrayAdpaterList);
        mSpPaymentOption = new ArrayAdapter<String>(mContext, R.layout.layout_spinner_item,al);
        mSpPaymentOption.setDropDownViewResource(R.layout.layout_spinner_item);
        mSpPaymentOptions.setAdapter(mSpPaymentOption);
        setAdapterSpinner(R.id.id_radio_banking);

        ArrayAdapter<String> spCardTypes = new ArrayAdapter<String>(mContext,
                R.layout.layout_spinner_item, getResources().getStringArray(R.array.cards_type));
        spCardTypes.setDropDownViewResource(R.layout.layout_spinner_item);
        mSpCardType.setAdapter(spCardTypes);

        ArrayAdapter<String> spMonths = new ArrayAdapter<String>(mContext,
                R.layout.layout_spinner_item, getResources().getStringArray(R.array.months_calendar));
        spMonths.setDropDownViewResource(R.layout.layout_spinner_item);
        mSpMonth.setAdapter(spMonths);




        showCardOrInternetBanking(R.id.id_radio_banking);



        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnCheckedChanged({R.id.id_radio_debit, R.id.id_radio_credit,
            R.id.id_radio_banking, R.id.id_radio_cash, R.id.id_radio_irctc})
    public void onRadioClick(RadioButton btn, boolean isChecked){
        if(isChecked){
            int btnRes = btn.getId();
            mRbDebitCard.setChecked(btnRes == R.id.id_radio_debit);
            mRbCreditCard.setChecked(btnRes == R.id.id_radio_credit);
            mRbBanking.setChecked(btnRes == R.id.id_radio_banking);
            mRbCashCards.setChecked(btnRes == R.id.id_radio_cash);
            mRbIrctcCard.setChecked(btnRes == R.id.id_radio_irctc);
            setAdapterSpinner(btnRes);
            showCardOrInternetBanking(btnRes);
        }
    }

    public void setAdapterSpinner(int nResId){
        int nArrId = R.array.net_banking_options;
        switch(nResId) {
            case R.id.id_radio_debit:
                nArrId = R.array.debit_card_options;
                break;
            case R.id.id_radio_credit:
                nArrId = R.array.credit_card_options;
                break;
            case R.id.id_radio_banking:
                nArrId = R.array.net_banking_options;
                break;
            case R.id.id_radio_cash:
                nArrId = R.array.cash_cards_option;
                break;
            case R.id.id_radio_irctc:
                nArrId = R.array.irctc_card_option;
                break;
        }

        arrayAdpaterList = Arrays.asList(getResources().getStringArray(nArrId));
        al=new ArrayList<>(arrayAdpaterList);
        mSpPaymentOption.notifyDataSetChanged();
        mSpPaymentOptions.setSelection(0);

    }

    public void showCardOrInternetBanking(int nRes){
        switch(nRes) {
            case R.id.id_radio_debit:
                mPaymentMode = "DEBIT_CARD";
                mvCardSection.setVisibility(View.VISIBLE);
                mvAtmPinNo.setVisibility(View.VISIBLE);
                mvInternetSection.setVisibility(View.GONE);
                mvCardTypeSection.setVisibility(View.VISIBLE);
                break;
            case R.id.id_radio_credit:
                mPaymentMode = "CREDIT_CARD";
                mvCardSection.setVisibility(View.VISIBLE);
                mvAtmPinNo.setVisibility(View.GONE);
                mvInternetSection.setVisibility(View.GONE);
                mvCardTypeSection.setVisibility(View.VISIBLE);
                break;
            case R.id.id_radio_banking:
                mPaymentMode = "NETBANKING";
                mvCardSection.setVisibility(View.GONE);
                mvAtmPinNo.setVisibility(View.GONE);
                mvInternetSection.setVisibility(View.VISIBLE);
                mvCardTypeSection.setVisibility(View.GONE);
                break;
            case R.id.id_radio_irctc:
                mPaymentMode = "IRCTC_PREPAID";
                mvCardSection.setVisibility(View.VISIBLE);
                mvAtmPinNo.setVisibility(View.GONE);
                mvInternetSection.setVisibility(View.GONE);
                mvCardTypeSection.setVisibility(View.GONE);
                break;
            case R.id.id_radio_cash:
                mPaymentMode = "CASH_CARD";
                mvCardSection.setVisibility(View.GONE);
                mvInternetSection.setVisibility(View.GONE);
                break;
            default:
                mPaymentMode = "NETBANKING";
                mvCardSection.setVisibility(View.GONE);
                mvInternetSection.setVisibility(View.GONE);
        }
        mvInternetSection.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
    public TicketJson GetJsonObjectFilled()
    {
         oJsonTicket = new TicketJson();

        oJsonTicket.setPaymentMode(mPaymentMode);
        int nMode = 0;
        if(mPaymentMode.equals("NETBANKING")){
            nMode = mArrBankingIDs[mSpPaymentOptions.getSelectedItemPosition()];
        }
        else if(mPaymentMode.equals("IRCTC_PREPAID")){
            nMode = mArrIrctIDs[mSpPaymentOptions.getSelectedItemPosition()];
        }
        else if(mPaymentMode.equals("CASH_CARD")){
            nMode = mArrCashIDs[mSpPaymentOptions.getSelectedItemPosition()];
        }
        else if(mPaymentMode.equals("CREDIT_CARD")){
            nMode = mArrCreditIDs[mSpPaymentOptions.getSelectedItemPosition()];
        }
        else if(mPaymentMode.equals("DEBIT_CARD")){
            nMode = mArrDebitIDs[mSpPaymentOptions.getSelectedItemPosition()];
        }
        oJsonTicket.setPaymentModeOptionID(""+nMode);
        oJsonTicket.setCardNumberValue(mvCardNumber.getText().toString());
        String sCard = mSpCardType.getSelectedItem().toString();
        sCard = sCard.equals("MASTER") ? "MC" : "VISA";
        oJsonTicket.setCardType(sCard);
        oJsonTicket.setExpiryMonth("" + mSpMonth.getSelectedItemPosition() + 1);
        oJsonTicket.setExpiryYear(mvYear.getText().toString());
        oJsonTicket.setCardCvvNumber(mvCVVNumber.getText().toString());
        oJsonTicket.setNameOnCard(mvCardName.getText().toString());

        return oJsonTicket;
    }

    @Subscribe
    public void onEvent(BookingPaymentEvent e) {

        savePaymentInfo();

    }

    public void savePaymentInfo()
    {
        iGet.getPaymentValue(GetJsonObjectFilled());
    }

}
