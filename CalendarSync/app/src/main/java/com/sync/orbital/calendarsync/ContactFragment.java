package com.sync.orbital.calendarsync;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager_cont);

        setHasOptionsMenu(true);

        //Tabs
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.AddFragment(new ContactAllFragment(), "All");
        adapter.AddFragment(new ContactGroupFragment(), "Groups");
        adapter.AddFragment(new ContactRequestFragment(), "Requests");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout_cont);
        tabLayout.setupWithViewPager(viewPager);

        ((MainActivity)getActivity()).setTitle("Contacts");

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_contacts, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case (R.id.action_add_contacts):
                Intent intent_search = new Intent(getActivity(), ContactSearchActivity.class);
                startActivity(intent_search);
                return true;
            case (R.id.action_settings_contacts):
                Intent intent_set = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent_set);
                return true;
//            case (R.id.action_all_users):
//                Intent intent_all = new Intent(getActivity(), UsersAllActivity.class);
//                startActivity(intent_all);
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
