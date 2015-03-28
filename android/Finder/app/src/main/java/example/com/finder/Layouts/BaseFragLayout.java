package example.com.finder.Layouts;

import android.support.v4.app.Fragment;

/**
 * Created by Alexandru on 28-Mar-15.
 */
public class BaseFragLayout extends Fragment {

    private String topbarTitle;

    public boolean onBackPressed()
    {
        return true;
    }

    public String getTopbarTitle() {
        return topbarTitle;
    }

    public void setTopbarTitle(String topbarTitle) {
        this.topbarTitle = topbarTitle;
    }
}