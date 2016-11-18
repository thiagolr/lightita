/*
  Copyright (c) 2016 Thiago Lopes Rosa

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package com.thiagorosa.lightita;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.thiagorosa.lightita.common.CustomFragment;
import com.thiagorosa.lightita.manager.DatabaseManager;
import com.thiagorosa.lightita.model.Effect;
import com.thiagorosa.lightita.model.Sequence;

import java.util.ArrayList;

public class FragmentSequenceList extends CustomFragment {

    private ArrayList<Sequence> mSequences = new ArrayList<>();

    private RecyclerView mList = null;
    private ImageAdapter mAdapter = null;
    private ProgressBar mProgress = null;

    /*******************************************************************************************
     *******************************************************************************************/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.sequence_list, null, false);

        mProgress = (ProgressBar) view.findViewById(R.id.progress);

        mAdapter = new ImageAdapter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ImageAdapter();

        mList = (RecyclerView) view.findViewById(R.id.list);
        mList.setLayoutManager(layoutManager);
        mList.setAdapter(mAdapter);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(null);

        update(R.string.load_title, true);

        try {
            new LoadTask().execute();
        } catch (Exception e) {
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.clear) {
            Effect.clear();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class LoadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                mSequences.clear();

                Cursor cursor = DatabaseManager.getInstance().fetchSequences();
                if (cursor != null) {
                    if (cursor.getCount() != 0) {
                        cursor.moveToFirst();
                        do {
                            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseManager.SEQUENCE_ID));
                            String value = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseManager.SEQUENCE_VALUE));

                            Sequence sequence = new Sequence(id, value);
                            mSequences.add(sequence);
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                }
            } catch (Exception ignored) {
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
            if (mProgress != null) {
                mProgress.setVisibility(View.GONE);
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
            if (mProgress != null) {
                mProgress.setVisibility(View.GONE);
            }
        }
    }

    /*******************************************************************************************
     *******************************************************************************************/

    public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public static final int TYPE_ITEM = 1;
        private Sequence mSequence = null;

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getActivity() == null) {
                return;
            }

            mSequence = mSequences.get(position);
            ImageItemHolder item = (ImageItemHolder) holder;

            ViewLed led = new ViewLed(getContext());
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
            lp.bottomMargin = position == mSequences.size() - 1 ? 0 : 20;
            led.setLayoutParams(lp);
            led.setFocusable(false);
            led.setClickable(false);
            led.load(mSequence.getEffects());

            item.getCustom().removeAllViews();
            item.getCustom().addView(led);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (viewType == TYPE_ITEM) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sequence_item, parent, false);
                return ImageItemHolder.newInstance(view, new ImageItemHolder.ViewHolderListener() {
                            @Override
                            public void onClick(View caller) {
                                try {
                                    int position = mList.getChildAdapterPosition(caller);
                                    mSequences.get(position).show();
                                } catch (Exception ignored) {
                                }
                            }

                            @Override
                            public void onLongClick(View caller) {
                                try {
                                    int position = mList.getChildAdapterPosition(caller);
                                    DatabaseManager.getInstance().deleteSequence(mSequences.get(position).getId());
                                    mSequences.remove(position);
                                    if (mAdapter != null) {
                                        mAdapter.notifyDataSetChanged();
                                    }
                                } catch (Exception ignored) {
                                }
                            }
                        }
                );
            }

            return null;
        }

        @Override
        public int getItemCount() {
            if (mSequences != null) {
                return mSequences.size();
            }
            return 0;
        }

        @Override
        public int getItemViewType(int position) {
            return TYPE_ITEM;
        }

    }

    public static class ImageItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public ViewHolderListener mListener;

        public FrameLayout mCustom;

        public interface ViewHolderListener {
            void onClick(View caller);

            void onLongClick(View caller);
        }

        public static ImageItemHolder newInstance(View parent, ViewHolderListener listener) {
            return new ImageItemHolder(parent, listener, (FrameLayout) parent.findViewById(R.id.custom));
        }

        public ImageItemHolder(View parent, ViewHolderListener listener, FrameLayout custom) {
            super(parent);
            parent.setOnClickListener(this);
            parent.setOnLongClickListener(this);

            mListener = listener;
            mCustom = custom;
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onClick(v);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mListener != null) {
                mListener.onLongClick(v);
            }
            return true;
        }

        public FrameLayout getCustom() {
            return mCustom;
        }

    }

}
