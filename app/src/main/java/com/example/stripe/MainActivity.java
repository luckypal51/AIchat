package com.example.stripe;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.FirebaseApp;
import com.google.firebase.vertexai.FirebaseVertexAI;
import com.google.firebase.vertexai.GenerativeModel;
import com.google.firebase.vertexai.java.GenerativeModelFutures;
import com.google.firebase.vertexai.type.Content;
import com.google.firebase.vertexai.type.GenerateContentResponse;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
private EditText editText;
private Button button;
private RecyclerView recyclerView;
private ArrayList<Data> arrayList;
Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase App
        FirebaseApp.initializeApp(this);

        // Edge-to-edge layout setup
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
       editText = findViewById(R.id.editext);
       button = findViewById(R.id.send);
       arrayList = new ArrayList<>();
       recyclerView = findViewById(R.id.recyclerView);
       adapter = new Adapter(arrayList,this);
       recyclerView.setLayoutManager(new LinearLayoutManager(this));
       recyclerView.setAdapter(adapter);
       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String msg = editText.getText().toString();
               Data a = new Data(Utile.SENDBYME,msg);
               adapter.notifyDataSetChanged();
               arrayList.add(a);
               editText.setText("");
           // Setup GenerativeModel from Firebase Vertex AI
               GenerativeModel gm = FirebaseVertexAI.getInstance()
                       .generativeModel("gemini-1.5-flash-preview-0514");
               GenerativeModelFutures model = GenerativeModelFutures.from(gm);

               // Provide a prompt to generate text
               Content prompt = new Content.Builder()
                       .addText(msg)
                       .build();

               // Call to generate content asynchronously
               ListenableFuture<GenerateContentResponse> response = model.generateContent(prompt);
               Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
                   @Override
                   public void onSuccess(GenerateContentResponse result) {
                       // Handle the generated content
                       String resultText = result.getText();
                       Log.d("data", "onSuccess: " + resultText);
                       Data b = new Data(Utile.SENDBYAI,resultText);
                       arrayList.add(b);
                       adapter.notifyDataSetChanged();
                   }

                   @Override
                   public void onFailure(Throwable t) {
                       // Handle the failure
                       t.printStackTrace();
                       Log.e("data", "Error in generating content", t);
                   }
               }, getMainExecutor());
           }
       });

    }
}
