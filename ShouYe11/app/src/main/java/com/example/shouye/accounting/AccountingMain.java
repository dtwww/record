package com.example.shouye.accounting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.shouye.R;
import com.example.shouye.util.Persister;

import java.util.List;

public class AccountingMain extends AppCompatActivity {
    private Button back;
    private List<Amount> allAmounts = null;
    private RecyclerView list_amount;
    private TextView grossIncomeText;
    private TextView netIncomeText;
    private TextView expenseText;
    private AmountAdapter mAdapter = null;
    private FloatingActionButton fab_add;
    private int requestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accounting_main);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        list_amount = (RecyclerView) findViewById(R.id.list_amount);
        grossIncomeText = (TextView) findViewById(R.id.gross_income);
        expenseText = (TextView) findViewById(R.id.expense);
        netIncomeText = (TextView) findViewById(R.id.net_income);

        list_amount.addOnItemTouchListener(new RecyclerViewClickListener(this, new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("amount", allAmounts.get(position).getAmount());
                intent.putExtra("content", allAmounts.get(position).getContent());
                intent.putExtra("price", allAmounts.get(position).getPrice());
                intent.putExtra("id", allAmounts.get(position).getId());
                intent.putExtra("type", allAmounts.get(position).getType());
                intent.putExtra("time", allAmounts.get(position).getTime());
                intent.setClass(AccountingMain.this,ShowAmount.class);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(AccountingMain.this, android.R.style.Theme_Material_Dialog_Alert);
                builder.setTitle("删除账目")
                        .setMessage("确定删除账目?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Persister persister= Persister.getInstance(AccountingMain.this);
                                Amount amount = allAmounts.get(position);
                                persister.deleteAmount(amount.getId());
                                AccountingMain.this.initData();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }));
        list_amount.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        initData();

        list_amount.setHasFixedSize(true);
        list_amount.setLayoutManager(layoutManager);
        fab_add = (FloatingActionButton)findViewById(R.id.add_fab);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AccountingMain.this,AddAmount.class);
                startActivityForResult(intent,requestCode);
            }
        });

        back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountingMain.this.finish();
            }
        });

    }

    @Override
    protected void onResume() {
        this.initData();
        super.onResume();
    }

    private void initData(){
        allAmounts = Amount.getAllAmounts(AccountingMain.this);
        mAdapter = new AmountAdapter(allAmounts,AccountingMain.this);
        list_amount.setAdapter(mAdapter);

        double grossIncome = 0, expense = 0;
        for (int i = 0; i < allAmounts.size(); i ++) {
            Amount amount = allAmounts.get(i);
            if (amount.getType() > 0) {
                grossIncome += amount.getPrice() * amount.getAmount();
            } else {
                expense += amount.getPrice() * amount.getAmount();
            }
        }
        grossIncomeText.setText(String.format ("%.2f", grossIncome));
        expenseText.setText(String.format ("%.2f", expense));
        netIncomeText.setText(String.format ("%.2f", grossIncome - expense));
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent intent){
        if(resultCode==2) {
            String amount = (String) intent.getSerializableExtra("Amount");
            String content = (String) intent.getSerializableExtra("Content");
            String number = (String) intent.getSerializableExtra("Number");
        }
        else return;
    }
}
