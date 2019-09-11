package com.example.imuhelper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imuhelper.R;
import com.example.imuhelper.adapter.TermListRvAdapter;
import com.example.imuhelper.bean.TermBean;
import com.example.imuhelper.db.TermDBHelper;
import com.example.imuhelper.utils.IntentTool;

import java.util.List;

public class TermListActivity extends AppCompatActivity {

    private RecyclerView contentRv;
    private TermListRvAdapter adapter;
    private List<TermBean> list;
    private int changePosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        init();
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.termList_toolbar);
        setSupportActionBar(toolbar);

        contentRv = findViewById(R.id.termList_content_Rv);

        TermDBHelper termDBHelper = new TermDBHelper(this, null);
        list = termDBHelper.getAllTerm();
        adapter = new TermListRvAdapter(list, this);

        adapter.setContentLongClick(new TermListRvAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClick(int position) {
                Intent intent = new Intent(getBaseContext(), AddTermActivity.class);
                intent.putExtra("id", list.get(position).getId());
                changePosition = position;
                startActivityForResult(intent, IntentTool.TermList_To_AddTerm_EditTerm);
            }
        });

        adapter.setOnContentClick(new TermListRvAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                setResult(IntentTool.RESULT_OK);
                finish();
            }
        });

        contentRv.setAdapter(adapter);
        contentRv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_termlist_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.termList_addTerm_menu:
                Intent intent = new Intent(getBaseContext(), AddTermActivity.class);
                startActivityForResult(intent, IntentTool.TermList_To_AddTerm_NewTerm);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case IntentTool.TermList_To_AddTerm_NewTerm:
                if (resultCode == IntentTool.RESULT_OK) {
                    String name = data.getStringExtra("name");
                    TermBean termBean = new TermDBHelper(this, null).getTerm(name);
                    adapter.addNewTerm(termBean);
                }
                break;

            case IntentTool.TermList_To_AddTerm_EditTerm:
                if (resultCode == IntentTool.RESULT_OK) {
                    String name = data.getStringExtra("name");
                    TermDBHelper termDBHelper = new TermDBHelper(getBaseContext(), null);
                    list.set(changePosition, termDBHelper.getTerm(name));
                    adapter.notifyItemChanged(changePosition);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        setResult(IntentTool.RESULT_OK);
        super.onBackPressed();
    }
}
