package android.bignerdranch.mycheckins;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import database.CheckInSchema.CheckInDbSchema;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import static android.bignerdranch.mycheckins.CheckInList.get;
import static database.CheckInSchema.CheckInBaseHelper.DATABASE_NAME;


public class CheckInFragment extends Fragment {

    private CheckIn mCheckIn;
    private File mPhotoFile;
    private EditText mTitleField;
    private EditText mPlaceField;
    private EditText mDetailsField;
    private Button mDateButton;
    public Button mDeleteButton;
    private ImageButton mPhotoButton;
    private Button mMapButton;
    private ImageView mPhotoView;
    private TextView mLocation;
    private Location mCheckInLocation;


    private static final String ARG_CHECKIN_ID = "checkin_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 2;

    private GoogleApiClient mClient;

    public static CheckInFragment newInstance(UUID checkinid) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CHECKIN_ID, checkinid);

        CheckInFragment fragment = new CheckInFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID checkinid = (UUID) getArguments().getSerializable(ARG_CHECKIN_ID);
        mCheckIn = get(getActivity()).getCheckInID(checkinid);
        mPhotoFile = get(getActivity()).getPhotoFile(mCheckIn);

        mClient = new GoogleApiClient.Builder(getActivity()).addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                LocationRequest request = LocationRequest.create();
                request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                request.setNumUpdates(1);
                request.setInterval(0);

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                LocationServices.FusedLocationApi.requestLocationUpdates(mClient, request, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        mCheckIn.setLatitude(location.getLatitude());
                        mCheckIn.setLongitude(location.getLongitude());
                        Log.i("LOCATION", "Got a fix: " + location);
                    }
                });

            }

            @Override
            public void onConnectionSuspended(int i) { }
        })
        .build();
    }

    @Override
    public void onPause() {
        super.onPause();
        get(getActivity()).updateCheckIn(mCheckIn);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_checkin, container, false);

        mTitleField = (EditText) v.findViewById(R.id.checkin_title);
        mTitleField.setText(mCheckIn.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCheckIn.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        mPlaceField = (EditText) v.findViewById(R.id.checkin_place);
        mPlaceField.setText(mCheckIn.getPlace());
        mPlaceField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCheckIn.setPlace(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        mDetailsField = (EditText) v.findViewById(R.id.checkin_details);
        mDetailsField.setText(mCheckIn.getDetails());
        mDetailsField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCheckIn.setDetails(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        mDateButton = (Button) v.findViewById(R.id.checkin_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCheckIn.getDate());
                dialog.setTargetFragment(CheckInFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mPhotoButton = (ImageButton) v.findViewById(R.id.checkin_camera);
        PackageManager packageManager = getActivity().getPackageManager();
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.bignerdranch.android.mycheckins.fileprovider",
                        mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.checkin_image);
        updatePhotoView();

        mMapButton = (Button) v.findViewById(R.id.checkin_location);
        mMapButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intentMap = new Intent(getContext(), MapsActivity.class);
                startActivity(intentMap);
            }
        });

        //EXPERIMENTAL
        mLocation = (TextView) v.findViewById(R.id.checkin_location_text);
        String latitiudetext = Double.toString(mCheckIn.getLatitude());
        String longitudetext = Double.toString(mCheckIn.getLongitude());
        mLocation.setText(latitiudetext + " , " + longitudetext);
        //EXPERIMENTAL

        //mDeleteButton = (Button) getView().findViewById(R.id.checkin_delete);
        mDeleteButton = (Button) v.findViewById(R.id.checkin_delete_button);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CheckInList.get(v.getContext()).deleteCheckIn(mCheckIn);
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode!= Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCheckIn.setDate(date);
            updateDate();
        } else if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.bignerdranch.android.mycheckins.fileprovider",
                    mPhotoFile);
            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updatePhotoView();
        }
    }

    private void updateDate() {
        mDateButton.setText(mCheckIn.getDate().toString());
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }
}
