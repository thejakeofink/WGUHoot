package collier.mckennon.com.wguhoot.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import collier.mckennon.com.wguhoot.Model.Term;
import collier.mckennon.com.wguhoot.R;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermVH> {
    Context context;
    List<Term> terms;

    OnItemClickListener clickListener;

    public TermAdapter(Context context, List<Term> terms) {
        this.context = context;
        this.terms = terms;
    }

    @Override
    public TermVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.term_item, parent, false);
        TermVH viewHolder = new TermVH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TermVH holder, int position) {
        holder.title.setText(terms.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return terms.size();
    }

    class TermVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;

        public TermVH(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.termListItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
}