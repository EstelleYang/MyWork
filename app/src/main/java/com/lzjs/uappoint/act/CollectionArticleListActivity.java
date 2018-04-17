package com.lzjs.uappoint.act;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.lzjs.uappoint.R;
import com.lzjs.uappoint.adapter.CollectionArticleAdapter;
import com.lzjs.uappoint.base.BaseActivity;
import com.lzjs.uappoint.bean.Article;
import com.lzjs.uappoint.bean.CollecteArticle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * create by yangjing 2018\4\13 17:12
 */
public class CollectionArticleListActivity extends BaseActivity {
    private CollecteArticle[]articles = {
            new CollecteArticle("这是一个文章标题---如何合理安排时间高效工作",R.drawable.apple_pic,"2018/09/12"),new CollecteArticle("这是一个文章标题---如何合理安排时间高效工作",R.drawable.apple_pic,"2018/09/12"),
            new CollecteArticle("这是一个文章标题---如何合理安排时间高效工作",R.drawable.apple_pic,"2018/09/12"),new CollecteArticle("这是一个文章标题---如何合理安排时间高效工作",R.drawable.apple_pic,"2018/09/12")
    };
    private List<CollecteArticle>articleList = new ArrayList<>();
    private MyAdapter adapter;
    private RecyclerView recyclerView;
    private Article article;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_article);

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
                Toast.makeText(CollectionArticleListActivity.this, "点击事件被触发,位置：" + position, Toast.LENGTH_SHORT).show();
                //转到新页面展示详细文章
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