package android.bignerdranch.mycheckins;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.w3c.dom.Text;

import java.nio.charset.CharsetEncoder;
import java.util.List;
import java.util.UUID;

import static android.bignerdranch.mycheckins.CheckInList.get;

public class CheckInListFragment extends Fragment {
    private static final String SAVED_SUBTITLE_VISIBILE = "subtitle";
    private static final int REQUEST_CHECKIN = 1;
    private RecyclerView mCheckInRecyclerView;
    private CheckInAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkinlist, container, false);
        mCheckInRecyclerView = (RecyclerView) view.findViewById(R.id.checkin_recycler_view);
        mCheckInRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_checkin_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_checkin:
                CheckIn checkin = new CheckIn();
                get(getActivity()).addCheckIn(checkin);
                Intent intent = CheckInPagerActivity.newIntent(getActivity(), checkin.getId()); //p260
                startActivity(intent);
                return true;

            case R.id.help:
                Intent intentHelp = new Intent(getContext(), HelpActivity.class);
                startActivity(intentHelp);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        CheckInList checkinlist = get(getActivity());
        List<CheckIn> checkins = checkinlist.getCheckInList();

        if (mAdapter == null) {
            mAdapter = new CheckInAdapter(checkins);
            mCheckInRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setCheckIns(checkins);
            mAdapter.notifyDataSetChanged();
        }

    }

    private class CheckInHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mPlaceTextView;
        private TextView mDateTextView;
        private CheckIn mCheckIn;

        public CheckInHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_checkin, parent, false));
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.checkin_title);
            mPlaceTextView = (TextView) itemView.findViewById(R.id.checkin_place);
            mDateTextView = (TextView) itemView.findViewById(R.id.checkin_date);
        }

        public void bind(CheckIn checkin) {
            mCheckIn = checkin;
            mTitleTextView.setText(mCheckIn.getTitle());
            mPlaceTextView.setText(mCheckIn.getPlace());
            mDateTextView.setText(mCheckIn.getDate().toString());
        }

        @Override
        public void onClick(View v) {
            Intent intent = CheckInPagerActivity.newIntent(getActivity(), mCheckIn.getId());
            startActivity(intent);
        }
    }

    private class CheckInAdapter extends RecyclerView.Adapter<CheckInHolder> {
        private List<CheckIn> mCheckInList;
        public CheckInAdapter(List<CheckIn> checkinlist) {
            mCheckInList = checkinlist;
        }

        @Override
        public CheckInHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CheckInHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CheckInHolder holder, int position) {
            CheckIn checkin = mCheckInList.get(position);
            holder.bind(checkin);
        }

        @Override
        public int getItemCount() {
            return mCheckInList.size();
        }

        public void setCheckIns(List<CheckIn> checkins) {
            mCheckInList = checkins;
        }
    }
}