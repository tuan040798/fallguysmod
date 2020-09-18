package com.fall.guys.mods;

/**
 * The {@link MenuItem} class.
 * <p>Defines the attributes for a restaurant menu item.</p>
 */
class MenuItem {

    private final String name;
    private final String photo;
    private final String img;
    private final String download;

    public MenuItem(String name, String photo, String img, String download) {
        this.name = name;
        this.photo = photo;
        this.img = img;
        this.download = download;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public String getImg() {
        return img;
    }

    public String getDownload() {
        return download;
    }
}
