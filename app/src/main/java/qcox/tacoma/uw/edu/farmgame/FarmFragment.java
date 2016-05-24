package qcox.tacoma.uw.edu.farmgame;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass which currently holds the buttons to the
 * highscore activity and the itemListfragment that shows the inventory.
 *
 * @author James, Quinn
 * @version 1.0
 * @since 2016-5-4
 */
public class FarmFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    BaseAdapterHelper_farmField myAdapter;

    public FarmFragment() {
        // Required empty public constructor
    }

    //this creates the view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_farm, container, false);
        GridView gridView = (GridView) v.findViewById(R.id.gridView);
        myAdapter = new BaseAdapterHelper_farmField(getContext(), FarmActivity.mLevel * Config.LEVELUPFIELDGAP + Config.INITIALFIELD);
        TextView moneyTextView = (TextView) v.findViewById(R.id.money_textView);
        moneyTextView.setText("$: "+Config.INITIALMONEY);
        TextView levelTextView = (TextView) v.findViewById(R.id.level_textView);
        levelTextView.setText("Lv: "+FarmActivity.mLevel);
        TextView expTextView = (TextView) v.findViewById(R.id.experience_textView);
        expTextView.setText("Exp: "+FarmActivity.mExp);
        gridView.setAdapter(myAdapter);
        gridView.setOnItemClickListener((AdapterView.OnItemClickListener) getActivity());
        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
class Field {
    int imageID;
    String typeOfCrops;
    int mutureTime;//change to Timer later
    Field(int imageID, String typeOfCrops, int mutureTime){
        this.imageID = imageID;
        this.typeOfCrops = typeOfCrops;
        this.mutureTime = mutureTime;
//        this.progressBar = progressBar;
//        this.textView = textView;
    }

}

class BaseAdapterHelper_farmField extends BaseAdapter{

    public static ArrayList<Field> field_arraylist;


    Context context;
    BaseAdapterHelper_farmField(Context context, int numOfField){
        this.context = context;
        if (field_arraylist == null){
            field_arraylist = new ArrayList<Field>();
            for (int i = 0; i < numOfField; i++){
                field_arraylist.add(new Field(R.drawable.field_100dp, Config.FIELD, 0));
            }
        }
        else {
            for (int i = 0; i < numOfField; i++){
                Log.i("1, new adapter " + numOfField+","+field_arraylist.size(),"levelup");
                if (field_arraylist.size() > i){
                    Log.i("2, new adapter " + numOfField+","+field_arraylist.size(),"levelup");
                    field_arraylist.set(i, new Field(R.drawable.field_100dp,
                            field_arraylist.get(i).typeOfCrops, field_arraylist.get(i).mutureTime));
                    Log.i("3, new adapter " + numOfField+","+field_arraylist.size(),"levelup");
                }
                else {
                    field_arraylist.add(new Field(R.drawable.field_100dp, Config.FIELD, 0));
                    Log.i("4, new adapter " + numOfField+","+field_arraylist.size(),"levelup");
                }

            }
        }

    }



    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate typeOfCrops before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right typeOfCrops (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View fieldView = convertView;
        ViewHolder viewHolder = null;
        if (fieldView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            fieldView = layoutInflater.inflate(R.layout.single_field, parent, false);
            viewHolder = new ViewHolder(fieldView);
            fieldView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) fieldView.getTag();
        }
        Field tempField = field_arraylist.get(position);
        viewHolder.myField_ImageView.setImageResource(tempField.imageID);
        if (tempField.mutureTime != 0){
            viewHolder.myTimer_TextView.setText(tempField.mutureTime/1000+"s");
        }
        if (tempField.mutureTime< 0){
            viewHolder.myTimer_TextView.setText("Ready!");
        }
        if (tempField.mutureTime == 0){
            viewHolder.myTimer_TextView.setText("");
        }

        return fieldView;
    }


    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        // Return true for clickable, false for not
        if (BaseAdapterHelper_farmField.field_arraylist.get(position).mutureTime < 0){
            return true;
        }else if (BaseAdapterHelper_farmField.field_arraylist.get(position).mutureTime == 0){
            return true;
        }else{
            return false;
        }
    }




    class ViewHolder{
        ImageView myField_ImageView;
        TextView myTimer_TextView;
        ViewHolder(View v){
            myField_ImageView = (ImageView) v.findViewById(R.id.imageView_field);
            myTimer_TextView = (TextView) v.findViewById(R.id.timer_textView);
        }
    }
    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return field_arraylist.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return field_arraylist.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }
}