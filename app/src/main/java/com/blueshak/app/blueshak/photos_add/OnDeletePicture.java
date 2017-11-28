package com.blueshak.app.blueshak.photos_add;

import java.io.File;

public interface OnDeletePicture {
    public void ondeleting(int position);
    void onImageClick(int position, File file, String name, Boolean isDefault);
}
