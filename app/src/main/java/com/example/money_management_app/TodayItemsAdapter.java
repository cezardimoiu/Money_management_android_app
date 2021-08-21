package com.example.money_management_app;

import static com.example.money_management_app.MainActivity.url_firebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.TokenData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TodayItemsAdapter extends RecyclerView.Adapter<TodayItemsAdapter.ViewHolder> {

    private Context mContext;
    private List<Data> myDataList;

    private String post_key = "";
    private String item = "";
    private int amount = 0;
    private String note = "";

    public TodayItemsAdapter(Context mContext, List<Data> myDataList) {
        this.mContext = mContext;
        this.myDataList = myDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.retrieve_layout, parent, false);
        return new TodayItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Data data = myDataList.get(position);

        holder.item.setText("Item: " + data.getItem());
        holder.amount.setText("Amount: " + data.getAmount());
        holder.date.setText("On: " + data.getDate());
        holder.notes.setText("Note: " + data.getNotes());


        switch(data.getItem()) {
            case "Transport":
                holder.imageView.setImageResource(R.drawable.ic_transport);
                break;
            case "Groceries":
                holder.imageView.setImageResource(R.drawable.ic_groceries);
                break;
            case "House":
                holder.imageView.setImageResource(R.drawable.ic_house);
                break;
            case "Entertainment":
                holder.imageView.setImageResource(R.drawable.ic_entertainment);
                break;
            case "Education":
                holder.imageView.setImageResource(R.drawable.ic_education);
                break;
            case "Economies":
                holder.imageView.setImageResource(R.drawable.ic_economies);
                break;
            case "Donations":
                holder.imageView.setImageResource(R.drawable.ic_donation);
                break;
            case "Investment":
                holder.imageView.setImageResource(R.drawable.ic_investment);
                break;
            case "Personal":
                holder.imageView.setImageResource(R.drawable.ic_personal);
                break;
            case "Other":
                holder.imageView.setImageResource(R.drawable.ic_other);
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post_key = data.getId();
                item = data.getItem();
                amount = data.getAmount();
                note = data.getNotes();
                updateData();
            }
        });
    }

    private void updateData() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View myView = inflater.inflate(R.layout.update_layout, null);

        myDialog.setView(myView);
        final AlertDialog dialog = myDialog.create();

        final TextView myItem = myView.findViewById(R.id.itemName);
        final EditText myAmount = myView.findViewById(R.id.amount);
        final EditText myNotes = myView.findViewById(R.id.note);

        myItem.setText(item);

        myAmount.setText(String.valueOf(amount));
        myAmount.setSelection(String.valueOf(amount).length());

        myNotes.setText(note);
        myNotes.setSelection(note.length());

        Button btnDelete = myView.findViewById(R.id.btnDelete);
        Button btnUpdate = myView.findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = Integer.parseInt(myAmount.getText().toString());
                note = myNotes.getText().toString();

                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar calendar = Calendar.getInstance();
                String date = dateFormat.format(calendar.getTime());

                MutableDateTime epoch = new MutableDateTime();
                epoch.setDate(0);
                DateTime now = new DateTime();
                Weeks weeks = Weeks.weeksBetween(epoch, now);
                Months months = Months.monthsBetween(epoch, now);

                Data data = new Data(item, date, post_key, note, amount, months.getMonths(), weeks.getWeeks());

                DatabaseReference reference = FirebaseDatabase.getInstance(url_firebase).getReference("expenses").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                reference.child(post_key).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mContext, "Spending item updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, task.getException().toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance(url_firebase).getReference("expenses").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                reference.child(post_key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mContext, "Spending item deleted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, task.getException().toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item, amount, date, notes;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.item);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
            notes = itemView.findViewById(R.id.note);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }
}
