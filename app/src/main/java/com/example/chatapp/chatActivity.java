package com.example.chatapp;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chatapp.databinding.ActivityChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class chatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    String recieverId, recieverName;
    DatabaseReference databaseReferenceSender;
    DatabaseReference databaseReferenceReciever;
    String senderRoom, recieverRoom;
    MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(this.getColor(R.color.blue));

        recieverId = getIntent().getStringExtra("id");
        recieverName = getIntent().getStringExtra("name");

        senderRoom = FirebaseAuth.getInstance().getUid()+recieverId;
        recieverRoom = recieverId+FirebaseAuth.getInstance().getUid();

        messageAdapter = new MessageAdapter(this);
        binding.recycler.setAdapter(messageAdapter);
        binding.recycler.setLayoutManager(new LinearLayoutManager (this));

        databaseReferenceSender = FirebaseDatabase.getInstance().getReference("chats").child(senderRoom);
        databaseReferenceReciever = FirebaseDatabase.getInstance().getReference("chats").child(recieverRoom);

        Query query = databaseReferenceSender.orderByChild("timeStamp");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messageAdapter.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                    messageAdapter.add(messageModel);
                    binding.recycler.scrollToPosition(messageAdapter.getItemCount()-1); // refresh the screen to the new message
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message= binding.messageId.getText().toString();
                if (message.trim().length()>0){
                    sendMessage(message);
                    binding.messageId.setText("");
                }
            }
        });

        binding.personName.setText(recieverName);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void sendMessage(String message) {
        String messageId = UUID.randomUUID().toString();
        long timeStamp = System.currentTimeMillis();
        MessageModel messageModel = new MessageModel(messageId, FirebaseAuth.getInstance().getUid(),message,timeStamp);
        messageAdapter.add(messageModel);
        databaseReferenceSender
                .child(messageId)
                .setValue(messageModel);
        databaseReferenceReciever
                .child(messageId)
                .setValue(messageModel);
    }
}