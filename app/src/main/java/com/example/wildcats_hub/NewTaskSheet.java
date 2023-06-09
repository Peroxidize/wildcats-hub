package com.example.wildcats_hub;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewTaskSheet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewTaskSheet extends BottomSheetDialogFragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextInputEditText taskNameEditText;
    private TextInputEditText taskDescriptionEditText;
    private AutoCompleteTextView priorityMenuAutoCompleteTextView;
    private TextInputEditText taskTagsEditText;
    private TextInputEditText taskduedate;
    private TextInputEditText taskduetime;
    private MaterialButton saveButton;
    private String taskName;
    private String taskDescription;
    private String taskTag;
    private String taskPriorityLevel;
    private String taskDueDate;
    private String taskDueTime;


    public NewTaskSheet() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewTaskSheet.
     */
    // TODO: Rename and change types and number of parameters
    public static NewTaskSheet newInstance(String param1, String param2) {
        NewTaskSheet fragment = new NewTaskSheet();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_task_sheet, container, false);

        taskNameEditText = rootView.findViewById(R.id.taskname);
        taskDescriptionEditText = rootView.findViewById(R.id.taskdescription);
        priorityMenuAutoCompleteTextView = rootView.findViewById(R.id.drop_priority_menu);
        taskTagsEditText = rootView.findViewById(R.id.tasktags);
        saveButton = rootView.findViewById(R.id.loginButton);
        taskduedate = rootView.findViewById(R.id.taskduedate);
        taskduetime = rootView.findViewById(R.id.taskduetime);
        taskduedate.setInputType(InputType.TYPE_NULL);
        taskduetime.setInputType(InputType.TYPE_NULL);


        saveButton.setOnClickListener(this);
        taskduedate.setOnClickListener(this);
        taskduetime.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // yyyy-MM-dd
            // HH:mm:ss
            // Save Button
//            private String taskName;
//            private String taskDescription;
//            private String taskPriorityLevel;
//            private String taskTag;
//            private String taskDueDate;
//            private String taskDueTime;
            case R.id.loginButton:
                taskName = taskNameEditText.getText().toString();
                taskDescription = taskDescriptionEditText.getText().toString();
                taskPriorityLevel = priorityMenuAutoCompleteTextView.getText().toString();
                taskTag = taskTagsEditText.getText().toString();
                taskDueDate = taskduedate.getText().toString();
                taskDueTime = taskduetime.getText().toString();

                if (taskName.isEmpty() || taskDescription.isEmpty() || taskPriorityLevel.isEmpty()
                        || taskDueDate.isEmpty() || taskDueTime.isEmpty()) {
                    Toast.makeText(getContext(), "Textfields must be non-empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                TaskDatabaseHelper db = new TaskDatabaseHelper(getActivity());
                db.addTask(taskName.trim(), taskDescription.trim(), taskPriorityLevel.trim(),
                        taskTag.trim(), taskDueDate.trim(), taskDueTime.trim());
                TaskMaster parentActivity = (TaskMaster) getActivity();
                parentActivity.reloadModels();
                break;
            case R.id.taskduedate:
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthofYear, int dayOfMonth) {
                                String month = "";
                                if (String.valueOf(monthofYear).length() == 1) {
                                    month = "0" + (monthofYear+1);
                                } else {
                                    month = String.valueOf(monthofYear+1);
                                }
                                String selectedDate = year + "-" + month + "-" + dayOfMonth;
                                taskduedate.setText(selectedDate);
                            }
                        },
                        year,month,day
                );
                datePickerDialog.show();
                break;
            case R.id.taskduetime:

                int hour = 0, minute = 0;
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        taskduetime.setText(String.format(Locale.getDefault(),
                                "%02d-%02d-00", selectedHour, selectedMinute));
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), onTimeSetListener, hour, minute, false);

                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
                break;
        }
    }
}