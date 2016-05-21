package qcox.tacoma.uw.edu.farmgame;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FieldPlantSeedListDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FieldPlantSeedListDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Communicater mCommunicater;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AdapterView<?> parent;
    private View view;
    private int position;
    private long id;


    public FieldPlantSeedListDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FieldPlantSeedListDialogFragment newInstance(String param1, String param2) {
        FieldPlantSeedListDialogFragment fragment = new FieldPlantSeedListDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        TextView textView = new TextView(getActivity());
//        textView.setText(R.string.hello_blank_fragment);
//        return textView;
//    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.plant_a_seed)
                .setItems(R.array.array_corps, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        Resources res = getActivity().getResources();
                        String[] colors = res.getStringArray(R.array.array_corps);

                        //talk to FarmFragment by using FarmActivity as a mCommunicater
                        mCommunicater = (Communicater) getActivity();
                        mCommunicater.plantSeed(colors[which]);
                    }
                });
        return builder.create();
    }
}
