package com.rudie.severin.eventorganizer.UtilityClasses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.rudie.severin.eventorganizer.CardClasses.EmptyEventCard;
import com.rudie.severin.eventorganizer.CardClasses.EventCard;
import com.rudie.severin.eventorganizer.CardClasses.SuperCard;
import com.rudie.severin.eventorganizer.DetailsActivity;
import com.rudie.severin.eventorganizer.EventsActivity;
import com.rudie.severin.eventorganizer.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by erikrudie on 7/10/16.
 */
// Inflates child views for ListView in activity_events.xml
public class EventsAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<SuperCard> mEventCards;
    SimpleLogger logger;
    static boolean swiping;

    public EventsAdapter(Context mContext, CardHolder holder) {
        this.mContext = mContext;
        holder = CardHolder.getInstance();
        this.mEventCards = holder.getEventHolder();
        logger = new SimpleLogger("EventsAdapter");
    }

    @Override
    public int getCount() {
        return mEventCards.size();
    }

    @Override
    public Object getItem(int position) {
        return mEventCards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View child, ViewGroup parent) {

        View v = child;
        CompleteListViewHolder viewHolder;
        String type = mEventCards.get(position).getType();

        if (type == null) {
            logger.debug("Type == null. Position == " + position);
        }

        if (child == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                v = inflater.inflate(R.layout.event_swipe_item, null);

            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }

        populateView(viewHolder, position, type);
        CardHolder cardHolder = CardHolder.getInstance();
        setListener(v, type, mEventCards.get(position), cardHolder, viewHolder);

        return v;
    }

    // collects all necessary view IDs all at once
    class CompleteListViewHolder {
        public TextView header;
        public TextView subtext1;
        public TextView subtext2;
        public LinearLayout linearLayout;
        public SwipeLayout swipeLayout;
        public ImageButton revealTrash;

        public CompleteListViewHolder(View base) {
            header = (TextView) base.findViewById(R.id.PARAM_ID_EVENT_HEADER);
            subtext1 = (TextView) base.findViewById(R.id.PARAM_ID_EVENT_SUBTEXT1);
            subtext2 = (TextView) base.findViewById(R.id.PARAM_ID_EVENT_SUBTEXT2);
            linearLayout = (LinearLayout) base.findViewById(R.id.PARAM_ID_EVENT_OVERALL);
            swipeLayout =  (SwipeLayout) base.findViewById(R.id.EVENT_SWIPELAYOUT);
            revealTrash = (ImageButton) base.findViewById(R.id.trash_reveal_button);

            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        }
    }

    // populates view according to card type
    private void populateView(CompleteListViewHolder viewHolder, int position, String type) {
        if (type != null) {

            if (type.equals(PH.PARAM_EVENT_CARD)) {
                EventCard card = (EventCard) mEventCards.get(position);

                viewHolder.header.setText(card.getHeader());
                int color = mContext.getResources().getColor(R.color.blankGrey);
                viewHolder.header.setBackgroundColor(color);

                String sub1 = card.getSubtext1();
                String sub2 = card.getSubtext2();

                if (sub1.equals("")){
                    viewHolder.subtext1.setText("Location: TBD");
                } else {
                    viewHolder.subtext1.setText("Location: " + sub1);
                }
                if (sub2.equals("")){
                    viewHolder.subtext2.setText("Time: TBD");
                } else {
                    viewHolder.subtext2.setText("Time: " + sub2);
                }
                Drawable primaryBackground = mContext.getResources().getDrawable(R.drawable.black_border);
                viewHolder.linearLayout.setBackground(primaryBackground);
            } else if (type.equals(PH.PARAM_EMPTY_EVENT_CARD)) {
                EmptyEventCard card = (EmptyEventCard) mEventCards.get(position);

                viewHolder.header.setText(card.getHeader());
                viewHolder.subtext1.setText(card.getSubtext1());
                viewHolder.subtext2.setText(card.getSubtext2());
                Drawable greyedBackground = mContext.getResources().getDrawable(R.drawable.black_border_greyed);
                viewHolder.linearLayout.setBackground(greyedBackground);

//                set background blank in case it's recycling an event card
                viewHolder.header.setBackgroundColor(00000000);
            }
        }
    }

    private void setListener(View view, String type, final SuperCard card, final CardHolder cardHolder,
                             CompleteListViewHolder viewHolder) {


        if (type.equals(PH.PARAM_EVENT_CARD)) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!swiping) {

                        EventCard eventCard = (EventCard) card;
                        CardHolder.setCurrentEvent(eventCard);

                        Intent intent = new Intent(mContext, DetailsActivity.class);
                        mContext.startActivity(intent);
                    } else {
                        swiping = false;
                    }
                }
            });

        } else if (type.equals(PH.PARAM_EMPTY_EVENT_CARD)) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cardHolder.addEventCard(new EventCard("Click here to add details", "", ""));
                }
            });
        }

        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, view.findViewById(R.id.bottom_wrapper));

        viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
                swiping = true;
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.

            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });

        viewHolder.revealTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardHolder cardHolder = CardHolder.getInstance();
                cardHolder.getEventHolder().remove(card);
            }
        });

    }
}



/*

// begin SwipeLayout
        SwipeLayout swipeLayout =  (SwipeLayout) findViewById(R.id.EVENT_SWIPELAYOUT);

//set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

//add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, findViewById(R.id.PARAM_ID_EVENT_OVERALL));

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });
        // end SwipeLayout



 */