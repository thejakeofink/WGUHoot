package collier.mckennon.com.wguhoot.Adapter;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import collier.mckennon.com.wguhoot.Model.Assessment;
import collier.mckennon.com.wguhoot.R;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentVH> {
    Context context;
    public List<AssessmentItem> assessments;
    boolean hasCheckboxes;

    OnItemClickListener clickListener;

    public AssessmentAdapter(Context context, List<AssessmentItem> assessments, boolean hasCheckboxes) {
        this.context = context;
        this.assessments = assessments;
        this.hasCheckboxes = hasCheckboxes;
    }

    @Override
    public AssessmentVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assessment_item, parent, false);
        AssessmentVH viewHolder = new AssessmentVH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AssessmentVH holder, final int position) {
        holder.title.setText(assessments.get(position).assessment.getTitle());
        if (hasCheckboxes) {
            holder.boxy.setVisibility(View.VISIBLE);
            holder.boxy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    assessments.get(position).isChecked = isChecked;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return assessments.size();
    }

    class AssessmentVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        CheckBox boxy;

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

    public static class AssessmentItem {
        public Assessment assessment;
        public Boolean isChecked;

        public AssessmentItem(Assessment assessment, Boolean isChecked) {
            this.assessment = assessment;
            this.isChecked = isChecked;
        }
    }
}