package com.example.imuhelper.utils;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class tipDialog extends Dialog {
    
    
    public tipDialog(@NonNull Context context) {
        super(context);
    }

    public tipDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected tipDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    
    
    static class Builder{
        
    }
}
