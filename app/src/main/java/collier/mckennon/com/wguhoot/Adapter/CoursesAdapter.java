package collier.mckennon.com.wguhoot.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import collier.mckennon.com.wguhoot.Model.Course;
import collier.mckennon.com.wguhoot.R;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CourseVH> {
    Context context;
    List<Course> courses;

    OnItemClickListener clickListener;

    public CoursesAdapter(Context context, List<Course> courses) {
        this.context = context;
        this.courses = courses;
    }

    @Override
    public CourseVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
        CourseVH viewHolder = new CourseVH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CourseVH holder, int position) {
        holder.title.setText(courses.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    class CourseVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;

        public CourseVH(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.courseListItem);
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