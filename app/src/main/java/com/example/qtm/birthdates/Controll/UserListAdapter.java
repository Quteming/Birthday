package com.example.qtm.birthdates.Controll;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qtm.birthdates.Model.Bean.UserBean;
import com.example.qtm.birthdates.R;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<UserBean> mData = new ArrayList<>();

    public UserListAdapter(Context context, List<UserBean> mData) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.adapter_user_list_item, parent, false);
        UserViewHolder holder = new UserViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof UserViewHolder){
             final UserBean userBean = mData.get(position);
            ((UserViewHolder)holder).item_user_name.setText(userBean.getName());
            ((UserViewHolder)holder).item_user_sex.setImageResource(userBean.getSex() == 0 ? R.drawable.sex_boy : R.drawable.sex_gril);
            String calendar = userBean.getSolarLunar() == 0 ? "阳历:  " : "阴历:  ";
            ((UserViewHolder)holder).item_user_birthday.setText(calendar + userBean.getBirthday());

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    adapterInterface.getLongClickPosition(position);
                    return false;
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterInterface.getOnClickPosition(userBean.getId());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size() > 0 && mData != null ? mData.size() : 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        private TextView item_user_name;
        private ImageView item_user_sex;
        private TextView item_user_birthday;

        public UserViewHolder(View itemView) {
            super(itemView);
            item_user_name = itemView.findViewById(R.id.item_user_name);
            item_user_sex = itemView.findViewById(R.id.item_user_sex);
            item_user_birthday = itemView.findViewById(R.id.item_user_birthday);
        }
    }

    public interface AdapterInterface{
        //获取长按的position
        void getLongClickPosition(int position);

        //获得点击的objectId
        void getOnClickPosition(String objectId);
    }

    private AdapterInterface adapterInterface;

    public void setAdapterInterface(AdapterInterface adapterInterface) {
        this.adapterInterface = adapterInterface;
    }
}
