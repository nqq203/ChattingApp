package com.main.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.main.activities.MessageActivity;
import com.main.activities.ProfileView;
import com.main.adapters.ChatAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends DialogFragment {
    RecyclerView chatContainer;
    Button backBtn;

    TextView sendBtn, infoBtn;
    EditText editTextMsg;
    ImageView userImage;

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

        backBtn = view.findViewById(R.id.back_chat);
        sendBtn = view.findViewById(R.id.button_send);
        editTextMsg = view.findViewById(R.id.edit_text_message);
        infoBtn = view.findViewById(R.id.button_info);
        userImage = view.findViewById(R.id.user_chat_image);

        userImage.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileView.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstaceState) {
        super.onViewCreated(view, savedInstaceState);
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        Glide.with(getContext()).load(getResources().getDrawable(R.drawable.lisa)).apply(requestOptions).into(userImage);

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

            backBtn.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), MessageActivity.class);
                startActivity(intent);
                getActivity().finish();
            });

            infoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Hiển thị DialogFragment
                    DialogFragment newFragment = new InfoDialogFragment();
                    newFragment.show(getActivity().getSupportFragmentManager(), "INFO_DIALOG");
                }
            });

            ChatAdapter chatAdapter = new ChatAdapter(chatList, getActivity());

            sendBtn.setOnClickListener(v -> {
                String message = editTextMsg.getText().toString().trim();
                if (!message.isEmpty()) {
                    chatList.add(message);
                    chatAdapter.notifyDataSetChanged();

                    editTextMsg.setText("");
                    chatContainer.scrollToPosition(chatList.size() - 1);
                }
            });

            populateChats(chatContainer, chatAdapter);
        }
    }

    public void populateChats(RecyclerView container, ChatAdapter chatAdapter) {
        container.setLayoutManager(new LinearLayoutManager(getContext()));
        container.setAdapter(chatAdapter);
    }
}
