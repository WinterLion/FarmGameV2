package qcox.tacoma.uw.edu.farmgame;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;


import java.util.HashMap;
import java.util.Map;

import qcox.tacoma.uw.edu.farmgame.data.PlayerValues;
import qcox.tacoma.uw.edu.farmgame.data.PlayerValuesDB;
import qcox.tacoma.uw.edu.farmgame.highscore.HighScore;

/**
 * This class is the major activity in the project.
 * It's the farm filed that player can harvest, and go to shop/silo and high score activity to do other activity.
 * @author James, Quinn
 * @version 1.0
 * @since 2016-5-4
 */
public class FarmActivity extends AppCompatActivity implements FarmFragment.OnFragmentInteractionListener,
        ItemListFragment.OnListFragmentInteractionListener, AdapterView.OnItemClickListener,
        Communicater{

    int mPos;
    BaseAdapterHelper_farmField mAdapter;
    Bundle myBundle;

    static int mLevel = 0;
    static int mExp = 0;
    static int mMoney = Config.INITIALMONEY;
    static Map<String, Integer> mInventory ;



    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm);
        myBundle = new Bundle();
        mInventory = new HashMap<>();
        mLevel = 0;
        mExp = 0;
        mMoney = Config.INITIALMONEY;
    }


    @Override
    public void onStart(){
        super.onStart();
        if (findViewById(R.id.fragment_container)!= null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new FarmFragment())
                    .commit();

        }
        PlayerValuesDB theTask = new PlayerValuesDB();
        theTask.GetUserMoney(this);
    }

    /**
     * this is used to starts the high score activity when the highscore button is pressed.
     * @param v the view that called the method (the button)
     */
    public void viewHighScores(View v) {
        Intent intent = new Intent(getApplicationContext(), HighScoreActivity.class);
        startActivity(intent);
    }

    /**
     * this starts the itemFragment which contains a list of items and how much the player has.
     * @param v the view that called the method (the button)
     */
    public void startSiloList(View v) {
        ItemListFragment itemFragment = new ItemListFragment();
        Bundle args = new Bundle();
        itemFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, itemFragment)
                .addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    /**
     * this is when an item has its buy button pressed.
     * @param v the view that called the method (the button)
     */
    public void buyInventoryItems(View v) {
        int cost = PlayerValues.getPlantItems(mPos).buyCost;
        int numToBuy = 0;
        EditText theNumToBuy = (EditText)findViewById(R.id.num_to_buy);
        if (theNumToBuy != null) {
            String a = theNumToBuy.getText().toString();
            if (!a.isEmpty()) {
                numToBuy = Math.abs(Integer.valueOf(a));
            }
        }
        CharSequence text;
        if (numToBuy < 1) {
            text= "You must pick a valid value for the amount";
        } else if (cost * numToBuy <= PlayerValues.getMoney()) {
            PlayerValues.addItemAmount(PlayerValues.getPlantItems(mPos).name, numToBuy);
            PlayerValues.setMoney(PlayerValues.getMoney() - cost * numToBuy);
            PlayerValuesDB theTask = new PlayerValuesDB();
            theTask.UpdateUserMoney(this, PlayerValues.getMoney());
            text = "You just bought " + numToBuy + " " + PlayerValues.getPlantItems(mPos).name
                    + " for " + cost * numToBuy + " coins.";
        } else {
            text = "You don't have enough money!";
        }

        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show();
        super.onBackPressed();
    }

    /**
     * this is when an item has its sell button pressed.
     * @param v the view that called the method (the button)
     */
    public void sellInventoryItems(View v) {
        int cost = PlayerValues.getPlantItems(mPos).sellCost;
        int numToSell = 0;
        EditText theNumToSell = (EditText)findViewById(R.id.num_to_sell);
        if (theNumToSell != null) {
            String a = theNumToSell.getText().toString();
            if (!a.isEmpty()) {
                numToSell = Math.abs(Integer.valueOf(a));
            }
        }
        CharSequence text;
        int currentAmount = PlayerValues.getItemAmount(PlayerValues.getPlantItems(mPos).name);
        if (numToSell < 1) {
            text= "You must pick a valid value for the amount";
        } else if (numToSell <= currentAmount) {
            PlayerValues.setItemAmount(PlayerValues.getPlantItems(mPos).name, currentAmount - numToSell);
            PlayerValues.setMoney(PlayerValues.getMoney() + cost * numToSell);
            PlayerValuesDB theTask = new PlayerValuesDB();
            theTask.UpdateUserMoney(this, PlayerValues.getMoney());
            text = "You just sold " + numToSell + " " + PlayerValues.getPlantItems(mPos).name
                    + " for " + cost * numToSell + " coins.";
        } else {
            text = "You don't have enough of that item!";
        }

        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show();
        super.onBackPressed();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    //this is when an inventory item is selected and the details need to be viewed.
    @Override
    public void onListFragmentInteraction(int position) {
        mPos = position;
        ItemDetailFragment itemDetailFragment = new ItemDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ItemDetailFragment.ARG_POSITION, position);
        itemDetailFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, itemDetailFragment)
                .addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mAdapter = (BaseAdapterHelper_farmField) parent.getAdapter();
        Log.e(position+",mutureOnclick111:  "+mAdapter.getMutureTime(position), "runnable");
        FieldPlantSeedListDialogFragment fieldPlantSeedListDialogFragment = new FieldPlantSeedListDialogFragment();
        myBundle.putInt("position", position);
        //TODO fix bugs
        if (mAdapter.getMutureTime(position) < 0){
            Log.e(position+",mutureOnclick222:  "+mAdapter.getMutureTime(position), "runnable");
            Toast.makeText(getApplicationContext(), "You have harvested your crops", Toast.LENGTH_SHORT).show();
            updateMoneyExp(mAdapter.field_arraylist.get(position).typeOfCrops);
            Log.e(position+",mutureOnclick333:  "+mAdapter.getMutureTime(position), "runnable");
            mAdapter.setMutureTime(position, 0);
            Log.e(position+",mutureOnclick444:  "+mAdapter.getMutureTime(position), "runnable");
        }
        else{
            fieldPlantSeedListDialogFragment.show(getSupportFragmentManager(), "what is this parameter?");
        }
    }



    @Override
    public void plantSeed(String seed) {

        if (seed.equals(Config.CORN)){
            int position = myBundle.getInt("position");
            final Field field = (Field) mAdapter.getItem(position);
            field.imageID = R.drawable.corn_100dp;
            field.mutureTime = Config.CORNMUTURETIME;
            field.typeOfCrops = Config.CORN;
            mAdapter.notifyDataSetChanged();
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    field.mutureTime -= 1000;
                    mAdapter.notifyDataSetChanged();
                    if (field.mutureTime > 0){
                        handler.postDelayed(this, 1000);
                    }
                    Log.i("1,mutureTime: "+field.mutureTime, "runnable");
                }
            };
            handler.postDelayed(runnable, 1000);
            Log.i("2,mutureTime: "+field.mutureTime, "runnable");
        }

        if (seed.equals(Config.WHEAT)){
            final int position = myBundle.getInt("position");
            final Field field = (Field) mAdapter.getItem(position);
            field.imageID = R.drawable.wheat_100dp;
            field.mutureTime = Config.WHEATMUTURETIME;
            field.typeOfCrops = Config.WHEAT;
            mAdapter.notifyDataSetChanged();
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    field.mutureTime -= 1000;
                    mAdapter.notifyDataSetChanged();
                    if (field.mutureTime > 0){
                        handler.postDelayed(this, 1000);
                    }
                    Log.i(position + ",muturePlantSeed: "+field.mutureTime, "runnable");
                }
            };
            handler.postDelayed(runnable, 1000);

        }
        if (seed.equals(Config.STRAWBERRY)){
            int position = myBundle.getInt("position");
            final Field field = (Field) mAdapter.getItem(position);
            field.imageID = R.drawable.strawberry_100dp;
            field.mutureTime = Config.STRAWBERRYMUTURETIME;
            field.typeOfCrops = Config.STRAWBERRY;
            mAdapter.notifyDataSetChanged();
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    field.mutureTime -= 1000;
                    mAdapter.notifyDataSetChanged();
                    if (field.mutureTime > 0){
                        handler.postDelayed(this, 1000);
                    }
                }
            };
            handler.postDelayed(runnable, 1000);

        }

        if (seed.equals(Config.POTATO)){
            int position = myBundle.getInt("position");
            final Field field = (Field) mAdapter.getItem(position);
            field.imageID = R.drawable.potato_100dp;
            field.mutureTime = Config.POTATOMUTURETIME;
            field.typeOfCrops = Config.POTATO;
            mAdapter.notifyDataSetChanged();
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    field.mutureTime -= 1000;
                    mAdapter.notifyDataSetChanged();
                    if (field.mutureTime > 0){
                        handler.postDelayed(this, 1000);
                    }
                }
            };
            handler.postDelayed(runnable, 1000);
        }
    }

    public void updateMoneyExp(String typeOfCrops){
        switch (typeOfCrops){
            case Config.CORN:{
                mExp += Config.CORNEXP;
                mMoney += Config.CORNMONEY;
                checkLevelUp();
            }
            case Config.WHEAT:{
                mExp += Config.WHEATEXP;
                mMoney += Config.WHEATMONEY;
                checkLevelUp();
            }
            case Config.STRAWBERRY:{
                mExp += Config.STRAWBERRYEXP;
                mMoney += Config.STRAWBERRYMONEY;
                checkLevelUp();
            }
            case Config.POTATO:{
                mExp += Config.POTATOEXP;
                mMoney += Config.POTATOMONEY;
                checkLevelUp();
            }
            default: break;
        }
    }

    public void checkLevelUp(){
        if (mExp >= Config.LEVELUPEXPERIENCEREQUIRED){
            mLevel++;
            mExp -= Config.LEVELUPEXPERIENCEREQUIRED;
        }
    }

//    /**
//     * happened when the one of the listfragment is clicked.
//     * add that specific fragment detail to activity.
//     * @param item
//     */
//    @Override
//    public void onListFragmentInteraction(HighScore item) {
//        HighscoreDetailFragment highscoreDetailFragment = new HighscoreDetailFragment();
//        Bundle args = new Bundle();
//        args.putSerializable(HighscoreDetailFragment.HIGHSCORE_ITEM_SELECTED, item);
//        highscoreDetailFragment.setArguments(args);
//
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.highScoreActivity_container, highscoreDetailFragment)
//                .addToBackStack(null)
//                .commit();
//    }
}
