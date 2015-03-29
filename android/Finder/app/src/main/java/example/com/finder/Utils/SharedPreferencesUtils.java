package example.com.finder.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Alexandru on 29-Mar-15.
 */
public class SharedPreferencesUtils {

    public static void addFacebookData(Context context, String userId, String accessToken, String macAddress)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences("finder", Context.MODE_PRIVATE).edit();
        editor.putString("facebook_user_id", userId);
        editor.putString("facebook_access_token", accessToken);
        editor.putString("facebook_mac_address", macAddress);
        editor.commit();
    }

    public static String getFacebookUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("finder", Context.MODE_PRIVATE);
        return prefs.getString("facebook_user_id", "");
    }
    public static String getFacebookAccessToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("finder", Context.MODE_PRIVATE);
        return prefs.getString("facebook_access_token", "");
    }
    public static String getFacebookMacAddress(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("finder", Context.MODE_PRIVATE);
        return prefs.getString("facebook_mac_address", "");
    }
}
