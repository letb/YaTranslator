package tp.translator.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by eugene on 04.10.15.
 */
public class UserInformer {
    public static void showMessage (String message, Activity where) {
        Toast.makeText(where, message, Toast.LENGTH_LONG).show();
    }
}
