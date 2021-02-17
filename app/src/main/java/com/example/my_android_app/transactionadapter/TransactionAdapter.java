package com.example.my_android_app.transactionadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_android_app.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<TransactionInfo> transactionList;
    private final ButtonClickListener listener;

    public TransactionAdapter(ButtonClickListener listener) {
        transactionList = new ArrayList<>();
        //Initialize ButtonClickListener interface
        this.listener = listener;

    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TransactionInfo ci = transactionList.get(position);

        Integer intInstance;
        String amount,brandText;

        final TransactionViewHolder dynamicHolder = (TransactionViewHolder) holder;
        brandText = ci.brandType +" "+ci.accountType;
        dynamicHolder.tvAccountType.setText(brandText);
        dynamicHolder.tvUserAccountName.setText(ci.accountName);


        intInstance = ci.totalAmount;
        amount = intInstance.toString();
        dynamicHolder.tvPaymentAmount.setText(amount);

        dynamicHolder.tvPaymentAmountText.setText(ci.paymentAmountText);
        dynamicHolder.tvPaymentDate.setText(ci.paymentDue);

        dynamicHolder.tvPaymentDateText.setText(ci.paymentDueText);

        }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View currentView;
        currentView = inflater.inflate(R.layout.transaction_layout, viewGroup, false);
        RecyclerView.ViewHolder viewHolder = new TransactionViewHolder(currentView,listener);

        return viewHolder;
    }


    /*-------------Adapter helpers-----------------*/

    public void add(TransactionInfo r) {
        transactionList.add(r);
        notifyItemInserted(transactionList.size() - 1);
    }

    public void addAll(List<TransactionInfo> moveResults) {
        for (TransactionInfo result : moveResults) {
            add(result);
        }
    }

    public void remove(TransactionInfo r) {
        int position = transactionList.indexOf(r);
        if (position > -1) {
            transactionList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        transactionList.clear();
        notifyDataSetChanged();
    }

    public void onBrandTypeBtnClickAddResult(List<TransactionInfo> firstResult){
        transactionList.clear();
        transactionList.addAll(firstResult);
        notifyDataSetChanged();
    }



     public void filteredList(List<TransactionInfo> filterList){
        if(filterList.size() > 0){
             transactionList.clear();
             transactionList.addAll(filterList);
             notifyDataSetChanged();
         }
     }




    public void onCancelSearch(List<TransactionInfo> info){
        transactionList.clear();
        transactionList.addAll(info);
        notifyDataSetChanged();
    }

    public void removeTransactionInfo(int pos){
        //for future use..
    }

    public TransactionInfo getItem(int position) {
        return transactionList.get(position);
    }


    protected class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private TextView tvAccountType;
        private TextView tvUserAccountName;
        private TextView tvPaymentAmount;
        private TextView tvPaymentAmountText;
        private TextView tvPaymentDate;
        private TextView tvPaymentDateText;
        private Button bViewAccount;
        private WeakReference<ButtonClickListener> listenerRef;


        private TransactionViewHolder(View v, ButtonClickListener listener) {
            super(v);

            listenerRef = new WeakReference<>(listener);

            tvAccountType =   v.findViewById(R.id.accountType);
            tvUserAccountName =   v.findViewById(R.id.userAccountName);
            tvPaymentAmount =   v.findViewById(R.id.paymentAmount);
            tvPaymentAmountText =  v.findViewById(R.id.paymentAmountText);
            tvPaymentDate =  v.findViewById(R.id.paymentDate);
            tvPaymentDateText =  v.findViewById(R.id.paymentDateText);

            bViewAccount = v.findViewById(R.id.viewAccount);

            bViewAccount.setOnClickListener(this);
            bViewAccount.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listenerRef.get().onPositionClicked(v,getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            listenerRef.get().onPositionClicked(v,getAdapterPosition());
            return true;
        }
    }





}
