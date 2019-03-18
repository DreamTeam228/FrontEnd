package msc.fooxer.studplaces;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import android.widget.ImageView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<DataModel> elements;

    CustomAdapter(Context context, List<DataModel> elements) {
        this.elements = elements;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.element, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DataModel element = elements.get(i);
        viewHolder.imageView.setImageResource(element.getImage());
        viewHolder.textView.setText(element.getText());
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView textView;

        ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.photo);
            textView = (TextView) view.findViewById(R.id.description);
        }
    }
}
