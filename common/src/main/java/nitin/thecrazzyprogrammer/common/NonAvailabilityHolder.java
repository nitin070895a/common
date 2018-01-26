package nitin.thecrazzyprogrammer.generics.Views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import nitin.thecrazzyprogrammer.generics.R;


/**
 * Created by Nitin Khurana on 7/19/2017.
 * <p>
 * A view class to show a screen to handle non availability scenarios.
 * With that I mean for example when we did not get any response from our servers,
 * user have internet issues, our serves went down, there is no data to show etc.
 * <p>
 * Components : (An image, A textview, A button_theme_selector)
 * <p>
 * Steps to use :
 * <p>
 * 1. Create an instance of this class
 * 2. Tweek in some properties if you like
 * 3. Show or hide the view by changing its visibility
 * <p>
 * <strong>Best practices :</strong> If you plan for a retry option make sure you hide the holder after pressing the button_theme_selector,
 * and after getting the response you can show it again based on your results
 */
public class NonAvailabilityHolder {

    private RelativeLayout root;    // or container or great grand parent, the first view in the layout file

    private ImageView image;
    private TextView msg;
    private Button button;

    @Deprecated
    /**
     * @deprecated use {@link #NonAvailabilityHolder(Context, View)} instead
     *
     * Now no need to include non_availability layout in your xml file
     * Remove the include if you have it, if not ignore
     * This constructor is deprecated and you should use the other constructor, the other constructor carry out the creation of layout for the non_availability screen itself
     *
     * @param view The parent view where you have included the non_availability layout in your xml
     */
    public NonAvailabilityHolder(View view) {

        root = (RelativeLayout) view.findViewById(R.id.root);

        image = (ImageView) view.findViewById(R.id.image);
        msg = (TextView) view.findViewById(R.id.msg);
        button = (Button) view.findViewById(R.id.button);

    }

    /**
     * Create and show a non_availability screen (An image, a retry button_theme_selector and a text message) with complete controls of the views
     *
     * @param context Activity context
     * @param view    the view where the non_availability screen will be shown
     */
    public NonAvailabilityHolder(Context context, View view) {

        root = new RelativeLayout(context);
        root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        root = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.non_availability_screen, (ViewGroup) view, false);
        ((ViewGroup) view).addView(root);

        image = (ImageView) root.findViewById(R.id.image);
        msg = (TextView) root.findViewById(R.id.msg);
        button = (Button) root.findViewById(R.id.button);

    }

    /**
     * Sets the message in the non availability screen
     *
     * @param msg the message to be shown
     */
    public void setMessage(String msg) {

        this.msg.setText(msg);
    }

    /**
     * Sets the properties of the bottom button_theme_selector in the non availability screen
     *
     * @param button          the text to be written on the button_theme_selector
     * @param onClickListener the action that the button_theme_selector will carry out
     */
    public void setButton(String button, View.OnClickListener onClickListener) {

        this.button.setText(button);
        this.button.setOnClickListener(onClickListener);
    }

    /**
     * Set the image to be shown in non availability screen (def: cloud)
     *
     * @param image the image to be shown
     */
    public void setImage(int image) {

        this.image.setImageResource(image);
    }

    /**
     * Sets the visibility of the non availability screen (all views)
     *
     * @param visibility same as android view (invisible, visible, gone)
     */
    public void setVisibility(int visibility) {

        this.root.setVisibility(visibility);
    }

    /**
     * Show or hide the bottom button_theme_selector in the non availability screen
     *
     * @param visibility
     */
    public void setButtonVisibility(int visibility) {

        this.button.setVisibility(visibility);
    }

    /**
     * Give a tint to the image in the non availability screen
     *
     * @param context the context of the activity
     * @param color   the tint color
     */
    public void setImageTint(Context context, int color) {

        image.setColorFilter(ContextCompat.getColor(context, color));
    }

}
