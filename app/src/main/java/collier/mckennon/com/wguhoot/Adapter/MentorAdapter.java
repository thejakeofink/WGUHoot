package collier.mckennon.com.wguhoot.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import collier.mckennon.com.wguhoot.Model.Mentor;
import collier.mckennon.com.wguhoot.R;

public class MentorAdapter extends RecyclerView.Adapter<MentorAdapter.MentorVH> {
    Context context;
    List<Mentor> mentors;

    OnItemClickListener clickListener;

    public MentorAdapter(Context context, List<Mentor> mentors) {
        this.context = context;
        this.mentors = mentors;
    }

    @Override
    public MentorVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mentor_item, parent, false);
        MentorVH viewHolder = new MentorVH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MentorVH holder, int position) {
        holder.name.setText(mentors.get(position).getName());
        holder.phone.setText(mentors.get(position).getPhone());
        holder.email.setText(mentors.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return mentors.size();
    }

    class MentorVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name, phone, email;

        public MentorVH(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.mentorNameListItem);
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
