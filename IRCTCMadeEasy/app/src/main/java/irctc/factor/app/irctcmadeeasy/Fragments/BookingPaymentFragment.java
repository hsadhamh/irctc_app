package irctc.factor.app.irctcmadeeasy.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rey.material.widget.CheckBox;
import com.rey.material.widget.EditText;
import com.rey.material.widget.RadioButton;
import com.rey.material.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import irctc.factor.app.irctcmadeeasy.R;

/**
 * Created by hassanhussain on 7/8/2016.
 */
public class BookingPaymentFragment extends Fragment {
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

    @BindView(R.id.specify_checkbox_name)
    public CheckBox mCbSaveCardInfo;
    @BindView(R.id.specify_name_txt)
    public EditText mvSaveCardWithName;

    private Unbinder unbinder;

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_irctc_payment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
