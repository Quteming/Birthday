package com.example.qtm.birthdates.Controll;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVUser;
import com.example.qtm.birthdates.Model.Model;
import com.example.qtm.birthdates.R;
import com.example.qtm.birthdates.Util.Utils;

public class MainFragment extends Fragment implements View.OnClickListener {
    private LinearLayout ll_create;
    private LinearLayout ll_search;
    private LinearLayout ll_solar_to_lunar;
    private LinearLayout ll_birthday_calendar;
    private LinearLayout ll_out;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        ll_create = view.findViewById(R.id.ll_create);
        ll_search = view.findViewById(R.id.ll_search);
        ll_solar_to_lunar = view.findViewById(R.id.ll_solar_to_lunar);
        ll_birthday_calendar = view.findViewById(R.id.ll_birthday_calendar);
        ll_out = view.findViewById(R.id.ll_out);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListener();
    }

    private void initListener() {
        ll_create.setOnClickListener(this);
        ll_search.setOnClickListener(this);
        ll_solar_to_lunar.setOnClickListener(this);
        ll_birthday_calendar.setOnClickListener(this);
        ll_out.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.ll_create:
                if (Utils.isFastClick())
                    return;
                intent = new Intent(getActivity(), CreateUserActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_search:
                if (Utils.isFastClick())
                    return;
                intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_solar_to_lunar:
                if (Utils.isFastClick())
                    return;
                intent = new Intent(getActivity(), SolarToLunarActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_birthday_calendar:
                if (Utils.isFastClick())
                    return;
                intent = new Intent(getActivity(), CalendarActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_out:
                if (Utils.isFastClick())
                    return;
                AVUser.logOut();// 清除缓存用户对象

                Model.getInstance().getDbManger().close();//关闭数据库连接

                intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

                getActivity().finish();
                break;
                default:
                    break;
        }
//        ((MainActivity)getActivity()).drawer_layout.closeDrawers();
    }
}
