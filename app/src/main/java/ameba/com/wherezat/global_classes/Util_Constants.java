package ameba.com.wherezat.global_classes;

import android.content.Context;
import android.content.SharedPreferences;

public class Util_Constants
    {

        public static String app_link="http://www.wherez-at.com/app/wherezatapp.apk";


        public static String registeration_url="http://112.196.34.42:8094/Customer/SaveCustomer";
        public static String SENDER_ID = "844308277515";
        public static String verifyCodeURL = "http://112.196.34.42:8094/Customer/ValidateMobileCode";
        public static String filterContacts = "http://112.196.34.42:8094/Customer/filterContacts";
        public static String inviteContactsURL = "http://112.196.34.42:8094/Customer/InviteContacts";
        public static String getinvites= "http://112.196.34.42:8094/Customer/GetInvites";
        public static String UpdateLocation = "http://112.196.34.42:8094/Customer/SetLatLongLocation";
        public static String GetActiveLocation = "http://112.196.34.42:8094/Customer/GetActiveLocations";
        public static String RespondToInvite = "http://112.196.34.42:8094/Customer/RespondToInvite";
        public static String revoke = "http://112.196.34.42:8094/Customer/RevokeRequest";

        public static String reverify="http://112.196.34.42:8094/Customer/ResendVerificationCode";


        public static final String BROADCAST_UPDATEMAP="com.gagan.updatemap";
        public static final String BROADCAST_UPDATERIGHTSIDE="com.gagan.updaterightside";


        public static boolean  is_continuous_hit=true;
        public static long time_duration_of_hit=0;




        public static final long timeSCreenActive=30*1000;


        public static final long timeScreenINActive=60*1000;

        public static  long time=timeSCreenActive;
        public static  boolean isOpen=false;



        public static long updateInterval(Context con)
        {
          SharedPreferences sharedpreferencesObj = con.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

           int G = sharedpreferencesObj.getInt("UpdateRate",5);

            if(G==0)
            {
                G=1;
            }

            return G*timeScreenINActive;
        }








       // public static boolean  is_root_drawn=false;

/*

    public static boolean is_valid_email(String g) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(g);
        return matcher.matches();
    }*/


}
