package uniks.de.iotexample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;

public class ServicePagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<String> mServices =  new ArrayList<String>();

    public ServicePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        ServiceFragment fragment = new ServiceFragment();
        Bundle bundle = new Bundle();
        bundle.putString("serviceurl",mServices.get(i));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return mServices.size();
    }

    public void addService(String serviceUrl) {
        mServices.add(serviceUrl);
        notifyDataSetChanged();
    }
}
