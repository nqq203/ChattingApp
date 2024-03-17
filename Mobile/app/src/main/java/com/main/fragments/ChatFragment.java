package com.main.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.main.activities.MessageActivity;
import com.main.adapters.ChatAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends DialogFragment {
    RecyclerView chatContainer;
    Button backBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        View view = inflater.inflate(R.layout.chat_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstaceState) {
        super.onViewCreated(view, savedInstaceState);

//      Get data from Bundle
        if (getArguments() != null) {
            int selectedPosition = getArguments().getInt("selected_position", -1);
            chatContainer = view.findViewById(R.id.chat_container);
//          Handle data with position which already selected
            List<String> chatList = new ArrayList<>();
//          Dummy data
            String msg1 = "Đang đi đâu vậy";
            String msg2 = "Tôi đang đi kiếm bạn đây?";

            chatList.add(msg1);
            chatList.add(msg2);

            populateChats(chatContainer, chatList);
            backBtn = view.findViewById(R.id.back_chat);
            backBtn.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), MessageActivity.class);
                startActivity(intent);
                getActivity().finish();
            });
        }
    }

    public void populateChats(RecyclerView container, List<String> chatMessage) {
        container.setLayoutManager(new LinearLayoutManager(getContext()));
        container.setAdapter(new ChatAdapter(chatMessage, getActivity()));
    }
}
