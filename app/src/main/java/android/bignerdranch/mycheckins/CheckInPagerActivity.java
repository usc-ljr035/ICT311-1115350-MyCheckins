package android.bignerdranch.mycheckins;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewParent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.UUID;

public class CheckInPagerActivity extends AppCompatActivity {
    private static final String EXTRA_CHECKIN_ID = "com.bignerdranch.android.mycheckins.checkin_id";
    private ViewPager mViewPager;
    private List<CheckIn> mCheckIns;

    public static Intent newIntent(Context packageContext, UUID checkinId) {
        Intent intent = new Intent(packageContext, CheckInPagerActivity.class);
        intent.putExtra(EXTRA_CHECKIN_ID, checkinId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin_pager);

        mViewPager = (ViewPager) findViewById(R.id.checkin_view_pager);

        mCheckIns = CheckInList.get(this).getCheckInList();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                CheckIn checkIn = mCheckIns.get(position);
                return CheckInFragment.newInstance(checkIn.getId());
            }

            @Override
            public int getCount() {
                return mCheckIns.size();
            }
        });

        for (int i = 0; i < mCheckIns.size(); i++) {
            if (mCheckIns.get(i).getId().equals(mCheckIns)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
