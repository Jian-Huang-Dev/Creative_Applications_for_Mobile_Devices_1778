package mobileapp.jianhuang.assign_4;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by jianhuang on 16-02-14.
 */
public class SwipeViews extends android.support.v4.app.Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Button mDelBtn, mSearchBtn;
    protected DBHelper mDB;
    protected Cursor mCS;

    SwipeViewsListener activityCallback;

    /**
     * Interface listener
     */
    public interface SwipeViewsListener {
        public void disableBtn();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            activityCallback = (SwipeViewsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement interface methods");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("mydebug", "mainOnreateView");
        View view = inflater.inflate(R.layout.swipe_views,
                container, false);
        mDB = new DBHelper(getActivity().getApplicationContext());
        mCS = mDB.getCursor();
        Helper.db_cs = mCS;

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        mDelBtn = (Button) view.findViewById(R.id.delBtn);
        mSearchBtn = (Button) view.findViewById(R.id.searchBtn);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        Log.d("progress1", DatabaseUtils.dumpCursorToString(mDB.getCursor()));

        mDelBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mDB.deleteTableRow(mViewPager.getCurrentItem() + 1);
                mSectionsPagerAdapter.notifyDataSetChanged();
//                mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

                Toast.makeText(getActivity().getApplicationContext(),
                        "Deleted!",
                        Toast.LENGTH_SHORT).show();

                // Database is empty
                Log.d("getcount", Integer.toString(mDB.getCursor().getCount()));
                if(mDB.getCursor().getCount() == 0) {
                    //disable clear and more-info buttons in mainactivity
                    activityCallback.disableBtn();
                }
            }
        });

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCS.moveToPosition(mViewPager.getCurrentItem());
                performSearch(mCS.getString(Helper.NAME_INDEX));
            }
        });

        return view;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Log.d("mydebug", "getItem");
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show total pages.
            Log.d("cursor", Integer.toString(mDB.getCursor().getCount()) + " getCount");
            return mCS.getCount();
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "SECTION 1";
//                case 1:
//                    return "SECTION 2";
//                case 2:
//                    return "SECTION 3";
//            }
//            return null;
//        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.d("mydebug", "OnreateView");

            View rootView = inflater.inflate(R.layout.sessions_fragment, container, false);
            TextView nameView = (TextView) rootView.findViewById(R.id.personName);
            TextView bioView = (TextView) rootView.findViewById(R.id.bioView);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.profilePicture);

//            nameView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            Helper.db_cs.moveToFirst();
            for (int i = 0; i < getArguments().getInt(ARG_SECTION_NUMBER); i++) {
                Helper.db_cs.moveToNext();
            }
            nameView.setText(Helper.db_cs.getString(Helper.NAME_INDEX));
            bioView.setText(Helper.db_cs.getString(Helper.BIO_INDEX));

            String filePath = Environment.getExternalStorageDirectory() +
                    File.separator +
                    Helper.db_cs.getString(Helper.PATH_INDEX);

            Bitmap bmp = BitmapFactory.decodeFile(filePath);
            imageView.setImageBitmap(bmp);

            Log.d("pathhh", filePath);

            return rootView;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("mydebug", "stop");
    }

    protected void performSearch (String string) {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, string);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getActivity().getApplicationContext(),
                    R.string.app_not_available,
                    Toast.LENGTH_SHORT).show();
        }
    }
}
