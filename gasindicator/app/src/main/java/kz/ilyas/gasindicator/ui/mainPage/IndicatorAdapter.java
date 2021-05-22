package kz.ilyas.gasindicator.ui.mainPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import kz.ilyas.gasindicator.R;
import kz.ilyas.gasindicator.data.model.Indicator;

public class IndicatorAdapter extends RecyclerView.Adapter<IndicatorAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<Indicator> indicators;

    IndicatorAdapter(Context context, List<Indicator> indicators){
        this.indicators = indicators;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.activity_gas_indicator_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull IndicatorAdapter.ViewHolder holder, int position) {
        Indicator indicator = indicators.get(position);
        holder.id.setText(indicator.getId());
        holder.address.setText(indicator.getAddress());
        holder.position.setText(indicator.getPosition());
        holder.showStat.setText(indicator.getCurrentValue());

    }

    @Override
    public int getItemCount() {
        return indicators.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView id,address,position;
        final Button showStat;
        ViewHolder(View view){
            super(view);
            id = (TextView)view.findViewById(R.id.gasItem_id_value);
            address = (TextView) view.findViewById(R.id.gasItem_address_value);
            position = (TextView) view.findViewById(R.id.gasItem_position_value);
            showStat = (Button) view.findViewById(R.id.gasIndicator_button);
        }
    }
}
