package com.thorwei.zz_alarmclock;

import static com.thorwei.zz_alarmclock.MainActivity.TAG;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Denys on 27.01.2017.
 */
public class RingActivity extends AppCompatActivity {

    RecyclerView rv_ringList;
    private List<String> ringList;
    private AlarmClockLab alarmClockLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_ring);
        rv_ringList = (RecyclerView)findViewById(R.id.ring_list);

        alarmClockLab = new AlarmClockBuilder().builderLab(0);

        ringList = showRingList(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.scrollToPositionWithOffset(alarmClockLab.ringPosition, 100);
        rv_ringList.setLayoutManager(linearLayoutManager);
        rv_ringList.setAdapter(new RingAdapter());
    }

    private List<String> showRingList(Context context) {
        List<String> ringNameList = new ArrayList<>();
        RingtoneManager ringtoneManager = new RingtoneManager(context);
        ringtoneManager.setType(RingtoneManager.TYPE_ALARM);
        Cursor cursor = ringtoneManager.getCursor();
        while (cursor.moveToNext()) {
            String ringName = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            ringNameList.add(ringName);
        }
        return ringNameList;
    }

    private class RingAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(RingActivity.this).inflate(R.layout.ring_list_item, parent, false);
            return new RingViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((RingViewHolder) holder).initView(position);
        }

        @Override
        public int getItemCount() {
            return ringList.size();
        }
    }


    private class RingViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout ringView;
        private TextView ringText;

        public RingViewHolder(View itemView) {
            super(itemView);
            ringView = (RelativeLayout) itemView.findViewById(R.id.id_ring_item);
            ringText = (TextView) itemView.findViewById(R.id.ring_list_text);
        }

        public void initView(final int position) {
            String ring = ringList.get(position);
            ringText.setText(ring);
            if(ring.equals(alarmClockLab.ring))
                ringText.setTextColor(ContextCompat.getColor(RingActivity.this, R.color.colorRed_500));
            else
                ringText.setTextColor(ContextCompat.getColor(RingActivity.this, R.color.colorBlack));

            ringView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alarmClockLab.setRingPosition(position);
                    alarmClockLab.setRing(ringList.get(position));

                    getIntent().putExtra("position", position);
                    getIntent().putExtra("ring", ringList.get(position));
                    setResult(RESULT_OK, getIntent());
                    finish();
                }
            });
        }
    }
}
