package kat.android.com.readerrss;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;


public class FirstActivity extends Activity {

//    Можна ще допиляти:
//    - при вході вказувати/вибирати RSS канал
//    - зберігання новин в БД
//    - При кліку на новини виводити детальну інфу (новий фрагмент)
//    - Якщо детальної інфи мало , то реалізувати кнопку для переходу на сайт новини
//    - для платшетів виводити зліва список новин , зправа детальну інфу ( два фрагменти)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        if (savedInstanceState == null) {
            addRssFragment();
        }
    }

    private void addRssFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        RssFragment fragment = new RssFragment();
        transaction.add(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("fragment_added", true);
    }
}
