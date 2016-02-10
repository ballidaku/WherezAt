package ameba.com.wherezat.services;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import ameba.com.wherezat.R;
import ameba.com.wherezat.fragments.Invite_Friends;
import ameba.com.wherezat.global_classes.Util_Constants;
import ameba.com.wherezat.global_classes.Utill_G_S;

/**
 * Created by Sharanpal on 8/18/2015.
 */
public
class Filter_Contacts_ProgressTask extends AsyncTask<Void, Void, Void>
{

    Context con;
    HttpResponse response;
    String ResponseString = "";

    String allNumbers = "";

    List<HashMap<String, String>> contacts;// = new ArrayList<>();
    HashMap<String, String> hashmapForMatchingNames = new HashMap<>();

    ArrayList<HashMap<String, String>> contact_list;

    String status = "";
    Fragment fragment;

    public Filter_Contacts_ProgressTask(Context context, Fragment fragment)
    {

        this.con = context;
        this.fragment = fragment;

        contacts = new ArrayList<>();
        contact_list = new ArrayList<>();
    }


    public Filter_Contacts_ProgressTask(Context con)
    {
        this.con = con;
        fragment=null;
        contacts = new ArrayList<>();
        contact_list = new ArrayList<>();
    }

    protected
    void onPreExecute()
    {


        super.onPreExecute();
    }

    @Override
    protected
    Void doInBackground(Void... params)
    {

        gettingPhoneContacts();

        for (int i = 0; i <= contacts.size() - 1; i++)
        {

            allNumbers = allNumbers.isEmpty() ? contacts.get(i).get("number") : allNumbers + "," + contacts.get(i).get("number");
            // Log.e("allNumbers-----------", "--------------------" + allNumbers);
        }

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Util_Constants.filterContacts);
        try
        {

            List<NameValuePair> paramsL = new LinkedList<>();

            paramsL.add(new BasicNameValuePair("ContactList", allNumbers));

            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("Accept", "application/json");
            HttpEntity entity = new UrlEncodedFormEntity(paramsL, "UTF-8");
            httpPost.setEntity(entity);

            response = httpClient.execute(httpPost);

            ResponseString = EntityUtils.toString(response.getEntity());


            try
            {
                JSONObject jsonObject = new JSONObject(ResponseString);



                if (jsonObject.getString("Status").equals("success"))
                {
                    status = "success";

                    JSONObject responseObject = jsonObject.getJSONObject("Message");



                    contact_list.addAll(parseContactList(responseObject.toString()));


                }
                else if (jsonObject.optString("Status").equals("error"))
                {
                    status = "error";
                    ResponseString = jsonObject.optString("Message");
                }


            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            ResponseString = "ERROR";
        }

        return null;

    }

    @Override
    protected
    void onPostExecute(Void result)
    {

        if (fragment!=null)
        {
            if (status.equals("success"))
			{

				((Invite_Friends) fragment).set_data(contact_list);
			}
			else
			{
				Utill_G_S.showToast(ResponseString, con);
			}
        }

        super.onPostExecute(result);
    }


    //*****************************************Getting Phone Contacts*********************************

    private
    void gettingPhoneContacts()
    {
        contacts.clear();
        ContentResolver cr = con.getContentResolver();
        // Read Contacts
        Cursor c = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.RawContacts.ACCOUNT_TYPE}, null, null, null);
        if (c.getCount() <= 0)
        {
            Toast.makeText(con, "No Phone Contact Found..!", Toast.LENGTH_SHORT).show();
        }
        else
        {

            while (c.moveToNext())
            {
                HashMap<String, String> innerContacts = new HashMap<>();
                String Phone_number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));          //Phone number
                Phone_number = Phone_number.replaceAll(" ", "");

                Phone_number = Phone_number.replaceAll("-", "");
                Phone_number = Phone_number.replaceAll("\\(", "");
                Phone_number = Phone_number.replaceAll("\\)", "");

                if (Phone_number.length() >= 10)
                {
                    Phone_number.substring(Phone_number.length() - 10, Phone_number.length());
                }
                String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));                        //Name of contact

                innerContacts.put("name", name);
                innerContacts.put("number", Phone_number);

                hashmapForMatchingNames.put(Phone_number, name);

                contacts.add(innerContacts);
            }
            c.close();
            Collections.sort(contacts, new Comparator<HashMap<String, String>>()
            {
                @Override
                public
                int compare(HashMap<String, String> s1, HashMap<String, String> s2)
                {
                    return s1.get("name").compareToIgnoreCase(s2.get("name"));
                }
            });

            //Log.e("Sharan_contacts",""+contacts);
        }
    }








    private ArrayList<HashMap<String, String>> parseContactList(String jsonData) throws Exception
    {
        ArrayList<HashMap<String, String>> contactList=new ArrayList<>();


        JSONObject responseObject=new JSONObject(jsonData);

        JSONArray ContactsFound = responseObject.getJSONArray("ContactFound");

        JSONArray jNewG=new JSONArray();


        for (int g = 0; g < ContactsFound.length(); g++)
        {

            String contactS = ContactsFound.getString(g);


            HashMap<String, String> map = new HashMap<>();

            map.put("name", hashmapForMatchingNames.get(contactS));
            map.put("contact", contactS);
            map.put("is_found", "yes");

            contactList.add(map);

            JSONObject j= new JSONObject();
            j.put("name", hashmapForMatchingNames.get(contactS));
            j.put("contact", contactS);
            j.put("is_found", "yes");
            jNewG.put(j);

        }

        JSONArray ContactNotFound = responseObject.getJSONArray("ContactNotFound");

        for (int g = 0; g < ContactNotFound.length(); g++)
        {
            String contactS = ContactNotFound.getString(g);


            HashMap<String, String> map = new HashMap<>();

            map.put("name", hashmapForMatchingNames.get(contactS));
            map.put("contact", contactS);
            map.put("is_found", "no");
            contactList.add(map);


            JSONObject j= new JSONObject();
            j.put("name", hashmapForMatchingNames.get(contactS));
            j.put("contact", contactS);
            j.put("is_found", "no");
            jNewG.put(j);
        }

        saveContactLocally(con,jNewG.toString());

        return contactList;
    }





    private void saveContactLocally(Context con,String jsonData)
    {
        SharedPreferences  sp = con.getSharedPreferences("MyPrefs", Activity.MODE_PRIVATE);

        SharedPreferences.Editor editPref=sp.edit();

        editPref.putString("contacts",jsonData);

        editPref.apply();
    }







}