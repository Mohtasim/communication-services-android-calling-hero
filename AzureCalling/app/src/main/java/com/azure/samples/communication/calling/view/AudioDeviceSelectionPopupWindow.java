// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.samples.communication.calling.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azure.samples.communication.calling.R;
import com.azure.samples.communication.calling.helpers.AudioDeviceType;
import com.azure.samples.communication.calling.helpers.AudioSelectionAdapter;
import com.azure.samples.communication.calling.helpers.AudioSessionManager;

import java.util.function.Consumer;

public class AudioDeviceSelectionPopupWindow extends PopupWindow {
    private static final String LOG_TAG = AudioDeviceSelectionPopupWindow.class.getSimpleName();
    private final Context context;
    private final AudioSessionManager audioSessionManager;
    private final Consumer<AudioDeviceType> audioDevice;

    public AudioDeviceSelectionPopupWindow(final Context context,
                                           final AudioSessionManager audioSessionManager,
                                           final Consumer<AudioDeviceType> audioDevice) {
        super(context);
        this.audioDevice = audioDevice;
        this.context = context;
        this.audioSessionManager = audioSessionManager;
        final LayoutInflater layoutInflater
                = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = layoutInflater.inflate(R.layout.buttom_drawer_view, null);
        setContentView(layout);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(0x80000000));
    }

    @Override
    public void setContentView(final View contentView) {
        super.setContentView(contentView);

        // Pass audio device data to RecyclerView Adapter
        final AudioSelectionAdapter bottomCellAdapter = new AudioSelectionAdapter(
                context, audioSessionManager, this::processSelection);
        final RecyclerView audioTable = contentView.findViewById(R.id.bottom_drawer_table);
        audioTable.setAdapter(bottomCellAdapter);
        audioTable.setLayoutManager(new LinearLayoutManager(context));

        contentView.findViewById(R.id.overlay).setOnClickListener(v -> {
            dismiss();
        });
    }

    private void processSelection() {
        audioDevice.accept(audioSessionManager.getCurrentAudioDeviceType());
        dismiss();
    }
}
