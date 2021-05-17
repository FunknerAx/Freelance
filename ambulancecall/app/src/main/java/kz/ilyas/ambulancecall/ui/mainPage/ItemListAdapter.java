package kz.ilyas.ambulancecall.ui.mainPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kz.ilyas.ambulancecall.R;
import kz.ilyas.ambulancecall.data.model.ItemOfList;
import kz.ilyas.ambulancecall.data.model.Symtom;

public class ItemListAdapter extends ArrayAdapter<ItemOfList> {

    private LayoutInflater inflater;
    private int layout;
    private List<ItemOfList> states;

    public ItemListAdapter(Context context, int resource, List<ItemOfList> states) {
        super(context, resource, states);
        this.states = states;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(this.layout, parent, false);

        TextView id = view.findViewById(R.id.item_list_id);
        TextView displayName = view.findViewById(R.id.item_list_fio);
        TextView address = view.findViewById(R.id.item_list_address);
        TextView category = view.findViewById(R.id.item_list_category);
        TextView status = view.findViewById(R.id.item_list_state);
        Button button = view.findViewById(R.id.button_complete_ILL);
        TextView symptoms = view.findViewById(R.id.item_list_symptoms);

        ItemOfList state = states.get(position);


        id.setText(state.getId());
        address.setText("Адресс: " + state.getAddress());
        category.setText("Срочность: " + state.getCategory());
        status.setText("Статус: " + state.getState());

        if (state.client()) {
            button.setText("отменить");

            symptoms.setVisibility(View.GONE);
            displayName.setText("ФИО: " + state.getDisplayNam());
        } else {
            ArrayList<Symtom> s = state.getSymtomArrayList();
            StringBuilder newS = new StringBuilder();
            for (Symtom cur : s) {
                newS.append("\n"+cur.getName() + ";");
            }
            symptoms.setVisibility(View.VISIBLE);
            displayName.setText("Карта клиента: " + state.getClientProfile().toString());
            symptoms.setText("Симптомы: " + newS.toString());
            if (state.getState().equals("Ожидает")) {
                button.setText("принять");
            } else {
                button.setText("завершить");
            }
        }

        if (state.getState().equals("Выполненно") || state.getState().equals("Отмененно") || state.getState().equals("Завершенно"))
            button.setVisibility(View.GONE);
        return view;
    }
}
