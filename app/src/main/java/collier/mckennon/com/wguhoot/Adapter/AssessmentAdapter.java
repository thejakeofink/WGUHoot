package collier.mckennon.com.wguhoot.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import collier.mckennon.com.wguhoot.Model.Assessment;
import collier.mckennon.com.wguhoot.R;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentVH> {
    Context context;
    List<Assessment> assessments;

    OnItemClickListener clickListener;

    public AssessmentAdapter(Context context, List<Assessment> assessments) {
        this.context = context;
        this.assessments = assessments;
    }

    @Override
    public AssessmentVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assessment_item, parent, false);
        AssessmentVH viewHolder = new AssessmentVH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AssessmentVH holder, int position) {
        holder.title.setText(assessments.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return assessments.size();
    }

    class AssessmentVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;

        public AssessmentVH(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.assessmentTitleListItem);
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
