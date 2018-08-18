package com.example.i864261.erp_hackathon;

        import android.content.Intent;
        import android.location.Location;
        import android.speech.RecognizerIntent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.RadioButton;
        import android.widget.TabHost;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Shea, this is not plagiarised.

    // const
    public String BACKEND_URL = "https://samplejaxrshcpwf680bd7c.int.sap.hana.ondemand.com/samplejaxrs-hcp/api/sql";

    private static String username = null;


    // form views
    public EditText nameTextView, collarIdTextView, ageTextView, weightTextView,
            dateTextView, timeTextView, locationLatTextView, locationLongTextView,
            descriptionTextView;
    private CheckBox drug1CheckBox, drug2CheckBox, drug3CheckBox, drug4CheckBox, drug5CheckBox;
    private RadioButton maleRadioButton, femaleRadioButton;

    // Model
    private Locationer locationer;
    private SpeechToText speechToText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.username = getIntent().getStringExtra("username");
        initViews();
        // Model Init
        locationer = new Locationer(this);
        speechToText = new SpeechToText(this);
        setAutoFields();
    }


    private void setAutoFields(){
        setDateTimeFields();
        setGeoLocation();
    }

    private void setGeoLocation(){
        Location loc = locationer.getLocation();
        if (loc == null) {
            Toast.makeText(this, "Couldn't retrieve GPS coordinates", Toast.LENGTH_SHORT).show();
            return;
        }
        setLocationAttr(locationLongTextView, loc.getLongitude());
        setLocationAttr(locationLatTextView, loc.getLatitude());
    }

    private void setLocationAttr(EditText locationEditText, double val){
        String loc = Double.toString(val);
        loc = String.format(loc, "%.2f");
        locationEditText.setText(loc);
    }

    private void setDateTimeFields(){
        Calendar cal = Calendar.getInstance();
        String date = "" + cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.YEAR);
        String time = "" + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);

        dateTextView.setText(date);
        timeTextView.setText(time);
    }

    private void initViews(){
        //EditTextViews
        nameTextView = findViewById(R.id.name);
        collarIdTextView = findViewById(R.id.collarId);
        ageTextView = findViewById(R.id.age);
        weightTextView = findViewById(R.id.weight);
        dateTextView = findViewById(R.id.date);
        timeTextView = findViewById(R.id.time);
        locationLatTextView = findViewById(R.id.location_lat);
        locationLongTextView = findViewById(R.id.location_long);
        descriptionTextView = findViewById(R.id.description);

        //Checkbox
        drug1CheckBox = findViewById(R.id.drug1);
        drug2CheckBox = findViewById(R.id.drug2);
        drug3CheckBox = findViewById(R.id.drug3);
        drug4CheckBox = findViewById(R.id.drug4);
        drug5CheckBox = findViewById(R.id.drug5);

        //RadioButtons
        maleRadioButton = findViewById(R.id.male);
        femaleRadioButton = findViewById(R.id.female);
    }

    public void submit(View view) {
        Map<String, Object> dataMap = new HashMap<>();
        addEditTextFieldData(dataMap);
        String data = mapToJsonString(dataMap);
        Log.i("Data", data);
        new Requester(this, BACKEND_URL).execute(data);
        addToThePastReports(representString());
    }

    private void addEditTextFieldData(Map<String, Object> dataMap){
        dataMap.put("name", getText(nameTextView));
        dataMap.put("collarId", getText(collarIdTextView));
        dataMap.put("age", Integer.parseInt(getText(ageTextView)));
        dataMap.put("weight", Integer.parseInt(getText(weightTextView)));
        dataMap.put("time", getText(timeTextView));
        dataMap.put("date", getText(dateTextView));
        dataMap.put("longitude", getText(locationLongTextView));
        dataMap.put("latitude", getText(locationLatTextView));
        dataMap.put("drugs_given", addDrugCheckboxes());
        dataMap.put("gender", getSelectedGender());
    }

    private String getSelectedGender(){
        String gender = null;
        if (maleRadioButton.isChecked()){
            gender = "male";
        } else if (femaleRadioButton.isChecked()) {
            gender = "female";
        }
        return gender;
    }

    private List<String> addDrugCheckboxes(){
        List<String> drugsGiven = new ArrayList<>();
        addCheckedCheckbox(drugsGiven, drug1CheckBox);
        addCheckedCheckbox(drugsGiven, drug2CheckBox);
        addCheckedCheckbox(drugsGiven, drug4CheckBox);
        addCheckedCheckbox(drugsGiven, drug4CheckBox);
        addCheckedCheckbox(drugsGiven, drug5CheckBox);
        return drugsGiven;
    }

    private static void addCheckedCheckbox(List<String> checkedList, CheckBox checkbox){
        if (checkbox.isChecked()){
            checkedList.add(checkbox.getText().toString());
        }
    }
    private static String getText(EditText editTextView){
        return editTextView.getText().toString();
    }

    private static String mapToJsonString(Map<String, Object> map){
        return new JSONObject(map).toString();
    }

    public void runSpeechToText(View view){
        speechToText.run();
    }

    // Text to speech Callback
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SpeechToText.REQ_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    descriptionTextView.setText(result.get(0));
                }
                break;
            }

        }
    }

    private void resetFields(){
        resetEditText(nameTextView);
        resetEditText(collarIdTextView);
        resetEditText(ageTextView);
        resetEditText(weightTextView);
        resetEditText(descriptionTextView);
        drug1CheckBox.setChecked(false);
        drug2CheckBox.setChecked(false);
        drug3CheckBox.setChecked(false);
        drug4CheckBox.setChecked(false);
        drug5CheckBox.setChecked(false);
    }

    private static void resetEditText(EditText view){
        view.setText("");
    }

    private static void addToThePastReports(String reportInfo){
        List<String> pastReportsForUser = PastReports.reports.get(username);
        pastReportsForUser.add(reportInfo);
        // Maybe I need to set list back to the hashmap here.
    }

    private String representString(){
        String data =  "Name: " + getText(nameTextView) + "\nCollarId: " + getText(collarIdTextView) + "\n" +
                "Date: " + getText(dateTextView) + "\nTime: " + getText(timeTextView) + "\nDescription: " + getText(descriptionTextView) + "\n\n";
        return data;
    }
}


// C'mon Shea this is not plagiarised. Stop looking.
