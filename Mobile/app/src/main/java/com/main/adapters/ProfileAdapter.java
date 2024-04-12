package com.main.adapters;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.group4.matchmingle.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ImageViewHolder> {
    private Context mContext;
    private List<String> mImageUrls;

    public ProfileAdapter(Context context, List<String> imageUrls) {
        mContext = context;
        mImageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.profile_grid_item_layout, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imageUrl = mImageUrls.get(position);
        // Kiểm tra xem đường dẫn là ảnh từ Internet hay là ảnh cục bộ
        if (isImageUrl(imageUrl)) {
            // Nếu là ảnh từ Internet, sử dụng Picasso như bình thường
            Picasso.get().load(imageUrl).into(holder.imageView);
        } else {
            // Nếu là ảnh cục bộ, sử dụng Uri.parse để chuyển đổi đường dẫn thành Uri và sử dụng Picasso
            Uri imageUri = Uri.parse(imageUrl);
            Picasso.get().load(imageUri).into(holder.imageView);
        }
    }

    // Phương thức để kiểm tra xem đường dẫn là ảnh từ Internet hay là ảnh cục bộ
    private boolean isImageUrl(String imageUrl) {
        // Bạn có thể thực hiện kiểm tra bằng cách kiểm tra tiền tố hoặc các đặc điểm khác của URL
        // ở đây tôi giả sử nếu đường dẫn chứa "http://" hoặc "https://" thì đây là ảnh từ Internet
        return imageUrl.startsWith("http://") || imageUrl.startsWith("https://");
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
