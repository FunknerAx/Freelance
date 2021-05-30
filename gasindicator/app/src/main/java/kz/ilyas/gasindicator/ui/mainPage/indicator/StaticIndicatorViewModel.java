package kz.ilyas.gasindicator.ui.mainPage.indicator;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Date;

import kz.ilyas.gasindicator.data.DataBaseSource;
import kz.ilyas.gasindicator.data.model.Statistic;
import kz.ilyas.gasindicator.data.model.Statistics;

public class StaticIndicatorViewModel extends ViewModel {
    private DataBaseSource db = new DataBaseSource(this);

    MutableLiveData<ArrayList<Statistics>> statistiscLiveDate = new MutableLiveData<>();

    LiveData<ArrayList<Statistics>> getStatistisc() {
        return statistiscLiveDate;
    }

    public void getStatistics(String id) {
        db.getStatistics(id);
    }

    public void sendResult(ArrayList<Statistics> statics, Exception exception) {
        if (exception == null) {
            statistiscLiveDate.setValue(statics);
        }
    }

    public ArrayList<Statistics> sort(ArrayList<Statistics> statistics) {
        ArrayList<Statistics> sortedArray = new ArrayList<>();
        sortedArray.addAll(statistics);

        for (int i = 0; i < sortedArray.size(); i++) {
            int year = Integer.valueOf(sortedArray.get(i).getDate().substring(0, 4));
            int month = Integer.valueOf(sortedArray.get(i).getDate().substring(5, 7));
            int day = Integer.valueOf(sortedArray.get(i).getDate().substring(8, 10));
            Date cur = new Date(year, month, day);

            for (int j = sortedArray.size() - 1; j > i; j--) {
                int year2 = Integer.valueOf(sortedArray.get(j).getDate().substring(0, 4));
                int month2 = Integer.valueOf(sortedArray.get(j).getDate().substring(5, 7));
                int day2 = Integer.valueOf(sortedArray.get(j).getDate().substring(8, 10));
                Date cur2 = new Date(year2, month2, day2);
                if (cur.after(cur2)) {
                    swap(sortedArray, i, j);
                }

            }
        }
        return sortedArray;
    }

    public ArrayList<Statistics> sortTime(ArrayList<Statistics> sortedArray) {
        ArrayList<Statistics> newSortedArray = new ArrayList<>();
        newSortedArray.addAll(sortedArray);

        for (int curIndex = 0; curIndex < newSortedArray.size(); curIndex++) {

            //Log.v("COUNT", sortedArray.get(curIndex).getStatics().size() + "");
            for (int i = 0; i < newSortedArray.get(curIndex).getStatics().size(); i++) {
                int hh = Integer.valueOf(newSortedArray.get(curIndex).getStatics().get(i).getTime().substring(0, 2));
                int mm = Integer.valueOf(newSortedArray.get(curIndex).getStatics().get(i).getTime().substring(3, 5));
                int ss = Integer.valueOf(newSortedArray.get(curIndex).getStatics().get(i).getTime().substring(6, 8));
                Date min = new Date(1997, 10, 20, hh, mm, ss);
                int minId = i;

                for (int j = i+1;j<newSortedArray.get(curIndex).getStatics().size(); j++) {
                    int hh2 = Integer.valueOf(newSortedArray.get(curIndex).getStatics().get(j).getTime().substring(0, 2));
                    int mm2 = Integer.valueOf(newSortedArray.get(curIndex).getStatics().get(j).getTime().substring(3, 5));
                    int ss2 = Integer.valueOf(newSortedArray.get(curIndex).getStatics().get(j).getTime().substring(6, 8));
                    Date cur = new Date(1997, 10, 20, hh2, mm2, ss2);
                    if (min.after(cur)) {
                        min = cur;
                        minId = j;
                       // Log.v("CONTSWAP", j + " " + i + " " + curIndex);
//                        swapTime(newSortedArray, curIndex, i, j);
                    }
                }
                Statistic tmp = newSortedArray.get(curIndex).getStatics().get(i);
                newSortedArray.get(curIndex).getStatics().set(i, newSortedArray.get(curIndex).getStatics().get(minId));
                newSortedArray.get(curIndex).getStatics().set(minId, tmp);
            }
        }
        return newSortedArray;
    }

    private void swapTime(ArrayList<Statistics> array, int index, int i, int j) {
        Statistic tmp = array.get(index).getStatics().get(i);
        Log.v("VALUESARR",array.get(index).getStatics().get(i).getTime());
        Log.v("VALUESARR",array.get(index).getStatics().get(j).getTime());
        array.get(index).getStatics().set(i, array.get(index).getStatics().get(j));
        array.get(index).getStatics().set(j, tmp);
        Log.v("VALUESARR",array.get(index).getStatics().get(i).getTime());
        Log.v("VALUESARR",array.get(index).getStatics().get(j).getTime());
    }

    private void swap(ArrayList<Statistics> sortedArray, int i, int j) {

        Statistics tmp = sortedArray.get(i);
        sortedArray.set(i, sortedArray.get(j));
        sortedArray.set(j, tmp);
    }

}


