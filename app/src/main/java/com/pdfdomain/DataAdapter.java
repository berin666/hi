package com.pdfdomain;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

    private ArrayList<Book> books;
    private Context context;

    public DataAdapter(ArrayList<Book> books, Context context) {
        this.books = books;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        final Book book = books.get(i);

        myViewHolder.tv_author.setText(book.getAuthor());
        myViewHolder.tv_title.setText(book.getTitle());
        myViewHolder.tv_year.setText(book.getYear());
        myViewHolder.tv_extension.setText(book.getExtension());
        
        myViewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class)
                        .putExtra("book", book)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    public void addItem(Book book){
        books.add(book);
        //notifyItemInserted(books.size() - 1);
        notifyDataSetChanged();
        book.save();
    }

    public void removeItem(Book book){
        for(Book b : books){
            if(b.getMD5()==book.getMD5()){
                books.remove(b);
                Book.deleteAll(Book.class, "MD5=?", b.getMD5());
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_title,tv_author,tv_year, tv_extension;
        private RelativeLayout relativeLayout;

        public MyViewHolder(View view) {
            super(view);

            relativeLayout = view.findViewById(R.id.card);
            tv_title = view.findViewById(R.id.tv_title);
            tv_author = view.findViewById(R.id.tv_author);
            tv_year = view.findViewById(R.id.tv_year);
            tv_extension = view.findViewById(R.id.tv_extension);
        }

    }

}