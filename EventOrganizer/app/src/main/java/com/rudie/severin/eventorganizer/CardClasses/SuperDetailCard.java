package com.rudie.severin.eventorganizer.CardClasses;

/*  DetailAdapter will receive an event object from a mType which extends SuperCard.  getType will be
 *  checked against a list of hardcoded parameters, then the object will be casted to the appropriate
 *  mType.  DetailAdapter will then inflate the proper view and assign values.
 *
 *  Coding is so cool.
 */

import com.rudie.severin.eventorganizer.UtilityClasses.CardHolder;
import com.rudie.severin.eventorganizer.UtilityClasses.PH;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class SuperDetailCard extends SuperCard implements Serializable {

    private String mHeader;
    private String mType;
    private String mSubtext1;
    private String mSubtext2;
    private String mSubtext3;
    private String mSubtext4;
    private String mIconResource;
    private ArrayList<String> enteredText, displayText;
    private EventCard linkedEvent;
    private String date, time;

    public SuperDetailCard(EventCard event, String type, String head, String sub1, String sub2, String sub3, String sub4) {
        super(type, head, sub1, sub2);
        setSubtext3(sub3);
        setSubtext4(sub4);
        this.enteredText = new ArrayList<>();
        enteredText.add("");
        this.displayText = new ArrayList<>();
        this.linkedEvent = CardHolder.getCurrentEvent();
        setIconResource("@drawable/ic_open_in_new_black_24dp");
        this.date = "";
        this.time = "";
    }

// EmptyDetailCard constructor
    public SuperDetailCard(EventCard event, String type, String head, String sub1, String sub2) {
        super(type, head, sub1, sub2);
        setSubtext3("");
        setSubtext4("");
        this.enteredText = new ArrayList<>();
        enteredText.add("");
        this.displayText = new ArrayList<>();
        this.linkedEvent = CardHolder.getCurrentEvent();
        this.date = "";
        this.time = "";
    }

// Begin getters & setters
    public String getType() {
        return this.mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public String getHeader() {
        return mHeader;
    }

    public void setHeader(String header) {
        this.mHeader = header;
    }

    public String getSubtext1() {
        return mSubtext1;
    }

    public void setSubtext1(String subtext1) {
        this.mSubtext1 = subtext1;
    }

    public String getSubtext2() {
        return mSubtext2;
    }

    public void setSubtext2(String subtext2) {
        this.mSubtext2 = subtext2;
    }

    public String getSubtext3() {
        return mSubtext3;
    }

    public void setSubtext3(String subtext3) {
        this.mSubtext3 = subtext3;
    }

    public String getSubtext4() {
        return mSubtext4;
    }

    public void setSubtext4(String subtext4) {
        this.mSubtext4 = subtext4;
    }

    public String getIconResource() {
        return mIconResource;
    }

    public void setIconResource(String icon) {
        this.mIconResource = icon;
    }

    public ArrayList<String> getEnteredText() {
        return enteredText;
    }

    public ArrayList<String> getDisplayText() { return displayText;}

    public void setEnteredText(ArrayList<String> enteredText, ArrayList<String> displayText) {
        this.enteredText = enteredText;
        this.displayText = displayText;
    }

    public EventCard getParentEvent() {
        return linkedEvent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        if (!(this.time.equals(""))  && this.getType().equals(PH.PARAM_TIME_DETAIL_CARD)) {
            enteredText.clear();
            enteredText.add(getTime() + ", " + getDate());
        }
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    //  End getters & setters


}
