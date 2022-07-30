package org.rmj.guanzongroup.marketplace.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.rmj.guanzongroup.marketplace.Model.ItemCartModel;
import org.rmj.guanzongroup.marketplace.R;

import java.util.List;

public class Adapter_ItemCart extends RecyclerView.Adapter<Adapter_ItemCart.OrderHolder> {

    private final List<ItemCartModel> poCart;
    private final OnCartAction poCallBck;

    public Adapter_ItemCart(List<ItemCartModel> foCart, OnCartAction foCallBck) {
        this.poCart = foCart;
        this.poCallBck = foCallBck;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_cart, parent, false);
        return new OrderHolder(view, poCallBck);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        ItemCartModel loCart = poCart.get(position);
        holder.lsListIdx = loCart.getListingId();
        holder.lblItemName.setText(loCart.getItemName());
        holder.lblItemPrice.setText("₱ " + loCart.getItemPrice());
        holder.lblItemQty.setText(loCart.getItemQty());
        holder.checkBox.setChecked(loCart.iscMktCheck());
        if(loCart.isMarket()){
            holder.setImage(loCart.getItemImage());
        }

        holder.checkBox.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if(isChecked) {
                poCallBck.onItemSelect(loCart.getListingId());
            } else {
                poCallBck.onItemDeselect(loCart.getListingId());
            }
        }));

       holder.btnPlus.setOnClickListener(v -> {
//            holder.lblItemQty.setText(String.valueOf(Integer.parseInt(holder.lblItemQty.getText().toString()) + 1));
                poCallBck.onQuantityClick(loCart.getListingId(), Integer.parseInt(holder.lblItemQty.getText().toString()) + 1);
        });

        holder.btnMinus.setOnClickListener(v -> {
            if(Integer.parseInt(holder.lblItemQty.getText().toString()) > 1) {
//                holder.lblItemQty.setText(String.valueOf(Integer.parseInt(holder.lblItemQty.getText().toString()) - 1));
                poCallBck.onQuantityClick(loCart.getListingId(), Integer.parseInt(holder.lblItemQty.getText().toString()) - 1);
            }
        });

        holder.btnDelete.setOnClickListener(v -> poCallBck.onItemDelete(loCart.getListingId()));
    }

    @Override
    public int getItemCount() {
        return poCart.size();
    }

    public static class OrderHolder extends RecyclerView.ViewHolder{
        public String lsListIdx = "";
        public CheckBox checkBox;
        public TextView lblItemName;
        public TextView lblItemPrice;
        public TextView lblItemQty;
        public ImageView imgItem;
        public ImageButton btnPlus;
        public ImageButton btnMinus;
        public ImageButton btnDelete;

        public OrderHolder(@NonNull View itemView, OnCartAction foCallBck) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            lblItemName = itemView.findViewById(R.id.lblProdNme);
            lblItemPrice = itemView.findViewById(R.id.lblProdPrice);
            lblItemQty = itemView.findViewById(R.id.lblQty);
            imgItem = itemView.findViewById(R.id.imgProduct);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnDelete = itemView.findViewById(R.id.btnRemoveItem);
        }

        public void setImage(String image){
            try {
                JSONArray laJson = new JSONArray(image);
                String lsImgVal = laJson.getJSONObject(0).getString("sImageURL");
                Picasso.get().load(lsImgVal).placeholder(R.drawable.ic_no_image_available)
                        .error(R.drawable.ic_no_image_available).into(imgItem);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public interface OnCartAction {
        void onItemSelect(String fsListIdx);
        void onItemDeselect(String fsListIdx);
        void onItemDelete(String fsListIDx);
        void onQuantityClick(String fsListIdx, int fnItemQty);
    }
}
