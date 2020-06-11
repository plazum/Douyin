package com.example.douyin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        final MyAdapter adapter = new MyAdapter(this);
        recyclerView.setAdapter(adapter);

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://beiyou.bytedance.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        GetFeedInterface getFeedInterface = retrofit.create(GetFeedInterface.class);
        Call<List<Item>> call = getFeedInterface.getFeed();
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(@NotNull Call<List<Item>> call, @NotNull Response<List<Item>> response) {
                List<Item> feedList = response.body();
                if (feedList == null) {
                    return;
                }
                adapter.setmList(feedList);
            }

            @Override
            public void onFailure(@NotNull Call<List<Item>> call, @NotNull Throwable t) {
                Log.e("fail", "Fail");
            }
        });

    }
}

class MyAdapter extends RecyclerView.Adapter<MyAdapter.VH> {
    private static final int W = 875;
    private static final int H = 492;
    private int realW;
    private int realH;
    private Context mContext;
    private List<Item> mList;

    public MyAdapter(Context context) {
        this.mContext = context;
        realW = context.getResources().getDisplayMetrics().widthPixels;
        realH = realW * H / W;
        this.mList = new ArrayList<>();
    }

    public void setmList(List<Item> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (mList.size() != 0) {
            final Item item = mList.get(position);
            holder.descriptionTextView.setText(item.getDescription());
            String text = "用户名：" + item.getNickname() + " 赞数：" + item.getLikecount();
            holder.likeTextView.setText(text);
            Glide.with(mContext).load(item.getAvatar()).into(holder.basicImageView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, Video.class);
                    intent.putExtra("url", item.getFeedurl());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onViewRecycled(VH holder) {
        if (holder != null) {
            Glide.with(mContext).clear(holder.basicImageView);
        }
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class VH extends RecyclerView.ViewHolder {

        public TextView descriptionTextView;
        public TextView likeTextView;
        public ImageView basicImageView;

        public VH(View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.description);
            basicImageView = itemView.findViewById(R.id.imageView);
            likeTextView = itemView.findViewById(R.id.likes);
            itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, realH));
        }
    }
}

class Item {

    private String _id;
    private String feedurl;
    private String nickname;
    private String description;
    private int likecount;
    private String avatar;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFeedurl() {
        return feedurl;
    }

    public void setFeedurl(String feedurl) {
        this.feedurl = feedurl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLikecount() {
        return likecount;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}

interface GetFeedInterface {
    @GET("api/invoke/video/invoke/video")
    Call<List<Item>> getFeed();
}