package com.example.shouye.accounting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.shouye.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ShowAmount extends AppCompatActivity {
    private Button back;
    private TextView aContent;
    private TextView aAmount;
    private TextView aPrice;
    private TextView aTime;
    private Button edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accounting_show_amount);
        aAmount = (TextView)findViewById(R.id.show_number);
        aContent = (TextView)findViewById(R.id.show_content);
        aPrice = (TextView)findViewById(R.id.show_price);
        aTime = (TextView)findViewById(R.id.show_time);

        Intent intent = getIntent();
        int amount = Integer.parseInt(intent.getSerializableExtra("amount").toString());
        String content = (String)intent.getSerializableExtra("content");
        double price = Double.parseDouble(intent.getSerializableExtra("price").toString());
        final String id = (String)intent.getSerializableExtra("id");
        final int type = Integer.parseInt(intent.getSerializableExtra("type").toString());
        final String time = intent.getSerializableExtra("time").toString();

        aAmount.setText(String.valueOf(amount));
        aContent.setText(content);
        aPrice.setText(String.valueOf(price));
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        aTime.setText(timeFormat.format(new Date(time)));

        back = (Button)findViewById(R.id.back4);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edit = (Button)findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ShowAmount.this,AddAmount.class);
                intent.putExtra("id", id);
                intent.putExtra("time", time);
                intent.putExtra("type", type);
                intent.putExtra("amount", aAmount.getText().toString());
                intent.putExtra("price", aPrice.getText().toString());
                intent.putExtra("content",aContent.getText().toString());
                startActivity(intent);
                finish();
            }
        });

    }
}
