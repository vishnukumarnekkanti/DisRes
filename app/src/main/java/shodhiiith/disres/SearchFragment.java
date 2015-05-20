package shodhiiith.disres;

/**
 * Created by rajat on 4/4/15.
 */

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import shodhiiith.disres.R;

public class SearchFragment extends Fragment {

    public SearchFragment() {
    }
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    EditText myTextBox;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        myTextBox = (EditText) rootView.findViewById(R.id.editText5);
        sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_APPEND);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        myTextBox.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                getSearchResultsByName();
            }
        });
    }

    public void getSearchResultsByName(){
        Log.d("searchText", "reading now");
        EditText et = (EditText) getActivity().findViewById(R.id.editText5);
        String searchText = et.getText().toString();
        //Log.d("searchText", searchText);
        String json = sharedPreferences.getString("organisations", null);
        List<OrgsData> orgsList = new OrgsData().savedOrgList(json);
        List<OrgsData> searchResults = new ArrayList<OrgsData>();
        for (OrgsData org: orgsList){
            //Toast.makeText(getBaseContext(),org.org_name,Toast.LENGTH_SHORT).show();
            if(org.org_name.toLowerCase().matches("(.*)"+searchText.toLowerCase()+"(.*)")){
                // Toast.makeText(getBaseContext(),org.org_name,Toast.LENGTH_SHORT).show();
                searchResults.add(org);
            }
        }
        TextView displayText = (TextView) getActivity().findViewById(R.id.textView6);
        displayText.setText("");
        if(!searchText.equals("")) {
            for (OrgsData org : searchResults) {
                displayText.append(org.org_name.toString() + "  :  " + org.mobile.toString() + "\n\n");
            }
        }
    }


}