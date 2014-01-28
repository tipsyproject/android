package com.tipsy.lib.util;


import android.os.Parcel;
import android.os.Parcelable;

import com.tipsy.lib.Ticket;

/**
 * Created by valoo on 04/01/14.
 */
public class Item implements Parcelable {
    private Ticket ticket;
    private int quantite;

    public Item() {
    }

    public Item(Ticket a, int q) {
        ticket = a;
        quantite = q;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public int getPrixTotal() {
        return ticket.getPrix() * quantite;
    }

    @Override
    public boolean equals(Object o) {
        return this.ticket.equals(((Item) o).getTicket());
    }

    @Override
    public int hashCode() {
        return ticket.getObjectId().hashCode();
    }


    // Implémentation de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(ticket, flags);
        dest.writeInt(quantite);
    }

    public Item(Parcel in) {
        ticket = in.readParcelable(Ticket.class.getClassLoader());
        quantite = in.readInt();
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
