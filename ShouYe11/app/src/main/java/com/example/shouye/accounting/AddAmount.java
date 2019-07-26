package com.example.shouye.accounting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shouye.R;

public class AddAmount extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button button_save;
    private Button back;
    private EditText add_price;
    private EditText add_amount;
    private EditText add_content;
    private Spinner add_type;
    private int resultCode = 2;
    private int type = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accounting_add_amount);
        button_save = (Button)findViewById(R.id.button_save);
        add_price = (EditText) findViewById(R.id.add_price);
        add_amount = (EditText)findViewById(R.id.add_num);
        add_content = (EditText)findViewById(R.id.add_content);
        add_type = (Spinner)findViewById(R.id.accounting_type_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.accounting_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_type.setAdapter(adapter);
        add_type.setOnItemSelectedListener(this);

        Intent intent = getIntent();
        String amount = (String)intent.getStringExtra("amount");
        String content = (String)intent.getSerializableExtra("content");
        String price = (String)intent.getSerializableExtra("price");
        final String id = (String)intent.getSerializableExtra("id");

        int passedType = 0;
        if (intent.getSerializableExtra("type") != null) {
            passedType = Integer.parseInt(intent.getSerializableExtra("type").toString());
        }

        if (passedType != 0) {
            add_type.setSelection(passedType < 0 ? 1 : 0);
        }

        if (content!=null) {
            add_content.setText(content);
        }
        if (price!=null){
            add_price.setText(price);
        }
        if(amount!=null){
            add_amount.setText(amount);
        }

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amount amount = new Amount();
                if (id != null) {
                    amount.setId(id);
                }
                amount.setType(type);
                amount.setAmount(Integer.parseInt(add_amount.getText().toString()));
                amount.setPrice(Double.parseDouble(add_price.getText().toString()));
                amount.setContent(add_content.getText().toString());
                amount.save(AddAmount.this);
                Toast.makeText(AddAmount.this, "账目保存成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        back = (Button)findViewById(R.id.back3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(0,intent);
                AddAmount.this.finish();
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        if (parent.getItemAtPosition(pos).equals("收入")) {
            type = 1;
        } else {
            type = -1;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }
}
