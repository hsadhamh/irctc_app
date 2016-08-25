package irctc.factor.app.irctcmadeeasy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import irctc.factor.app.irctcmadeeasy.R;
import irctc.factor.app.irctcmadeeasy.Utils.StationDetails;

public class StationAutoCompleteAdapter extends BaseAdapter implements Filterable {
    private final Object mLock = new Object();
    private LayoutInflater mInflater = null;
    private List<StationDetails> mObjects;
    private boolean mNotifyOnChange = true;
    private Context mContext;
    private ArrayList<StationDetails> mOriginalValues;
    private ArrayFilter mFilter;

    class ViewHolderText{ TextView txtView; }

    public StationAutoCompleteAdapter(Context context, List<StationDetails> objects) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mObjects = objects;
    }

    public void add(StationDetails object) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.add(object);
            } else {
                mObjects.add(object);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void addAll(Collection<? extends StationDetails> collection) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.addAll(collection);
            } else {
                mObjects.addAll(collection);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void addAll(StationDetails ... items) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                Collections.addAll(mOriginalValues, items);
            } else {
                Collections.addAll(mObjects, items);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void insert(StationDetails object, int index) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.add(index, object);
            } else {
                mObjects.add(index, object);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void remove(java.lang.String object) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.remove(object);
            } else {
                mObjects.remove(object);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void clear() {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.clear();
            } else {
                mObjects.clear();
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }
    public Context getContext() {
        return mContext;
    }

    public int getCount() {
        return mObjects.size();
    }

    public StationDetails getItem(int position) {
        return mObjects.get(position);
    }

    public int getPosition(StationDetails item) {
        return mObjects.indexOf(item);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(mInflater, position, convertView, parent, R.layout.auto_complete_row_view);
    }

    private View createViewFromResource(LayoutInflater inflater, int position, View convertView, ViewGroup parent, int resource) {
        View view = convertView;
        ViewHolderText oHolder = null;
        if (convertView == null) {
            view = inflater.inflate(resource, parent, false);
            oHolder = new ViewHolderText();
            oHolder.txtView = (TextView) view.findViewById(R.id.lbl_name);
            view.setTag(oHolder);
        }
        oHolder = (ViewHolderText) view.getTag();
        StationDetails item = getItem(position);
        if (item != null) {
            oHolder.txtView.setText(item.toString());
        }
        return view;
    }

    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<StationDetails>(mObjects);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                ArrayList<StationDetails> list;
                synchronized (mLock) {
                    list = new ArrayList<StationDetails>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<StationDetails> values;
                synchronized (mLock) {
                    values = new ArrayList<StationDetails>(mOriginalValues);
                }

                final int count = values.size();
                final ArrayList<StationDetails> finalValues = new ArrayList<StationDetails>();
                for (int i = 0; i < count; i++) {
                    final StationDetails value = values.get(i);
                    if(value.StationCode.toLowerCase().contains(prefixString.toLowerCase())){
                        int iLoc = ((value.StationCode.length() == prefix.length())  ?  0 : 1);
                        if(finalValues.size() > 1 && iLoc == 1)
                            finalValues.add(1, value);
                        else if(finalValues.size() > 0 && iLoc == 0)
                            finalValues.add(0, value);
                        else
                            finalValues.add(value);
                    }
                    else if(value.StationName.toLowerCase().contains(prefixString.toLowerCase())){
                        finalValues.add(value);
                    }
                }
                results.values = finalValues;
                results.count = finalValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mObjects = (List<StationDetails>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
