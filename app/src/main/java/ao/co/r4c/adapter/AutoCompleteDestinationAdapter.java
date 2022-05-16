package ao.co.r4c.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ao.co.r4c.R;
import ao.co.r4c.model.LocalItem;

public class AutoCompleteDestinationAdapter extends ArrayAdapter<LocalItem> {

    private final List<LocalItem> localItemListFull;
    private final Filter destinationFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<LocalItem> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(localItemListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (LocalItem item : localItemListFull) {
                    if (item.getDestinationName().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((LocalItem) resultValue).getDestinationName();
        }
    };

    public AutoCompleteDestinationAdapter(Context context, List<LocalItem> localItemList) {
        super(context, 0, localItemList);
        localItemListFull = new ArrayList<>(localItemList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return destinationFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.destination_autocomplete_row, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.txt_destination_item);

        LocalItem localItem = getItem(position);

        if (localItem != null) {
            textView.setText(localItem.getDestinationName());
        }

        return convertView;

    }
}