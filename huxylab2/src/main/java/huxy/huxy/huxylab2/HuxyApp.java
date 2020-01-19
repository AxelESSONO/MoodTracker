package huxy.huxy.huxylab2;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class HuxyApp extends AppCompatActivity {

    private final static HuxyApp INSTANCE = new HuxyApp();
    static LayoutInflater inflater;
    static Toast toast;
    static TextView text;

    /**
     * This method is capable for showing the success toast message.
     *
     * @param activity This is the activity.
     * @param message  This is the message of the toast.
     * @return This returns the instance of the class.
     */
    public static HuxyApp successToast(Activity activity, String message) {

        inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.lib_activity_main,
                (ViewGroup) activity.findViewById(R.id.custom_toast_container));

        layout.setBackgroundColor(activity
                .getResources().getColor(R.color.successMessage));

        text = layout.findViewById(R.id.header1);
        text.setText(message);

        toast = new Toast(activity);

        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

        return INSTANCE;

    }

    /**
     * This is the method for showing a danger toast message.
     *
     * @param activity This is the activity.
     * @param message  This is the message of the toast.
     * @return This returns the instance of the class.
     */
    public static HuxyApp dangerToast(Activity activity, String message) {

        inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.lib_activity_main,
                (ViewGroup) activity.findViewById(R.id.custom_toast_container));

        layout.setBackgroundColor(activity
                .getResources().getColor(R.color.dangerMessage));

        text = layout.findViewById(R.id.header1);
        text.setText(message);

        toast = new Toast(activity);

        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

        return INSTANCE;

    }

    /**
     * This method is responsible for displaying the warning toast message.
     *
     * @param activity This is the activity.
     * @param message  This is the message of the toast.
     * @return Return the instance of the class.
     */
    public static HuxyApp warningToast(Activity activity, String message) {

        inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.lib_activity_main,
                (ViewGroup) activity.findViewById(R.id.custom_toast_container));

        layout.setBackgroundColor(activity
                .getResources().getColor(R.color.warningMessage));

        text = layout.findViewById(R.id.header1);
        text.setText(message);

        toast = new Toast(activity);

        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

        return INSTANCE;

    }

    /**
     * This is the method for a custom toast message, you can change the color of text and the background.
     *
     * @param activity            This is the Activity.
     * @param message             The toast message.
     * @param backgroundColorCode This is the background clor.
     * @param textColorCode       This is the text color.
     * @return Returns the instance.
     */
    public static HuxyApp customToast(Activity activity, String message, String backgroundColorCode, String textColorCode) {

        inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.lib_activity_main,
                (ViewGroup) activity.findViewById(R.id.custom_toast_container));

        text = layout.findViewById(R.id.header1);
        text.setText(message);
        text.setTextColor(Color.parseColor(textColorCode));
        layout.setBackgroundColor(Color.parseColor(backgroundColorCode));

        toast = new Toast(activity);

        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

        return INSTANCE;

    }

    /**
     * This method is capable for setting the position and offsets.
     *
     * @param position This aligns the toast position.
     * @param xOffset  Sets the x offset.
     * @param yOffset  Sets the y offset.
     * @return returns the instance of the class.
     */
    public static HuxyApp setPositionAndOffSet(int position, int xOffset, int yOffset) {

        toast.setGravity(position, xOffset, yOffset);
        return INSTANCE;
    }

    /**
     * This is responsible setting the textSize.
     *
     * @param size This is the size of the Toast message.
     * @return
     */
    public static HuxyApp setTextSize(float size) {

        text.setTextSize(size);
        return INSTANCE;
    }

    /**
     * This is responsible for setting padding.
     *
     * @param padding This is the padding of toast message.
     * @return returns the instance of the class.
     */
    public static HuxyApp setPadding(int padding) {
        text.setPadding(padding, padding, padding, padding);
        return INSTANCE;
    }

    /**
     * This method deals with setting the font types or styles of the toast
     * message.
     * @param activity This is context of the application.
     * @param fontType This is the path of the font. e.g (R.id.font_name)
     * @return
     */

    public HuxyApp setFontStyle(Activity activity, int fontType) {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Typeface typeface = activity.getResources().getFont(fontType);
            text.setTypeface(typeface);
        } else {

            Typeface typeface = ResourcesCompat.getFont(activity, fontType);
            text.setTypeface(typeface);
        }

        return INSTANCE;
    }


}
