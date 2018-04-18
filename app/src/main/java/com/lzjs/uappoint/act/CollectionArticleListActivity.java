package com.lzjs.uappoint.act;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



import com.lzjs.uappoint.R;
import com.lzjs.uappoint.adapter.CollectionArticleAdapter;
import com.lzjs.uappoint.base.BaseActivity;
import com.lzjs.uappoint.bean.Article;
import com.lzjs.uappoint.bean.CollecteArticle;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * create by yangjing 2018\4\13 17:12
 */
public class CollectionArticleListActivity extends BaseActivity {
    private static final String TAG = "CollectionArticleListAc";
    private CollecteArticle[]articles = {
            new CollecteArticle("这是一个文章标题---如何合理安排时间高效工作",R.drawable.apple_pic,"2018/09/12"),new CollecteArticle("这是一个文章标题---如何合理安排时间高效工作",R.drawable.apple_pic,"2018/09/12"),
            new CollecteArticle("这是一个文章标题---如何合理安排时间高效工作",R.drawable.apple_pic,"2018/09/12"),new CollecteArticle("这是一个文章标题---如何合理安排时间高效工作",R.drawable.apple_pic,"2018/09/12")
    };
    private List<CollecteArticle>articleList = new ArrayList<>();
    private MyAdapter adapter;
    private RecyclerView recyclerView;
    private CollecteArticle collecteArticle;
    private Toolbar toolbar;
    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_article);
        Log.d(TAG, "onCreate: is running");
        toolbar = (Toolbar)findViewById(R.id.colArticleTitle);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textView = (TextView)findViewById(R.id.toolbar_name) ;
        textView.setText("文章列表");
        textView.setTextSize(18);
        recyclerView = (RecyclerView)findViewById(R.id.recycle_view_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        initArticle();

    }
    class MyAdapter extends CollectionArticleAdapter<CollecteArticle> {

        public MyAdapter(Context ctx, List<CollecteArticle> l) {
            super(ctx, l);
        }

        @Override
        protected void delete(int position) {

        }

        @Override
        protected void add(int position) {

        }

    }
    private void initArticle(){
        articleList.clear();
        //获取收藏文章列表
        for (int i = 0;i<20;i++){
            Random random = new Random();
            int index = random.nextInt(articles.length);
            articleList.add(articles[index]);
        }
        adapter = new MyAdapter(this,articleList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CollectionArticleAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                collecteArticle = articleList.get(position);
                Intent intent = new Intent(CollectionArticleListActivity.this,CollectedArticleDetailActivity.class);
                intent.putExtra("article",collecteArticle);
                //Toast.makeText(CollectionArticleListActivity.this, "点击事件被触发,位置：" + position, Toast.LENGTH_SHORT).show();
                //转到新页面展示详细文章
                startActivity(intent);
            }
        });

        adapter.setOnItemLongClickListener(new CollectionArticleAdapter.OnItemLongClickListener() {
            @Override
            public void OnItemLongClick(View view, int position) {
                Toast.makeText(CollectionArticleListActivity.this, "点击长事件被触发,位置：" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

}