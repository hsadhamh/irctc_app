package irctc.factor.app.irctcmadeeasy.Adapters;

/**
 * Created by hassanhussain on 7/8/2016.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import irctc.factor.app.irctcmadeeasy.R;

/**
 * Created by akshay on 1/2/15.
 */
public class AutoCompleteStringAdapter extends ArrayAdapter<String> {

    Context context;
    int resource, textViewResourceId;
    List<String> items, tempItems, suggestions;

    public AutoCompleteStringAdapter(Context context, int resource, int textViewResourceId, List<String> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<String>(items); // this makes the difference.
        suggestions = new ArrayList<String>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolderText oHolder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.auto_complete_row_view, parent, false);
            oHolder = new ViewHolderText();
            oHolder.txtView = (TextView) view.findViewById(R.id.lbl_name);
            view.setTag(oHolder);
        }
        oHolder = (ViewHolderText) view.getTag();
        String people = items.get(position);
        if (people != null) {
            oHolder.txtView.setText(people);
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    class ViewHolderText{
        TextView txtView;
    }
    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = (String)resultValue;
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (String people : tempItems) {
                    if (people.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<String> filterList = (ArrayList<String>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (String people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}