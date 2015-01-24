package ui_update.android.com.draganddrow;

import android.app.Fragment;


public class DragAndDrawActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new DragAndDrawFragment();
    }


}
