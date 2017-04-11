package com.langoor.app.blueshak.register;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.AppController;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.services.model.FacebookRegisterModel;
import java.util.ArrayList;
import java.util.ArrayList;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Manu .
 */
public class GoogleAccountsFragment
             extends DialogFragment
{
    private ArrayList<Item> list = null;
    private ListView listView;
    private GoogleAccountsAdapter listadaptor;
    TextView none;
    public final static String TAG = "GoogleAccountsFragment";
    private ProgressBar mLoadingIndicator;
    Context context;
    Activity activity;
    FacebookRegisterModel facebookRegisterModel=new FacebookRegisterModel();
    public GoogleAccountsFragment()
    {
        /*
             All subclasses of Fragment must include a public empty constructor. The framework will often
             re-instantiate a fragment class when needed, in particular during state restore, and needs to
             be able to find this constructor to instantiate it. If the empty constructor is not available,
             a runtime exception will occur in some cases during state restore.
        */
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        context=getActivity();
        activity=getActivity();
        View mView = inflater.inflate(R.layout.account_selection, null);
        mLoadingIndicator = (ProgressBar)mView.findViewById(R.id.loading_indicator);
        listView = (ListView) mView.findViewById(R.id.detected_tv_list);
        none=(TextView)mView.findViewById(R.id.none);
        none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        showLoadingIndicator(true);
        list = getData();
        listadaptor = new GoogleAccountsAdapter(context, R.layout.account_list_item, list);
        listView.setAdapter(listadaptor);
        showLoadingIndicator(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                facebookRegisterModel.setEmail(list.get(position).getValue());
                facebookRegisterModel.setName(list.get(position).getKey());
                facebookRegisterModel.setPhone(list.get(position).getPhoneNumber());
                try{
                    ((OnNewsItemSelectedListener) activity).onNewsItemPicked(list.get(position));
                }catch (ClassCastException cce){

                }
                dismiss();
            }
        });
        return mView;
    }

    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState)
    {
        Dialog d = super.onCreateDialog(savedInstanceState);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return d;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setUpWindowLayout();
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface)
    {
         super.onDismiss(dialogInterface);
    }

    private void setUpWindowLayout()
    {
        if((this.getDialog() == null)||(this.getDialog().getWindow() == null))
            return;

        Window w = this.getDialog().getWindow();
        w.setLayout(context.getResources().getDimensionPixelSize(R.dimen.tv_selection_fragment_window_width), WindowManager.LayoutParams.WRAP_CONTENT);
        w.setGravity(Gravity.CENTER);
    }

    public void showLoadingIndicator(boolean show)
    {
        if(show)
        {
            mLoadingIndicator.setVisibility(View.VISIBLE);
           // listView.setVisibility(View.GONE);
           // listView.setVisibility(View.GONE);
        }
        else
        {
            mLoadingIndicator.setVisibility(View.GONE);
           // listView.setVisibility(View.VISIBLE);
        }
    }

    private ArrayList<Item> getData() {
        ArrayList<Item> accountsList = new ArrayList<Item>();
        String phoneNumber=null;
       /* Account[] wts_accounts = AccountManager.get(context).getAccountsByType("com.whatsapp");
        if(wts_accounts!=null)
            if(wts_accounts.length>0)
            for (Account account : wts_accounts) {
                if(account.type.equals("com.whatsapp")){
                     phoneNumber = account.name;
                    System.out.println("######account.type######"+account.type);
                    System.out.println("######account.name######"+account.name);
                }
        }*/

        //Getting all registered Google Accounts;
        try {
            Account[] google_accounts = AccountManager.get(context).getAccountsByType("com.google");
            phoneNumber= AppController.getMobileNumber(context);
            if(!TextUtils.isEmpty(phoneNumber))
                phoneNumber=phoneNumber.trim();
            for (Account account : google_accounts) {
                String name=account.name;
                if(!TextUtils.isEmpty(name)){
                    if(name.contains("@")){
                        String[] items=name.split("@");
                        if(items.length>0)
                            name=GlobalFunctions.getSentenceFormat(items[0]);;
                    }
                }
                Item item = new Item(name,account.name,phoneNumber);
                accountsList.add(item);
            }

        } catch (Exception e) {
            Log.i("Exception", "Exception:" + e);
        }

        //For all registered accounts;
		/*try {
			Account[] accounts = AccountManager.get(this).getAccounts();
			for (Account account : accounts) {
				Item item = new Item( account.type, account.name);
				accountsList.add(item);
			}
		} catch (Exception e) {
			Log.i("Exception", "Exception:" + e);
		}*/
        return accountsList;
    }

    public String getPhoneNumber() {
        TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        return  mPhoneNumber;
    }
    public interface OnNewsItemSelectedListener{
        public void onNewsItemPicked(Item position);
    }
}

