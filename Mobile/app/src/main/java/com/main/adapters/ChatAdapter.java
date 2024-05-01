package com.main.adapters;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.group4.matchmingle.R;
import com.group4.matchmingle.databinding.ImageViewerFragmentBinding;
import com.main.activities.ChatActivity;
import com.main.activities.UserSessionManager;
import com.main.entities.ChatList;
import com.main.fragments.ImageViewerFragment;

import java.io.IOException;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    public interface OnImageClickListener {
        void showImage(String imageUrl);
    }
    private OnImageClickListener listener;
    private List<ChatList> chatList;
    private final Context context;
    private UserSessionManager sessionManager;
    private String myPhone;
    private MediaPlayer mediaPlayer;

    public void setOnImageClickListener(OnImageClickListener listener) {
        this.listener = listener;
    }

    public ChatAdapter(List<ChatList> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;
        this.sessionManager = new UserSessionManager(context.getApplicationContext());
        this.myPhone = this.sessionManager.getUserDetails().get(UserSessionManager.KEY_PHONE_NUMBER);
    }
    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {
        ChatList item = chatList.get(position);
        Log.d(TAG, "onBindViewHolder: " + item.getType());

        if (item.getType().equals("text")) {
            if (item.getMobile().equals(myPhone)) {
                holder.myLayout.setVisibility(View.VISIBLE);
                holder.guestLayout.setVisibility(View.GONE);
                holder.myVoiceLayout.setVisibility(View.GONE);
                holder.guestVoiceLayout.setVisibility(View.GONE);
                holder.myImageLayout.setVisibility(View.GONE);
                holder.guestImageLayout.setVisibility(View.GONE);
                holder.myIconVoice.setVisibility(View.GONE);
                holder.guestIconVoice.setVisibility(View.GONE);
                holder.guestVoice.setVisibility(View.GONE); // guest's seekbar
                holder.myVoice.setVisibility(View.GONE); //my seekbar
                holder.myMessage.setText(item.getMessage());
            }
            else {
                holder.myLayout.setVisibility(View.GONE);
                holder.guestLayout.setVisibility(View.VISIBLE);
                holder.myVoiceLayout.setVisibility(View.GONE);
                holder.guestVoiceLayout.setVisibility(View.GONE);
                holder.myImageLayout.setVisibility(View.GONE);
                holder.guestImageLayout.setVisibility(View.GONE);
                holder.myIconVoice.setVisibility(View.GONE);
                holder.guestIconVoice.setVisibility(View.GONE);
                holder.guestVoice.setVisibility(View.GONE); // guest's seekbar
                holder.myVoice.setVisibility(View.GONE); //my seekbar
                holder.guestMessage.setText(item.getMessage());
            }
        }
        else if (item.getType().equals("audio")) {
            if (item.getMobile().equals(myPhone)) {
                holder.myLayout.setVisibility(View.GONE);
                holder.guestLayout.setVisibility(View.GONE);
                holder.myVoiceLayout.setVisibility(View.VISIBLE);
                holder.guestVoiceLayout.setVisibility(View.GONE);
                holder.myImageLayout.setVisibility(View.GONE);
                holder.guestImageLayout.setVisibility(View.GONE);
                holder.myIconVoice.setVisibility(View.VISIBLE);
                holder.guestIconVoice.setVisibility(View.GONE);
                holder.guestVoice.setVisibility(View.GONE); // guest's seekbar
                holder.myVoice.setVisibility(View.VISIBLE); //my seekbar

                holder.myIconVoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "hello 1 2 3", Toast.LENGTH_SHORT).show();
                        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                            stopAudio();  // Dừng phát nếu đang phát
                        } else {
                            playAudio(item.getMessage());  // Phát file âm thanh
                        }
                    }
                });
            }
            else {
                holder.myLayout.setVisibility(View.GONE);
                holder.guestLayout.setVisibility(View.GONE);
                holder.myVoiceLayout.setVisibility(View.GONE);
                holder.guestVoiceLayout.setVisibility(View.VISIBLE);
                holder.myImageLayout.setVisibility(View.GONE);
                holder.guestImageLayout.setVisibility(View.GONE);
                holder.myIconVoice.setVisibility(View.GONE);
                holder.guestIconVoice.setVisibility(View.VISIBLE);
                holder.guestVoice.setVisibility(View.VISIBLE); // guest's seekbar
                holder.myVoice.setVisibility(View.GONE); //my seekbar

                holder.guestIconVoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                            stopAudio();  // Dừng phát nếu đang phát
                        } else {
                            playAudio(item.getMessage());  // Phát file âm thanh
                        }
                    }
                });
            }
        }
        else if (item.getType().equals("image")) {
            if (item.getMobile().equals(myPhone)) {
                holder.myLayout.setVisibility(View.GONE);
                holder.guestLayout.setVisibility(View.GONE);
                holder.myVoiceLayout.setVisibility(View.GONE);
                holder.guestVoiceLayout.setVisibility(View.GONE);
                holder.myImageLayout.setVisibility(View.VISIBLE);
                holder.guestImageLayout.setVisibility(View.GONE);
                holder.myIconVoice.setVisibility(View.GONE);
                holder.guestIconVoice.setVisibility(View.GONE);
                holder.guestVoice.setVisibility(View.GONE); // guest's seekbar
                holder.myVoice.setVisibility(View.GONE); //my seekbar
                Glide.with(context).load(item.getMessage()).transform(new CenterCrop(), new RoundedCorners(15)).into(holder.myImageItem);

            }
            else {
                holder.myLayout.setVisibility(View.GONE);
                holder.guestLayout.setVisibility(View.GONE);
                holder.myVoiceLayout.setVisibility(View.GONE);
                holder.guestVoiceLayout.setVisibility(View.GONE);
                holder.myImageLayout.setVisibility(View.GONE);
                holder.guestImageLayout.setVisibility(View.VISIBLE);
                holder.myIconVoice.setVisibility(View.GONE);
                holder.guestIconVoice.setVisibility(View.GONE);
                holder.guestVoice.setVisibility(View.GONE); // guest's seekbar
                holder.myVoice.setVisibility(View.GONE); //my seekbar
                holder.guestMessage.setText(item.getMessage());
                Glide.with(context).load(item.getMessage()).transform(new CenterCrop(), new RoundedCorners(15)).into(holder.guestImageItem);
            }
            holder.myImageItem.setOnClickListener(v -> listener.showImage(item.getMessage()));
            holder.guestImageItem.setOnClickListener(v -> listener.showImage(item.getMessage()));
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public void updateChatLists(List<ChatList> chatList) {
        this.chatList = chatList;
        notifyDataSetChanged();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout guestLayout, myLayout, myVoiceLayout, guestVoiceLayout, myImageLayout, guestImageLayout;
        private TextView guestMessage, myMessage;
        private SeekBar guestVoice, myVoice;
        private ImageView guestIconVoice, myIconVoice, myImageItem, guestImageItem;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            guestLayout = itemView.findViewById(R.id.chat_item_guest);
            myLayout = itemView.findViewById(R.id.chat_item);
            guestVoiceLayout = itemView.findViewById(R.id.voice_chat_item_guest);
            myVoiceLayout = itemView.findViewById(R.id.voice_chat_item);
            myImageLayout = itemView.findViewById(R.id.image_item);
            guestImageLayout = itemView.findViewById(R.id.image_item_guest);

            guestMessage = itemView.findViewById(R.id.msg_item_guest);
            myMessage = itemView.findViewById(R.id.msg_item);

            guestVoice = itemView.findViewById(R.id.voice_seekbar_guest);
            myVoice = itemView.findViewById(R.id.voice_seekbar);

            guestIconVoice = itemView.findViewById(R.id.voice_icon_guest);
            myIconVoice = itemView.findViewById(R.id.voice_icon);

            myImageItem = itemView.findViewById(R.id.image_view_item);
            guestImageItem = itemView.findViewById(R.id.image_view_item_guest);
        }
    }

    private void playAudio(String audioUri) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null && audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == 0) {
            Toast.makeText(context, "Volume is muted", Toast.LENGTH_LONG).show();
        }
//        if (mediaPlayer != null) {
//            mediaPlayer.stop();
//            mediaPlayer.release();
//        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioUri);
            //mediaPlayer.prepareAsync();  // Sử dụng prepareAsync để không block UI thread
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Log.e("Audio", "Error setting data source", e);
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        });
    }

    private void stopAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}